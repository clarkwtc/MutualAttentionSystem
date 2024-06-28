package domain

type IUserRepository interface {
    FindByUsername(username string) *User
    Register(user *User)
}
