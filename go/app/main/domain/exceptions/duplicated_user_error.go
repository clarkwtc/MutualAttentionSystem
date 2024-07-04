package exceptions

type DuplicatedUserError struct {
    Message string
}

func (error *DuplicatedUserError) Error() string {
    return error.Message
}
