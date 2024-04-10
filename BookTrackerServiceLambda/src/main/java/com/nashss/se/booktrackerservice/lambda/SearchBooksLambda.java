package com.nashss.se.booktrackerservice.lambda;

import com.nashss.se.booktrackerservice.activity.requests.SearchBooksRequest;
import com.nashss.se.booktrackerservice.activity.results.SearchBooksResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SearchBooksLambda
        extends LambdaActivityRunner<SearchBooksRequest, SearchBooksResult>
        implements RequestHandler<LambdaRequest<SearchBooksRequest>, LambdaResponse> {

    private final Logger log = LogManager.getLogger();

    @Override
    public LambdaResponse handleRequest(LambdaRequest<SearchBooksRequest> input, Context context) {
        log.info("handleRequest:");
        return super.runActivity(
            () -> input.fromQuery(query ->
                    SearchBooksRequest.builder()
                            .withCriteria(query.get("q"))
                            .build()),
            (request, serviceComponent) ->
                    serviceComponent.provideSearchBooksActivity().handleRequest(request)
        );
    }
}
