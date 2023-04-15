package com.sgtech.vertxweb.apis.handlers;

import com.sgtech.vertxweb.apis.WatchListRestAPI;
import com.sgtech.vertxweb.model.WatchList;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class GetWatchListHandler implements Handler<RoutingContext> {
    private final Map<UUID, WatchList> watchListPerAccount;

    public GetWatchListHandler(Map<UUID, WatchList> watchListPerAccount) {
        this.watchListPerAccount = watchListPerAccount;
    }

    @Override
    public void handle(RoutingContext routingContext) {
        final String accountId = WatchListRestAPI.getAccountId(routingContext);
        var watchList = Optional.ofNullable(watchListPerAccount.get(UUID.fromString(accountId)));
        if (watchList.isEmpty()) {
            routingContext.response()
                    .putHeader(HttpHeaders.CONTENT_TYPE.toString(), HttpHeaderValues.APPLICATION_JSON.toString())
                    .setStatusCode(HttpResponseStatus.NOT_FOUND.code()).end(
                            new JsonObject()
                                    .put("message", "WatchList for account " + accountId + " Not Available!!")
                                    .put("path", routingContext.normalizedPath())
                                    .toBuffer()
                    );
            return;
        }
        routingContext.response()
                .putHeader(HttpHeaders.CONTENT_TYPE.toString(), HttpHeaderValues.APPLICATION_JSON.toString())
                .end(watchList.get().toJsonObject().toBuffer());
    }
}
