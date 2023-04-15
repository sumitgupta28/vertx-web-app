package com.sgtech.vertxweb;

import com.sgtech.vertxweb.apis.AssetsRestAPI;
import com.sgtech.vertxweb.apis.GetQuotesRestAPI;
import com.sgtech.vertxweb.apis.WatchListRestAPI;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import lombok.extern.slf4j.Slf4j;

import static com.sgtech.vertxweb.MainVerticle.PORT;

@Slf4j
public class RestAPIVerticle extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) {
        startHttpServerAndAttachRoute(startPromise);

    }


    private void startHttpServerAndAttachRoute(Promise<Void> startPromise) {
        final var restAPIs = Router.router(vertx);

        restAPIs.route()
                .handler(BodyHandler.create())
                .failureHandler(handleFailure());

        // Register API's
        AssetsRestAPI.attach(restAPIs);
        GetQuotesRestAPI.attach(restAPIs);
        WatchListRestAPI.attach(restAPIs);

        vertx.createHttpServer().requestHandler(restAPIs)
                .exceptionHandler(error ->
                        log.error(" HTTP Server Error... ", error)
                ).listen(PORT, http -> {
                    if (http.succeeded()) {
                        startPromise.complete();
                        log.info("HTTP server started on port 8888");
                    } else {
                        startPromise.fail(http.cause());
                    }
                });
    }

    private Handler<RoutingContext> handleFailure() {
        return errorContext -> {
            if (errorContext.response().ended()) {
                return;
            }
            log.error(" HTTP Server Error... ", errorContext.failure());
            errorContext.response()
                    .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                    .end(new JsonObject().put("message", "Error while processing request").toBuffer());
        };
    }

}
