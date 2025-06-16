package finalmission.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import finalmission.client.dto.HolidaysResponse;
import java.net.URI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class DataClient {

    private static final String DATA_API_URI = "https://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService";

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    @Value("${data.secret}}")
    private String SERVICE_KEY;

    public DataClient(ObjectMapper objectMapper, RestClient restClient) {
        this.objectMapper = objectMapper;
        this.restClient = restClient.mutate()
                .build();
    }

    public HolidaysResponse getHolidayData(int year, int month) {
        String monthMessage = buildMonthMessage(month);

        StringBuilder uri = new StringBuilder(DATA_API_URI + "/getRestDeInfo");
        uri.append("?serviceKey=");
        uri.append(SERVICE_KEY);
        uri.deleteCharAt(uri.lastIndexOf("}"));
        uri.append("&solYear=");
        uri.append(year + "");
        uri.append("&solMonth=");
        uri.append(monthMessage);
        uri.append("&_type=json");

        return restClient.get()
                .uri(URI.create(uri.toString()))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(HolidaysResponse.class)
                .getBody();
    }

    private String buildMonthMessage(int month) {
        if (month < 10) {
            return "0" + month;
        }
        return "" + month;
    }
}
