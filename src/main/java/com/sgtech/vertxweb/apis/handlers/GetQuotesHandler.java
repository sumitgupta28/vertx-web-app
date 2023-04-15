package com.sgtech.vertxweb.apis.handlers;

import com.sgtech.vertxweb.model.Quote;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Optional;

@Slf4j
public class GetQuotesHandler implements Handler<RoutingContext> {

    Map<String, Quote> cachedQuote;

    public GetQuotesHandler(final Map<String, Quote> cachedQuote) {
        this.cachedQuote = cachedQuote;
    }


    @Override
    public void handle(RoutingContext routingContext) {

        final var assetParam = routingContext.pathParam("asset");
        log.info("assetParam - {} ", assetParam);

        var mayBeQuote = Optional.ofNullable(cachedQuote.get(assetParam));
        if (mayBeQuote.isEmpty()) {
            routingContext.response()
                    .putHeader(HttpHeaders.CONTENT_TYPE.toString(), HttpHeaderValues.APPLICATION_JSON.toString())
                    .setStatusCode(HttpResponseStatus.NOT_FOUND.code()).end(
                            new JsonObject()
                                    .put("message", "Quotes Not Available for " + assetParam)
                                    .put("path", routingContext.normalizedPath())
                                    .toBuffer()
                    );
            return;
        }

        var response = mayBeQuote.get().toJsonObject();
        log.info(" path {} with response {} ", routingContext.normalizedPath(), response);
        routingContext.response()
                .putHeader(HttpHeaders.CONTENT_TYPE.toString(), HttpHeaderValues.APPLICATION_JSON.toString())
                .setStatusCode(HttpResponseStatus.OK.code()).end(response.toBuffer());
    }
}
