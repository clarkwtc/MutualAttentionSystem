package org.app.infrastructure.endpoints.dto;

import org.app.domain.User;

import java.util.List;

public class GetSubscriptionListDTO {
    public String id;
    public String nickname;
    public String username;

    public GetSubscriptionListDTO(User user) {
        this.id = user.getId().toString();
        this.username = user.getUsername();
    }

    public static List<GetSubscriptionListDTO> from(User user){
        return user.getFriends().stream().map(GetSubscriptionListDTO::new).toList();
    }
}
