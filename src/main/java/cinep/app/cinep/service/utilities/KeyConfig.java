package cinep.app.cinep.service.utilities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties (prefix = "app.rating")
public class KeyConfig {
    private String url;
}
