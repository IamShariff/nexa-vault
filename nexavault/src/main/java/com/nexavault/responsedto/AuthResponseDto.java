package com.nexavault.responsedto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponseDto {
	private String token;
	private String message;
}