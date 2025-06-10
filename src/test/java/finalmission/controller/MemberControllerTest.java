package finalmission.controller;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

import finalmission.dto.LoginRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
class MemberControllerTest {

    @DisplayName("사용자는 로그인할 수 있다.")
    @Test
    void loginTest() {
        LoginRequest loginRequest = new LoginRequest("moda@woowa.com", "password");
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .when().post("/login")
                .then().log().all()
                .statusCode(200);
    }
}