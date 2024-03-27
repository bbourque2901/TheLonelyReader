package com.nashss.se.musicplaylistservice.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.nashss.se.musicplaylistservice.activity.requests.RemoveBooklistRequest;
import com.nashss.se.musicplaylistservice.activity.results.RemoveBooklistResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RemoveBooklistLambda
        extends LambdaActivityRunner<RemoveBooklistRequest, RemoveBooklistResult>
        implements RequestHandler<LambdaRequest<RemoveBooklistRequest>, LambdaResponse> {

    private final Logger log = LogManager.getLogger();

    @Override
    public LambdaResponse handleRequest(LambdaRequest<RemoveBooklistRequest> input, Context context) {
        log.info("handleRequest");
        return super.runActivity(
                () -> input.fromPath(path ->
                        RemoveBooklistRequest.builder()
                                .withId(path.get("id"))
                                .build()),
                (request, serviceComponent) ->
                        serviceComponent.provideRemoveBooklistActivity().handleRequest(request)
        );
    }
}
