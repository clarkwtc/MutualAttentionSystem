package org.app.infrastructure.utils;

import org.app.domain.exceptions.PageSizeTooLargeException;

public class Pageable {
    private final int offset;
    private final int end;
    private final int totalPage;

    public Pageable(int start, int limit, int size) {
        this.offset = Math.max(0, (start - 1) * limit);
        this.end = Math.min(offset + limit, size);
        this.totalPage = size % limit == 0? size / limit: size / limit + 1;

        if(size > 0 && offset > size){
            throw new PageSizeTooLargeException();
        }
    }

    public int getOffset() {
        return offset;
    }

    public int getEnd() {
        return end;
    }

    public int getTotalPage() {
        return totalPage;
    }
}
