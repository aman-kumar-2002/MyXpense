package core;

public abstract class Expense {
    protected int id;             // Auto-incremented in DB
    protected String date;        // Format: YYYY-MM-DD
    protected double amount;
    protected String description;
    
    public Expense(String date, double amount, String description) {
        // Input validation
        if (date == null || date.trim().isEmpty()) {
            throw new IllegalArgumentException("Date cannot be null or empty");
        }
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be null or empty");
        }
        
        this.date = date;
        this.amount = amount;
        this.description = description.trim(); // Remove extra spaces
    }
    
    // Getters
    public int getId() {
        return id;
    }
    
    public String getDate() {
        return date;
    }
    
    public double getAmount() {
        return amount;
    }
    
    public String getDescription() {
        return description;
    }
    
    // Setter for ID (after DB insert)
    public void setId(int id) {
        this.id = id;
    }
    
    // Setters for updating expense details
    public void setDate(String date) {
        if (date == null || date.trim().isEmpty()) {
            throw new IllegalArgumentException("Date cannot be null or empty");
        }
        this.date = date;
    }
    
    public void setAmount(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        this.amount = amount;
    }
    
    public void setDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be null or empty");
        }
        this.description = description.trim();
    }
    
    // Abstract method for expense type (Mess or Canteen)
    public abstract String getType();
    
    // String representation with better formatting
    @Override
    public String toString() {
        return String.format("[%s] %s - ₹%.2f : %s", getType(), date, amount, description);
    }
    
    // equals and hashCode for comparing expenses (based on ID)
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Expense expense = (Expense) obj;
        return id == expense.id;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}