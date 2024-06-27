package test

import (
    "github.com/google/uuid"
    "github.com/stretchr/testify/assert"
    "mutualAttentionSystem/app/main/domain"
    "testing"
)

func initSystem() *domain.MututalAttentionSystem {
    system := domain.NewMutualAttentionSysyem()
    var relationships []*domain.Relationship
    var relationships1 []*domain.Relationship
    system.AddUser(domain.User{ID: uuid.New(), Username: "sk22", Relationships: relationships})
    system.AddUser(domain.User{ID: uuid.New(), Username: "pk67", Relationships: relationships1})
    return system
}

func TestFollow(t *testing.T) {
    // Given
    system := initSystem()
    users := system.Users
    user := &users[0]
    user1 := &users[1]

    // When
    user.Follow(user1)

    // Then
    assert.Equal(t, 1, len(user.GetFollowings()))
    assert.Equal(t, 0, len(user.GetFans()))
    assert.Equal(t, 0, len(user1.GetFollowings()))
    assert.Equal(t, 1, len(user1.GetFans()))
    assert.Equal(t, false, user.IsFriend(user1))
    assert.Equal(t, false, user1.IsFriend(user))
}

func TestMakeFriend(t *testing.T) {
    // Given
    system := initSystem()
    users := system.Users
    user := &users[0]
    user1 := &users[1]

    // When
    user.Follow(user1)
    user1.Follow(user)

    // Then
    assert.Equal(t, 1, len(user.GetFollowings()))
    assert.Equal(t, 1, len(user.GetFans()))
    assert.Equal(t, 1, len(user1.GetFollowings()))
    assert.Equal(t, 1, len(user1.GetFans()))
    assert.Equal(t, true, user.IsFriend(user1))
    assert.Equal(t, true, user1.IsFriend(user))
}

func TestUnFollow(t *testing.T) {
    // Given
    system := initSystem()
    users := system.Users
    user := &users[0]
    user1 := &users[1]
    user.Follow(user1)

    // When
    user.UnFollow(user1)

    // Then
    assert.Equal(t, 0, len(user.GetFollowings()))
    assert.Equal(t, 0, len(user.GetFans()))
    assert.Equal(t, 0, len(user1.GetFollowings()))
    assert.Equal(t, 0, len(user1.GetFans()))
    assert.Equal(t, false, user.IsFriend(user1))
    assert.Equal(t, false, user1.IsFriend(user))
}

func TestUnFriend(t *testing.T) {
    // Given
    system := initSystem()
    users := system.Users
    user := &users[0]
    user1 := &users[1]
    user.Follow(user1)
    user1.Follow(user)

    // When
    user.UnFollow(user1)

    // Then
    assert.Equal(t, 0, len(user.GetFollowings()))
    assert.Equal(t, 1, len(user.GetFans()))
    assert.Equal(t, 1, len(user1.GetFollowings()))
    assert.Equal(t, 0, len(user1.GetFans()))
    assert.Equal(t, false, user.IsFriend(user1))
    assert.Equal(t, false, user1.IsFriend(user))
}
