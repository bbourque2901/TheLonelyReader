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
        return super.runActivity(
            ()  -> {
                log.error("INPUT!!:: " + input.toString());
                RemoveBooklistRequest unAuthRequest = input.fromUserClaims(claims ->
                        RemoveBooklistRequest.builder()
                               .withCustomerId(claims.get("email"))
                               .build());

                return input.fromPath(path ->
                        RemoveBooklistRequest.builder()
                                .withId(path.get("id"))
                                .withCustomerId(unAuthRequest.getCustomerId())
                                .build());
            },
            (request, serviceComponent) ->
                        serviceComponent.provideRemoveBooklistActivity().handleRequest(request)
        );
    }
}
