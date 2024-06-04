package org.app.infrastructure.endpoints;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.app.application.*;
import org.app.domain.User;
import org.app.domain.events.FanEvent;
import org.app.domain.events.FollowingEvent;
import org.app.domain.events.FriendEvent;
import org.app.infrastructure.endpoints.dto.*;
import org.app.infrastructure.utils.RateLimiter;
import org.app.infrastructure.utils.RetryUtil;
import org.jboss.resteasy.reactive.RestPath;
import org.jboss.resteasy.reactive.RestQuery;

import java.util.function.Supplier;

@Path("/users")
public class UserResource {
    private static final RateLimiter rateLimiter = new RateLimiter(5000, 1000, 1);
    @Inject
    RegisterUserUserCase registerUserUserCase;
    @Inject
    GetUserUserCase getUserUserCase;
    @Inject
    SubscribeUserCase subscribeUserCase;
    @Inject
    UnsubscribeUserCase unsubscribeUserCase;
    @Inject
    GetFollowingsUserCase getFollowingsUserCase;
    @Inject
    GetFansUserCase getFansUserCase;
    @Inject
    GetFriendsUserCase getFriendsUserCase;

    public static class registerUserBody {
        @NotBlank(message = "username is required")
        public String username;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerUser(@Valid UserResource.registerUserBody body) {
        Supplier<Response> responseSupplier = () -> {
            User user = registerUserUserCase.execute(body.username);
            return Response.ok(RegisterUserDTO.from(user)).status(Response.Status.CREATED).build();
        };
        return RetryUtil.retry(responseSupplier, 3, 500);
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
        Supplier<Response> responseSupplier = () -> {
            User user = getUserUserCase.execute(params.id);
            return Response.ok(GetUserDTO.from(user)).build();
        };
        return rateLimiter.execute(responseSupplier);
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
        Supplier<Response> responseSupplier = () -> {
            subscribeUserCase.execute(path.id, body.followingId);
            return Response.ok().build();
        };
        return RetryUtil.retry(responseSupplier, 3, 500);
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}/followings")
    public Response getFollowingList(@BeanParam UserPath path, @Valid PageBody body) {
        Supplier<Response> responseSupplier = () -> {
            FollowingEvent event = getFollowingsUserCase.execute(path.id, body.page, body.limit);
            return Response.ok(GetFollowingListDTO.from(event)).build();
        };
        return rateLimiter.execute(responseSupplier);
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}/unsubscribe")
    public Response unsubscribe(@BeanParam UserPath path, @Valid FollowingBody body) {
        Supplier<Response> responseSupplier = () -> {
            unsubscribeUserCase.execute(path.id, body.followingId);
            return Response.ok().build();
        };
        return RetryUtil.retry(responseSupplier, 3, 500);
    }

    public static class PageBody {
        @RestQuery
        @Min(1)
        @DefaultValue("1")
        public int page;
        @RestQuery
        @Min(1)
        @Max(30)
        @DefaultValue("10")
        public int limit;
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}/fans")
    public Response getFanList(@BeanParam UserPath path, @Valid PageBody body) {
        Supplier<Response> responseSupplier = () -> {
            FanEvent event = getFansUserCase.execute(path.id, body.page, body.limit);
            return Response.ok(GetFanListDTO.from(event)).build();
        };
        return rateLimiter.execute(responseSupplier);
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}/friends")
    public Response getFriendList(@BeanParam UserPath path, @Valid PageBody body) {
        Supplier<Response> responseSupplier = () -> {
            FriendEvent event = getFriendsUserCase.execute(path.id, body.page, body.limit);
            return Response.ok(GetFriendListDTO.from(event)).build();
        };
        return rateLimiter.execute(responseSupplier);
    }
}
