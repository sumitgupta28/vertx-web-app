package com.sgtech.vertxweb.apis.handlers;

import com.sgtech.vertxweb.model.Asset;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class GetAssetsHandler implements Handler<RoutingContext> {

    public static final List<String> ASSETS_LIST = Arrays.asList("APPL", "AMZN", "NFLX", "TSLA", "GOOG", "WALL", "FB");

    @Override
    public void handle(RoutingContext routingContext) {
        final var response = new JsonArray();
        ASSETS_LIST.stream().map(Asset::new).forEach(response::add);
        log.info(" path {} with response {} ", routingContext.normalizedPath(), response);
        routingContext.response()
                .putHeader(HttpHeaders.CONTENT_TYPE.toString(), HttpHeaderValues.APPLICATION_JSON.toString())
                .end(response.toBuffer());
    }


}
