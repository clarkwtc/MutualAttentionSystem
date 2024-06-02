package org.app.domain.exceptions;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class NotExistAccountExceptionHandler implements ExceptionMapper<NotExistAccountException> {
    @Override
    public Response toResponse(NotExistAccountException e) {
        return Response.status(Response.Status.NOT_FOUND).entity(ExceptionMessage.NOT_EXIST_ACCOUNT).build();
    }
}
