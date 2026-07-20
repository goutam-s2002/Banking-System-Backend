package com.bankingsystem.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.bankingsystem.dto.AccountResponse;
import com.bankingsystem.entity.Account;
import com.bankingsystem.entity.AccountStatus;
import com.bankingsystem.entity.User;
import com.bankingsystem.repository.AccountRepository;
import com.bankingsystem.repository.UserRepository;
import com.bankingsystem.repository.TransactionRepository;
import com.bankingsystem.entity.Transaction;

import java.util.List;
import java.util.Date;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {

    // Added: SLF4J Logger (30-06-2026)
    private static final Logger log = LoggerFactory.getLogger(AccountService.class);

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AuditService auditService;
    private final TransactionRepository transactionRepository;

    private AccountResponse mapToResponse(Account account) {
        AccountResponse response = new AccountResponse();
        response.setAccountId(account.getId());
        response.setAccountNumber(account.getAccountNumber());
        response.setAccountType(account.getAccountType());
        response.setBalance(account.getBalance());
        response.setOwnerName(account.getUser().getName());
        response.setOwnerEmail(account.getUser().getEmail());
        response.setStatus(account.getStatus() != null ? account.getStatus().toString() : null);
        return response;
    }

    public List<AccountResponse> getAccountsByEmail(String email) {
        log.info("Fetching accounts for user email={}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return accountRepository.findByUser(user)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<AccountResponse> getAccountsByUserId(Long userId) {
        log.info("Fetching accounts for userId={}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return accountRepository.findByUser(user)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Modified: Default Account Status ACTIVE (01-07-2026)
    public AccountResponse createAccount(Long userId, String accountType, double initialBalance) {

        log.info("Create Account Request | userId={} accountType={} initialBalance={}", userId, accountType, initialBalance);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        log.info("User Found | Name={} Email={}", user.getName(), user.getEmail());

        Account account = new Account();
        account.setAccountNumber(generateAccountNumber());
        account.setAccountType(accountType);
        account.setBalance(initialBalance);
        account.setStatus(AccountStatus.ACTIVE);   // New
        account.setUser(user);

        Account savedAccount = accountRepository.save(account);

        if (initialBalance > 0) {
            Transaction txn = new Transaction();
            txn.setType("Account Opening Balance");
            txn.setAmount(initialBalance);
            txn.setDate(new Date());
            txn.setToAccountId(savedAccount.getId());
            transactionRepository.save(txn);
        }

        auditService.saveLog(user.getEmail(), "ACCOUNT CREATED WITH INITIAL BALANCE ₹" + initialBalance);

        log.info("Account Created Successfully | AccountNo={} Status={} Balance={}",
                savedAccount.getAccountNumber(),
                savedAccount.getStatus(),
                savedAccount.getBalance());

        return mapToResponse(savedAccount);
    }

    private String generateAccountNumber() {
        return "ACC" + System.currentTimeMillis();
    }

    // Added: Balance Check Logging (30-06-2026)
    public double getBalance(String email, Long accountId) {

        log.info("Balance Check Request | Email={} AccountId={}", email, accountId);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Account account = accountRepository
                .findByIdAndUser(accountId, user)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        log.info("Balance Retrieved | AccountId={} Balance={}", accountId, account.getBalance());

        return account.getBalance();
    }

    // Added: Delete Account Logging (30-06-2026)
    public String deleteAccount(Long accountId) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        accountRepository.delete(account);
        auditService.saveLog(account.getUser().getEmail(), "ACCOUNT DELETED");

        log.info("Account Deleted Successfully | AccountId={}", accountId);

        return "Account Deleted Successfully";
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

    public List<AccountResponse> getAllAccounts() {
        log.info("Fetching all accounts in the system");
        return accountRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
}