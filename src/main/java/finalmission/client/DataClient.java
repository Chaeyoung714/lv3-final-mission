package finalmission.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import finalmission.client.dto.HolidaysResponse;
import java.net.URI;
import java.util.Map;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriBuilder;

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
        Map<String, String> queryParams = Map.of(
                "serviceKey", SERVICE_KEY,
                "solYear", year + "",
                "solMonth", monthMessage,
                "_type", "json"
        );

        Function<UriBuilder, URI> uriBuilderURIFunction = uriBuilder ->
                uriBuilder.path(DATA_API_URI + "/getRestDeInfo")
                    .queryParam("serviceKey", SERVICE_KEY)
                    .queryParam("solYear", year + "")
                    .queryParam("solMonth", monthMessage)
                    .queryParam("_type", "json")
                    .build();
        return restClient.get()
                .uri(uriBuilderURIFunction)
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
