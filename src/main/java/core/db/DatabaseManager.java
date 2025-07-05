package db;

import core.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;

public class DatabaseManager {
    private static DatabaseManager instance;
    private Connection connection;
    private String dbUrl;
    private String dbUser;
    private String dbPassword;

    private DatabaseManager() {
        loadConfig();
        initializeConnection();
        createTables();
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    private void loadConfig() {
        Properties props = new Properties();
        try (FileInputStream input = new FileInputStream("config.properties")) {
            props.load(input);
            dbUrl = props.getProperty("db.url");
            dbUser = props.getProperty("db.username");
            dbPassword = props.getProperty("db.password");
        } catch (IOException e) {
            // Default values for development
            dbUrl = "jdbc:mysql://localhost:3306/expense_tracker";
            dbUser = "root";
            dbPassword = "root";
        }
    }

    private void initializeConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            System.out.println("Database connected successfully!");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
        }
    }

    private void createTables() {
        String createExpensesTable =
                "CREATE TABLE IF NOT EXISTS expenses (" +
                        "id INT AUTO_INCREMENT PRIMARY KEY," +
                        "amount DECIMAL(10,2) NOT NULL," +
                        "category ENUM('MESS', 'CANTEEN') NOT NULL," +
                        "date DATE NOT NULL," +
                        "description TEXT," +
                        "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                        ")";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createExpensesTable);
            System.out.println("Tables created successfully!");
        } catch (SQLException e) {
            System.err.println("Error creating tables: " + e.getMessage());
        }
    }

    public boolean saveExpense(Expense expense) {
        String sql = "INSERT INTO expenses (amount, category, date, description) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDouble(1, expense.getAmount());
            pstmt.setString(2, expense.getCategory());
            pstmt.setDate(3, Date.valueOf(expense.getDate()));
            pstmt.setString(4, expense.getDescription());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error saving expense: " + e.getMessage());
            return false;
        }
    }

    public List<Expense> getExpensesByDate(LocalDate date) {
        String sql = "SELECT * FROM expenses WHERE date = ? ORDER BY created_at DESC";
        List<Expense> expenses = new ArrayList<>();

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDate(1, Date.valueOf(date));
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                expenses.add(createExpenseFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting expenses by date: " + e.getMessage());
        }

        return expenses;
    }

    public List<Expense> getExpensesByMonth(int year, int month) {
        String sql = "SELECT * FROM expenses WHERE YEAR(date) = ? AND MONTH(date) = ? ORDER BY date DESC";
        List<Expense> expenses = new ArrayList<>();

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, year);
            pstmt.setInt(2, month);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                expenses.add(createExpenseFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting monthly expenses: " + e.getMessage());
        }

        return expenses;
    }

    private Expense createExpenseFromResultSet(ResultSet rs) throws SQLException {
        double amount = rs.getDouble("amount");
        String category = rs.getString("category");
        LocalDate date = rs.getDate("date").toLocalDate();
        String description = rs.getString("description");

        if ("MESS".equals(category)) {
            return new MessExpense(amount, date, description);
        } else {
            return new CanteenExpense(amount, date, description);
        }
    }
}