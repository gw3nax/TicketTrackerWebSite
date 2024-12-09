package ru.gw3nax.tickettrackerwebsite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.gw3nax.tickettrackerwebsite.configuration.properties.ApplicationConfig;
import ru.gw3nax.tickettrackerwebsite.configuration.properties.KafkaConsumerProperties;
import ru.gw3nax.tickettrackerwebsite.configuration.properties.KafkaProducerProperties;

@SpringBootApplication
@EnableConfigurationProperties({KafkaConsumerProperties.class, KafkaProducerProperties.class, ApplicationConfig.class})
public class TicketTrackerWebSiteApplication {

    public static void main(String[] args) {
        SpringApplication.run(TicketTrackerWebSiteApplication.class, args);
    }
}
