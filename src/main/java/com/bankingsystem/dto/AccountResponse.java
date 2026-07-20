package com.bankingsystem.dto;

import lombok.Data;

@Data
public class AccountResponse {
	
	private Long accountId;
    private String accountNumber;
    private String accountType;
    private double balance;

    private String ownerName;
    private String ownerEmail;
    private String status;

}
