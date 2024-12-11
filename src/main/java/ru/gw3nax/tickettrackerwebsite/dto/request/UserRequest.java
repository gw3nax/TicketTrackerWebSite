package ru.gw3nax.tickettrackerwebsite.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Request object for registering a new user")
public class UserRequest {

    @Schema(description = "User's email address", example = "user@example.com")
    private String email;

    @Schema(description = "User's password", example = "strongpassword123")
    private String password;
}
