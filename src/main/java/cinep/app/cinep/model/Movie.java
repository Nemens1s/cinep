package cinep.app.cinep.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "movies")
@Setter
@Getter
@NoArgsConstructor
public class Movie {

    @Id
    private Long id;
    private String originalTitle;
    private String estonianTitle;
    private String russianTitle;
    private String englishTitle;
    private String theatre;
    private String theatreAuditorium;
    private Integer durationInMinutes;
    private LocalTime startTime;
    private LocalDate startDate;
    private String userRating;
    private String showUrl;
    private String productionYear = "";

    @OneToMany(mappedBy = "movie")
    private List<Genre> genres = new ArrayList<>();
}
