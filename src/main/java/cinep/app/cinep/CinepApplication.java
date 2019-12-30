package cinep.app.cinep;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CinepApplication {

    public static void main(String[] args) {
        SpringApplication.run(CinepApplication.class, args);
    }
}
