package com.nashss.se.booktrackerservice.lambda;

import com.nashss.se.booktrackerservice.activity.requests.GetUserBooklistsRequest;
import com.nashss.se.booktrackerservice.activity.results.GetUserBooklistsResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GetUserBooklistsLambda
    extends LambdaActivityRunner<GetUserBooklistsRequest, GetUserBooklistsResult>
    implements RequestHandler<AuthenticatedLambdaRequest<GetUserBooklistsRequest>, LambdaResponse> {

    private final Logger log = LogManager.getLogger();

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<GetUserBooklistsRequest> input, Context context) {
        log.info("handleRequest");
        return super.runActivity(
            () -> input.fromUserClaims(claims ->
                    GetUserBooklistsRequest.builder()
                            .withCustomerId(claims.get("email"))
                            .build()),
            (request, serviceComponent) ->
                    serviceComponent.provideGetUserBooklistsActivity().handleRequest(request)
        );
    }
}
