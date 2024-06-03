package org.app.domain.events;

import org.app.domain.User;

import java.util.List;

public class FanEvent {
    private final List<User> fans;
    private final int page;
    private final int limit;
    private final int totalPage;

    public FanEvent(List<User> fans, int page, int limit, int totalPage) {
        this.fans = fans;
        this.page = page;
        this.limit = limit;
        this.totalPage = totalPage;
    }

    public List<User> getFans() {
        return fans;
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
