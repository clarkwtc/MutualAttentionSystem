package utils

type Pageable struct {
    Offset    int
    End       int
    TotalPage int
}

func NewPableable(start int, limit int, size int) *Pageable {
    offset := max(0, (start-1)*limit)
    end := min(offset+limit, size)
    totalPage := calculateTotalPage(limit, size)

    return &Pageable{offset, end, totalPage}
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
