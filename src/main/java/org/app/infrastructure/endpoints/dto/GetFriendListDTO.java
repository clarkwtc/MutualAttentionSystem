package org.app.infrastructure.endpoints.dto;

import org.app.domain.User;

import java.util.List;

public class GetFriendListDTO {
    public String id;
    public String nickname;
    public String username;

    public GetFriendListDTO() {}

    public GetFriendListDTO(User user) {
        this.id = user.getId().toString();
        this.username = user.getUsername();
    }

    public static List<GetFriendListDTO> from(User user){
        return user.getFriends().stream().map(GetFriendListDTO::new).toList();
    }
}
