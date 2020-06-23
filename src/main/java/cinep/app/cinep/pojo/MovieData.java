package cinep.app.cinep.pojo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovieData {

    private String originalTitle;
    private String estTitle = "Pealkiri pole saadaval";
    private String engTitle = "Title is not available";
    private String rusTitle = "Название недоступно";

    private String estGenres = "Not available";
    private String engGenres = "Not available";
    private String rusGenres = "Not available";
}
