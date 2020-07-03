package cinep.app.cinep.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private String estonianTitle;
    private String russianTitle;
    private String englishTitle;
    private String theatre;
    private String theatreAuditorium;
    private Integer durationInMinutes;
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate startDate;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;
    private String userRating;
    private String showUrl;
    private String productionYear;
}
