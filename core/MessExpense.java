package core;

public class MessExpense extends Expense {
    
    public MessExpense(String date, double amount, String description) {
        super(date, amount, description);
    }
    
    @Override
    public String getType() {
        return "Mess";
    }
    
    // Optional: Add a method specific to mess expenses
    public boolean isMonthlyPayment() {
        // Check if this is a monthly mess payment (you can customize this logic)
        return description.toLowerCase().contains("monthly") || 
               description.toLowerCase().contains("mess fee");
    }
    
    // Optional: Get formatted mess-specific details
    public String getMessDetails() {
        return String.format("Mess Expense: %s on %s - ₹%.2f", 
                           description, date, amount);
    }
}