package cinep.app.cinep.service;

import cinep.app.cinep.model.Movie;
import cinep.app.cinep.model.Rating;
import cinep.app.cinep.repository.MovieRepository;
import cinep.app.cinep.repository.RatingsRepository;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.*;

@Service
public class RatingService {

    private final String URL = "http://www.omdbapi.com/?apikey=99b14a7f&";
    private final RatingsRepository ratingsRepository;

    @Autowired
    public RatingService(RatingsRepository ratingsRepository) {
        this.ratingsRepository = ratingsRepository;
    }

    private String findMovieRating(String movieTitle) throws UnirestException{
        if(movieTitle.matches("\\A\\p{ASCII}*\\z")){
            Unirest.setTimeouts(10000, 20000);
            HttpResponse<String> response = Unirest.get(URL + "t=" + movieTitle.replaceAll("\\s", "%20") +
                    "&y=" + Year.now().getValue())
                    .asString();
            try {
                JSONArray jsonArray = new JSONObject(response.getBody()).getJSONArray("Ratings");
                StringBuilder rating = new StringBuilder();
                for (int i = 0; i < jsonArray.length() - 1; i++) {
                    rating.append(jsonArray.get(i));
                    rating.append(" ");
                }
                String correctRating = rating.toString().replaceAll("\\\\", " ").replaceAll("Value", "Rating");
                correctRating = correctRating.replaceAll("\\{", "");
                correctRating = correctRating.replaceAll("}", "");
                correctRating = correctRating.replaceAll("\"", "");
                return correctRating;
            } catch (JSONException e){
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
            if (!knownRatings.contains(movie.getOriginalTitle())){
                try {
                    String rating = findMovieRating(movie.getOriginalTitle());
                    if(rating.equalsIgnoreCase("")){
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
