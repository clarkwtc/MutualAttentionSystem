package org.app;

import io.quarkus.test.junit.QuarkusTest;
import org.app.domain.MutualAttentionSystem;
import org.app.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

@QuarkusTest
public class UserTestCase {
    MutualAttentionSystem system;
    @BeforeEach
    void setUp() {
        MutualAttentionSystem system = new MutualAttentionSystem();
        system.addUser("sk22");
        system.addUser("pk67");
        this.system = system;
    }

    @Test
    void follow() {
        // Given
        List<User> users = system.getUsers();
        User user = users.get(0);
        User user1 = users.get(1);

        // When
        user.follow(user1);

        // Then
        Assertions.assertEquals(1, user.getFollowings().size());
        Assertions.assertEquals(0, user.getFans().size());
        Assertions.assertEquals(0, user1.getFollowings().size());
        Assertions.assertEquals(1, user1.getFans().size());
        Assertions.assertFalse(user.isFriend(user1));
        Assertions.assertFalse(user1.isFriend(user));
    }

    @Test
    void makeFriend() {
        // Given
        List<User> users = system.getUsers();
        User user = users.get(0);
        User user1 = users.get(1);

        // When
        user.follow(user1);
        user1.follow(user);

        // Then
        Assertions.assertEquals(1, user.getFollowings().size());
        Assertions.assertEquals(1, user.getFans().size());
        Assertions.assertEquals(1, user1.getFollowings().size());
        Assertions.assertEquals(1, user1.getFans().size());
        Assertions.assertTrue(user.isFriend(user1));
        Assertions.assertTrue(user1.isFriend(user));
    }

    @Test
    void unfollow() {
        // Given
        List<User> users = system.getUsers();
        User user = users.get(0);
        User user1 = users.get(1);
        user.follow(user1);

        // When
        user.unfollow(user1);

        // Then
        Assertions.assertEquals(0, user.getFollowings().size());
        Assertions.assertEquals(0, user.getFans().size());
        Assertions.assertEquals(0, user1.getFollowings().size());
        Assertions.assertEquals(0, user1.getFans().size());
        Assertions.assertFalse(user.isFriend(user1));
        Assertions.assertFalse(user1.isFriend(user));
    }

    @Test
    void unfriend() {
        // Given
        List<User> users = system.getUsers();
        User user = users.get(0);
        User user1 = users.get(1);
        user.follow(user1);
        user1.follow(user);

        // When
        user.unfollow(user1);

        // Then
        Assertions.assertEquals(0, user.getFollowings().size());
        Assertions.assertEquals(1, user.getFans().size());
        Assertions.assertEquals(1, user1.getFollowings().size());
        Assertions.assertEquals(0, user1.getFans().size());
        Assertions.assertFalse(user.isFriend(user1));
        Assertions.assertFalse(user1.isFriend(user));
    }
}
