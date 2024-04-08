package com.nashss.se.musicplaylistservice.lambda;

import com.nashss.se.musicplaylistservice.activity.requests.GetBookFromBooklistRequest;
import com.nashss.se.musicplaylistservice.activity.results.GetBookFromBooklistResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GetBookFromBooklistLambda
        extends LambdaActivityRunner<GetBookFromBooklistRequest, GetBookFromBooklistResult>
        implements RequestHandler<AuthenticatedLambdaRequest<GetBookFromBooklistRequest>, LambdaResponse> {

    private final Logger log = LogManager.getLogger();

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<GetBookFromBooklistRequest> input, Context context) {
        return super.runActivity(
            () -> input.fromPath(path ->
                    GetBookFromBooklistRequest.builder()
                            .withBooklistId(path.get("id"))
                            .withBookAsin(path.get("asin"))
                            .build()),
            (request, serviceComponent) ->
                    serviceComponent.provideGetBookFromBooklistActivity().handleRequest(request)
            );
    }
}
