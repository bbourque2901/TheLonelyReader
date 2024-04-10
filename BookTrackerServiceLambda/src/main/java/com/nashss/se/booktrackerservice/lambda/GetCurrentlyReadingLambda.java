package com.nashss.se.booktrackerservice.lambda;

import com.nashss.se.booktrackerservice.activity.requests.GetCurrentlyReadingRequest;
import com.nashss.se.booktrackerservice.activity.results.GetCurrentlyReadingResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

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
                            .withId(path.get("id"))
                            .build()), (request, serviceComponent) ->
                    serviceComponent.provideGetCurrentlyReadingActivity().handleRequest(request)
        );
    }
}
