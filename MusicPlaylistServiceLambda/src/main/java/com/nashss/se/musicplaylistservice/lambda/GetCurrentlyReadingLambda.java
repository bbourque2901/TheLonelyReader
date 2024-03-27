package com.nashss.se.musicplaylistservice.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.nashss.se.musicplaylistservice.activity.requests.GetCurrentlyReadingRequest;
import com.nashss.se.musicplaylistservice.activity.results.GetCurrentlyReadingResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GetCurrentlyReadingLambda
    extends LambdaActivityRunner<GetCurrentlyReadingRequest, GetCurrentlyReadingResult>
    implements RequestHandler<LambdaRequest<GetCurrentlyReadingRequest>, LambdaResponse> {

    private final Logger log = LogManager.getLogger();

    @Override
    public LambdaResponse handleRequest(LambdaRequest<GetCurrentlyReadingRequest> input, Context context) {
        log.info("handleRequest");
        return super.runActivity(
                () -> input.fromPath(path ->
                        GetCurrentlyReadingRequest.builder()
                                .withCurrentlyReading(Boolean.parseBoolean(path.get("currentlyReading")))
                                .build()),
                (request, serviceComponent) ->
                        serviceComponent.provideGetCurrentlyReadingActivity().handleRequest(request)
        );
    }
}
