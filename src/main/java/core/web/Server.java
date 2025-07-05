package web;

import static spark.Spark.*;
import core.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class Server {
    private static ExpenseManager expenseManager = new ExpenseManager();

    public static void main(String[] args) {
        // Server configuration
        port(4567);
        staticFiles.location("/public");

        // Routes
        get("/", (req, res) -> {
            res.redirect("/index.html");
            return null;
        });

        // Add expense
        post("/add-expense", (req, res) -> {
            try {
                double amount = Double.parseDouble(req.queryParams("amount"));
                String category = req.queryParams("category");
                String description = req.queryParams("description");

                Expense expense;
                if ("MESS".equals(category)) {
                    expense = new MessExpense(amount, description);
                } else {
                    expense = new CanteenExpense(amount, description);
                }

                if (expenseManager.addExpense(expense)) {
                    res.redirect("/today.html");
                } else {
                    return "Error adding expense";
                }
            } catch (Exception e) {
                return "Invalid input: " + e.getMessage();
            }
            return null;
        });

        // Get today's expenses (JSON)
        get("/api/today", (req, res) -> {
            res.type("application/json");
            List<Expense> expenses = expenseManager.getTodayExpenses();
            double total = expenseManager.getTodayTotal();

            StringBuilder json = new StringBuilder();
            json.append("{\"expenses\":[");

            for (int i = 0; i < expenses.size(); i++) {
                Expense exp = expenses.get(i);
                json.append(String.format(
                        "{\"amount\":%.2f,\"category\":\"%s\",\"description\":\"%s\",\"date\":\"%s\"}",
                        exp.getAmount(), exp.getCategory(), exp.getDescription(), exp.getDate()
                ));
                if (i < expenses.size() - 1) json.append(",");
            }

            json.append(String.format("],\"total\":%.2f}", total));
            return json.toString();
        });

        System.out.println("Server started on http://localhost:4567");
    }
}