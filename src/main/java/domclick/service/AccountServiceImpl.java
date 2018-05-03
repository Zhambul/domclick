package domclick.service;

import domclick.model.Account;
import domclick.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    private final static Logger LOGGER = LoggerFactory.getLogger(AccountServiceImpl.class);

    private final AccountRepository repository;

    @Autowired
    public AccountServiceImpl(AccountRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    @Override
    public Optional<Account> get(Long id) {
        LOGGER.info("getting account by id {}", id);
        return repository.findById(id);
    }

    @Override
    @Transactional
    public void create(String name) {
        if (repository.findByName(name).isPresent()) {
            throw new IllegalArgumentException("Account with name " + name + " already exists");
        }
        LOGGER.info("creating account with name {}", name);
        repository.save(new Account(name));
    }

    @Override
    public void delete(Long id) {
        LOGGER.info("deleting account by id {}", id);
        repository.deleteById(id);
    }
}
