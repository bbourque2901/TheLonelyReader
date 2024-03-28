package com.nashss.se.musicplaylistservice.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.nashss.se.musicplaylistservice.activity.requests.RemoveBookFromBooklistRequest;
import com.nashss.se.musicplaylistservice.activity.results.RemoveBookFromBooklistResult;

public class RemoveBookFromBooklistLambda
        extends LambdaActivityRunner<RemoveBookFromBooklistRequest, RemoveBookFromBooklistResult>
        implements RequestHandler<AuthenticatedLambdaRequest<RemoveBookFromBooklistRequest>, LambdaResponse> {
    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<RemoveBookFromBooklistRequest> input, Context context) {
        return super.runActivity(() -> {
            RemoveBookFromBooklistRequest unauthenticatedRequest = input.fromBody(RemoveBookFromBooklistRequest.class);
            return input.fromUserClaims(claims ->
                    RemoveBookFromBooklistRequest.builder()
                            .withId(unauthenticatedRequest.getId())
                            .withAsin(unauthenticatedRequest.getAsin())
                            .withCustomerId(claims.get("email"))
                            .build());
        }, (request, serviceComponent) ->
                serviceComponent.provideRemoveBookFromBooklistActivity().handleRequest(request));
    }
}
