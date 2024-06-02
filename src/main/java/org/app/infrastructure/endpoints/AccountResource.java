package org.app.infrastructure.endpoints;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.app.application.account.*;
import org.app.domain.Account;
import org.app.infrastructure.endpoints.dto.*;
import org.jboss.resteasy.reactive.RestPath;
import org.jboss.resteasy.reactive.RestQuery;

@Path("/accounts")
public class AccountResource {
    @Inject
    RegisterAccountUserCase registerAccountUserCase;
    @Inject
    GetAccountUserCase getAccountUserCase;
    @Inject
    AddFriendUserCase addFriendUserCase;
    @Inject
    RemoveFriendUserCase removeFriendUserCase;
    @Inject
    SubscribeUserCase subscribeUserCase;
    @Inject
    UnsubscribeUserCase unsubscribeUserCase;

    public static class registerAccountBody {
        @NotBlank(message = "username is required")
        public String username;
        @NotBlank(message = "password is required")
        public String password;
        @NotBlank(message = "nickname is required")
        public String nickname;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerAccount(@Valid registerAccountBody body) {
        Account account = registerAccountUserCase.execute(body.username, body.password, body.nickname);
        return Response.ok(RegisterAccountDTO.from(account)).status(Response.Status.CREATED).build();
    }

    public static class AccountQuery {
        @RestQuery
        @NotNull(message = "id is required")
        public String id;
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAccount(@BeanParam @Valid AccountQuery params) {
        Account account = getAccountUserCase.execute(params.id);
        return Response.ok(GetAccountDTO.from(account)).build();
    }

    public static class AccountPath{
        @RestPath
        public String id;
    }

    public static class FriendBody {
        @NotBlank(message = "friendId is required")
        public String friendId;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}/add_friend")
    public Response addFriend(@BeanParam AccountPath path, @Valid FriendBody body) {
        addFriendUserCase.execute(path.id, body.friendId);
        return Response.ok().build();
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}/friends")
    public Response getFriendList(@BeanParam AccountPath path) {
        Account account = getAccountUserCase.execute(path.id);
        return Response.ok(GetFriendListDTO.from(account)).build();
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}/remove_friend")
    public Response removeFriend(@BeanParam AccountPath path, @Valid FriendBody body) {
        removeFriendUserCase.execute(path.id, body.friendId);
        return Response.ok().build();
    }

    public static class SubscriptionBody {
        @NotBlank(message = "subscriptionId is required")
        public String subscriptionId;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}/subscribe")
    public Response subscribe(@BeanParam AccountPath path, @Valid SubscriptionBody body) {
        subscribeUserCase.execute(path.id, body.subscriptionId);
        return Response.ok().build();
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}/subscriptions")
    public Response getSubscriptionList(@BeanParam AccountPath path) {
        Account account = getAccountUserCase.execute(path.id);
        return Response.ok(GetSubscriptionListDTO.from(account)).build();
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}/unsubscribe")
    public Response unsubscribe(@BeanParam AccountPath path, @Valid SubscriptionBody body) {
        unsubscribeUserCase.execute(path.id, body.subscriptionId);
        return Response.ok().build();
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}/followers")
    public Response getFollowerList(@BeanParam AccountPath path) {
        Account account = getAccountUserCase.execute(path.id);
        return Response.ok(GetFollowerListDTO.from(account)).build();
    }

    public static class RemoveFollowerBody {
        @NotBlank(message = "followerId is required")
        public String followerId;
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}/remove_follower")
    public Response removeFollower(@BeanParam AccountPath path, @Valid RemoveFollowerBody body) {
        unsubscribeUserCase.execute(body.followerId, path.id);
        return Response.ok().build();
    }
}
