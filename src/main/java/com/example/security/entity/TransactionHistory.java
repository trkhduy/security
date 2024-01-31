package com.example.security.entity;

import com.example.security.config.AesEncryptor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "transactionHistory")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TransactionHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String transactionId;
    @Convert(converter = AesEncryptor.class)
    private String account;
    private Double inDebt;
    private Double have;
    private Date time;

}
