package com.student.northwind.connector;

import com.student.northwind.config.ApplicationProperties;
import com.student.northwind.error.ErrorMapper;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.util.function.Consumer;

public abstract class BaseClient<Exc extends Throwable, Mapper extends ErrorMapper<Exc>> {

    protected WebClient client;
    protected Mapper mapper;

    protected BaseClient(String baseUrl, ApplicationProperties appProp, Consumer<HttpHeaders> headers, Mapper mapper) {
        var exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(1024 * appProp.getHttp().getByteBuffer())).build();

        this.client = WebClient.builder()
                .clientConnector(getConnector(appProp))
                .baseUrl(baseUrl)
                .exchangeStrategies(exchangeStrategies)
                .defaultHeaders(headers)
                .build();
        this.mapper = mapper;
    }

    private ClientHttpConnector getConnector(ApplicationProperties appConfig) {
        // Connection Timeout
        // Read Timeout
        var httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, appConfig.getHttp().getConnectionTimeout()) // Connection Timeout
                .doOnConnected(connection ->
                        connection.addHandlerLast(new ReadTimeoutHandler(appConfig.getHttp().getReadTimeout())) // Read Timeout
                                .addHandlerLast(new WriteTimeoutHandler(appConfig.getHttp().getWriteTimeout()))); // Write Timeout

        return new ReactorClientHttpConnector(httpClient);
    }

    public WebClient.ResponseSpec getSpec(String uri, Object... params) {
        return client.get()
                .uri(uri, params)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, mapper::http4xxMapper)
                .onStatus(HttpStatus::is5xxServerError, mapper::http5xxMapper);
    }

    public <O> Mono<O> get(String uri, Class<O> clazz, Object... params) {
        return getSpec(uri, params).bodyToMono(clazz);
    }

    public <O> Mono<O> get(String uri, ParameterizedTypeReference<O> clazz, Object... params) {
        return getSpec(uri, params).bodyToMono(clazz);
    }

    /**
     * Use for POST, PATCH, PUT
     *
     * @param uri    uri
     * @param body   object body
     * @param method {@link HttpMethod} method
     * @param clazz  {@link Class} class of response
     * @return Mono
     */
    public <O> Mono<O> query(String uri, Object body, HttpMethod method, Class<O> clazz, Object... param) {
        return client.method(method)
                .uri(uri, param)
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(body)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, mapper::http4xxMapper)
                .onStatus(HttpStatus::is5xxServerError, mapper::http5xxMapper)
                .bodyToMono(clazz)
                ;
    }


    /**
     * Use for Multipartfile
     *
     * @param uri           uri
     * @param multiPartBody MultipartBodyBuilder body
     * @param method        {@link HttpMethod} method
     * @param clazz         {@link Class} class of response
     * @return Mono
     */
    public <O> Mono<O> queryWithFile(String uri, MultipartBodyBuilder multiPartBody, HttpMethod method, Class<O> clazz, Object... param) {
        return client.method(method)
                .uri(uri, param)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(multiPartBody.build()))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, mapper::http4xxMapper)
                .onStatus(HttpStatus::is5xxServerError, mapper::http5xxMapper)
                .bodyToMono(clazz)
                ;
    }

    /**
     * Use for POST, PATCH, PUT without body
     *
     * @param uri    uri
     * @param method {@link HttpMethod} method
     * @param clazz  {@link Class} class of response
     * @return Mono
     */
    public <O> Mono<O> query(String uri, HttpMethod method, Class<O> clazz, Object... param) {
        return client.method(method)
                .uri(uri, param)
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, mapper::http4xxMapper)
                .onStatus(HttpStatus::is5xxServerError, mapper::http5xxMapper)
                .bodyToMono(clazz)
                ;
    }

    /**
     * Use for POST, PATCH, PUT without body
     *
     * @param uri    uri
     * @param method {@link HttpMethod} method
     * @param clazz  {@link Class} class of response
     * @return Mono
     */
    public <O> Mono<O> query(String uri, HttpMethod method, ParameterizedTypeReference<O> clazz, Object... param) {
        return client.method(method)
                .uri(uri, param)
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, mapper::http4xxMapper)
                .onStatus(HttpStatus::is5xxServerError, mapper::http5xxMapper)
                .bodyToMono(clazz)
                ;
    }

    public <O> Mono<O> queryByteArrayBody(String uri, byte[] body, HttpMethod method, Class<O> clazz, Object... param) {
        return client
                .method(method)
                .uri(uri, param)
                .contentLength(body.length)
                .syncBody(body)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, mapper::http4xxMapper)
                .onStatus(HttpStatus::is5xxServerError, mapper::http5xxMapper)
                .bodyToMono(clazz)
                ;
    }
}
