package com.example.security.controller;

import com.example.security.config.RSAUtils;
import com.example.security.dto.TransactionEncrypted;
import com.example.security.dto.TransactionRequest;
import com.example.security.entity.TransactionHistory;
import com.example.security.service.TransactionHistoryServiceImpl;
import com.example.security.utils.LogMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Transaction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for handling transactions, providing encryption and saving functionality.
 */
@RestController
@Slf4j
@RequestMapping("api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {


    private final TransactionHistoryServiceImpl service;


    /**
     * Endpoint for encrypting transaction data.
     *
     * @param request The TransactionRequest containing data to be encrypted.
     * @return ResponseEntity with the encrypted data and HttpStatus OK.
     */
    @PostMapping("/encrypt")
    public ResponseEntity<String> encryptData(@Validated @RequestBody TransactionRequest request){
        String logMessage = "(create) Request : {}" + request;
        log.info(LogMessage.logInfo(logMessage));
        return new ResponseEntity<>(service.encrypt(request), HttpStatus.OK);
    }

    /**
     * Endpoint for saving decrypted transaction data.
     *
     * @param encryptedData The TransactionEncrypted object containing encrypted data.
     * @return ResponseEntity with the decrypted and saved transaction data and HttpStatus CREATED.
     */
    @PostMapping("/save")
    public ResponseEntity<List<TransactionHistory>> save(@RequestBody TransactionEncrypted encryptedData){
        TransactionRequest decryptedData = service.decrypt(encryptedData);
        return new ResponseEntity<>(service.save(decryptedData),HttpStatus.CREATED);
    }

}
