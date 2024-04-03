package com.nashss.se.musicplaylistservice.lambda;

import com.nashss.se.musicplaylistservice.activity.requests.GetBooklistBooksRequest;
import com.nashss.se.musicplaylistservice.activity.results.GetBooklistBooksResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class GetBooklistBooksLambda
        extends LambdaActivityRunner<GetBooklistBooksRequest, GetBooklistBooksResult>
        implements RequestHandler<LambdaRequest<GetBooklistBooksRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(LambdaRequest<GetBooklistBooksRequest> input, Context context) {
        return super.runActivity(
            () -> input.fromPathAndQuery((path, query) ->
                    GetBooklistBooksRequest.builder()
                            .withId(path.get("id"))
                            .withOrder(query.get("order"))
                            .build()),
            (request, serviceComponent) ->
                    serviceComponent.provideGetBooklistBooksActivity().handleRequest(request)
    );
    }
}
