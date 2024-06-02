package org.app.domain.exceptions;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class DuplicatedAccountExceptionHandler implements ExceptionMapper<DuplicatedAccountException> {
    @Override
    public Response toResponse(DuplicatedAccountException e) {
        return Response.status(Response.Status.CONFLICT).entity(ExceptionMessage.DUPLICATED_USERNAME).build();
    }
}
