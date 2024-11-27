package Helpers;

import io.restassured.response.Response;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import Helpers.DataHelper.*;
import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class APIHelper {

    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("https://aqa-api.javacode.ru/api")
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();
    private static String token;

    private APIHelper() {
    }

    public static User authorization(AuthInfo authInfo) {
        LoginUser loginUser = given()
                .spec(requestSpec)
                .body(authInfo)
                .when()
                .post("auth/login")
                .then().log().body()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("LoginUser.json"))
                .body("user.username", is(authInfo.username))
                .extract().body().as(LoginUser.class);
        token = loginUser.token;
        return loginUser.user;
    }

    public static String getToken() {
        if (token == null) authorization(new AuthInfo("somov_oleg", "DY;nwmkgzpnNx9n"));
        return token;
    }

    public static Response addingUser(AddingInfo addingInfo, String token) {
        return given()
                .spec(requestSpec)
                .header("Authorization", token)
                .body(addingInfo)
                .when()
                .post("user-auth1")
                .then().log().body()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("AddingUser.json"))
                .extract().response();
    }

    public static Question addingQuestion(QuestionInfo question, String token) {
        AddingQuestion addingQuestion = given()
                .spec(requestSpec)
                .header("Authorization", token)
                .body(question)
                .when()
                .post("theme-question")
                .then().log().body()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("AddingQuestion.json"))
                .extract().body().as(AddingQuestion.class);
        return addingQuestion.data;
    }

    public static VersionDB editingQuestion(EditingQuestionInfo editQuestion, String token) {
        EditingQuestion editingQuestion = given()
                .spec(requestSpec)
                .header("Authorization", token)
                .body(editQuestion)
                .when()
                .post("create-lts")
                .then().log().body()
                .statusCode(200)
                .extract()
                .body().as(EditingQuestion.class);
        return editingQuestion.data.getVersionDB();
    }

    public static Response addingQuiz(QuizInfo quiz, String token) {
        return given()
                .spec(requestSpec)
                .header("Authorization", token)
                .body(quiz)
                .when()
                .post("quiz")
                .then().log().body()
                .statusCode(200)
                .extract().response();
    }

}
