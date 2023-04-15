package com.sgtech.vertxweb.apis;

import com.sgtech.vertxweb.MainVerticle;
import com.sgtech.vertxweb.common.EndPoints;
import io.netty.handler.codec.http.HttpHeaderValues;
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
class AssetsAPITest {

    @BeforeEach
    void deployVerticle(Vertx vertx, VertxTestContext vertxTestContext) {
        vertx.deployVerticle(new MainVerticle(), vertxTestContext.succeeding(event -> vertxTestContext.completeNow()));
    }

    @Test
    void returnsAllAssets(Vertx vertx, VertxTestContext vertxTestContext) {
        String expectedResponse = "[{\"symbol\":\"APPL\"},{\"symbol\":\"AMZN\"},{\"symbol\":\"NFLX\"},{\"symbol\":\"TSLA\"},{\"symbol\":\"GOOG\"},{\"symbol\":\"WALL\"},{\"symbol\":\"FB\"}]";

        var webClient = WebClient.create(vertx, new WebClientOptions().setDefaultPort(MainVerticle.PORT));
        webClient.get(EndPoints.ASSETS)
                .send()
                .onComplete(vertxTestContext.succeeding(response -> {
                    var jsonArray = response.bodyAsJsonArray();
                    log.info("Response {} ", jsonArray);
                    assertNotNull(jsonArray);
                    assertEquals(expectedResponse, jsonArray.encode());
                    assertEquals(200, response.statusCode());
                    assertEquals(HttpHeaderValues.APPLICATION_JSON.toString(),
                            response.getHeader(HttpHeaders.CONTENT_TYPE.toString()));
                    vertxTestContext.completeNow();
                }));

    }
}