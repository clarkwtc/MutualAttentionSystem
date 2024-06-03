package org.app.infrastructure.endpoints.dto;

import org.app.domain.User;

import java.util.List;

public class GetFollowingListDTO {
    public String id;
    public String username;

    public GetFollowingListDTO(User user) {
        this.id = user.getId().toString();
        this.username = user.getUsername();
    }

    public static List<GetFollowingListDTO> from(User user){
        return user.getFollowings().stream().map(GetFollowingListDTO::new).toList();
    }
}
