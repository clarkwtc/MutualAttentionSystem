package org.app.infrastructure.endpoints.dto;

import org.app.domain.User;

public class GetUserItemDTO {
    public String id;
    public String username;

    public GetUserItemDTO(String id, String username) {
        this.id = id;
        this.username = username;
    }

    public GetUserItemDTO(User user) {
        this.id = user.getId().toString();
        this.username = user.getUsername();
    }
}
