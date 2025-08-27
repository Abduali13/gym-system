package com.company.gym_system.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class TransactionLogger {

    public String startTransaction(String endpoint, Object request) {
        String txId = UUID.randomUUID().toString();
        log.info("TX_START | txId={} | endpoint={} | request={}", txId, endpoint, request);
        return txId;
    }
    public void success(String txId, Object response) {
        log.info("TX_SUCCESS | txId={} | response={}", txId, response);
    }
    public void error(String txId, Exception ex) {
        log.error("TX_ERROR | txId={} | error={}", txId, ex.getMessage(), ex);
    }
}