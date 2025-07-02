package core;

public class CanteenExpense extends Expense {
    
    public CanteenExpense(String date, double amount, String description) {
        super(date, amount, description);
    }
    
    @Override
    public String getType() {
        return "Canteen";
    }
    
    // Check if this is a snack/beverage expense
    public boolean isSnack() {
        String lowerDesc = description.toLowerCase();
        return lowerDesc.contains("chai") || 
               lowerDesc.contains("snack") || 
               lowerDesc.contains("coffee") ||
               lowerDesc.contains("biscuit") ||
               lowerDesc.contains("samosa");
    }
    
    // Check if this is a beverage
    public boolean isBeverage() {
        String lowerDesc = description.toLowerCase();
        return lowerDesc.contains("chai") ||
               lowerDesc.contains("coffee") ||
               lowerDesc.contains("juice") ||
               lowerDesc.contains("cold drink");
    }
    
    // Get formatted canteen-specific details
    public String getCanteenDetails() {
        String type = isBeverage() ? " (Beverage)" : isSnack() ? " (Snack)" : "";
        return String.format("Canteen Expense: %s%s on %s - ₹%.2f", 
                           description, type, date, amount);
    }
    
    // Get expense category for reports
    public String getCategory() {
        if (isBeverage()) {
            return "Beverages";
        } else if (isSnack()) {
            return "Snacks";
        } else {
            return "Other";
        }
    }
}