package org.app.domain.exceptions;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class PageSizeTooLargeExceptionHandler implements ExceptionMapper<PageSizeTooLargeException> {
    @Override
    public Response toResponse(PageSizeTooLargeException e) {
        return Response.status(Response.Status.NOT_FOUND).entity(ExceptionMessage.PAGE_SIZE_TOO_LARGE).build();
    }
}
