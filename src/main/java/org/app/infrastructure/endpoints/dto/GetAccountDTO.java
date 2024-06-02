package org.app.infrastructure.endpoints.dto;

import org.app.domain.User;

public class GetAccountDTO {
    public String id;
    public String nickname;
    public String username;
    public Integer followers;
    public Integer friends;
    public Integer fans;

    public GetAccountDTO() {}

    public GetAccountDTO(User user) {
        this.id = user.getId().toString();
        this.username = user.getUsername();
        this.followers = user.getFollowings().size();
        this.friends = user.getFriends().size();
        this.fans = user.getFans().size();
    }

    public static GetAccountDTO from(User user){
        return new GetAccountDTO(user);
    }
}
