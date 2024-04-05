package com.nashss.se.musicplaylistservice.lambda;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.nashss.se.musicplaylistservice.activity.requests.UpdateBookInBooklistRequest;
import com.nashss.se.musicplaylistservice.activity.results.UpdateBookInBooklistResult;

public class UpdateBookInBooklistLambda
        extends LambdaActivityRunner<UpdateBookInBooklistRequest, UpdateBookInBooklistResult>
        implements RequestHandler<AuthenticatedLambdaRequest<UpdateBookInBooklistRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<UpdateBookInBooklistRequest> input, Context context) {
        return super.runActivity(
                () -> {
                    UpdateBookInBooklistRequest unauthenticatedRequest = input.fromBody(UpdateBookInBooklistRequest.class);
                    return input.fromUserClaims(claims ->
                            UpdateBookInBooklistRequest.builder()
                                    .withCustomerId(unauthenticatedRequest.getCustomerId())
                                    .withAsin(unauthenticatedRequest.getAsin())
                                    .withCurrentlyReading(unauthenticatedRequest.isCurrentlyReading())
                                    .withRating(unauthenticatedRequest.getRating())
                                    .withCommentText(unauthenticatedRequest.getCommentText())
                                    .build());
                },
                (request, serviceComponent) ->
                        serviceComponent.provideUpdateBookInBooklistActivity().handleRequest(request)
        );


    }
}
