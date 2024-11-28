package Helpers;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.response.Response;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import Helpers.DataHelper.*;
import io.restassured.specification.ResponseSpecification;

import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class APIHelper {

    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("https://aqa-api.javacode.ru/api")
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();
    private static final ResponseSpecification responseSpec = new ResponseSpecBuilder()
            .expectStatusCode(200)
            .log(LogDetail.BODY)
            .build();
    private static String token;
    private static final AuthInfo authInfo = new AuthInfo("somov_oleg", "DY;nwmkgzpnNx9n");

    private APIHelper() {
    }

    public static User authorization() {
        LoginUser loginUser = given()
                .spec(requestSpec)
                .body(authInfo)
                .when()
                .post("auth/login")
                .then().spec(responseSpec)
                .body(matchesJsonSchemaInClasspath("LoginUser.json"))
                .body("user.username", is(authInfo.username))
                .extract().body().as(LoginUser.class);
        token = loginUser.token;
        return loginUser.user;
    }

    public static String getToken() {
        if (token == null) authorization();
        return token;
    }

    public static Response apiRequest(Info body, String token, String path) {
        return given()
                .spec(requestSpec)
                .header("Authorization", token)
                .body(body)
                .when()
                .post(path)
                .then().spec(responseSpec)
                .body(matchesJsonSchemaInClasspath(path + ".json"))
                .extract().response();
    }

    public static void badAuthorization(AuthInfo authInfo, ResponseSpecification responseSpec) {
        given()
                .spec(requestSpec)
                .body(authInfo)
                .when()
                .post("auth/login")
                .then().spec(responseSpec).extract().statusCode();
    }
}
