package com.nashss.se.booktrackerservice.lambda;

import com.nashss.se.booktrackerservice.activity.requests.GetBooklistBooksRequest;
import com.nashss.se.booktrackerservice.activity.results.GetBooklistBooksResult;

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
