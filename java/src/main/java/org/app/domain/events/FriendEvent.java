package org.app.domain.events;

import org.app.domain.User;

import java.util.List;

public class FriendEvent {
    private final List<User> friends;
    private final int page;
    private final int limit;
    private final int totalPage;

    public FriendEvent(List<User> friends, int page, int limit, int totalPage) {
        this.friends = friends;
        this.page = page;
        this.limit = limit;
        this.totalPage = totalPage;
    }

    public List<User> getFriends() {
        return friends;
    }

    public int getPage() {
        return page;
    }

    public int getLimit() {
        return limit;
    }

    public int getTotalPage() {
        return totalPage;
    }
}
