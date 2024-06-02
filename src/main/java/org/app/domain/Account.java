package org.app.domain;

import org.app.domain.exceptions.DuplicatedAccountException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Account {
    private final UUID id;
    private final String nickname;
    private final String username;
    private String password;
    private final List<Account> subscription;
    private final List<Account> friends;
    private final List<Account> follower;

    public Account(String username, String password, String nickname){
        this.id = UUID.randomUUID();
        this.nickname = nickname;
        this.username = username;
        this.password = password;
        this.subscription = new ArrayList<>();
        this.friends = new ArrayList<>();
        this.follower = new ArrayList<>();
    }

    public UUID getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getUsername() {
        return username;
    }

    public List<Account> getFriends() {
        return friends;
    }

    public List<Account> getSubscription() {
        return subscription;
    }

    public List<Account> getFollower() {
        return follower;
    }

    public void subscribe(Account follower){
        if (subscription.contains(follower)){
            return;
        }
        follower.addFans(this);
        subscription.add(follower);
    }

    public void unsubscribe(Account follower){
        if (!subscription.contains(follower)){
            return;
        }
        follower.removeFans(this);
        subscription.remove(follower);
    }

    public void addFriend(Account friend){
        if (friends.contains(friend)){
            throw new DuplicatedAccountException();
        }
        friend.getFriends().add(this);
        friends.add(friend);
    }

    public void removeFriend(Account friend){
        if (!friends.contains(friend)){
            return;
        }
        friend.getFriends().remove(this);
        friends.remove(friend);
    }

    private void addFans(Account fan){
        follower.add(fan);
    }

    private void removeFans(Account fan){
        follower.remove(fan);
    }
}
