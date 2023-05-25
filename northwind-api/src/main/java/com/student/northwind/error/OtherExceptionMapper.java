package com.student.northwind.error;

import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

public class OtherExceptionMapper implements ErrorMapper<ErrorCodeException> {
    @Override
    public Mono http4xxMapper(ClientResponse clientResponse) {
        return null;
    }

    @Override
    public Mono http5xxMapper(ClientResponse clientResponse) {
        return null;
    }
}
