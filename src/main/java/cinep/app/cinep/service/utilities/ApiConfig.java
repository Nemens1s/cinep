package cinep.app.cinep.service.utilities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties (prefix = "app.api")
public class ApiConfig {
    private String url;
    private String rus;
    private String eng;
}
