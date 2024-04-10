package com.nashss.se.booktrackerservice.lambda;

import com.nashss.se.booktrackerservice.activity.requests.UpdateBooklistRequest;
import com.nashss.se.booktrackerservice.activity.results.UpdateBooklistResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

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
