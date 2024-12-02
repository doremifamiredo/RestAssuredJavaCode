import Helpers.*;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.ResponseSpecification;
import org.bson.Document;
import org.joda.time.LocalDateTime;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.format.DateTimeFormatter;

import static Helpers.APIHelper.*;
import static Helpers.DataHelper.*;
import static Helpers.UserGenerator.*;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class Tests {
    MongoHelper dataBase = new MongoHelper(
            "mongodb://javacode:bestEducationEver@80.66.64.141:27017/estim?authSource=admin");

    @Test
    @DisplayName("1. Authorization on the portal")
    void authorizationOnThePortal() {
        var actual = APIHelper.authorization();
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
        var actual = apiRequest(addingInfo, getToken(), "user-auth1").jsonPath();
        Document expected = dataBase.getDocument("users", "_id", actual.getInt("data._id"));
        assertAll(() -> assertEquals(expected.get("_id"), actual.getInt("data._id")),
                () -> assertEquals(expected.get("surname"), addingInfo.surname),
                () -> assertEquals(expected.get("first_name"), addingInfo.first_name),
                () -> assertEquals(expected.get("username"), addingInfo.username),
                () -> assertEquals(addingInfo.roles, actual.getString("data.roles[0]")),
                () -> assertEquals(expected.get("name"), actual.getString("data.name")),
                () -> assertEquals(expected.get("email"), addingInfo.email));
    }

    @Test
    @DisplayName("3. Adding a question")
    void addingQuestion() {
        var actual = apiRequest(new QuestionInfo(faker.internet().emailSubject() + "?"), getToken(), "theme-question").jsonPath();
        Document expected = dataBase.getDocument("themequestions", "_id", actual.getInt("data._id"));
        assertAll(() -> assertEquals(expected.get("_id"), actual.getInt("data._id")),
                () -> assertEquals(expected.get("name"), actual.getString("data.name")));
    }

    @Test
    @DisplayName("4. Editing a question")
    void editingQuestion() {
        int id = apiRequest(new QuestionInfo(faker.internet().emailSubject() + "?"), getToken(), "theme-question")
                .jsonPath().getInt("data._id");
        var actual = apiRequest(getEditInfo(id), getToken(), "create-lts").jsonPath();
        Document expected = dataBase.getDocument("themequestions", "_id", id);
        assertAll(() -> assertEquals(expected.get("answer"), actual.getString("data.versionDB.data.answer")),
                () -> assertEquals(expected.get("name"), actual.getString("data.versionDB.data.name")));
    }

    @Test
    @DisplayName("5. Adding a quiz")
    void addingQuiz() {
        JsonPath actual = apiRequest(getQuizInfo(), getToken(), "quiz").jsonPath();
        Document expected = dataBase.getDocument("quizzes", "_id", actual.getInt("data._id"));
        assertAll(() -> assertEquals(expected.get("_id"), actual.getInt("data._id")),
                () -> assertEquals(expected.get("answerType"), actual.getString("data.answerType")),
                () -> assertTrue(actual.getBoolean("data.isValid")),
                () -> assertEquals(expected.get("name"), actual.getString("data.name")),
                () -> assertArrayEquals(new Object[]{actual.getList("data.variations")},
                        new Object[]{expected.get("variations")}));
    }

    @Test
    @DisplayName("6. Adding a module")
    void addingModule() {
        JsonPath actual = apiRequest(getModuleInfo(), getToken(), "course-module").jsonPath();
        Document expected = dataBase.getDocument("coursemodules", "_id", actual.getInt("data._id"));
        assertAll(() -> assertEquals(expected.get("_id"), actual.getInt("data._id")),
                () -> assertArrayEquals(new Object[]{expected.get("questions")},
                        new Object[]{actual.getList("data.questions")}));
    }

    @Test
    @DisplayName("7. Adding a course")
    void addingCourse() {
        JsonPath actual = apiRequest(getCourseInfo(), getToken(), "course").jsonPath();
        Document expected = dataBase.getDocument("courses", "_id", actual.getInt("data._id"));
        assertAll(() -> assertEquals(expected.get("_id"), actual.getInt("data._id")),
                () -> assertEquals(expected.get("name"), actual.getString("data.name")));
    }

    @Test
    @DisplayName("8. Adding an exam")
    void addingExam() {
        JsonPath actual = apiRequest(getExamInfo(), getToken(), "exam").jsonPath();
        Document expected = dataBase.getDocument("exams", "_id", actual.getInt("data._id"));
        assertAll(() -> assertEquals(expected.get("_id"), actual.getInt("data._id")),
                () -> assertEquals(expected.get("name"), actual.getString("data.name")),
                () -> assertEquals(expected.get("minutesStr"), actual.getString("data.minutesStr")));
    }

    @Test
    @DisplayName("9. Adding a template")
    void addingTemplate() {
        JsonPath actual = apiRequest(getTemplateInfo(), getToken(), "user-hr-template").jsonPath();
        Document expected = dataBase.getDocument("userhrtemplates", "_id", actual.getInt("data._id"));
        assertAll(() -> assertEquals(expected.get("_id"), actual.getInt("data._id")),
                () -> assertEquals(expected.get("name"), actual.getString("data.name")),
                () -> assertEquals(expected.get("desc"), actual.getString("data.desc")));
    }

    @Test
    @DisplayName("10. Authorization with an invalid username or password")
    void authorizationWithInvalidUsernameAndPassword() {
        ResponseSpecification responseError = new ResponseSpecBuilder()
                .expectStatusCode(400)
                .expectBody("error", is("Неверный логин / пароль"))
                .log(LogDetail.BODY)
                .build();
        badAuthorization(new AuthInfo(faker.internet().username(), faker.internet().password()), responseError);
        badAuthorization(new AuthInfo("", ""), responseError);
    }

    @Test
    @DisplayName("11 Добавление уже существующего пользователя")
    void addingExistingUser() {
        AddingInfo addingInfo = new AddingInfo(new DataHelper.CustomData(faker.bool().bool(),
                LocalDateTime.now().toString().formatted(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss Z")),
                "active_search"),
                faker.name().firstName(), faker.name().lastName(), faker.internet().emailAddress(),
                faker.internet().username(), faker.internet().password(),
                "user");
        apiRequest(addingInfo, getToken(), "user-auth1").jsonPath();
        APIHelper.addingExistingUser(addingInfo, getToken(), new ResponseSpecBuilder()
                .expectStatusCode(400)
                .log(LogDetail.BODY)
                .build());
    }

    @Test
    @DisplayName("12. Обновление информации о модуле")
    void updatingModuleInformation() {
        JsonPath firstInfo = apiRequest(getModuleInfo(), getToken(), "course-module").jsonPath();
        JsonPath secondInfo = APIHelper.updateInfo(getUpdateModuleInfo(firstInfo.getInt("data._id")), getToken(),
                "course-module").jsonPath();
        assertAll(() -> assertEquals(firstInfo.getInt("data._id"), secondInfo.getInt("data._id")),
                () -> assertNotEquals(firstInfo.getString("data.name"),
                        secondInfo.getString("data.name")));
    }

    @Test
    @DisplayName("13. Изменение названия квиза")
    void changingQuizName() {
        JsonPath firstInfo = apiRequest(getQuizInfo(), getToken(), "quiz").jsonPath();
        JsonPath secondInfo = APIHelper.updateInfo(getUpdateQuizInfo(firstInfo.getInt("data._id")), getToken(),
                "quiz").jsonPath();
        assertAll(() -> assertEquals(firstInfo.getInt("data._id"), secondInfo.getInt("data._id")),
                () -> assertNotEquals(firstInfo.getString("data.name"),
                        secondInfo.getString("data.name")));
    }

    @Test
    @DisplayName("14. Редактирование наименования курса")
    void updateModule() {
        JsonPath firstInfo = apiRequest(getCourseInfo(), getToken(), "course").jsonPath();
        JsonPath secondInfo = APIHelper.updateInfo(getUpdateCourseInfo(firstInfo.getInt("data._id")), getToken(),
                "course").jsonPath();
        assertAll(() -> assertEquals(firstInfo.getInt("data._id"), secondInfo.getInt("data._id")),
                () -> assertNotEquals(firstInfo.getString("data.name"),
                        secondInfo.getString("data.name")));
    }

    @Test
    @DisplayName("15. Обновление информации об экзамене")
    void updateExamInfo() {
        JsonPath firstInfo = apiRequest(getExamInfo(), getToken(), "exam").jsonPath();
        JsonPath secondInfo = APIHelper.updateInfo(getUpdateExamInfo(firstInfo.getInt("data._id")), getToken(),
                "exam").jsonPath();
        assertAll(() -> assertEquals(firstInfo.getInt("data._id"), secondInfo.getInt("data._id")),
                () -> assertNotEquals(firstInfo.getString("data.name"),
                        secondInfo.getString("data.name")));
    }
}
