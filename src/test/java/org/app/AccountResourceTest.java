package org.app;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import jakarta.inject.Inject;
import org.app.domain.Account;
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
class AccountResourceTest {
    @Inject
    InMemoryAccountRepository repository;

    List<Account> accounts;

    @BeforeEach
    void setUp(){
        Account account1 = new Account("sk22", "bb753951", "lisa");
        Account account2 = new Account("cp200", "cc456850", "mega");
        accounts = new ArrayList<>();
        accounts.add(account1);
        accounts.add(account2);
        accounts.forEach(repository::addAccount);
    }

    @AfterEach
    void tearDown(){
        repository.removeAccounts(accounts);
        accounts.clear();
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
        Account account = accounts.get(0);

        RequestSpecification requestSpecification = given()
                .queryParam("id", account.getId().toString());

        // When
        Response response = requestSpecification
                .when()
                .get("/accounts");

        // Then
        GetAccountDTO registerAccountDTO = response.then()
                .statusCode(200)
                .extract().body()
                .as(GetAccountDTO.class);

        Assertions.assertEquals(registerAccountDTO.id, account.getId().toString());
        Assertions.assertEquals(registerAccountDTO.username, account.getUsername());
        Assertions.assertEquals(registerAccountDTO.nickname, account.getNickname());
        Assertions.assertEquals(registerAccountDTO.followers, account.getSubscription().size());
        Assertions.assertEquals(registerAccountDTO.friends, account.getFriends().size());
        Assertions.assertEquals(registerAccountDTO.fans, account.getFollower().size());
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
        Account account = accounts.get(0);
        Account friend = accounts.get(1);
        Map<String, Object> body = new HashMap<>();
        body.put("friendId", friend.getId());

        RequestSpecification requestSpecification = given()
                .pathParam("id", account.getId().toString())
                .contentType(ContentType.JSON)
                .body(body);

        // When
        Response response = requestSpecification
                .when()
                .post("/accounts/{id}/add_friend");

        // Then
        response.then().statusCode(200);
        Assertions.assertEquals(friend, repository.findAccount(account.getId()).getFriends().get(0));
        Assertions.assertEquals(account, repository.findAccount(friend.getId()).getFriends().get(0));
    }

    @ParameterizedTest
    @MethodSource()
    void addNotExistedFriendFail(boolean isAccount) {
        // Given
        UUID friendId = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();
        if (isAccount){
            accountId = accounts.get(0).getId();
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
        Account account = accounts.get(0);
        Account friend = accounts.get(1);
        account.addFriend(friend);
        repository.updateAccounts(List.of(account));

        RequestSpecification requestSpecification = given()
                .pathParam("id", account.getId().toString());

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
            Assertions.assertEquals(getFriendListDTO.get("nickname"), friend.getNickname());
        }
    }

    @Test
    void removeFriend() {
        // Given
        Account account = accounts.get(0);
        Account friend = accounts.get(1);
        account.addFriend(friend);
        repository.updateAccounts(List.of(account, friend));

        Map<String, Object> body = new HashMap<>();
        body.put("friendId", friend.getId());

        RequestSpecification requestSpecification = given()
                .pathParam("id", account.getId().toString())
                .contentType(ContentType.JSON)
                .body(body);

        // When
        Response response = requestSpecification
                .when()
                .delete("/accounts/{id}/remove_friend");

        // Then
        response.then().statusCode(200);
        Assertions.assertEquals(0, repository.findAccount(account.getId()).getFriends().size());
        Assertions.assertEquals(0, repository.findAccount(friend.getId()).getFriends().size());
    }

    @Test
    void removeNotExistedFriendFail() {
        // Given
        UUID accountId = UUID.randomUUID();
        UUID friendId = accounts.get(0).getId();

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
        Account account = accounts.get(0);
        Account subscription = accounts.get(1);
        Map<String, Object> body = new HashMap<>();
        body.put("subscriptionId", subscription.getId());

        RequestSpecification requestSpecification = given()
                .pathParam("id", account.getId().toString())
                .contentType(ContentType.JSON)
                .body(body);

        // When
        Response response = requestSpecification
                .when()
                .post("/accounts/{id}/subscribe");

        // Then
        response.then().statusCode(200);
        Assertions.assertEquals(subscription, repository.findAccount(account.getId()).getSubscription().get(0));
        Assertions.assertEquals(account, repository.findAccount(subscription.getId()).getFollower().get(0));
    }

    @ParameterizedTest
    @MethodSource()
    void subscribeNotExistAccountFail(boolean isAccount) {
        // Given
        UUID subscriptionId = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();
        if (isAccount){
            accountId = accounts.get(0).getId();
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
    void getSubscriptionList() {
        // Given
        Account account = accounts.get(0);
        Account subscription = accounts.get(1);
        account.subscribe(subscription);
        repository.updateAccounts(List.of(account));

        RequestSpecification requestSpecification = given()
                .pathParam("id", account.getId().toString());

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
            Assertions.assertEquals(getSubscriptionListDTO.get("nickname"), subscription.getNickname());
        }
    }

    @Test
    void unsubscribe() {
        // Given
        Account account = accounts.get(0);
        Account subscription = accounts.get(1);
        account.subscribe(subscription);
        repository.updateAccounts(List.of(account, subscription));

        Map<String, Object> body = new HashMap<>();
        body.put("subscriptionId", subscription.getId());

        RequestSpecification requestSpecification = given()
                .pathParam("id", account.getId().toString())
                .contentType(ContentType.JSON)
                .body(body);

        // When
        Response response = requestSpecification
                .when()
                .delete("/accounts/{id}/unsubscribe");

        // Then
        response.then().statusCode(200);
        Assertions.assertEquals(0, repository.findAccount(account.getId()).getSubscription().size());
        Assertions.assertEquals(0, repository.findAccount(subscription.getId()).getFollower().size());
    }

    @Test
    void unsubscribeNotAccountFail() {
        // Given
        UUID accountId = UUID.randomUUID();
        UUID subscriptionId = accounts.get(0).getId();

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
    void getFollowerList() {
        // Given
        Account account = accounts.get(0);
        Account subscription = accounts.get(1);
        account.subscribe(subscription);
        repository.updateAccounts(List.of(account));

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
            Assertions.assertEquals(getFollowerListDTO.get("id"), account.getId().toString());
            Assertions.assertEquals(getFollowerListDTO.get("username"), account.getUsername());
            Assertions.assertEquals(getFollowerListDTO.get("nickname"), account.getNickname());
        }
    }

    @Test
    void removeFollower() {
        // Given
        Account account = accounts.get(0);
        Account subscription = accounts.get(1);
        account.subscribe(subscription);
        repository.updateAccounts(List.of(account, subscription));

        Map<String, Object> body = new HashMap<>();
        body.put("followerId", account.getId());

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
        Assertions.assertEquals(0, repository.findAccount(subscription.getId()).getFollower().size());
        Assertions.assertEquals(0, repository.findAccount(account.getId()).getSubscription().size());
    }
}
