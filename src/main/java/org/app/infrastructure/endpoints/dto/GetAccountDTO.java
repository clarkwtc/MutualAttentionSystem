package org.app.infrastructure.endpoints.dto;

import org.app.domain.Account;

public class GetAccountDTO {
    public String id;
    public String nickname;
    public String username;
    public Integer followers;
    public Integer friends;
    public Integer fans;

    public GetAccountDTO() {}

    public GetAccountDTO(Account account) {
        this.id = account.getId().toString();
        this.nickname = account.getNickname();
        this.username = account.getUsername();
        this.followers = account.getSubscription().size();
        this.friends = account.getFriends().size();
        this.fans = account.getFollower().size();
    }

    public static GetAccountDTO from(Account account){
        return new GetAccountDTO(account);
    }
}
