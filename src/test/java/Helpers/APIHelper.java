package Helpers;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import Helpers.DataHelper.*;

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

    public static LoginUser authorization(AuthInfo authInfo) {
        LoginUser loginUser = given()
                .spec(requestSpec)
                .body(authInfo)
                .when()
                .post("auth/login")
                .then().log().body()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("LoginUser.json"))
                .extract().body().as(LoginUser.class);
        token = loginUser.token;
        return loginUser;
    }

    public static String getToken() {
        if (token == null) {
            token = authorization(new AuthInfo("somov_oleg", "DY;nwmkgzpnNx9n")).token;
        }
        return token;
    }

    public static User addingUser(AddingInfo addingInfo, String token) {
        AddingUser addingUser = given()
                .spec(requestSpec)
                .header("Authorization", token)
                .body(addingInfo)
                .when()
                .post("user-auth1")
                .then().log().body()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("AddingUser.json"))
                .extract().body().as(AddingUser.class);
        return addingUser.data;
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

  //  public static Question editingQuestion(EditingQuestionInfo editQuestion, String token) {

  //  }
}
