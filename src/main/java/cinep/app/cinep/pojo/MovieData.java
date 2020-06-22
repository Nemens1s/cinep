package cinep.app.cinep.pojo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovieData {

    private String originalTitle;
    private String estTitle;
    private String engTitle;
    private String rusTitle;

    private String estGenres;
    private String engGenres;
    private String rusGenres;
}
