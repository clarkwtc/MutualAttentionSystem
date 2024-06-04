package org.app;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import jakarta.inject.Inject;
import org.app.domain.Relationship;
import org.app.domain.User;
import org.app.domain.exceptions.ExceptionMessage;
import org.app.infrastructure.endpoints.dto.*;
import org.app.infrastructure.local.InMemoryRelationshipRepository;
import org.app.infrastructure.local.InMemoryUserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;

@QuarkusTest
class UserResourceTest {
    @Inject
    InMemoryUserRepository userRepository;

    @Inject
    InMemoryRelationshipRepository relationshipRepository;

    List<User> users;

    @BeforeEach
    void setUp(){
        User user1 = new User("sk22");
        User user2 = new User("cp200");
        users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        users.forEach(userRepository::register);
    }

    @AfterEach
    void tearDown(){
        userRepository.removeUsers(users);
        users.clear();
    }


    @Test
    void registerUser() {
        // Given
        Map<String, Object> body = new HashMap<>();
        body.put("username", "elk33");

        RequestSpecification requestSpecification = given()
                .contentType(ContentType.JSON)
                .body(body);

        // When
        Response response = requestSpecification
                .when()
                .post("/users");

        // Then
        RegisterUserDTO registerUserDTO = response.then()
                .statusCode(201)
                .extract().body()
                .as(RegisterUserDTO.class);

        Assertions.assertNotNull(registerUserDTO.id);
    }

    @Test
    void registerDuplicatedUserFail() {
        // Given
        Map<String, Object> body = new HashMap<>();
        body.put("username", "sk22");

        RequestSpecification requestSpecification = given()
                .contentType(ContentType.JSON)
                .body(body);

        // When
        Response response = requestSpecification
                .when()
                .post("/users");

        // Then
        String message = response.then().statusCode(409)
                .extract().body()
                .asPrettyString();

        Assertions.assertEquals(ExceptionMessage.DUPLICATED_USERNAME, message);
    }

    @Test
    void getUser() {
        // Given
        User user = users.get(0);

        RequestSpecification requestSpecification = given()
                .queryParam("id", user.getId().toString());

        // When
        Response response = requestSpecification
                .when()
                .get("/users");

        // Then
        GetUserDTO getUserDTO = response.then()
                .statusCode(200)
                .extract().body()
                .as(GetUserDTO.class);

        Assertions.assertEquals(user.getId().toString(), getUserDTO.id);
        Assertions.assertEquals(user.getUsername(), getUserDTO.username);
        Assertions.assertEquals(0, getUserDTO.followings);
        Assertions.assertEquals(0, getUserDTO.friends);
        Assertions.assertEquals(0, getUserDTO.fans);
    }

    @Test
    void getNotExistedUserFail() {
        // Given
        RequestSpecification requestSpecification = given()
                .queryParam("id", UUID.randomUUID());

        // When
        Response response = requestSpecification
                .when()
                .get("/users");

        // Then
        String message = response.then().statusCode(404)
                .extract().body()
                .asPrettyString();

        Assertions.assertEquals(ExceptionMessage.NOT_EXIST_USER, message);
    }

    @Test
    void follow() {
        // Given
        User user = users.get(0);
        User following = users.get(1);
        Map<String, Object> body = new HashMap<>();
        body.put("followingId", following.getId());

        RequestSpecification requestSpecification = given()
                .pathParam("id", user.getId().toString())
                .contentType(ContentType.JSON)
                .body(body);

        // When
        Response response = requestSpecification
                .when()
                .post("/users/{id}/follow");

        // Then
        response.then().statusCode(200);
        List<Relationship> relationships = relationshipRepository.find(following.getId(), user.getId());
        Assertions.assertEquals(1, relationships.size());
        Assertions.assertFalse(relationships.get(0).isFriend());
    }

    @ParameterizedTest
    @MethodSource()
    void followNotExistUserFail(boolean isUser) {
        // Given
        UUID followingId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        if (isUser){
            userId = users.get(0).getId();
        }

        Map<String, Object> body = new HashMap<>();
        body.put("followingId", followingId);

        RequestSpecification requestSpecification = given()
                .pathParam("id", userId)
                .contentType(ContentType.JSON)
                .body(body);

        // When
        Response response = requestSpecification
                .when()
                .post("/users/{id}/follow");

        // Then
        String message = response.then().statusCode(404)
                .extract().body()
                .asPrettyString();

        Assertions.assertEquals(ExceptionMessage.NOT_EXIST_USER, message);
    }

    private static Stream<Arguments> followNotExistUserFail(){
        return Stream.of(
                Arguments.of(true),
                Arguments.of(false )
        );
    }

    @Test
    void getFollowingsList() {
        // Given
        User user = users.get(0);
        User following = users.get(1);
        user.follow(following);
        relationshipRepository.addAll(user.getRelationships());

        RequestSpecification requestSpecification = given()
                .pathParam("id", user.getId().toString());

        // When
        Response response = requestSpecification
                .when()
                .get("/users/{id}/followings");

        // Then
        GetFollowingListDTO getFollowingsListDTOs = response.then().statusCode(200)
                .extract().body()
                .as(GetFollowingListDTO.class);

        Assertions.assertEquals(1, getFollowingsListDTOs.following.size());
        for (GetUserItemDTO getUserItemDTO: getFollowingsListDTOs.following) {
            Assertions.assertEquals(following.getId().toString(), getUserItemDTO.id);
            Assertions.assertEquals(following.getUsername(), getUserItemDTO.username);
        }
    }

    @Test
    void unfollow() {
        // Given
        User user = users.get(0);
        User following = users.get(1);
        user.follow(following);
        relationshipRepository.addAll(user.getRelationships());
        Assertions.assertEquals(1, relationshipRepository.findByUserId(user.getId()).size());
        Assertions.assertEquals(1, relationshipRepository.findByUserId(following.getId()).size());

        Map<String, Object> body = new HashMap<>();
        body.put("followingId", following.getId());

        RequestSpecification requestSpecification = given()
                .pathParam("id", user.getId().toString())
                .contentType(ContentType.JSON)
                .body(body);

        // When
        Response response = requestSpecification
                .when()
                .delete("/users/{id}/unfollow");

        // Then
        response.then().statusCode(200);
        Assertions.assertEquals(0, relationshipRepository.findByUserId(user.getId()).size());
        Assertions.assertEquals(0, relationshipRepository.findByUserId(following.getId()).size());
    }

    @Test
    void unfollowNotUserFail() {
        // Given
        UUID userId = UUID.randomUUID();
        UUID followingId = users.get(0).getId();

        Map<String, Object> body = new HashMap<>();
        body.put("followingId", followingId);

        RequestSpecification requestSpecification = given()
                .pathParam("id", userId)
                .contentType(ContentType.JSON)
                .body(body);

        // When
        Response response = requestSpecification
                .when()
                .delete("/users/{id}/unfollow");

        // Then
        String message = response.then().statusCode(404).extract().asPrettyString();
        Assertions.assertEquals(message, ExceptionMessage.NOT_EXIST_USER);
    }

    @Test
    void getFansList() {
        // Given
        User user = users.get(0);
        User following = users.get(1);
        user.follow(following);
        relationshipRepository.addAll(user.getRelationships());
        Assertions.assertEquals(1, relationshipRepository.findByUserId(user.getId()).size());
        Assertions.assertEquals(1, relationshipRepository.findByUserId(following.getId()).size());

        RequestSpecification requestSpecification = given()
                .pathParam("id", following.getId().toString());

        // When
        Response response = requestSpecification
                .when()
                .get("/users/{id}/fans");

        // Then
        GetFanListDTO getFanListDTOs = response.then().statusCode(200)
                .extract().body()
                .as(GetFanListDTO.class);

        Assertions.assertEquals(1, getFanListDTOs.fans.size());
        for (GetUserItemDTO getUserItemDTO: getFanListDTOs.fans) {
            Assertions.assertEquals(user.getId().toString(), getUserItemDTO.id);
            Assertions.assertEquals(user.getUsername(), getUserItemDTO.username);
        }
    }

    @Test
    void makeFriend() {
        // Given
        User user = users.get(0);
        User following = users.get(1);
        Map<String, Object> body = new HashMap<>();
        body.put("followingId", following.getId());

        RequestSpecification requestSpecification = given()
                .pathParam("id", user.getId().toString())
                .contentType(ContentType.JSON)
                .body(body);

        Response response = requestSpecification
                .when()
                .post("/users/{id}/follow");

        body = new HashMap<>();
        body.put("followingId", user.getId());

        RequestSpecification followingRequestSpecification = given()
                .pathParam("id", following.getId().toString())
                .contentType(ContentType.JSON)
                .body(body);

        // When
        Response followingResponse = followingRequestSpecification
                .when()
                .post("/users/{id}/follow");

        // Then
        response.then().statusCode(200);
        followingResponse.then().statusCode(200);
        Assertions.assertEquals(2, relationshipRepository.find(following.getId(), user.getId()).size());
    }


    @Test
    void getFriendList() {
        // Given
        User user = users.get(0);
        User friend = users.get(1);
        user.follow(friend);
        friend.follow(user);
        relationshipRepository.addAll(user.getRelationships());
        Assertions.assertEquals(2, relationshipRepository.find(friend.getId(), user.getId()).size());

        RequestSpecification requestSpecification = given()
                .pathParam("id", user.getId().toString());

        // When
        Response response = requestSpecification
                .when()
                .get("/users/{id}/friends");

        // Then
        GetFriendListDTO getFriendListDTOs = response.then().statusCode(200)
                .extract().body()
                .as(GetFriendListDTO.class);

        Assertions.assertEquals(1, getFriendListDTOs.friends.size());
        for (GetUserItemDTO getUserItemDTO: getFriendListDTOs.friends) {
            Assertions.assertEquals(friend.getId().toString(), getUserItemDTO.id);
            Assertions.assertEquals(friend.getUsername(), getUserItemDTO.username);
        }
    }
}
