package com.sgtech.vertxweb.apis;

import com.sgtech.vertxweb.apis.handlers.GetAssetsHandler;
import com.sgtech.vertxweb.apis.handlers.GetQuotesHandler;
import com.sgtech.vertxweb.model.Asset;
import com.sgtech.vertxweb.model.Quote;
import io.vertx.ext.web.Router;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static com.sgtech.vertxweb.common.EndPoints.QUOTES_ASSET;

@Slf4j
public class GetQuotesRestAPI {

    public static void attach(Router parentRouter) {

        final Map<String, Quote> cachedQuote = new HashMap<>();
        GetAssetsHandler.ASSETS_LIST.forEach(s ->
                cachedQuote.put(s, initRandomQuote(s))
        );
        parentRouter.get(QUOTES_ASSET).handler(new GetQuotesHandler(cachedQuote));
    }

    private static Quote initRandomQuote(String assetParam) {
        return Quote.builder().asset(new Asset(assetParam))
                .bid(randomValue())
                .ask(randomValue())
                .lastBidPrice(randomValue())
                .volume(randomValue()).build();
    }

    private static BigDecimal randomValue() {
        return BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(1, 1000));
    }


}
