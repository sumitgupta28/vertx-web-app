package com.sgtech.vertxweb.apis;

import com.sgtech.vertxweb.MainVerticle;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpHeaders;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(VertxExtension.class)
@Slf4j
class QuoteAPITest {

    @BeforeEach
    void deployVerticle(Vertx vertx, VertxTestContext vertxTestContext) {
        vertx.deployVerticle(new MainVerticle(), vertxTestContext.succeeding(event -> vertxTestContext.completeNow()));
    }

    @Test
    void returns_quote_for_known_asset(Vertx vertx, VertxTestContext vertxTestContext) {
        String expectedResponse = "{\"symbol\":\"AMZN\"}";

        var webClient = WebClient.create(vertx, new WebClientOptions().setDefaultPort(MainVerticle.PORT));
        webClient.get("/quotes/AMZN")
                .send()
                .onComplete(vertxTestContext.succeeding(response -> {
                    var jsonObject = response.bodyAsJsonObject();
                    log.info("Response {} ", jsonObject.encode());
                    assertNotNull(jsonObject);
                    assertEquals(expectedResponse, jsonObject.getJsonObject("asset").encode());
                    assertEquals(HttpResponseStatus.OK.code(), response.statusCode());
                    assertEquals(HttpHeaderValues.APPLICATION_JSON.toString(),
                            response.getHeader(HttpHeaders.CONTENT_TYPE.toString()));
                    vertxTestContext.completeNow();
                }));

    }


    @Test
    void returns_quote_not_found_for_unknown_asset(Vertx vertx, VertxTestContext vertxTestContext) {
        String expectedResponse = "{\"message\":\"Quotes Not Available for DONTKNOW\",\"path\":\"/quotes/DONTKNOW\"}";
        String expectedMessage = "Quotes Not Available for DONTKNOW";

        var webClient = WebClient.create(vertx, new WebClientOptions().setDefaultPort(MainVerticle.PORT));
        webClient.get("/quotes/DONTKNOW")
                .send()
                .onComplete(vertxTestContext.succeeding(response -> {
                    var jsonObject = response.bodyAsJsonObject();
                    log.info("Response {} ", jsonObject.encode());
                    assertNotNull(jsonObject);
                    assertEquals(expectedResponse, jsonObject.encode());
                    assertEquals(expectedMessage, jsonObject.getString("message"));
                    assertEquals(HttpResponseStatus.NOT_FOUND.code(), response.statusCode());
                    assertEquals(HttpHeaderValues.APPLICATION_JSON.toString(),
                            response.getHeader(HttpHeaders.CONTENT_TYPE.toString()));
                    vertxTestContext.completeNow();
                }));

    }
}