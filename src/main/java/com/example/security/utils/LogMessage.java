package com.example.security.utils;

public class LogMessage {

    public static String logInfo(String log) {
        String[] fieldsToMask = {"transactionId", "senderAccount", "recipientAccount", "amount","inDebt","have","account","time"};

        String maskedLog = log;
        for (String field : fieldsToMask) {
            maskedLog = maskedLog.replaceAll(field + "=\\S+", field + "=?");
        }

        return maskedLog;
    }
}
