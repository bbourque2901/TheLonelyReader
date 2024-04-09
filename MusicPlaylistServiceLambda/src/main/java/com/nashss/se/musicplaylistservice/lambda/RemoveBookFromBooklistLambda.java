package com.nashss.se.musicplaylistservice.lambda;

import com.nashss.se.musicplaylistservice.activity.requests.RemoveBookFromBooklistRequest;
import com.nashss.se.musicplaylistservice.activity.results.RemoveBookFromBooklistResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RemoveBookFromBooklistLambda
        extends LambdaActivityRunner<RemoveBookFromBooklistRequest, RemoveBookFromBooklistResult>
        implements RequestHandler<AuthenticatedLambdaRequest<RemoveBookFromBooklistRequest>, LambdaResponse> {

    private final Logger log = LogManager.getLogger();

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<RemoveBookFromBooklistRequest> inpt, Context ctxt) {
        return super.runActivity(
            () -> {
                RemoveBookFromBooklistRequest unAuthRequest = inpt.fromUserClaims(claims ->
                        RemoveBookFromBooklistRequest.builder()
                                .withCustomerId(claims.get("email"))
                                .build());

                return inpt.fromPath(path ->
                        RemoveBookFromBooklistRequest.builder()
                                .withId(path.get("id"))
                                .withAsin(path.get("asin"))
                                .withCustomerId(unAuthRequest.getCustomerId())
                                .build());
            },
            (request, serviceComponent) ->
                    serviceComponent.provideRemoveBookFromBooklistActivity().handleRequest(request)
        );
    }
}
