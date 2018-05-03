import domclick.model.Account;
import domclick.repository.AccountRepository;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Random;

public class TestAccountFactory {

    private final AccountRepository accountRepository;
    private final Random random = new Random();

    public TestAccountFactory(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account createDefaultAccount() {
        return createAccount(100);
    }

    public Account createZeroFundsAccount() {
        return createAccount(0);
    }

    public Account createNegativeFundsAccount() {
        return createAccount(-1);
    }

    public Account createAccount(int amount) {
        Account account = new Account();
        account.setFunds(new BigDecimal(amount));

        account.setId(random.nextLong());

        Mockito.when(accountRepository.findById(account.getId())).then(i -> Optional.of(account));

        return account;
    }
}
