package com.sgtech.vertxweb.apis;

import com.sgtech.vertxweb.apis.handlers.DeleteWatchListHandler;
import com.sgtech.vertxweb.apis.handlers.GetWatchListHandler;
import com.sgtech.vertxweb.apis.handlers.PutWatchListHandler;
import com.sgtech.vertxweb.model.WatchList;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.sgtech.vertxweb.common.EndPoints.WATCHLIST;

@Slf4j
public class WatchListRestAPI {


    public static final List<String> ASSETS_LIST = Arrays.asList("APPL", "AMZN", "NFLX", "TSLA", "GOOG", "WALL", "FB");

    public static void attach(Router parentRouter) {
        Map<UUID, WatchList> watchListPerAccount = new HashMap<>();
        parentRouter.get(WATCHLIST).handler(new GetWatchListHandler(watchListPerAccount));
        parentRouter.put(WATCHLIST).handler(new PutWatchListHandler(watchListPerAccount));
        parentRouter.delete(WATCHLIST).handler(new DeleteWatchListHandler(watchListPerAccount));
    }

    public static String getAccountId(RoutingContext routingContext) {
        final var accountId = routingContext.pathParam("accountId");
        log.info("Method {} Path {} accountId - {} ", routingContext.request().method().name(),
                routingContext.normalizedPath(),
                accountId);
        return accountId;
    }
}
