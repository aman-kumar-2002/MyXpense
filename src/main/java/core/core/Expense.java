package core;

import java.time.LocalDate;

public abstract class Expense {
    protected double amount;
    protected LocalDate date;
    protected String description;

    public Expense(double amount, String description) {
        this.amount = amount;
        this.date = LocalDate.now();
        this.description = description;
    }

    public Expense(double amount, LocalDate date, String description) {
        this.amount = amount;
        this.date = date;
        this.description = description;
    }

    // Abstract method - must be implemented by subclasses
    public abstract String getCategory();

    // Getters and Setters
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return String.format("%s: ₹%.2f - %s (%s)",
                getCategory(), amount, description, date);
    }
}