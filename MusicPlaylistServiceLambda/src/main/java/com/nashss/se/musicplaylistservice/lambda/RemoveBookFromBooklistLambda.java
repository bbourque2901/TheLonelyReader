package com.nashss.se.musicplaylistservice.lambda;

import com.nashss.se.musicplaylistservice.activity.requests.RemoveBookFromBooklistRequest;
import com.nashss.se.musicplaylistservice.activity.results.RemoveBookFromBooklistResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class RemoveBookFromBooklistLambda
        extends LambdaActivityRunner<RemoveBookFromBooklistRequest, RemoveBookFromBooklistResult>
        implements RequestHandler<AuthenticatedLambdaRequest<RemoveBookFromBooklistRequest>, LambdaResponse> {
    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<RemoveBookFromBooklistRequest> inpt, Context ctxt) {
        return super.runActivity(() -> {
            RemoveBookFromBooklistRequest unauthenticatedRequest = inpt.fromBody(RemoveBookFromBooklistRequest.class);
            return inpt.fromUserClaims(claims ->
                    RemoveBookFromBooklistRequest.builder()
                            .withId(unauthenticatedRequest.getId())
                            .withAsin(unauthenticatedRequest.getAsin())
                            .withCustomerId(claims.get("email"))
                            .build());
        }, (request, serviceComponent) ->
                serviceComponent.provideRemoveBookFromBooklistActivity().handleRequest(request));
    }
}
