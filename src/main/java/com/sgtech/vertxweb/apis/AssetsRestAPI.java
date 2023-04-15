package com.sgtech.vertxweb.apis;

import com.sgtech.vertxweb.apis.handlers.GetAssetsHandler;
import io.vertx.ext.web.Router;
import lombok.extern.slf4j.Slf4j;

import static com.sgtech.vertxweb.common.EndPoints.ASSETS;

@Slf4j
public class AssetsRestAPI {


    public static void attach(Router parentRouter) {
        parentRouter.get(ASSETS).handler(new GetAssetsHandler());
    }
}
