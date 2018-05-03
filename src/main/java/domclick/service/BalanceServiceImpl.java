package domclick.service;

import domclick.LockService;
import domclick.exception.AccountNotFoundException;
import domclick.exception.NotEnoughFundsException;
import domclick.model.Account;
import domclick.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Objects;

@Service
public class BalanceServiceImpl implements BalanceService {

    private final AccountRepository repository;
    private static final Logger LOGGER = LoggerFactory.getLogger(BalanceServiceImpl.class);
    private final LockService lockService;

    @Autowired
    public BalanceServiceImpl(AccountRepository repository, LockService lockService) {
        this.repository = Objects.requireNonNull(repository);
        this.lockService = Objects.requireNonNull(lockService);
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void transfer(Long fromId, Long toId, BigDecimal amount) {
        Objects.requireNonNull(fromId);
        Objects.requireNonNull(toId);
        checkPositiveAmount(amount);
        LOGGER.info("Transferring {} from accountId {} to accountId {}", amount, fromId, toId);

        //in order to prevent deadlocks, first locks for higher ids should be acquired
        long higherId = fromId > toId ? fromId : toId;
        long lowerId = toId > fromId ? toId : fromId;

        lockService.lockForId(higherId);
        lockService.lockForId(lowerId);

        try {
            withdrawFunds(findAccount(fromId), amount);
            depositFunds(findAccount(toId), amount);
        } finally {
            lockService.unlockForId(lowerId);
            lockService.unlockForId(higherId);
        }

        LOGGER.info("Successfully transferred {} from accountId {} to accountId {}", amount, fromId, toId);
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void deposit(Long toId, BigDecimal amount) {
        singleAccountOperation(toId, amount, () -> {
            LOGGER.info("Depositing {} from accountId {}", amount, toId);
            depositFunds(findAccount(toId), amount);
            LOGGER.info("Successfully deposited {} from accountId {}", amount, toId);
        });
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void withdraw(Long fromId, BigDecimal amount) {
        singleAccountOperation(fromId, amount, () -> {
            LOGGER.info("Withdrawing {} from accountId {}", amount, fromId);
            withdrawFunds(findAccount(fromId), amount);
            LOGGER.info("Successfully withdraw {} from accountId {}", amount, fromId);
        });
    }

    private void singleAccountOperation(Long id, BigDecimal amount, Runnable runnable) {
        Objects.requireNonNull(id);
        checkPositiveAmount(amount);

        lockService.lockForId(id);
        try {
            runnable.run();
        } finally {
            lockService.unlockForId(id);
        }
    }

    private void depositFunds(Account account, BigDecimal amount) {
        account.setFunds(account.getFunds().add(amount));

        repository.save(account);
    }

    private void withdrawFunds(Account account, BigDecimal amount) {
        checkEnoughFunds(account, amount);

        account.setFunds(account.getFunds().subtract(amount));

        repository.save(account);
    }

    private Account findAccount(Long id) {
        LOGGER.info("finding account with id " + id);
        return repository.findById(id).orElseThrow(() -> new AccountNotFoundException(id));
    }

    private void checkEnoughFunds(Account account, BigDecimal amount) {
        if (account.getFunds().compareTo(amount) < 0) {
            throw new NotEnoughFundsException(account.getId(), amount, account.getFunds());
        }
    }

    private void checkPositiveAmount(BigDecimal amount) {
        Objects.requireNonNull(amount);

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be non zero positive BigDecimal");
        }
    }
}
