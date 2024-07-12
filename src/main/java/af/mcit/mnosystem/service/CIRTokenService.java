package af.mcit.mnosystem.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class CIRTokenService {

    private final Logger log = LoggerFactory.getLogger(CIRTokenService.class);

    private String token;
    private ZonedDateTime validationDate;

    public CIRTokenService() {
        this.token = "";
        this.validationDate = ZonedDateTime.now();
    }

    public String getToken() {
        if (token.isBlank() || ZonedDateTime.now().isAfter(validationDate)) authenticateToCIRServer();

        return token;
    }

    private void authenticateToCIRServer() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String apiUrl = "http://localhost:8080/api/authenticate";

            Map<String, String> params = new HashMap<String, String>();
            params.put("username", "MTNA");
            params.put("password", "1234");

            String json = restTemplate.postForObject(apiUrl, params, String.class);

            JsonNode node = new ObjectMapper().readTree(json);
            this.token = node.get("id_token").textValue();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("{}", e.getMessage());
        }
    }
}
