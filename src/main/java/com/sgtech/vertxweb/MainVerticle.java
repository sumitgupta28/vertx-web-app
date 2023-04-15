package com.sgtech.vertxweb;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MainVerticle extends AbstractVerticle {


    public static final int PORT = 8888;

    public static void main(String[] args) {
        var vertx = Vertx.vertx();
        vertx.exceptionHandler(event ->
                log.error(" error while serving request ", event)
        );
        vertx.deployVerticle(new MainVerticle())
                .onFailure(err -> log.error(" Failed to Deploy ", err))
                .onSuccess(id ->
                        log.info("Deployed {} with id {}", MainVerticle.class.getSimpleName(), id)
                );
    }

    private int processors() {
        return Math.max(1, Runtime.getRuntime().availableProcessors());
    }


    @Override
    public void start(Promise<Void> startPromise) {
        vertx.deployVerticle(RestAPIVerticle.class.getName()
                        , new DeploymentOptions().setInstances(processors()))
                .onFailure(startPromise::fail)
                .onSuccess(id -> {
                    log.info("Deployed {} with id {}", RestAPIVerticle.class.getSimpleName(), id);
                    startPromise.complete();
                });

    }


}
