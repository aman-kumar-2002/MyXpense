package core;

import java.time.LocalDate;

public class MessExpense extends Expense {

    public MessExpense(double amount, String description) {
        super(amount, description);
    }

    public MessExpense(double amount, LocalDate date, String description) {
        super(amount, date, description);
    }

    @Override
    public String getCategory() {
        return "MESS";
    }
}