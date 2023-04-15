package com.sgtech.vertxweb.apis.handlers;

import com.sgtech.vertxweb.apis.WatchListRestAPI;
import com.sgtech.vertxweb.model.WatchList;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class DeleteWatchListHandler implements Handler<RoutingContext> {
    private final Map<UUID, WatchList> watchListPerAccount;

    public DeleteWatchListHandler(Map<UUID, WatchList> watchListPerAccount) {
        this.watchListPerAccount = watchListPerAccount;
    }

    @Override
    public void handle(RoutingContext routingContext) {
        final String accountId = WatchListRestAPI.getAccountId(routingContext);
        var deletedWatchList = Optional.ofNullable(watchListPerAccount.remove(UUID.fromString(accountId)));
        if (deletedWatchList.isEmpty()) {
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
        log.info("Deleted: {}  Remaining: {}", deletedWatchList.get().toJsonObject(), watchListPerAccount);
        routingContext.response()
                .putHeader(HttpHeaders.CONTENT_TYPE.toString(), HttpHeaderValues.APPLICATION_JSON.toString())
                .end(deletedWatchList.get().toJsonObject().toBuffer());
    }
}
