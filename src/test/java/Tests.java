import Helpers.*;

import io.restassured.path.json.JsonPath;
import org.bson.Document;
import org.junit.jupiter.api.*;
import Helpers.DataHelper.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static Helpers.APIHelper.getToken;
import static Helpers.DataHelper.getEditInfo;
import static Helpers.DataHelper.getQuizInfo;
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
        Document expected = dataBase.getDocument("users", "_id", actual._id);
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
        Document expected = dataBase.getDocument("users", "_id", actual._id);
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
        Document expected = dataBase.getDocument("themequestions", "_id", actual._id);
        assertAll(() -> assertEquals(expected.get("_id"), actual._id),
                () -> assertEquals(expected.get("name"), actual.name));
    }

    @Test
    @DisplayName("4. Editing a question")
    void editingQuestion() {
        String question = faker.internet().emailSubject() + "?";
        String answer = faker.internet().emailSubject();
        int id = APIHelper.addingQuestion(new QuestionInfo(question), getToken())._id;
        var actual = APIHelper.editingQuestion(getEditInfo(question, answer, Integer.toString(id)), getToken());
        Document expected = dataBase.getDocument("themequestions", "_id", id);
        assertAll(() -> assertEquals(expected.get("answer"), actual.data.answer),
                () -> assertEquals(expected.get("name"), actual.name));
    }

    @Test
    @DisplayName("5. Adding a quiz")
    void addingQuiz() {
        QuizInfo expectedQuizInfo = getQuizInfo();
        var response = APIHelper.addingQuiz(expectedQuizInfo, getToken());
        JsonPath actual = response.jsonPath();
        Document expected = dataBase.getDocument("quizzes", "_id", actual.getInt("data._id"));
        assertAll(() -> assertEquals(expected.get("_id"), actual.getInt("data._id")),
                () -> assertEquals(expected.get("answerType"), actual.getString("data.answerType")),
                () -> assertTrue(actual.getBoolean("data.isValid")),
                () -> assertEquals(expected.get("name"), actual.getString("data.name")),
                () -> assertArrayEquals(new Object[]{actual.getList("data.variations")},
                        new Object[]{expected.get("variations")}));
    }
}
