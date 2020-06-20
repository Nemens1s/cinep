package cinep.app.cinep.service;

import cinep.app.cinep.model.Movie;
import cinep.app.cinep.model.Rating;
import cinep.app.cinep.repository.RatingsRepository;
import cinep.app.cinep.service.utilities.ApiConfig;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.Setter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Setter
public class RatingService {

    private ApiConfig config;
    private final RatingsRepository ratingsRepository;

    @Autowired
    public RatingService(RatingsRepository ratingsRepository, ApiConfig config) {
        this.ratingsRepository = ratingsRepository;
        this.config = config;
    }

    private String findMovieRating(Movie movie) throws UnirestException {
        String movieTitle = movie.getOriginalTitle();
        String productionYear = movie.getProductionYear();
        String url = config.getUrl();
        if (movieTitle.matches("\\A\\p{ASCII}*\\z")) {
            Unirest.setTimeouts(10000, 20000);
            HttpResponse<String> response = Unirest.get(url + "t=" + movieTitle.replaceAll("\\s", "%20")
                    + "&y=" + productionYear)
                    .asString();
            try {
                JSONArray jsonArray = new JSONObject(response.getBody()).getJSONArray("Ratings");
                StringBuilder ratings = new StringBuilder();
                for (int i = 0; i < jsonArray.length() - 1; i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String source = jsonObject.getString("Source");
                    String rating = jsonObject.getString("Value");
                    ratings.append(source).append(":").append(rating).append("\n");
                }
                return ratings.toString();
            } catch (JSONException e) {
                return "Currently not available";
            }
        } else {
            return "Currently not available";
        }
    }

    public void updateRatings(List<Movie> movies) {
        ratingsRepository.deleteAll();
        List<Rating> ratings = new ArrayList<>();
        Set<String> knownRatings = new HashSet<>();
        for (Movie movie : movies) {
            if (!knownRatings.contains(movie.getOriginalTitle())
                    && movie.getTheatre().equalsIgnoreCase("Coca Cola Plaza")) {
                try {
                    String rating = findMovieRating(movie);
                    if (rating.equalsIgnoreCase("")) {
                        rating = "Currently not available";
                    }
                    ratings.add(new Rating(movie.getOriginalTitle(), rating));
                    knownRatings.add(movie.getOriginalTitle());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        ratingsRepository.saveAll(ratings);
    }


}
