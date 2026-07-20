package com.bankingsystem.controller;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bankingsystem.dto.TransactionResponse;
import com.bankingsystem.service.TransactionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/deposit")
    public String deposit(@RequestParam Long accountId,
                          @RequestParam double amount,
                          Principal principal) {
    	
    	System.out.println(">>> Deposit Controller Called");

        return transactionService.deposit(accountId, amount, principal.getName());
    }
    
    @PostMapping("/withdraw")
    public String withdraw(@RequestParam Long accountId,
                           @RequestParam double amount,
                           Principal principal) {

        return transactionService.withdraw(accountId, amount, principal.getName());
    }
    
    @PostMapping("/transfer")
    public String transfer(@RequestParam Long fromAccountId,
                           @RequestParam String toAccountNumber,
                           @RequestParam double amount,
                           Principal principal) {
    	System.out.println("Transfer API called");

        return transactionService.transfer(fromAccountId, toAccountNumber, amount, principal.getName());
    }
    
    @GetMapping("/history")
    public List<TransactionResponse> getHistory(
            @RequestParam Long accountId,
            @RequestParam int page,
            @RequestParam int size,
            Principal principal) {

        return transactionService.getHistory(accountId, page, size, principal.getName());
    }
    
    @GetMapping("/statement")
    public List<TransactionResponse> getStatement(
            @RequestParam Long accountId,
            @RequestParam String from,
            @RequestParam String to,
            Principal principal) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date fromDate = sdf.parse(from);
        Date toDate = sdf.parse(to);

        return transactionService.getStatement(accountId, fromDate, toDate, principal.getName());
    }
}