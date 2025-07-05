package core;

import java.time.LocalDate;

public class CanteenExpense extends Expense {

    public CanteenExpense(double amount, String description) {
        super(amount, description);
    }

    public CanteenExpense(double amount, LocalDate date, String description) {
        super(amount, date, description);
    }

    @Override
    public String getCategory() {
        return "CANTEEN";
    }
}