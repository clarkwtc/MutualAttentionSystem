package org.app.domain.exceptions;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

public class ServiceOverloadExceptionHandler implements ExceptionMapper<ServiceOverloadException> {
    @Override
    public Response toResponse(ServiceOverloadException e) {
        return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(e.getMessage()).build();
    }
}
