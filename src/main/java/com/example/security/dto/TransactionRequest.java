package com.example.security.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TransactionRequest implements Serializable {
    @NotBlank
    private String transactionId;
    @NotBlank
    private String senderAccount;
    @NotBlank
    private String recipientAccount;
    @NotNull
    @Min(value = 10000, message = "Amount must be greater than or equal to 10000")
    private Double amount;

}
