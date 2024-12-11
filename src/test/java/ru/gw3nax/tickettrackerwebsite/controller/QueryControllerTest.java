package ru.gw3nax.tickettrackerwebsite.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import ru.gw3nax.tickettrackerwebsite.dto.request.FlightRequest;
import ru.gw3nax.tickettrackerwebsite.service.QueryService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class QueryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private QueryService queryService;

    @InjectMocks
    private QueryController queryController;

    private FlightRequest flightRequest;

    @BeforeEach
    void setUp() {
        flightRequest = new FlightRequest();
        flightRequest.setFromPlace("Москва");
        flightRequest.setToPlace("Владивосток");
        flightRequest.setFromDate(LocalDate.of(2024, 12, 12));
        flightRequest.setToDate(LocalDate.of(2024, 12, 20));
        flightRequest.setCurrency("RUB");
        flightRequest.setPrice(BigDecimal.valueOf(20000));

        mockMvc = MockMvcBuilders.standaloneSetup(queryController)
                .setViewResolvers(new InternalResourceViewResolver("/templates/", ".html"))
                .build();
    }

    @Test
    void getFlights_ShouldReturnSearchPage() throws Exception {
        mockMvc.perform(get("/flights"))
                .andExpect(status().isOk())
                .andExpect(view().name("search"));
    }

    @Test
    void postQuery_ShouldRedirectToHomePage() throws Exception {
        mockMvc.perform(post("/flights/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "fromPlace": "Москва",
                                    "toPlace": "Владивосток",
                                    "fromDate": "2024-12-12",
                                    "toDate": "2024-12-20",
                                    "currency": "RUB",
                                    "price": 20000
                                }
                                """))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/home"));

        verify(queryService, times(1)).postQuery(any(FlightRequest.class));
    }

    @Test
    void getSearchFlightsPage_ShouldReturnSearchPage() throws Exception {
        mockMvc.perform(get("/flights/search"))
                .andExpect(status().isOk())
                .andExpect(view().name("search"));
    }

    @Test
    void getQueriesPage_ShouldReturnQueriesPage() throws Exception {
        when(queryService.getQueriesFromCurrentUser()).thenReturn(List.of(new FlightRequest()));

        mockMvc.perform(get("/flights/queries"))
                .andExpect(status().isOk())
                .andExpect(view().name("queries"))
                .andExpect(model().attributeExists("tickets"));
    }

    @Test
    void deleteQuery_ShouldRedirectToQueriesPage() throws Exception {
        mockMvc.perform(delete("/flights/queries")
                        .param("queryId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/flights/queries"));

        verify(queryService, times(1)).deleteQuery(1L);
    }
}
