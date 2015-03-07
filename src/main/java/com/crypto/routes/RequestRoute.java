package com.crypto.routes;

import com.crypto.helpers.Metadata;
import com.crypto.helpers.SoapHelper;
import com.crypto.helpers.Utils;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.List;

/**
 * The route for the request
 */
public class RequestRoute implements Route {
    private static final String METHOD_NAME = "createSmevHeaderSecurity";

    private final RouteContext context;

    public RequestRoute(RouteContext context) {
        this.context = context;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        context.response = null;
        context.filteredElements = null;

        Metadata metadata = readMetadata(request);

        if (metadata.request != null && !metadata.request.isEmpty()) {
            String encodedRequest = Utils.encodeToBase64(metadata.request);

            String result = SoapHelper.invokeMethod(metadata.address, METHOD_NAME, encodedRequest);
            context.response = Utils.decodeFromBase64(result);
        }

        if (context.response != null && !context.response.isEmpty() &&
            metadata.filteringExpression != null && !metadata.filteringExpression.isEmpty()) {

            List<String> filteredElements = Utils.filterElements(context.response, metadata.filteringExpression);
            if (!filteredElements.isEmpty()) {
                context.filteredElements = String.join(", ", filteredElements);
            }
        }

        response.status(201);
        response.redirect("/");

        return "";
    }

    private static Metadata readMetadata(Request request) {
        Metadata result = new Metadata();
        result.address = request.queryMap("address").value();
        result.request = request.queryMap("request").value();
        result.filteringExpression = request.queryMap("filteringExpression").value();
        return result;
    }
}