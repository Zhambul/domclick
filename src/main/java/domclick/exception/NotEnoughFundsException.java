package domclick.exception;

import java.math.BigDecimal;

public class NotEnoughFundsException extends RuntimeException {
    public NotEnoughFundsException(Long accountId, BigDecimal amount, BigDecimal currentFunds) {
        super("Account with id " + accountId + " has not enough funds." +
                " (expected " + amount + ", current funds" + currentFunds + ")");
    }
}
