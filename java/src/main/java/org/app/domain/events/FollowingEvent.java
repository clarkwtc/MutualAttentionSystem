package org.app.domain.events;

import org.app.domain.User;

import java.util.List;

public class FollowingEvent {
    private final List<User> followings;
    private final int page;
    private final int limit;
    private final int totalPage;

    public FollowingEvent(List<User> followings, int page, int limit, int totalPage) {
        this.followings = followings;
        this.page = page;
        this.limit = limit;
        this.totalPage = totalPage;
    }

    public List<User> getFollowings() {
        return followings;
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
