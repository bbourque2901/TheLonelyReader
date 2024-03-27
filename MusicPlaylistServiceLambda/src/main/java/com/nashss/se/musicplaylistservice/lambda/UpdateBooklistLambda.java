package com.nashss.se.musicplaylistservice.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.nashss.se.musicplaylistservice.activity.requests.UpdateBooklistRequest;
import com.nashss.se.musicplaylistservice.activity.requests.UpdatePlaylistRequest;
import com.nashss.se.musicplaylistservice.activity.results.UpdateBooklistResult;

public class UpdateBooklistLambda
        extends LambdaActivityRunner<UpdateBooklistRequest, UpdateBooklistResult>
        implements RequestHandler<AuthenticatedLambdaRequest<UpdateBooklistRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<UpdateBooklistRequest> input, Context context) {
        return super.runActivity(
                () -> {
                    UpdateBooklistRequest unauthenticatedRequest = input.fromBody(UpdateBooklistRequest.class);
                    return input.fromUserClaims(claims ->
                            UpdateBooklistRequest.builder()
                                    .withId(unauthenticatedRequest.getId())
                                    .withName(unauthenticatedRequest.getName())
                                    .withCustomerId(claims.get("email"))
                                    .build());
                },
                (request, serviceComponent) ->
                        serviceComponent.provideUpdateBooklistActivity().handleRequest(request)
        );
    }
}
