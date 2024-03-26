package com.nashss.se.musicplaylistservice.lambda;

import com.nashss.se.musicplaylistservice.activity.requests.CreateBooklistRequest;
import com.nashss.se.musicplaylistservice.activity.results.CreateBooklistResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class CreateBooklistLambda
        extends LambdaActivityRunner<CreateBooklistRequest, CreateBooklistResult>
        implements RequestHandler<AuthenticatedLambdaRequest<CreateBooklistRequest>, LambdaResponse> {
    @Override
    public  LambdaResponse handleRequest(AuthenticatedLambdaRequest<CreateBooklistRequest> input, Context context) {
        return super.runActivity(
            () -> {
                CreateBooklistRequest unauthenticatedRequest = input.fromBody(CreateBooklistRequest.class);
                return input.fromUserClaims(claims ->
                        CreateBooklistRequest.builder()
                                .withName(unauthenticatedRequest.getName())
                                .withTags(unauthenticatedRequest.getTags())
                                .withCustomerId(claims.get("email"))
                                .build());
            },
            (request, serviceComponent) ->
                    serviceComponent.provideCreateBooklistActivity().handleRequest(request)
        );
    }
}
