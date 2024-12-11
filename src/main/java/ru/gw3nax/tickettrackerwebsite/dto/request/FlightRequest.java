package ru.gw3nax.tickettrackerwebsite.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Request object for submitting a flight query")
public class FlightRequest {

    @Schema(description = "Unique ID of the flight query", example = "1")
    private Long id;

    @Schema(description = "Action to be performed", example = "POST")
    private Action action;

    @Schema(description = "User ID of the person creating the query", example = "123")
    private String userId;

    @Schema(description = "Origin city or airport", example = "Владивосток")
    private String fromPlace;

    @Schema(description = "Destination city or airport", example = "Москва")
    private String toPlace;

    @Schema(description = "Start date of the flight", example = "2024-12-12")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fromDate;

    @Schema(description = "End date of the flight", example = "2024-12-20")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate toDate;

    @Schema(description = "Currency for the flight price", example = "RUB")
    private String currency;

    @Schema(description = "Price of the flight", example = "20000")
    private BigDecimal price;
}
