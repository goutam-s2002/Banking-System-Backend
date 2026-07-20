package com.bankingsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
	
	private String accessToken;
	private String refreshToken;
	private String name;
	private String email;
	private Long userId;

}
