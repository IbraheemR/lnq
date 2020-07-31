package com.changeme.lnq.restapi;

import com.changeme.lnq.util.ModUtil;
import com.changeme.lnq.util.ServerUtil;
import com.sun.org.apache.xpath.internal.operations.Mod;

import static spark.Spark.get;
import static spark.Spark.port;

public class RestAPI {
    public static void register() {
        port(8000);

        get("/", (request, response) ->
                String.format(
                        "LNQ v%s REST API. <br> Try GET <a href=\"/data\">/data</a>",
                        ModUtil.getLNQModMetadata().getVersion().getFriendlyString()
                )
        );

        get("/data", (request, response) -> {
            response.type("application/json");
            return String.format(
                    "{\"time\":%d,\"tick\":{\"tps\":%.1f,\"time\":%.2f},\"players\":{\"online\":%d,\"max\":%d}}",
                    System.currentTimeMillis(),
                    ServerUtil.getServerTPS(),
                    ServerUtil.getServerTickTime(),
                    ServerUtil.getPlayerCount(),
                    ServerUtil.getMaxPlayerCount()
            );
        });

    }
}
