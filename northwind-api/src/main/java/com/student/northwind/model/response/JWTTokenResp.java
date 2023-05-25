package com.student.northwind.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JWTTokenResp {
    private String idToken;

    public JWTTokenResp(String idToken) {
        this.idToken = idToken;
    }

    @JsonProperty("id_token")
    String getIdToken() {
        return idToken;
    }

    void setIdToken(String idToken) {
        this.idToken = idToken;
    }
}
