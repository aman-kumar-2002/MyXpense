package core;

import db.DatabaseManager;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class ExpenseManager {
    private DatabaseManager dbManager;

    public ExpenseManager() {
        this.dbManager = DatabaseManager.getInstance();
    }

    public boolean addExpense(Expense expense) {
        try {
            return dbManager.saveExpense(expense);
        } catch (Exception e) {
            System.err.println("Error adding expense: " + e.getMessage());
            return false;
        }
    }

    public List<Expense> getTodayExpenses() {
        try {
            return dbManager.getExpensesByDate(LocalDate.now());
        } catch (Exception e) {
            System.err.println("Error getting today's expenses: " + e.getMessage());
            return List.of();
        }
    }

    public List<Expense> getExpensesByDate(LocalDate date) {
        try {
            return dbManager.getExpensesByDate(date);
        } catch (Exception e) {
            System.err.println("Error getting expenses by date: " + e.getMessage());
            return List.of();
        }
    }

    public Map<String, Double> getTodayTotalsByCategory() {
        List<Expense> todayExpenses = getTodayExpenses();
        Map<String, Double> totals = new HashMap<>();

        for (Expense expense : todayExpenses) {
            String category = expense.getCategory();
            totals.put(category, totals.getOrDefault(category, 0.0) + expense.getAmount());
        }

        return totals;
    }

    public double getTodayTotal() {
        return getTodayExpenses().stream()
                .mapToDouble(Expense::getAmount)
                .sum();
    }

    public List<Expense> getMonthlyExpenses(int year, int month) {
        try {
            return dbManager.getExpensesByMonth(year, month);
        } catch (Exception e) {
            System.err.println("Error getting monthly expenses: " + e.getMessage());
            return List.of();
        }
    }
}