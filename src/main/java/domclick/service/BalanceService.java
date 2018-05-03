package domclick.service;

import domclick.exception.*;
import domclick.model.Account;

import java.math.BigDecimal;

/**
 * A service class for balance operations of an {@link Account}
 */
public interface BalanceService {

    /**
     * Transfers funds from one account to another
     *
     * @param fromId id of an {@link Account} to transfer funds from
     * @param toId   id of an {@link Account} to transfer funds to
     * @param amount amount of funds to transfer
     * @throws AccountNotFoundException if account does not exist
     * @throws NotEnoughFundsException  if account does not have enough funds to transfer
     * @throws IllegalArgumentException if amount is non-positive BigDecimal
     * @throws NullPointerException     if either of params is null
     */
    void transfer(Long fromId, Long toId, BigDecimal amount);

    /**
     * Deposits funds to an account
     *
     * @param toId   id of an {@link Account} to deposit funds to
     * @param amount amount of funds to transfer
     * @throws AccountNotFoundException if account does not exist
     * @throws IllegalArgumentException if amount is non-positive BigDecimal
     * @throws NullPointerException     if either of params is null
     */
    void deposit(Long toId, BigDecimal amount);

    /**
     * Withdraws funds from an account
     *
     * @param fromId id of an {@link Account} to withdraw funds from
     * @param amount amount of funds to transfer
     * @throws AccountNotFoundException if account does not exist
     * @throws NotEnoughFundsException  if account does not have enough funds to transfer
     * @throws IllegalArgumentException if amount is non-positive BigDecimal
     * @throws NullPointerException     if either of params is null
     */
    void withdraw(Long fromId, BigDecimal amount);
}
