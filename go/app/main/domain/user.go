package domain

import "github.com/google/uuid"

type User struct {
    ID            uuid.UUID
    Username      string
    Relationships []*Relationship
}

func NewUser(username string) *User {
    var relationship []*Relationship
    return &User{uuid.New(), username, relationship}
}

func (selfUser *User) Follow(user *User) {
    if selfUser != user && selfUser.IsFollowing(user) {
        return
    }

    relationship := NewRelationship(user, selfUser)
    user.Relationships = append(user.Relationships, relationship)
    selfUser.Relationships = append(selfUser.Relationships, relationship)
}

func (selfUser *User) UnFollow(user *User) {
    if selfUser != user && !selfUser.IsFollowing(user) {
        return
    }

    selfUserIndex, selfUserRelationship := selfUser.GetRelationship(user, selfUser)
    if selfUserRelationship == nil {
        return
    }
    userIndex, userRelationship := user.GetRelationship(user, selfUser)
    if userRelationship == nil {
        return
    }

    selfUser.Relationships = append(selfUser.Relationships[:selfUserIndex], selfUser.Relationships[selfUserIndex+1:]...)
    user.Relationships = append(user.Relationships[:userIndex], user.Relationships[userIndex+1:]...)

    selfUserIndex, selfUserRelationship = selfUser.GetRelationship(selfUser, user)
    if selfUserRelationship == nil {
        return
    }
    selfUserRelationship.UnFriend()
}

func (selfUser *User) GetFriends() []*User {
    var friends []*User
    for _, relationship := range selfUser.Relationships {
        if relationship.Fan.ID == selfUser.ID && relationship.IsFriend {
            friends = append(friends, relationship.Following)
        }
    }
    return friends
}

func (selfUser *User) GetFollowings() []*User {
    var followings []*User
    for _, relationship := range selfUser.Relationships {
        if relationship.Fan.ID == selfUser.ID {
            followings = append(followings, relationship.Following)
        }
    }
    return followings
}

func (selfUser *User) GetFans() []*User {
    var fans []*User
    for _, relationship := range selfUser.Relationships {
        if relationship.Following.ID == selfUser.ID {
            fans = append(fans, relationship.Fan)
        }
    }
    return fans
}

func (selfUser *User) GetRelationship(following *User, fan *User) (int, *Relationship) {
    for index, relationship := range selfUser.Relationships {
        if relationship.Following.ID == following.ID && relationship.Fan.ID == fan.ID {
            return index, relationship
        }
    }
    return -1, nil
}

func (selfUser *User) IsFollowing(user *User) bool {
    for _, relationship := range selfUser.Relationships {
        if relationship.Following.ID == user.ID && relationship.Fan.ID == selfUser.ID {
            return true
        }
    }
    return false
}

func (selfUser *User) IsFriend(user *User) bool {
    _, relationship := selfUser.GetRelationship(user, selfUser)
    if relationship == nil {
        return false
    }
    return relationship.IsFriend
}
