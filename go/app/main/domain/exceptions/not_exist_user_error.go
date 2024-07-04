package exceptions

type NotExistUserError struct {
    Message string
}

func (error *NotExistUserError) Error() string {
    return error.Message
}
