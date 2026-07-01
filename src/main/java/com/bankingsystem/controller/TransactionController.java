package com.bankingsystem.controller;

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
                          @RequestParam double amount) {
    	
    	System.out.println(">>> Deposit Controller Called");

        return transactionService.deposit(accountId, amount);
    }
    
    @PostMapping("/withdraw")
    public String withdraw(@RequestParam Long accountId,
                           @RequestParam double amount) {

        return transactionService.withdraw(accountId, amount);
    }
    
    @PostMapping("/transfer")
    public String transfer(@RequestParam Long fromAccountId,
                           @RequestParam Long toAccountId,
                           @RequestParam double amount) {
    	System.out.println("Transfer API called");

        return transactionService.transfer(fromAccountId, toAccountId, amount);
        
        
    }
    
    @GetMapping("/history")
    public List<TransactionResponse> getHistory(
            @RequestParam Long accountId,
            @RequestParam int page,
            @RequestParam int size) {

        return transactionService.getHistory(accountId, page, size);
    }
    
    @GetMapping("/statement")
    public List<TransactionResponse> getStatement(
            @RequestParam Long accountId,
            @RequestParam String from,
            @RequestParam String to) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date fromDate = sdf.parse(from);
        Date toDate = sdf.parse(to);

        return transactionService.getStatement(accountId, fromDate, toDate);
    }
}