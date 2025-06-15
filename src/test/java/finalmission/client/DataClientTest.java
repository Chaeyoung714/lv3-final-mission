package finalmission.client;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import finalmission.client.dto.HolidaysResponse;
import finalmission.config.RestClientConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestClient;

//@ContextConfiguration(classes = {RestClientConfiguration.class, DataClient.class, ObjectMapper.class})
//@Import(value = {DataClient.class, ObjectMapper.class})
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class DataClientTest {

    @Autowired
    private RestClient restClient;

    @Autowired
    private DataClient dataClient;


    @DisplayName("공휴일을 조회한다.")
    @Test
    void getHolidayDataTest() {
        HolidaysResponse holidayData = dataClient.getHolidayData(2025, 5);
        System.out.println(holidayData.numOfRows());
    }
}