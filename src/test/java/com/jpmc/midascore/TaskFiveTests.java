package com.jpmc.midascore;

import com.jpmc.midascore.foundation.Balance;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
public class TaskFiveTests {
    static final Logger logger = LoggerFactory.getLogger(TaskFiveTests.class);

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private UserPopulator userPopulator;

    @Autowired
    private FileLoader fileLoader;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private BalanceQuerier balanceQuerier;


    @Test
    void task_five_verifier() throws InterruptedException {
        userPopulator.populate();
        String[] transactionLines = fileLoader.loadStrings("/test_data/rueiwoqp.tyruei");
        for (String transactionLine : transactionLines) {
            kafkaProducer.send(transactionLine);
            try {
                String[] parts = transactionLine.split(",");
                if (parts.length != 3) {
                    logger.warn("Invalid transaction format: {}", transactionLine);
                    continue;
                }

                Long senderId = Long.parseLong(parts[0].trim());
                Long recipientId = Long.parseLong(parts[1].trim());
                float amount = Float.parseFloat(parts[2].trim());

                transactionService.processTransaction(senderId, recipientId, amount);
            } catch (Exception e) {
                logger.error("Error processing transaction line: {}, reason: {}", transactionLine, e.getMessage(), e);
            }
        }
        Thread.sleep(2000);

        logger.info("----------------------------------------------------------");
        logger.info("----------------------------------------------------------");
        logger.info("----------------------------------------------------------");
        logger.info("submit the following output to complete the task (include begin and end output denotations)");
        StringBuilder output = new StringBuilder("\n").append("---begin output ---").append("\n");
        for (int i = 0; i < 13; i++) {
            Balance balance = balanceQuerier.query((long) i);
            output.append(balance.toString()).append("\n");
        }
        output.append("---end output ---");
        logger.info(output.toString());
    }
}
