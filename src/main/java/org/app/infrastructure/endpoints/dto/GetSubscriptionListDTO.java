package org.app.infrastructure.endpoints.dto;

import org.app.domain.Account;

import java.util.List;

public class GetSubscriptionListDTO {
    public String id;
    public String nickname;
    public String username;

    public GetSubscriptionListDTO(Account account) {
        this.id = account.getId().toString();
        this.nickname = account.getNickname();
        this.username = account.getUsername();
    }

    public static List<GetSubscriptionListDTO> from(Account account){
        return account.getFriends().stream().map(GetSubscriptionListDTO::new).toList();
    }
}
