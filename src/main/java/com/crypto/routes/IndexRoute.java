package com.crypto.routes;

import spark.*;

import java.util.HashMap;

import static spark.Spark.modelAndView;

/**
 * The route for index page
 */
public class IndexRoute implements TemplateViewRoute {
    private final RouteContext context;

    public IndexRoute(RouteContext context) {
        this.context = context;
    }

    @Override
    public ModelAndView handle(Request request, Response response) {
        HashMap<String, Object> options = new HashMap<>();
        if (context.response != null && !context.response.isEmpty()) {
            options.put("response", context.response);
        }

        if (context.filteredElements != null && !context.filteredElements.isEmpty()) {
            options.put("filteredElements", context.filteredElements);
        }

        return modelAndView(options, "index.wm");
    }
}