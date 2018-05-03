package domclick.controller;

import domclick.service.BalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class BalanceController {

    private final BalanceService balanceService;

    @Autowired
    public BalanceController(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    @PostMapping("/balance/transfer")
    public void transfer(@RequestParam("fromId") Long fromId, @RequestParam("toId") Long toId, @RequestParam("amount") BigDecimal amount) {
        balanceService.transfer(fromId, toId, amount);
    }

    @PostMapping("/balance/deposit")
    public void deposit(@RequestParam("toId") Long toId, @RequestParam("amount") BigDecimal amount) {
        balanceService.deposit(toId, amount);
    }

    @PostMapping("/balance/withdraw")
    public void withdraw(@RequestParam("fromId") Long fromId, @RequestParam("amount") BigDecimal amount) {
        balanceService.withdraw(fromId, amount);
    }
}
