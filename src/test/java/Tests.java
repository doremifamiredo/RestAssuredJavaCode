import Helpers.*;

import net.datafaker.Faker;
import org.bson.Document;
import org.junit.jupiter.api.*;
import Helpers.DataHelper.*;
import io.restassured.response.Response;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static Helpers.APIHelper.getToken;
import static Helpers.UserGenerator.*;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class Tests {
    MongoHelper dataBase = new MongoHelper(
            "mongodb://javacode:bestEducationEver@80.66.64.141:27017/estim?authSource=admin");

    @Test
    @DisplayName("1. Authorization on the portal")
    void authorizationOnThePortal() {
        var authInfo = new AuthInfo("somov_oleg", "DY;nwmkgzpnNx9n");
        var actual = APIHelper.authorization(authInfo).user;
        Document expected = dataBase.getDocument("users", "username", authInfo.username);
        assertAll(() -> assertEquals(expected.get("_id"), actual._id),
                () -> assertEquals(expected.get("surname"), actual.surname),
                () -> assertEquals(expected.get("first_name"), actual.first_name),
                () -> assertEquals(expected.get("username"), actual.username),
                () -> assertEquals(expected.get("name"), actual.name),
                () -> assertEquals(expected.get("email"), actual.email));
    }

    @ParameterizedTest
    @MethodSource("Helpers.UserGenerator#dataFakerStream")
    @DisplayName("2. Adding a user")
    void addingUser(AddingInfo addingInfo) {
        var actual = APIHelper.addingUser(addingInfo, getToken());
        Document expected = dataBase.getDocument("users", "username", addingInfo.username);
        String roles = expected.get("roles").toString().replaceAll("\\[|\\]", "");
        assertAll(() -> assertEquals(expected.get("_id"), actual._id),
                () -> assertEquals(expected.get("surname"), addingInfo.surname),
                () -> assertEquals(expected.get("first_name"), addingInfo.first_name),
                () -> assertEquals(expected.get("username"), addingInfo.username),
                () -> assertEquals(roles, addingInfo.roles),
                () -> assertEquals(expected.get("name"), actual.name),
                () -> assertEquals(expected.get("email"), addingInfo.email));
    }

    @Test
    @DisplayName("3. Adding a question")
    void addingQuestion() {
        String question = faker.internet().emailSubject();
        var actual = APIHelper.addingQuestion(new QuestionInfo(question), getToken());
        Document expected = dataBase.getDocument("themequestions", "name", question);
        assertAll(() -> assertEquals(expected.get("_id"), actual._id),
                () -> assertEquals(expected.get("name"), actual.name));
    }

    @Test
    @DisplayName("4. Editing a question")
    void editingQuestion() {
        String question = faker.internet().emailSubject();
        var actual = APIHelper.addingQuestion(new QuestionInfo(question), getToken());
     //   var actual = APIHelper.editingQuestion(new QuestionInfo(question), getToken());
    }
}
