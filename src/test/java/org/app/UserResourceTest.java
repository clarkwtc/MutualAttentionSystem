package org.app;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import jakarta.inject.Inject;
import org.app.domain.User;
import org.app.domain.exceptions.ExceptionMessage;
import org.app.infrastructure.endpoints.dto.GetAccountDTO;
import org.app.infrastructure.endpoints.dto.RegisterAccountDTO;
import org.app.infrastructure.local.InMemoryAccountRepository;
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
    InMemoryAccountRepository repository;

    List<User> users;

    @BeforeEach
    void setUp(){
        User user1 = new User("sk22");
        User user2 = new User("cp200");
        users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        users.forEach(repository::addAccount);
    }

    @AfterEach
    void tearDown(){
        repository.removeAccounts(users);
        users.clear();
    }


    @Test
    void registerAccount() {
        // Given
        Map<String, Object> body = new HashMap<>();
        body.put("username", "elk33");
        body.put("password", "aa159357");
        body.put("nickname", "jason");

        RequestSpecification requestSpecification = given()
                .contentType(ContentType.JSON)
                .body(body);

        // When
        Response response = requestSpecification
                .when()
                .post("/accounts");

        // Then
        RegisterAccountDTO registerAccountDTO = response.then()
                .statusCode(201)
                .extract().body()
                .as(RegisterAccountDTO.class);

        Assertions.assertNotNull(registerAccountDTO.id);
    }

    @Test
    void registerDuplicatedAccountFail() {
        // Given
        Map<String, Object> body = new HashMap<>();
        body.put("username", "sk22");
        body.put("password", "bb753951");
        body.put("nickname", "lisa");

        RequestSpecification requestSpecification = given()
                .contentType(ContentType.JSON)
                .body(body);

        // When
        Response response = requestSpecification
                .when()
                .post("/accounts");

        // Then
        String message = response.then().statusCode(409)
                .extract().body()
                .asPrettyString();

        Assertions.assertEquals(ExceptionMessage.DUPLICATED_USERNAME, message);
    }

    @Test
    void getAccount() {
        // Given
        User user = users.get(0);

        RequestSpecification requestSpecification = given()
                .queryParam("id", user.getId().toString());

        // When
        Response response = requestSpecification
                .when()
                .get("/accounts");

        // Then
        GetAccountDTO registerAccountDTO = response.then()
                .statusCode(200)
                .extract().body()
                .as(GetAccountDTO.class);

        Assertions.assertEquals(registerAccountDTO.id, user.getId().toString());
        Assertions.assertEquals(registerAccountDTO.username, user.getUsername());
        Assertions.assertEquals(registerAccountDTO.followers, user.getFollowings().size());
        Assertions.assertEquals(registerAccountDTO.friends, user.getFriends().size());
        Assertions.assertEquals(registerAccountDTO.fans, user.getFans().size());
    }

    @Test
    void getNotExistedAccountFail() {
        // Given
        RequestSpecification requestSpecification = given()
                .queryParam("id", UUID.randomUUID());

        // When
        Response response = requestSpecification
                .when()
                .get("/accounts");

        // Then
        String message = response.then().statusCode(404)
                .extract().body()
                .asPrettyString();

        Assertions.assertEquals(ExceptionMessage.NOT_EXIST_ACCOUNT, message);
    }

    @Test
    void addFriend() {
        // Given
        User user = users.get(0);
        User friend = users.get(1);
        Map<String, Object> body = new HashMap<>();
        body.put("friendId", friend.getId());

        RequestSpecification requestSpecification = given()
                .pathParam("id", user.getId().toString())
                .contentType(ContentType.JSON)
                .body(body);

        // When
        Response response = requestSpecification
                .when()
                .post("/accounts/{id}/add_friend");

        // Then
        response.then().statusCode(200);
        Assertions.assertEquals(friend, repository.findAccount(user.getId()).getFriends().get(0));
        Assertions.assertEquals(user, repository.findAccount(friend.getId()).getFriends().get(0));
    }

    @ParameterizedTest
    @MethodSource()
    void addNotExistedFriendFail(boolean isAccount) {
        // Given
        UUID friendId = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();
        if (isAccount){
            accountId = users.get(0).getId();
        }

        Map<String, Object> body = new HashMap<>();
        body.put("friendId", friendId);

        RequestSpecification requestSpecification = given()
                .pathParam("id", accountId)
                .contentType(ContentType.JSON)
                .body(body);

        // When
        Response response = requestSpecification
                .when()
                .post("/accounts/{id}/add_friend");

        // Then
        String message = response.then().statusCode(404)
                .extract().body()
                .asPrettyString();

        Assertions.assertEquals(ExceptionMessage.NOT_EXIST_ACCOUNT, message);
    }

    private static Stream<Arguments> addNotExistedFriendFail(){
        return Stream.of(
                Arguments.of(true),
                Arguments.of(false )
        );
    }

    @Test
    void getFriendList() {
        // Given
        User user = users.get(0);
        User friend = users.get(1);
        repository.updateAccounts(List.of(user));

        RequestSpecification requestSpecification = given()
                .pathParam("id", user.getId().toString());

        // When
        Response response = requestSpecification
                .when()
                .get("/accounts/{id}/friends");

        // Then
        List<?> getFriendListDTOs = response.then().statusCode(200)
                .extract().body()
                .as(ArrayList.class);

        for (Object object: getFriendListDTOs) {
            Map<String, String> getFriendListDTO = (Map<String, String>) object;
            Assertions.assertEquals(getFriendListDTO.get("id"), friend.getId().toString());
            Assertions.assertEquals(getFriendListDTO.get("username"), friend.getUsername());
        }
    }

    @Test
    void removeFriend() {
        // Given
        User user = users.get(0);
        User friend = users.get(1);
        repository.updateAccounts(List.of(user, friend));

        Map<String, Object> body = new HashMap<>();
        body.put("friendId", friend.getId());

        RequestSpecification requestSpecification = given()
                .pathParam("id", user.getId().toString())
                .contentType(ContentType.JSON)
                .body(body);

        // When
        Response response = requestSpecification
                .when()
                .delete("/accounts/{id}/remove_friend");

        // Then
        response.then().statusCode(200);
        Assertions.assertEquals(0, repository.findAccount(user.getId()).getFriends().size());
        Assertions.assertEquals(0, repository.findAccount(friend.getId()).getFriends().size());
    }

    @Test
    void removeNotExistedFriendFail() {
        // Given
        UUID accountId = UUID.randomUUID();
        UUID friendId = users.get(0).getId();

        Map<String, Object> body = new HashMap<>();
        body.put("friendId", friendId);

        RequestSpecification requestSpecification = given()
                .pathParam("id", accountId)
                .contentType(ContentType.JSON)
                .body(body);

        // When
        Response response = requestSpecification
                .when()
                .delete("/accounts/{id}/remove_friend");

        // Then
        String message = response.then().statusCode(404)
                .extract().body()
                .asPrettyString();

        Assertions.assertEquals(ExceptionMessage.NOT_EXIST_ACCOUNT, message);
    }

    @Test
    void subscribe() {
        // Given
        User user = users.get(0);
        User subscription = users.get(1);
        Map<String, Object> body = new HashMap<>();
        body.put("subscriptionId", subscription.getId());

        RequestSpecification requestSpecification = given()
                .pathParam("id", user.getId().toString())
                .contentType(ContentType.JSON)
                .body(body);

        // When
        Response response = requestSpecification
                .when()
                .post("/accounts/{id}/subscribe");

        // Then
        response.then().statusCode(200);
        Assertions.assertEquals(subscription, repository.findAccount(user.getId()).getFollowings().get(0));
        Assertions.assertEquals(user, repository.findAccount(subscription.getId()).getFans().get(0));
    }

    @ParameterizedTest
    @MethodSource()
    void subscribeNotExistAccountFail(boolean isAccount) {
        // Given
        UUID subscriptionId = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();
        if (isAccount){
            accountId = users.get(0).getId();
        }

        Map<String, Object> body = new HashMap<>();
        body.put("subscriptionId", subscriptionId);

        RequestSpecification requestSpecification = given()
                .pathParam("id", accountId)
                .contentType(ContentType.JSON)
                .body(body);

        // When
        Response response = requestSpecification
                .when()
                .post("/accounts/{id}/subscribe");

        // Then
        String message = response.then().statusCode(404)
                .extract().body()
                .asPrettyString();

        Assertions.assertEquals(ExceptionMessage.NOT_EXIST_ACCOUNT, message);
    }

    private static Stream<Arguments> subscribeNotExistAccountFail(){
        return Stream.of(
                Arguments.of(true),
                Arguments.of(false )
        );
    }

    @Test
    void getFollowingsList() {
        // Given
        User user = users.get(0);
        User subscription = users.get(1);
        user.subscribe(subscription);
        repository.updateAccounts(List.of(user));

        RequestSpecification requestSpecification = given()
                .pathParam("id", user.getId().toString());

        // When
        Response response = requestSpecification
                .when()
                .get("/accounts/{id}/subscriptions");

        // Then
        List<?> getSubscriptionListDTOs = response.then().statusCode(200)
                .extract().body()
                .as(ArrayList.class);

        for (Object object: getSubscriptionListDTOs) {
            Map<String, String> getSubscriptionListDTO = (Map<String, String>) object;
            Assertions.assertEquals(getSubscriptionListDTO.get("id"), subscription.getId().toString());
            Assertions.assertEquals(getSubscriptionListDTO.get("username"), subscription.getUsername());
        }
    }

    @Test
    void unsubscribe() {
        // Given
        User user = users.get(0);
        User subscription = users.get(1);
        user.subscribe(subscription);
        repository.updateAccounts(List.of(user, subscription));

        Map<String, Object> body = new HashMap<>();
        body.put("subscriptionId", subscription.getId());

        RequestSpecification requestSpecification = given()
                .pathParam("id", user.getId().toString())
                .contentType(ContentType.JSON)
                .body(body);

        // When
        Response response = requestSpecification
                .when()
                .delete("/accounts/{id}/unsubscribe");

        // Then
        response.then().statusCode(200);
        Assertions.assertEquals(0, repository.findAccount(user.getId()).getFollowings().size());
        Assertions.assertEquals(0, repository.findAccount(subscription.getId()).getFans().size());
    }

    @Test
    void unsubscribeNotAccountFail() {
        // Given
        UUID accountId = UUID.randomUUID();
        UUID subscriptionId = users.get(0).getId();

        Map<String, Object> body = new HashMap<>();
        body.put("subscriptionId", subscriptionId);

        RequestSpecification requestSpecification = given()
                .pathParam("id", accountId)
                .contentType(ContentType.JSON)
                .body(body);

        // When
        Response response = requestSpecification
                .when()
                .delete("/accounts/{id}/unsubscribe");

        // Then
        String message = response.then().statusCode(404).extract().asPrettyString();
        Assertions.assertEquals(message, ExceptionMessage.NOT_EXIST_ACCOUNT);
    }

    @Test
    void getFansList() {
        // Given
        User user = users.get(0);
        User subscription = users.get(1);
        user.subscribe(subscription);
        repository.updateAccounts(List.of(user));

        RequestSpecification requestSpecification = given()
                .pathParam("id", subscription.getId().toString());

        // When
        Response response = requestSpecification
                .when()
                .get("/accounts/{id}/followers");

        // Then
        List<?> getFanListDTOs = response.then().statusCode(200)
                .extract().body()
                .as(ArrayList.class);

        for (Object object: getFanListDTOs) {
            Map<String, String> getFollowerListDTO = (Map<String, String>) object;
            Assertions.assertEquals(getFollowerListDTO.get("id"), user.getId().toString());
            Assertions.assertEquals(getFollowerListDTO.get("username"), user.getUsername());
        }
    }

    @Test
    void removeFollower() {
        // Given
        User user = users.get(0);
        User subscription = users.get(1);
        user.subscribe(subscription);
        repository.updateAccounts(List.of(user, subscription));

        Map<String, Object> body = new HashMap<>();
        body.put("followerId", user.getId());

        RequestSpecification requestSpecification = given()
                .pathParam("id", subscription.getId().toString())
                .contentType(ContentType.JSON)
                .body(body);

        // When
        Response response = requestSpecification
                .when()
                .delete("/accounts/{id}/remove_follower");

        // Then
        response.then().statusCode(200);
        Assertions.assertEquals(0, repository.findAccount(subscription.getId()).getFans().size());
        Assertions.assertEquals(0, repository.findAccount(user.getId()).getFollowings().size());
    }
}
