package domclick.service;

import domclick.model.Account;

import java.util.Optional;

/**
 * A service class for basic operations of an {@link Account}
 */
public interface AccountService {

    /**
     * Retrieves existing {@link Account}
     *
     * @param id id of an {@link Account} to retrieve
     * @return {@link Optional} of {@link Account}
     */
    Optional<Account> get(Long id);

    /**
     * Creates new {@link Account}
     *
     * @param name name of a new {@link Account} to be created
     * @throws IllegalArgumentException if {@link Account} with specified name already exists
     */
    void create(String name);

    /**
     * Deletes {@link Account}
     *
     * @param id id of an {@link Account} to be deleted
     */
    void delete(Long id);
}
