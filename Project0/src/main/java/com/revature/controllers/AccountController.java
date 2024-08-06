package com.revature.controllers;

import com.revature.exception.ClientErrorException;
import com.revature.exception.DuplicateNameException;
import com.revature.exception.UnAuthorizedException;
import com.revature.models.Account;
import com.revature.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
@CrossOrigin
public class AccountController {
    AccountService as;

    @Autowired
    public AccountController(AccountService as) {this.as = as;}

    // Login to account
    @PostMapping(value = "/login")
    public @ResponseBody ResponseEntity<Account> accountLogin(@RequestBody Account a) throws UnAuthorizedException {
        return ResponseEntity.status(200).body(as.login(a));
    }

    // Create new account
    @PostMapping(value = "/register", consumes = "application/json", produces ="application/json")
    public @ResponseBody ResponseEntity<Account> addAccount(@RequestBody Account a) throws ClientErrorException, DuplicateNameException {
        return ResponseEntity.status(200).body(as.createAccount(a));
    }
}
