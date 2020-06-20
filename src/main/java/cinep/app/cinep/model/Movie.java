package cinep.app.cinep.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Entity
@Table(name = "movies")
@Setter
@Getter
@NoArgsConstructor
public class Movie {

    @Id
    private Long id;
    private String originalTitle;
    private String theatre;
    private String theatreAuditorium;
    private Integer durationInMinutes;
    private LocalDate startDate;
    private LocalTime startTime;
    private String userRating;
    private String showUrl;
    private String productionYear = "";
}
