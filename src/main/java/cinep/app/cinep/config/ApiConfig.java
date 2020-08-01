package cinep.app.cinep.config;

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
    private String rusForum;
    private String engForum;
    private String estForum;
    private String rusApollo;
    private String engApollo;
    private String estApollo;
    private String t1En;
    private String t1Ru;
}
