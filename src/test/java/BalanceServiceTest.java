import domclick.LockService;
import domclick.exception.NotEnoughFundsException;
import domclick.model.Account;
import domclick.repository.AccountRepository;
import domclick.service.BalanceService;
import domclick.service.BalanceServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;

public class BalanceServiceTest {

    private BalanceService service;
    private TestAccountFactory testAccountFactory;

    @Before
    public void before() {
        AccountRepository repository = Mockito.mock(AccountRepository.class);
        service = new BalanceServiceImpl(repository, new LockService());
        testAccountFactory = new TestAccountFactory(repository);
    }

    @Test
    public void transferAllFunds() {
        Account from = testAccountFactory.createAccount(100);
        Account to = testAccountFactory.createAccount(100);

        transferTest(from, to, new BigDecimal(100));
    }

    @Test
    public void transferPartialFunds() {
        Account from = testAccountFactory.createAccount(200);
        Account to = testAccountFactory.createAccount(100);

        transferTest(from, to, new BigDecimal(100));
    }

    @Test
    public void transferToZeroFundsAccount() {
        Account from = testAccountFactory.createAccount(200);
        Account to = testAccountFactory.createAccount(0);

        transferTest(from, to, new BigDecimal(200));
    }

    private void transferTest(Account from, Account to, BigDecimal amount) {
        BigDecimal fromInitialFunds = from.getFunds();
        BigDecimal toInitialFunds = to.getFunds();

        service.transfer(from.getId(), to.getId(), amount);

        Assert.assertEquals(fromInitialFunds.subtract(amount), from.getFunds());
        Assert.assertEquals(toInitialFunds.add(amount), to.getFunds());
    }

    @Test
    public void deposit() {
        Account account = testAccountFactory.createAccount(100);
        depositTest(account, new BigDecimal(100));
    }

    @Test
    public void depositZeroFundsAccount() {
        Account account = testAccountFactory.createZeroFundsAccount();
        depositTest(account, new BigDecimal(100));
    }

    private void depositTest(Account account, BigDecimal amount) {
        BigDecimal initialFunds = account.getFunds();

        service.deposit(account.getId(), amount);

        Assert.assertEquals(initialFunds.add(amount), account.getFunds());
    }

    @Test
    public void withdraw() {
        Account account = testAccountFactory.createAccount(100);
        withdrawTest(account, new BigDecimal(10));
    }

    private void withdrawTest(Account account, BigDecimal amount) {
        BigDecimal initialFunds = account.getFunds();

        service.withdraw(account.getId(), amount);

        Assert.assertEquals(initialFunds.subtract(amount), account.getFunds());
    }

    @Test(expected = NullPointerException.class)
    public void transferShouldFailFastOnNullParams() {
        service.transfer(null, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void depositShouldFailFastOnNullParams() {
        service.deposit(null, null);
    }

    @Test(expected = NullPointerException.class)
    public void withdrawShouldFailFastOnNullParams() {
        service.withdraw(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void transferShouldFailOnInvalidAmount() {
        Long fromId = testAccountFactory.createDefaultAccount().getId();
        Long toId = testAccountFactory.createDefaultAccount().getId();

        service.transfer(fromId, toId, new BigDecimal(-1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void depositShouldFailOnInvalidAmount() {
        Long fromId = testAccountFactory.createDefaultAccount().getId();

        service.deposit(fromId, new BigDecimal(-1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void withdrawShouldFailOnInvalidAmount() {
        Long fromId = testAccountFactory.createDefaultAccount().getId();

        service.withdraw(fromId, new BigDecimal(-1));
    }

    @Test(expected = NotEnoughFundsException.class)
    public void withdrawShouldFailWithNotEnoughFunds() {
        Long fromId = testAccountFactory.createZeroFundsAccount().getId();

        service.withdraw(fromId, new BigDecimal(100));
    }

    @Test(expected = NotEnoughFundsException.class)
    public void transferShouldFailWithNotEnoughFunds() {
        Long fromId = testAccountFactory.createZeroFundsAccount().getId();
        Long toId = testAccountFactory.createDefaultAccount().getId();
        service.transfer(fromId, toId, new BigDecimal(100));
    }

    @Test(expected = NotEnoughFundsException.class)
    public void withdrawShouldFailNegativeFundsAccount() {
        Long accountId = testAccountFactory.createNegativeFundsAccount().getId();
        service.withdraw(accountId, new BigDecimal(100));
    }

    @Test
    public void testLoad() {

    }
}
