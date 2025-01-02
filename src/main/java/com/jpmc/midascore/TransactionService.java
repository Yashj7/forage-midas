package com.jpmc.midascore;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.User;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionService {
    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    private final UserRepository userRepository;
    private final TransactionRecordRepository transactionRecordRepository;

    public TransactionService(UserRepository userRepository, TransactionRecordRepository transactionRecordRepository) {
        this.userRepository = userRepository;
        this.transactionRecordRepository = transactionRecordRepository;
    }

    @Transactional
    public void processTransaction(Long senderId, Long recipientId, float amount) {
        User sender = userRepository.findById(senderId).orElse(null);
        User recipient = userRepository.findById(recipientId).orElse(null);

        if (isTransactionValid(sender, recipient, amount)) {
            processValidTransaction(sender, recipient, amount);
        } else {
            logger.warn("Invalid transaction: senderId={}, recipientId={}, amount={}", senderId, recipientId, amount);
        }
    }

    private boolean isTransactionValid(User sender, User recipient, float amount) {
        if (amount <= 0) {
            logger.warn("Transaction amount must be greater than zero: {}", amount);
            return false;
        }
        if (sender == null || recipient == null) {
            logger.warn("Sender or recipient not found.");
            return false;
        }
        if (sender.getBalance() < amount) {
            // logger.warn("Insufficient balance for senderId={}", sender.getId());
            logger.info("Sender balance:{}, amount:{}",sender.getBalance(),amount);
            return false;
        }
        logger.info("Sender balance:{}, amount:{}",sender.getBalance(),amount);
        return true;
    }

    private void processValidTransaction(User sender, User recipient, float amount) {
        sender.setBalance(sender.getBalance()-amount);
        recipient.setBalance(recipient.getBalance()+amount);

        TransactionRecord transactionRecord = new TransactionRecord();
        transactionRecord.setSender(sender);
        transactionRecord.setRecipient(recipient);
        transactionRecord.setAmount(amount);
        transactionRecordRepository.save(transactionRecord);

        userRepository.save(sender);
        userRepository.save(recipient);

        logger.info("Sender balance:{}, Receiver balance:{}",sender.getBalance(),recipient.getBalance());
    }
}