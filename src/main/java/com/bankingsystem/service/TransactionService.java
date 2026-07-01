package com.bankingsystem.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.bankingsystem.dto.TransactionResponse;
import com.bankingsystem.entity.Account;
import com.bankingsystem.entity.AccountStatus;
import com.bankingsystem.entity.Transaction;
import com.bankingsystem.exception.AccountNotFoundException;
import com.bankingsystem.repository.AccountRepository;
import com.bankingsystem.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {

    // Added: SLF4J Logger (30-06-2026)
    private static final Logger log = LoggerFactory.getLogger(TransactionService.class);
    private final AuditService auditService;
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    // Added: Deposit Logging (30-06-2026)
 // Modified: Deposit with Account Status Validation (01-07-2026)
    public String deposit(Long accountId, double amount) {

        log.info("Deposit Request | AccountId={} Amount={}", accountId, amount);

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        // Added: Account Status Validation (01-07-2026)
        if (account.getStatus() != AccountStatus.ACTIVE) {

            log.warn("Deposit Failed | Account is {}", account.getStatus());

            throw new RuntimeException(
                    "Transaction not allowed. Account is " + account.getStatus());
        }

        account.setBalance(account.getBalance() + amount);

        Transaction txn = new Transaction();
        txn.setType("DEPOSIT");
        txn.setAmount(amount);
        txn.setDate(new Date());
        txn.setToAccountId(account.getId());

        transactionRepository.save(txn);
        accountRepository.save(account);
        
        auditService.saveLog(
                account.getUser().getEmail(),
                "DEPOSIT ₹" + amount);

        log.info("Deposit Successful | AccountId={} NewBalance={}",
                accountId, account.getBalance());

        return "Deposit Successful";
    }
    
 // Added: Update Account Status (01-07-2026)
    public String updateAccountStatus(Long accountId, AccountStatus status) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        account.setStatus(status);

        accountRepository.save(account);

        log.info("Account Status Updated | AccountId={} Status={}",
                accountId, status);

        return "Account Status Updated Successfully";
    }

    // Added: Withdraw Logging (30-06-2026)
 // Modified: Withdraw with Account Status Validation (01-07-2026)
    public String withdraw(Long accountId, double amount) {

        log.info("Withdraw Request | AccountId={} Amount={}", accountId, amount);

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        // Added: Account Status Validation (01-07-2026)
        if (account.getStatus() != AccountStatus.ACTIVE) {

            log.warn("Withdraw Failed | Account is {}", account.getStatus());

            throw new RuntimeException(
                    "Transaction not allowed. Account is " + account.getStatus());
        }

        if (account.getBalance() < amount) {

            log.warn("Withdraw Failed | Insufficient Balance | AccountId={}", accountId);

            throw new RuntimeException("Insufficient balance");
        }

        account.setBalance(account.getBalance() - amount);

        Transaction txn = new Transaction();
        txn.setType("WITHDRAW");
        txn.setAmount(amount);
        txn.setDate(new Date());
        txn.setFromAccountId(account.getId());

        transactionRepository.save(txn);
        accountRepository.save(account);
        
        auditService.saveLog(
                account.getUser().getEmail(),
                "WITHDRAW ₹" + amount);

        log.info("Withdraw Successful | AccountId={} RemainingBalance={}",
                accountId, account.getBalance());

        return "Withdraw Successful";
    }

    // Added: Transfer Logging (30-06-2026)
 // Modified: Transfer with Account Status Validation (01-07-2026)
    public String transfer(Long fromAccountId, Long toAccountId, double amount) {

        log.info("Transfer Request | From={} To={} Amount={}",
                fromAccountId, toAccountId, amount);

        Account from = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new RuntimeException("Sender account not found"));

        Account to = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new RuntimeException("Receiver account not found"));

        // Added: Sender Account Status Validation (01-07-2026)
        if (from.getStatus() != AccountStatus.ACTIVE) {

            log.warn("Transfer Failed | Sender Account is {}", from.getStatus());

            throw new RuntimeException(
                    "Sender account is " + from.getStatus());
        }

        // Added: Receiver Account Status Validation (01-07-2026)
        if (to.getStatus() != AccountStatus.ACTIVE) {

            log.warn("Transfer Failed | Receiver Account is {}", to.getStatus());

            throw new RuntimeException(
                    "Receiver account is " + to.getStatus());
        }

        if (from.getBalance() < amount) {

            log.warn("Transfer Failed | Insufficient Balance | AccountId={}", fromAccountId);

            throw new RuntimeException("Insufficient balance");
        }

        from.setBalance(from.getBalance() - amount);
        to.setBalance(to.getBalance() + amount);

        accountRepository.save(from);
        accountRepository.save(to);

        Transaction txn = new Transaction();
        txn.setType("TRANSFER");
        txn.setAmount(amount);
        txn.setDate(new Date());
        txn.setFromAccountId(fromAccountId);
        txn.setToAccountId(toAccountId);

        transactionRepository.save(txn);
        
        auditService.saveLog(
                from.getUser().getEmail(),
                "TRANSFER ₹" + amount + " TO " + to.getAccountNumber());

        log.info("Transfer Successful | From={} To={} Amount={}",
                fromAccountId, toAccountId, amount);

        return "Transfer Successful";
    }

    // Added: Mini Statement Logging (30-06-2026)
    public List<TransactionResponse> getHistory(Long accountId, int page, int size) {

        log.info("Mini Statement Request | AccountId={} Page={} Size={}",
                accountId, page, size);

        accountRepository.findById(accountId)
                .orElseThrow(() ->
                        new AccountNotFoundException("Account not found with id: " + accountId));

        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());

        Page<Transaction> pageResult =
                transactionRepository.findByFromAccountIdOrToAccountId(
                        accountId,
                        accountId,
                        pageable
                );

        List<Transaction> transactions = pageResult.getContent();

        return transactions.stream().map(txn -> {

            TransactionResponse res = new TransactionResponse();
            res.setType(txn.getType());
            res.setAmount(txn.getAmount());
            res.setDate(txn.getDate());

            if (txn.getToAccountId() != null &&
                    txn.getToAccountId().equals(accountId)) {

                res.setDirection("CREDIT");

            } else {

                res.setDirection("DEBIT");

            }

            return res;

        }).toList();
    }

    // Added: Bank Statement Logging (30-06-2026)
    public List<TransactionResponse> getStatement(Long accountId, Date from, Date to) {

        log.info("Statement Request | AccountId={} From={} To={}",
                accountId, from, to);

        accountRepository.findById(accountId)
                .orElseThrow(() ->
                        new AccountNotFoundException("Account not found with id: " + accountId));

        List<Transaction> transactions =
                transactionRepository.findByFromAccountIdOrToAccountIdAndDateBetween(
                        accountId,
                        accountId,
                        from,
                        to
                );

        return transactions.stream().map(txn -> {

            TransactionResponse res = new TransactionResponse();
            res.setType(txn.getType());
            res.setAmount(txn.getAmount());
            res.setDate(txn.getDate());

            if (txn.getToAccountId() != null &&
                    txn.getToAccountId().equals(accountId)) {

                res.setDirection("CREDIT");

            } else {

                res.setDirection("DEBIT");

            }

            return res;

        }).toList();
    }

}