package errors

type ServiceOverloadError struct {
    CustomError
}

func NewServiceOverloadError(msg string) *ServiceOverloadError {
    return &ServiceOverloadError{CustomError{msg}}
}
