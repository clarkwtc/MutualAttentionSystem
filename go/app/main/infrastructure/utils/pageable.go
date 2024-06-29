package utils

import "mutualAttentionSystem/app/main/domain/errors"

type Pageable struct {
    Offset    int
    End       int
    TotalPage int
}

func NewPableable(start int, limit int, size int) (*Pageable, error) {
    offset := max(0, (start-1)*limit)
    end := min(offset+limit, size)
    totalPage := calculateTotalPage(limit, size)

    if size > 0 && offset > size {
        return nil, &errors.PageSizeTooLargeError{}
    }

    return &Pageable{offset, end, totalPage}, nil
}

func calculateTotalPage(limit int, size int) int {
    var totalPage int
    if size%limit == 0 {
        totalPage = size / limit
    } else {
        totalPage = size/limit + 1
    }
    return totalPage
}
