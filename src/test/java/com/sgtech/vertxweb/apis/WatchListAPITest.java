package com.sgtech.vertxweb.apis;

import com.sgtech.vertxweb.MainVerticle;
import com.sgtech.vertxweb.model.Asset;
import com.sgtech.vertxweb.model.WatchList;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(VertxExtension.class)
@Slf4j
class WatchListAPITest {

    @BeforeEach
    void deployVerticle(Vertx vertx, VertxTestContext vertxTestContext) {
        vertx.deployVerticle(new MainVerticle(), vertxTestContext.succeeding(event -> vertxTestContext.completeNow()));
    }

    @Test
    void get_non_available_watchList_account_id(Vertx vertx, VertxTestContext vertxTestContext) {

        var webClient = WebClient.create(vertx, new WebClientOptions().setDefaultPort(MainVerticle.PORT));
        var accountId = UUID.randomUUID();

        webClient.get("/account/watchlist/" + accountId)
                .send().onComplete(vertxTestContext.succeeding(response -> {
                    var jsonObject = response.bodyAsJsonObject();
                    log.info("Get Response {} ", jsonObject.encode());
                    assertNotNull(jsonObject);
                    assertEquals(HttpResponseStatus.NOT_FOUND.code(), response.statusCode());
                    assertEquals(HttpHeaderValues.APPLICATION_JSON.toString(),
                            response.getHeader(HttpHeaders.CONTENT_TYPE.toString()));
                    vertxTestContext.completeNow();
                }));
    }

    @Test
    void delete_non_available_watchList_account_id(Vertx vertx, VertxTestContext vertxTestContext) {

        var webClient = WebClient.create(vertx, new WebClientOptions().setDefaultPort(MainVerticle.PORT));
        var accountId = UUID.randomUUID();

        webClient.delete("/account/watchlist/" + accountId)
                .send().onComplete(vertxTestContext.succeeding(response -> {
                    var jsonObject = response.bodyAsJsonObject();
                    log.info("Delete Response {} ", jsonObject.encode());
                    assertNotNull(jsonObject);
                    assertEquals(HttpResponseStatus.NOT_FOUND.code(), response.statusCode());
                    assertEquals(HttpHeaderValues.APPLICATION_JSON.toString(),
                            response.getHeader(HttpHeaders.CONTENT_TYPE.toString()));
                    vertxTestContext.completeNow();
                }));
    }


    @Test
    void add_and_delete_watchList_account_id(Vertx vertx, VertxTestContext vertxTestContext) {
        String expectedResponse = "{\"assets\":[{\"symbol\":\"AMZN\"},{\"symbol\":\"TSLA\"}]}";

        var webClient = WebClient.create(vertx, new WebClientOptions().setDefaultPort(MainVerticle.PORT));
        var accountId = UUID.randomUUID();
        webClient.put("/account/watchlist/" + accountId)
                .sendJsonObject(getBody())
                .onComplete(vertxTestContext.succeeding(response -> {
                    var jsonObject = response.bodyAsJsonObject();
                    log.info("PUT Response {} ", jsonObject.encode());
                    assertNotNull(jsonObject);
                    assertEquals(expectedResponse, jsonObject.encode());
                    assertEquals(HttpHeaderValues.APPLICATION_JSON.toString(),
                            response.getHeader(HttpHeaders.CONTENT_TYPE.toString()));
                    assertEquals(HttpResponseStatus.OK.code(), response.statusCode());
                    // vertxTestContext.completeNow();
                })).compose(next -> {
                    webClient.delete("/account/watchlist/" + accountId)
                            .send()
                            .onComplete(vertxTestContext.succeeding(response -> {
                                var jsonObject = response.bodyAsJsonObject();
                                log.info("DELETE Response {} ", jsonObject.encode());
                                assertNotNull(jsonObject);
                                assertEquals(expectedResponse, jsonObject.encode());
                                assertEquals(HttpHeaderValues.APPLICATION_JSON.toString(),
                                        response.getHeader(HttpHeaders.CONTENT_TYPE.toString()));
                                assertEquals(HttpResponseStatus.OK.code(), response.statusCode());
                                vertxTestContext.completeNow();
                            }));
                    return Future.succeededFuture();
                });

    }

    @Test
    void add_and_return_watchList_for_account_id(Vertx vertx, VertxTestContext vertxTestContext) {
        String expectedResponse = "{\"assets\":[{\"symbol\":\"AMZN\"},{\"symbol\":\"TSLA\"}]}";

        var webClient = WebClient.create(vertx, new WebClientOptions().setDefaultPort(MainVerticle.PORT));
        var accountId = UUID.randomUUID();
        webClient.put("/account/watchlist/" + accountId)
                .sendJsonObject(getBody())
                .onComplete(vertxTestContext.succeeding(response -> {
                    var jsonObject = response.bodyAsJsonObject();
                    log.info("PUT Response {} ", jsonObject.encode());
                    assertNotNull(jsonObject);
                    assertEquals(expectedResponse, jsonObject.encode());
                    assertEquals(HttpHeaderValues.APPLICATION_JSON.toString(),
                            response.getHeader(HttpHeaders.CONTENT_TYPE.toString()));
                    assertEquals(HttpResponseStatus.OK.code(), response.statusCode());
                    // vertxTestContext.completeNow();
                })).compose(next -> {
                    webClient.get("/account/watchlist/" + accountId)
                            .send()
                            .onComplete(vertxTestContext.succeeding(response -> {
                                var jsonObject = response.bodyAsJsonObject();
                                log.info("GET Response {} ", jsonObject.encode());
                                assertNotNull(jsonObject);
                                assertEquals(expectedResponse, jsonObject.encode());
                                assertEquals(HttpHeaderValues.APPLICATION_JSON.toString(),
                                        response.getHeader(HttpHeaders.CONTENT_TYPE.toString()));
                                assertEquals(HttpResponseStatus.OK.code(), response.statusCode());
                                vertxTestContext.completeNow();
                            }));
                    return Future.succeededFuture();
                });

    }

    private JsonObject getBody() {
        return new WatchList(Arrays.asList(new Asset("AMZN"), new Asset("TSLA"))).toJsonObject();
    }


}