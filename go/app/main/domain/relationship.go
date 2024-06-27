package domain

import "github.com/google/uuid"

type Relationship struct {
    ID        uuid.UUID
    Following *User
    Fan       *User
    IsFriend  bool
}

func NewRelationship(following *User, fan *User) *Relationship {
    relationship := &Relationship{uuid.New(), following, fan, false}
    relationship.makeFriend()
    return relationship
}

func (selfRelationship *Relationship) makeFriend() {
    fan := selfRelationship.Fan
    following := selfRelationship.Following
    var isFollowing bool = following.IsFollowing(fan)
    if isFollowing {
        _, relationship := following.GetRelationship(fan, following)
        relationship.IsFriend = true
    }
    selfRelationship.IsFriend = isFollowing
}

func (selfRelationship *Relationship) UnFriend() {
    selfRelationship.IsFriend = false
}
