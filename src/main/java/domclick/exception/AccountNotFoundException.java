package domclick.exception;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(Long accountId) {
        super("Account with id " + accountId + " is not found");
    }
}
