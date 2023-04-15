package com.sgtech.vertxweb.apis.handlers;

import com.sgtech.vertxweb.apis.WatchListRestAPI;
import com.sgtech.vertxweb.model.WatchList;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.ext.web.RoutingContext;

import java.util.Map;
import java.util.UUID;

public class PutWatchListHandler implements Handler<RoutingContext> {
    private final Map<UUID, WatchList> watchListPerAccount;

    public PutWatchListHandler(Map<UUID, WatchList> watchListPerAccount) {
        this.watchListPerAccount = watchListPerAccount;
    }

    @Override
    public void handle(RoutingContext routingContext) {
        final String accountId = WatchListRestAPI.getAccountId(routingContext);
        var json = routingContext.body().asJsonObject();
        var watchList = json.mapTo(WatchList.class);
        watchListPerAccount.put(UUID.fromString(accountId), watchList);
        routingContext.response()
                .putHeader(HttpHeaders.CONTENT_TYPE.toString(), HttpHeaderValues.APPLICATION_JSON.toString())
                .end(watchList.toJsonObject().toBuffer());
    }
}
