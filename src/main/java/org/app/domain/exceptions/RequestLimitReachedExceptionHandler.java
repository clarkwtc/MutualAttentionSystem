package org.app.domain.exceptions;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class RequestLimitReachedExceptionHandler implements ExceptionMapper<RequestLimitReachedException> {
    @Override
    public Response toResponse(RequestLimitReachedException e) {
        return Response.status(Response.Status.TOO_MANY_REQUESTS).entity(e.getMessage()).build();
    }
}
