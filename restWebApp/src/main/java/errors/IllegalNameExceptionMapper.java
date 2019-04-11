package errors;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
@Provider
public class IllegalNameExceptionMapper implements ExceptionMapper<IllegalNameException> {
    @Override
    public Response toResponse(IllegalNameException e) {
        return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
    }
}