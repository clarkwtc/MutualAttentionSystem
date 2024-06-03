package org.app.domain.events;

import org.app.domain.User;

public class UserEvent {
    private final User user;
    private final int page;
    private final int limit;
    private final int totalPage;

    public UserEvent(User user, int page, int limit, int totalPage) {
        this.user = user;
        this.page = page;
        this.limit = limit;
        this.totalPage = totalPage;
    }

    public User getUser() {
        return user;
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
