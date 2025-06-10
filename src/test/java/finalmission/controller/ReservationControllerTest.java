package finalmission.controller;

import finalmission.auth.JwtTokenProvider;
import finalmission.dto.ReservationCreateRequest;
import finalmission.entity.Member;
import finalmission.service.MemberService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
class ReservationControllerTest {

    String loginToken;

    @Autowired
    MemberService memberService;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        Member member = memberService.findMemberById(1L);
        loginToken = jwtTokenProvider.createToken(member);
    }

    @DisplayName("모든 사용자는 예약 현황을 확인할 수 있다.")
    @Test
    void readAllReservationsTest() {
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("사용자는 뮤지컬 티켓 한 매를 예약할 수 있다.")
    @Test
    void createReservationTest() {
        ReservationCreateRequest reservationCreateRequest = new ReservationCreateRequest(
                LocalDate.of(2025, 6, 30),
                LocalTime.of(14, 30),
                1L,
                1L
        );

        RestAssured.given().log().all()
                .cookie("token", loginToken)
                .contentType(ContentType.JSON)
                .body(reservationCreateRequest)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);
    }
}