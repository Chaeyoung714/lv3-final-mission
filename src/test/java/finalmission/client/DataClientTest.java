package finalmission.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import finalmission.client.dto.HolidaysResponse;
import finalmission.dto.LoginMemberInfo;
import finalmission.dto.ReservationFullRequest;
import finalmission.dto.ReservationSimpleResponse;
import finalmission.service.ReservationService;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.web.client.RestClient;

//@ContextConfiguration(classes = {RestClientConfiguration.class, DataClient.class, ObjectMapper.class})
//@Import(value = {DataClient.class, ObjectMapper.class})
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@Sql(scripts = "/data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
class DataClientTest {

    @Autowired
    private RestClient restClient;

    @Autowired
    private DataClient dataClient;

    @Autowired
    private ReservationService reservationService;

    @DisplayName("공휴일을 조회한다.")
    @Test
    void getHolidayDataTest() {
        HolidaysResponse holidayData = dataClient.getHolidayData(2025, 5);
        System.out.println(holidayData);
    }

    @DisplayName("공휴일을 조회하면서 예약을 생성한다.")
    @Test
    void getHolidayDataIntegrationTest() {
        LoginMemberInfo moda = new LoginMemberInfo(1L, "moda");
        LocalDate date = LocalDate.of(2025, 6, 10);
        ReservationFullRequest reservationRequest = new ReservationFullRequest(
                date,
                LocalTime.of(14, 30),
                1L,
                1L
        );
        ReservationSimpleResponse reservation = reservationService.createReservation(reservationRequest, moda);
        assertThat(reservation.date()).isEqualTo(date);
    }

    @DisplayName("공휴일에 예약하는 경우 예약 생성이 불가능하다.")
    @Test
    void getHolidayDataIntegrationFailTest() {
        LoginMemberInfo moda = new LoginMemberInfo(1L, "moda");
        LocalDate date = LocalDate.of(2025, 6, 6);
        ReservationFullRequest reservationRequest = new ReservationFullRequest(
                date,
                LocalTime.of(14, 30),
                1L,
                1L
        );
        assertThatThrownBy(() -> reservationService.createReservation(reservationRequest, moda))
                .isInstanceOf(IllegalArgumentException.class);
    }
}