package domclick.controller;

import domclick.exception.AccountNotFoundException;
import domclick.model.Account;
import domclick.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class AccountController {

    private final AccountService service;

    @Autowired
    public AccountController(AccountService service) {
        this.service = service;
    }

    @PostMapping("/account")
    public void create(@RequestParam("name") String name) {
        service.create(name);
    }

    @GetMapping("/account")
    public Account account(@RequestParam("id") Long id) {
        return service.get(id).orElseThrow(() -> new AccountNotFoundException(id));
    }

    @DeleteMapping("/account")
    public void delete(@RequestParam("id") Long id) {
        service.delete(id);
    }
}
