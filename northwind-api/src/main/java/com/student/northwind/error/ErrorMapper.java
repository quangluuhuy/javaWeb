package com.student.northwind.error;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

public interface ErrorMapper<Exc extends Throwable> {
    Mono<Exc> http4xxMapper(ClientResponse clientResponse);
    Mono<Exc> http5xxMapper(ClientResponse clientResponse);
}
