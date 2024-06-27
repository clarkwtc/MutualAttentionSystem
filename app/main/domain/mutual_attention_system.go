package domain

type MututalAttentionSystem struct {
    Users []User
}

func NewMutualAttentionSysyem() *MututalAttentionSystem {
    return &MututalAttentionSystem{[]User{}}
}

func (system *MututalAttentionSystem) AddUser(user User) {
    for _, _user := range system.Users {
        if _user.Username == user.Username {
            return
        }
    }
    system.Users = append(system.Users, user)
}

func existUser(users []User, targetUser User) bool {
    for _, user := range users {
        if user.ID == targetUser.ID {
            return true
        }
    }
    return false
}

func (system *MututalAttentionSystem) AddUserAll(users []User) {
    var newUser []User

    for _, user := range users {
        if !existUser(system.Users, user) {
            newUser = append(newUser, user)
        }
    }

    system.Users = append(system.Users, newUser...)
}

func (system *MututalAttentionSystem) GetUser(userId string) User {
    for _, user := range system.Users {
        if user.ID.String() == userId {
            return user
        }
    }
    panic("Not exist user")
}
