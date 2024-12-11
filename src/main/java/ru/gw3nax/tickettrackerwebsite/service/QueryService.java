package ru.gw3nax.tickettrackerwebsite.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import ru.gw3nax.tickettrackerwebsite.dto.request.Action;
import ru.gw3nax.tickettrackerwebsite.dto.request.FlightRequest;
import ru.gw3nax.tickettrackerwebsite.entity.FlightRequestEntity;
import ru.gw3nax.tickettrackerwebsite.exception.exceptions.FlightNotFoundException;
import ru.gw3nax.tickettrackerwebsite.producer.QueryProducer;
import ru.gw3nax.tickettrackerwebsite.repository.FlightRequestRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class QueryService {
    private final FlightRequestRepository flightRequestRepository;
    private final QueryProducer queryProducer;
    private final ConversionService conversionService;
    private final CityService cityService;
    private final UserService userService;

    public void postQuery(FlightRequest flightRequest) {
        log.info("Sending update for flight request {}", flightRequest);
        var request = FlightRequest.builder()
                .currency(flightRequest.getCurrency())
                .fromDate(flightRequest.getFromDate())
                .toDate(flightRequest.getToDate())
                .fromPlace(cityService.getIataCode(flightRequest.getFromPlace()))
                .toPlace(cityService.getIataCode(flightRequest.getToPlace()))
                .price(flightRequest.getPrice())
                .userId(userService.getCurrentUser().getId().toString())
                .action(flightRequest.getAction())
                .build();
        queryProducer.sendMessage(request);
        var requestEntity = Objects.requireNonNull(conversionService.convert(request, FlightRequestEntity.class));
        requestEntity.setUser(userService.getCurrentUser());
        flightRequestRepository.save(requestEntity);
    }
    public void deleteQuery(Long queryId) {
        log.info("Sending update for flight request {}", queryId);
        var optionalFlightRequestEntity = flightRequestRepository.findById(queryId);
        if (optionalFlightRequestEntity.isEmpty()) throw new FlightNotFoundException("No flight request found");
        var flightRequestEntity = optionalFlightRequestEntity.get();
        flightRequestRepository.deleteById(queryId);
        var flightRequest = Objects.requireNonNull(conversionService.convert(flightRequestEntity, FlightRequest.class));
        flightRequest.setAction(Action.DELETE);
        queryProducer.sendMessage(flightRequest);
    }

    public List<FlightRequest> getQueriesFromCurrentUser() {
        Long userId = userService.getCurrentUser().getId();
        List<FlightRequestEntity> entities = flightRequestRepository.findAllByUserId(userId);

        return entities.stream()
                .map(entity -> conversionService.convert(entity, FlightRequest.class))
                .collect(Collectors.toList());
    }
}

