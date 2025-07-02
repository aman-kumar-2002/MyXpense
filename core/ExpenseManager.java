package core;

import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ExpenseManager {
    private final List<Expense> expenses;
    
    public ExpenseManager() {
        this.expenses = new ArrayList<>();
    }
    
    // Add a new expense with validation
    public boolean addExpense(Expense expense) {
        if (expense == null) {
            return false;
        }
        expenses.add(expense);
        return true;
    }
    
    // Remove an expense by ID
    public boolean removeExpense(int id) {
        return expenses.removeIf(expense -> expense.getId() == id);
    }
    
    // Find expense by ID
    public Expense findExpenseById(int id) {
        for (Expense e : expenses) {
            if (e.getId() == id) {
                return e;
            }
        }
        return null;
    }
    
    // Get all expenses
    public List<Expense> getAllExpenses() {
        return new ArrayList<>(expenses); // Return a copy
    }
    
    // Get expenses for today
    public List<Expense> getTodayExpenses() {
        String today = LocalDate.now().toString(); // "YYYY-MM-DD"
        return getExpensesByDate(today);
    }
    
    // Get expenses for a specific date
    public List<Expense> getExpensesByDate(String date) {
        List<Expense> result = new ArrayList<>();
        for (Expense e : expenses) {
            if (e.getDate().equals(date)) {
                result.add(e);
            }
        }
        return result;
    }
    
    // Get expenses by date range
    public List<Expense> getExpensesByDateRange(String startDate, String endDate) {
        List<Expense> result = new ArrayList<>();
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        
        for (Expense e : expenses) {
            LocalDate expenseDate = LocalDate.parse(e.getDate());
            if (!expenseDate.isBefore(start) && !expenseDate.isAfter(end)) {
                result.add(e);
            }
        }
        return result;
    }
    
    // Get expenses by month & year (e.g., July 2025)
    public List<Expense> getExpensesByMonth(int month, int year) {
        List<Expense> result = new ArrayList<>();
        for (Expense e : expenses) {
            try {
                LocalDate date = LocalDate.parse(e.getDate());
                if (date.getMonthValue() == month && date.getYear() == year) {
                    result.add(e);
                }
            } catch (Exception ex) {
                // Skip invalid date formats
                System.err.println("Invalid date format in expense: " + e.getDate());
            }
        }
        return result;
    }
    
    // Get expenses by type (Mess / Canteen)
    public List<Expense> getExpensesByType(String type) {
        List<Expense> result = new ArrayList<>();
        for (Expense e : expenses) {
            if (e.getType().equalsIgnoreCase(type)) {
                result.add(e);
            }
        }
        return result;
    }
    
    // Get total spent in a list
    public double calculateTotal(List<Expense> list) {
        double sum = 0;
        for (Expense e : list) {
            sum += e.getAmount();
        }
        return Math.round(sum * 100.0) / 100.0; // Round to 2 decimal places
    }
    
    // Get today's total
    public double getTodayTotal() {
        return calculateTotal(getTodayExpenses());
    }
    
    // Get this month's total
    public double getThisMonthTotal() {
        LocalDate now = LocalDate.now();
        return calculateTotal(getExpensesByMonth(now.getMonthValue(), now.getYear()));
    }
    
    // Group by date for charting today's data
    public Map<String, Double> groupByTypeToday() {
        Map<String, Double> map = new HashMap<>();
        for (Expense e : getTodayExpenses()) {
            String type = e.getType();
            map.put(type, map.getOrDefault(type, 0.0) + e.getAmount());
        }
        return map;
    }
    
    // Group by type for any date range
    public Map<String, Double> groupByType(List<Expense> expenseList) {
        Map<String, Double> map = new HashMap<>();
        for (Expense e : expenseList) {
            String type = e.getType();
            map.put(type, map.getOrDefault(type, 0.0) + e.getAmount());
        }
        return map;
    }
    
    // Group by category for canteen chart (snack, beverage, other)
    public Map<String, Double> groupCanteenByCategoryToday() {
        Map<String, Double> map = new HashMap<>();
        for (Expense e : getTodayExpenses()) {
            if (e instanceof CanteenExpense) {
                String category = ((CanteenExpense) e).getCategory();
                map.put(category, map.getOrDefault(category, 0.0) + e.getAmount());
            }
        }
        return map;
    }
    
    // Get expense count
    public int getExpenseCount() {
        return expenses.size();
    }
    
    // Get average daily spending (based on days with expenses)
    public double getAverageDailySpending() {
        if (expenses.isEmpty()) return 0.0;
        
        Set<String> uniqueDates = new HashSet<>();
        double totalAmount = 0.0;
        
        for (Expense e : expenses) {
            uniqueDates.add(e.getDate());
            totalAmount += e.getAmount();
        }
        
        return totalAmount / uniqueDates.size();
    }
    
    // Clear all data (for dev/debug only)
    public void clearAll() {
        expenses.clear();
    }
    
    // Get summary statistics
    public String getSummary() {
        if (expenses.isEmpty()) {
            return "No expenses recorded yet.";
        }
        
        double todayTotal = getTodayTotal();
        double monthTotal = getThisMonthTotal();
        double avgDaily = getAverageDailySpending();
        
        return String.format(
            "Expense Summary:\n" +
            "- Today's spending: ₹%.2f\n" +
            "- This month's spending: ₹%.2f\n" +
            "- Average daily spending: ₹%.2f\n" +
            "- Total expenses recorded: %d",
            todayTotal, monthTotal, avgDaily, getExpenseCount()
        );
    }
}