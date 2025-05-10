package api;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class Specifications {
    private static final String URL = "https://stellarburgers.nomoreparties.site";

    public static RequestSpecification requestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(URL)
                .setContentType(ContentType.JSON)
                .build();
    }

    public static ResponseSpecification responseSpec(int statusCode) {
        return new ResponseSpecBuilder()
                .expectStatusCode(statusCode)
                .build();
    }

    public static ResponseSpecification responseSpec200Ok(){
        return responseSpec(200);
    }


    public static ResponseSpecification responseSpec202Accepted(){
        return responseSpec(202);
    }

    public static ResponseSpecification responseSpec400BadReq(){
        return responseSpec(400);
    }

    public static ResponseSpecification responseSpec401Unauthorized() {
        return responseSpec(401);
    }

    public static ResponseSpecification responseSpec403Forbidden() {
        return responseSpec(403);
    }


    public static ResponseSpecification responseSpec500InternalServerError(){
        return responseSpec(500);
    }

    public static void installSpecification(RequestSpecification request, ResponseSpecification response){
        RestAssured.requestSpecification = request;
        RestAssured.responseSpecification = response;
    }
}