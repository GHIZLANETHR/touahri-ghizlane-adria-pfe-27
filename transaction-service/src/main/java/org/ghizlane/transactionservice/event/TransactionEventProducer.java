package org.ghizlane.transactionservice.event;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TransactionEventProducer {

    private static final String TOPIC =
            "bank.transactions.created";

    private final KafkaTemplate<
            String,
            TransactionCreatedEvent
            > kafkaTemplate;

    public TransactionEventProducer(
            KafkaTemplate<String, TransactionCreatedEvent>
                    kafkaTemplate
    ) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(TransactionCreatedEvent event) {
        kafkaTemplate.send(
                TOPIC,
                event.accountId().toString(),
                event
        );
    }
}