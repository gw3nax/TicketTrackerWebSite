package ru.gw3nax.tickettrackerwebsite.configuration.properties;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "kafka-consumer")
public record KafkaConsumerProperties(
        @NotEmpty
        String bootstrapServer,
        @NotNull
        Integer fetchMaxByte,
        @NotNull
        Integer maxPollRecords,
        @NotNull
        Integer maxPollInterval,
        @NotNull
        Boolean enableAutoCommit,
        @NotNull
        Boolean allowAutoCreate,
        @NotEmpty
        String isolationLevel,
        @NotEmpty
        String groupId,
        TopicProp topicProp,
        Credential credential
) {

    public record Credential(
            @NotEmpty String username,
            @NotEmpty String password
    ) {
    }
    public record TopicProp(
            @NotEmpty
            String name,
            @NotNull
            Integer partitions,
            @NotNull
            Integer replicas
    ) {

    }
}
