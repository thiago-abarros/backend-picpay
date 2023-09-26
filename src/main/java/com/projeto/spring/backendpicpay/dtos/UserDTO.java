package com.projeto.spring.backendpicpay.dtos;

import com.projeto.spring.backendpicpay.domain.user.UserType;

import java.math.BigDecimal;

public record UserDTO(String firstName, String lastName, String document, BigDecimal balance, String email, String password, UserType userType) {
}
