package cinep.app.cinep.service.parsers;


import cinep.app.cinep.model.Movie;
import cinep.app.cinep.config.ApiConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdditionalDataParser {

    private final ApiConfig config;

    @Autowired
    public AdditionalDataParser(ApiConfig config) {
        this.config = config;
    }

    public List<Movie> getData(List<Movie> movies) {
        return null;
    }

    private List<Movie> getEnglishData(List<Movie> movies) {
        return null;
    }


}
