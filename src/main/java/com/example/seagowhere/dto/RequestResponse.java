package com.example.seagowhere.dto;

import com.example.seagowhere.Model.EnumRole;
import com.example.seagowhere.Model.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true) // ignore any properties in JSON input that are not bound to any fields during deserialization.
@JsonInclude(JsonInclude.Include.NON_NULL)   // ignored fields that are empty or null during serialization
public class RequestResponse {

    private String message;
    private String token;
    private String refreshToken;
    private String expirationTime;
    private String name;
    private String email;
    private EnumRole role;
    private String password;
    private User users;

}
