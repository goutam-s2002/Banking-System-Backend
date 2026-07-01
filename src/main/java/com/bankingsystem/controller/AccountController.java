package com.bankingsystem.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bankingsystem.dto.AccountResponse;
import com.bankingsystem.entity.AccountStatus;
import com.bankingsystem.service.AccountService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/create")
    public AccountResponse createAccount(
            @RequestParam Long userId,
            @RequestParam String accountType) {


        System.out.println("Controller userId = " + userId);

        return accountService.createAccount(userId, accountType);
    }

    @GetMapping("/balance")
    public double getBalance(@RequestParam Long accountId,
                             Principal principal) {

        return accountService.getBalance(
                principal.getName(),
                accountId
        );
    }

    @DeleteMapping("/delete")
    public String deleteAccount(@RequestParam Long accountId) {

        return accountService.deleteAccount(accountId);
    }

    // Added: Update Account Status API (01-07-2026)
    @PutMapping("/status/{accountId}")
    public String updateAccountStatus(
            @PathVariable Long accountId,
            @RequestBody AccountStatusRequest request) {

        return accountService.updateAccountStatus(
                accountId,
                request.getStatus());
    }

    // Added: Request DTO for Account Status (01-07-2026)
    static class AccountStatusRequest {

        private AccountStatus status;

        public AccountStatus getStatus() {
            return status;
        }

        public void setStatus(AccountStatus status) {
            this.status = status;
        }
    }

}