package org.app.infrastructure.endpoints;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.app.application.*;
import org.app.domain.User;
import org.app.infrastructure.endpoints.dto.*;
import org.jboss.resteasy.reactive.RestPath;
import org.jboss.resteasy.reactive.RestQuery;

@Path("/users")
public class UserResource {
    @Inject
    RegisterUserUserCase registerUserUserCase;
    @Inject
    GetUserUserCase getUserUserCase;
    @Inject
    SubscribeUserCase subscribeUserCase;
    @Inject
    UnsubscribeUserCase unsubscribeUserCase;
    @Inject
    GetFollowingListUserCase getFollowingListUserCase;
    @Inject
    GetFanListUserCase getFanListUserCase;

    public static class registerUserBody {
        @NotBlank(message = "username is required")
        public String username;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerUser(@Valid UserResource.registerUserBody body) {
        User user = registerUserUserCase.execute(body.username);
        return Response.ok(RegisterUserDTO.from(user)).status(Response.Status.CREATED).build();
    }

    public static class UserQuery {
        @RestQuery
        @NotNull(message = "id is required")
        public String id;
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@BeanParam @Valid UserQuery params) {
        User user = getUserUserCase.execute(params.id);
        return Response.ok(GetUserDTO.from(user)).build();
    }

    public static class UserPath {
        @RestPath
        public String id;
    }

    public static class FollowingBody {
        @NotBlank(message = "followingId is required")
        public String followingId;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}/subscribe")
    public Response subscribe(@BeanParam UserPath path, @Valid FollowingBody body) {
        subscribeUserCase.execute(path.id, body.followingId);
        return Response.ok().build();
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}/followings")
    public Response getFollowingList(@BeanParam UserPath path) {
        User user = getFollowingListUserCase.execute(path.id);
        return Response.ok(GetFollowingListDTO.from(user)).build();
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}/unsubscribe")
    public Response unsubscribe(@BeanParam UserPath path, @Valid UserResource.FollowingBody body) {
        unsubscribeUserCase.execute(path.id, body.followingId);
        return Response.ok().build();
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}/fans")
    public Response getFanList(@BeanParam UserPath path) {
        User user = getFanListUserCase.execute(path.id);
        return Response.ok(GetFanListDTO.from(user)).build();
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}/friends")
    public Response getFriendList(@BeanParam UserPath path) {
        User user = getUserUserCase.execute(path.id);
        return Response.ok(GetFriendListDTO.from(user)).build();
    }
}
