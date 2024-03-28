package com.nashss.se.musicplaylistservice.lambda;

import com.nashss.se.musicplaylistservice.activity.requests.RemoveBooklistRequest;
import com.nashss.se.musicplaylistservice.activity.results.RemoveBooklistResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RemoveBooklistLambda
        extends LambdaActivityRunner<RemoveBooklistRequest, RemoveBooklistResult>
        implements RequestHandler<AuthenticatedLambdaRequest<RemoveBooklistRequest>, LambdaResponse> {

    private final Logger log = LogManager.getLogger();

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<RemoveBooklistRequest> input, Context context) {
        log.info("handleRequest");
        return super.runActivity(() -> {
            RemoveBooklistRequest unauthenticatedRequest = input.fromBody(RemoveBooklistRequest.class);
            return input.fromUserClaims(claims ->
                    RemoveBooklistRequest.builder()
                            .withId(unauthenticatedRequest.getId())
                            .withCustomerId(claims.get("email"))
                            .build());
        }, (request, serviceComponent) ->
                serviceComponent.provideRemoveBooklistActivity().handleRequest(request));

    }
}
