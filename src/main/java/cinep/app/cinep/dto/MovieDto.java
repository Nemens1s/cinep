package cinep.app.cinep.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
public class MovieDto {
    private Long id;
    private String originalTitle;
    private String theatre;
    private String theatreAuditorium;
    private Integer durationInMinutes;
    private LocalDate startDate;
    private LocalTime startTime;
    private String userRating;
    private String showUrl;
}
