package com.nashss.se.booktrackerservice.lambda;

import com.nashss.se.booktrackerservice.activity.requests.AddBookToBooklistRequest;
import com.nashss.se.booktrackerservice.activity.results.AddBookToBooklistResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class AddBookToBooklistLambda
        extends LambdaActivityRunner<AddBookToBooklistRequest, AddBookToBooklistResult>
        implements RequestHandler<AuthenticatedLambdaRequest<AddBookToBooklistRequest>, LambdaResponse> {
    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<AddBookToBooklistRequest> input, Context context) {
        return super.runActivity(() -> {
            AddBookToBooklistRequest unauthenticatedRequest = input.fromBody(AddBookToBooklistRequest.class);
            return input.fromUserClaims(claims ->
                    AddBookToBooklistRequest.builder()
                            .withId(unauthenticatedRequest.getId())
                            .withAsin(unauthenticatedRequest.getAsin())
                            .withCustomerId(claims.get("email"))
                            .build());
        }, (request, serviceComponent) -> {
                try {
                    return serviceComponent.provideAddBookToBooklistActivity().handleRequest(request);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
    }
}
