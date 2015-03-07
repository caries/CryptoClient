package com.crypto;

import com.crypto.routes.IndexRoute;
import com.crypto.routes.RequestRoute;
import com.crypto.routes.RouteContext;
import spark.template.velocity.VelocityTemplateEngine;

import static spark.Spark.get;
import static spark.Spark.post;

/**
 * Configures endpoints
 */
class EndpointsConfigurer {
    public void configure() {
        RouteContext context = new RouteContext();

        get("/", new IndexRoute(context), new VelocityTemplateEngine());
        post("/send-request", new RequestRoute(context));
    }
}