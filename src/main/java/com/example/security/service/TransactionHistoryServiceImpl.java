package com.example.security.service;

import com.example.security.config.RSAUtils;
import com.example.security.dto.TransactionEncrypted;
import com.example.security.dto.TransactionRequest;
import com.example.security.entity.TransactionHistory;
import com.example.security.repository.TransactionHistoryRepository;

import com.example.security.utils.LogMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Service class for managing transaction history, providing functionality to save transaction data.
 */
@Service
@Slf4j
public class TransactionHistoryServiceImpl {
    @Autowired
    private RSAUtils rsaUtils;
    @Autowired
    private TransactionHistoryRepository repository;

    /**
     * Saves transaction data for both the sender and recipient.
     *
     * @param data The TransactionRequest containing transaction details.
     * @return List of TransactionHistory objects representing the saved transactions.
     * @throws DataIntegrityViolationException if there is an error saving transaction data due to data integrity violation.
     * @throws RuntimeException                if there is an unexpected error during the transaction data saving process.
     */
    public List<TransactionHistory> save(TransactionRequest data) {
        List<TransactionHistory> transactions = new ArrayList<>();
        // Save transaction data for the sender
        this.validate(data.getSenderAccount(), data.getRecipientAccount());
        Date date = new Date();
        try {
            TransactionHistory sender = new TransactionHistory();
            sender.setTransactionId(data.getTransactionId());
            sender.setInDebt(data.getAmount());
            sender.setHave(0.0);
            sender.setAccount(data.getSenderAccount());
            sender.setTime(date);
            transactions.add(repository.save(sender));
        } catch (DataIntegrityViolationException ex) {
            log.error(LogMessage.logInfo("Error saving transaction data: " + ex.getMessage()));
            throw new DataIntegrityViolationException("Error saving transaction data", ex);
        } catch (Exception ex) {
            log.error(LogMessage.logInfo("Unexpected error: " + ex.getMessage()));
            throw new RuntimeException();
        }
        // Save transaction data for the recipient
        try {
            TransactionHistory recipient = new TransactionHistory();
            recipient.setTransactionId(data.getTransactionId());
            recipient.setInDebt(0.0);
            recipient.setHave(data.getAmount());
            recipient.setAccount(data.getRecipientAccount());
            recipient.setTime(date);
            transactions.add(repository.save(recipient));
        } catch (DataIntegrityViolationException ex) {
            log.error(LogMessage.logInfo("Error saving transaction data: " + ex.getMessage()));
            throw new DataIntegrityViolationException("Error saving transaction data", ex);
        } catch (Exception ex) {
            log.error(LogMessage.logInfo("Unexpected error: " + ex.getMessage()));
            throw new RuntimeException();
        }
        return transactions;
    }

    /**
     * Validate senderAccount and recipientAccount must be different
     *
     * @param senderAccount    The SenderAccount.
     * @param recipientAccount The RecipientAccount.
     * @throws IllegalArgumentException with message
     */
    public void validate(String senderAccount, String recipientAccount) {
        if (senderAccount != null && senderAccount.equals(recipientAccount)) {
            throw new IllegalArgumentException("Sender account must be different from recipient account");
        }
    }

    public TransactionRequest decrypt(TransactionEncrypted encryptedData) {
        TransactionRequest decryptedData = (TransactionRequest) rsaUtils.decrypt(encryptedData.getEncryptedData());
        return decryptedData;
    }

    public String encrypt(TransactionRequest data){
        return rsaUtils.encrypt(data);
    }
}
