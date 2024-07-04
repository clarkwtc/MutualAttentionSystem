package domain

type MututalAttentionSystem struct {
    Users []*User
}

func NewMutualAttentionSysyem() *MututalAttentionSystem {
    return &MututalAttentionSystem{}
}

func (system *MututalAttentionSystem) AddUserByUsername(username string) {
    for _, _user := range system.Users {
        if _user.Username == username {
            return
        }
    }
    system.Users = append(system.Users, NewUser(username))
}

func (system *MututalAttentionSystem) AddUser(user *User) {
    for _, _user := range system.Users {
        if _user.ID == user.ID {
            return
        }
    }
    system.Users = append(system.Users, user)
}

func (system *MututalAttentionSystem) AddUserAll(users []*User) {
    for _, user := range users {
        system.AddUser(user)
    }
}

func (system *MututalAttentionSystem) GetUser(userId string) *User {
    for _, user := range system.Users {
        if user.ID.String() == userId {
            return user
        }
    }
    panic("Not exist user")
}
