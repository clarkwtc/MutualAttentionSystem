package org.app.infrastructure.endpoints.dto;

import org.app.domain.User;

public class GetUserDTO {
    public String id;
    public String username;
    public Integer followings;
    public Integer friends;
    public Integer fans;

    public GetUserDTO() {}

    public GetUserDTO(User user) {
        this.id = user.getId().toString();
        this.username = user.getUsername();
        this.followings = user.getFollowings().size();
        this.friends = user.getFriends().size();
        this.fans = user.getFans().size();
    }

    public static GetUserDTO from(User user){
        return new GetUserDTO(user);
    }
}
