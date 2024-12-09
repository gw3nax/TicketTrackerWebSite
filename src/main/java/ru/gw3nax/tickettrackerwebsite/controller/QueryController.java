package ru.gw3nax.tickettrackerwebsite.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.gw3nax.tickettrackerwebsite.dto.request.FlightRequest;
import ru.gw3nax.tickettrackerwebsite.service.QueryService;

@Controller
@RequestMapping("/flights")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Flight Queries", description = "Endpoints for managing flight queries")
public class QueryController {

    private final QueryService queryService;

    @Operation(summary = "Get search flights page", description = "Returns the search page for flights")
    @GetMapping()
    public String getFlights() {
        return "search";
    }

    @Operation(summary = "Get search flights page", description = "Returns the search page for flights")
    @GetMapping("/search")
    public String getSearchFlightsPage() {
        return "search";
    }

    @Operation(summary = "Submit a flight query", description = "Submit a new flight query for the current user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Redirects to the user home page after submission"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @PostMapping("/search")
    public String postQuery(@RequestBody FlightRequest flightRequest) {
        queryService.postQuery(flightRequest);
        return "redirect:/users/home";
    }

    @Operation(summary = "View all flight queries", description = "View all flight queries for the current user")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved flight queries")
    @GetMapping("/queries")
    public String getQueriesPage(Model model) {
        model.addAttribute("tickets", queryService.getQueriesFromCurrentUser());
        return "queries";
    }

    @Operation(summary = "Delete a flight query", description = "Delete a flight query by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Redirects to the queries page after deletion"),
            @ApiResponse(responseCode = "404", description = "Query not found")
    })
    @DeleteMapping("/queries")
    public String deleteQuery(@RequestParam @Parameter(description = "ID of the query to delete") Long queryId) {
        log.info("Deleting query {}", queryId);
        queryService.deleteQuery(queryId);
        return "redirect:/flights/queries";
    }
}
