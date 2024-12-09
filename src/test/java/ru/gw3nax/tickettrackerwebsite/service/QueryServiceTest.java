package ru.gw3nax.tickettrackerwebsite.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.ConversionService;
import ru.gw3nax.tickettrackerwebsite.dto.request.Action;
import ru.gw3nax.tickettrackerwebsite.dto.request.FlightRequest;
import ru.gw3nax.tickettrackerwebsite.entity.FlightRequestEntity;
import ru.gw3nax.tickettrackerwebsite.entity.UserEntity;
import ru.gw3nax.tickettrackerwebsite.exception.exceptions.FlightNotFoundException;
import ru.gw3nax.tickettrackerwebsite.producer.QueryProducer;
import ru.gw3nax.tickettrackerwebsite.repository.FlightRequestRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class QueryServiceTest {

    private final UserEntity mockUser = UserEntity.builder()
            .id(1L)
            .email("test@example.com")
            .build();
    @Mock
    private FlightRequestRepository flightRequestRepository;
    @Mock
    private QueryProducer queryProducer;
    @Mock
    private ConversionService conversionService;
    @Mock
    private CityService cityService;
    @Mock
    private UserService userService;
    @InjectMocks
    private QueryService queryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void postQuery_ShouldSaveFlightRequestAndSendMessage() {
        var flightRequest = FlightRequest.builder()
                .fromPlace("Москва")
                .toPlace("Владивосток")
                .fromDate(LocalDate.of(2024, 12, 12))
                .toDate(LocalDate.of(2024, 12, 20))
                .currency("RUB")
                .price(BigDecimal.valueOf(20000))
                .action(Action.POST)
                .build();

        when(cityService.getIataCode("Москва")).thenReturn("MOW");
        when(cityService.getIataCode("Владивосток")).thenReturn("VVO");
        when(userService.getCurrentUser()).thenReturn(mockUser);
        FlightRequestEntity mockEntity = new FlightRequestEntity();
        when(conversionService.convert(any(FlightRequest.class), eq(FlightRequestEntity.class)))
                .thenReturn(mockEntity);

        queryService.postQuery(flightRequest);

        verify(cityService, times(2)).getIataCode(anyString());
        verify(queryProducer).sendMessage(any(FlightRequest.class));
        verify(flightRequestRepository).save(mockEntity);
    }

    @Test
    void deleteQuery_ShouldDeleteFlightRequestAndSendMessage() {
        Long queryId = 1L;
        FlightRequestEntity flightRequestEntity = new FlightRequestEntity();
        flightRequestEntity.setId(queryId);
        when(flightRequestRepository.findById(queryId)).thenReturn(Optional.of(flightRequestEntity));
        FlightRequest mockFlightRequest = FlightRequest.builder()
                .id(queryId)
                .action(Action.DELETE)
                .build();
        when(conversionService.convert(flightRequestEntity, FlightRequest.class))
                .thenReturn(mockFlightRequest);

        queryService.deleteQuery(queryId);

        verify(flightRequestRepository).deleteById(queryId);
        verify(queryProducer).sendMessage(mockFlightRequest);
    }

    @Test
    void deleteQuery_ShouldThrowException_WhenFlightNotFound() {
        Long queryId = 1L;
        when(flightRequestRepository.findById(queryId)).thenReturn(Optional.empty());

        assertThrows(FlightNotFoundException.class, () -> queryService.deleteQuery(queryId));
        verify(flightRequestRepository, never()).deleteById(any());
    }

    @Test
    void getQueriesFromCurrentUser_ShouldReturnListOfFlightRequests() {
        when(userService.getCurrentUser()).thenReturn(mockUser);
        FlightRequestEntity entity = new FlightRequestEntity();
        entity.setId(1L);
        entity.setFromPlace("MOW");
        entity.setToPlace("VVO");
        when(flightRequestRepository.findAllByUserId(mockUser.getId()))
                .thenReturn(List.of(entity));
        FlightRequest flightRequest = FlightRequest.builder()
                .id(1L)
                .fromPlace("MOW")
                .toPlace("VVO")
                .build();
        when(conversionService.convert(entity, FlightRequest.class)).thenReturn(flightRequest);

        List<FlightRequest> result = queryService.getQueriesFromCurrentUser();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("MOW", result.get(0).getFromPlace());
        verify(flightRequestRepository).findAllByUserId(mockUser.getId());
    }
}
