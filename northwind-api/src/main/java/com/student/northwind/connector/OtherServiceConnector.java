package com.student.northwind.connector;

import com.fasterxml.jackson.databind.JsonNode;
import com.student.northwind.config.ApplicationProperties;
import com.student.northwind.error.ErrorCodeException;
import com.student.northwind.error.OtherExceptionMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class OtherServiceConnector extends BaseClient<ErrorCodeException, OtherExceptionMapper> {
    public OtherServiceConnector(@Value("${other-service.base-url}") String baseUrl,
                                    ApplicationProperties appProp) {
        super(baseUrl,
                appProp,
                httpHeaders -> {
            //add more default header here: example Basic Authen, secret key
        }, new OtherExceptionMapper());
    }

    public Mono<JsonNode> getDevice(String deviceCode) {
        log.info("Get device by device code: {}", deviceCode);
        return query("/devices/{deviceCode}", deviceCode, HttpMethod.GET, JsonNode.class);
    }
}
