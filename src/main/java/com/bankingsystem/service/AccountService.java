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

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {

    // Added: SLF4J Logger (30-06-2026)
    private static final Logger log = LoggerFactory.getLogger(AccountService.class);

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AuditService auditService;

    // Modified: Default Account Status ACTIVE (01-07-2026)
    public AccountResponse createAccount(Long userId, String accountType) {

        log.info("Create Account Request | userId={} accountType={}", userId, accountType);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        log.info("User Found | Name={} Email={}", user.getName(), user.getEmail());

        Account account = new Account();
        account.setAccountNumber(generateAccountNumber());
        account.setAccountType(accountType);
        account.setBalance(0.0);
        account.setStatus(AccountStatus.ACTIVE);   // New
        account.setUser(user);

        Account savedAccount = accountRepository.save(account);
        auditService.saveLog(user.getEmail(), "ACCOUNT CREATED");

        log.info("Account Created Successfully | AccountNo={} Status={}",
                savedAccount.getAccountNumber(),
                savedAccount.getStatus());

        AccountResponse response = new AccountResponse();
        response.setAccountId(savedAccount.getId());
        response.setAccountNumber(savedAccount.getAccountNumber());
        response.setAccountType(savedAccount.getAccountType());
        response.setBalance(savedAccount.getBalance());
        response.setOwnerName(user.getName());
        response.setOwnerEmail(user.getEmail());

        return response;
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
}