package cinep.app.cinep.specifications;

import cinep.app.cinep.model.Genre;
import cinep.app.cinep.model.Movie;
import cinep.app.cinep.model.Movie_;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Setter
public class MovieSpecification {


    public static Specification<Movie> createSpecification(Map<String, String> request) {
        List<Specification<Movie>> specifications = getSpecificationList(request);
        Specification<Movie> specification = null;
        for (Specification<Movie> s : specifications) {
            if (specification == null) {
                specification = Specification.where(s);
            } else {
                specification = specification.and(s);
            }
        }
        return specification;
    }

    private static List<Specification<Movie>> getSpecificationList(Map<String, String> request) {
        List<Specification<Movie>> specifications = new ArrayList<>();
        for (String key : request.keySet()) {
            if (key.equalsIgnoreCase("title")) {
                Specification<Movie> titleSpecification = Specification.where(withTitle(new SearchCriteria("originalTitle", request.get(key)))
                        .or(withTitle(new SearchCriteria("estonianTitle", request.get(key)))
                                .or(withTitle(new SearchCriteria("englishTitle", request.get(key)))
                                        .or(withTitle(new SearchCriteria("russianTitle", request.get(key)))))));
                specifications.add(titleSpecification);

            } else if (key.equalsIgnoreCase("genre")) {
                String[] genres = request.get(key).split(",");
                Specification<Movie> genreSpecification = withGenre(new SearchCriteria("description", genres[0]));
                for (int i = 1; i < genres.length; i++) {
                    genreSpecification = Objects.requireNonNull(genreSpecification).or(withGenre(new SearchCriteria("description", genres[i])));
                }
                specifications.add(genreSpecification);
            } else if (key.equalsIgnoreCase("theatre")) {
                String[] theatres = request.get(key).split(",");
                Specification<Movie> theatreSpecification = withTheatre(new SearchCriteria("theatre", theatres[0]));
                for (int i = 1; i < theatres.length; i++) {
                    theatreSpecification = Objects.requireNonNull(theatreSpecification)
                            .or(withTheatre(new SearchCriteria("theatre", theatres[i])));
                }
                specifications.add(theatreSpecification);
            } else if (key.equalsIgnoreCase("st") || key.equalsIgnoreCase("et")) {
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
                LocalTime time = LocalTime.parse(request.get(key), timeFormatter);
                String criteriaKey = key.equalsIgnoreCase("st") ? "start" : "end";
                specifications.add(withTime(new SearchCriteria(criteriaKey, time)));
                if (!request.containsKey("ed") && !request.containsKey("sd")) {
                    specifications.add(withDate(new SearchCriteria("startD", LocalDate.now())));
                    specifications.add(withDate(new SearchCriteria("endD", LocalDate.now())));
                }
            } else if (key.equalsIgnoreCase("sd") || key.equalsIgnoreCase("ed")) {
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                LocalDate date = LocalDate.parse(request.get(key), dateFormatter);
                String criteriaKey = key.equalsIgnoreCase("sd") ? "start" : "end";
                specifications.add(withDate(new SearchCriteria(criteriaKey, date)));
            }

        }
        return specifications;
    }

    private static Specification<Movie> withTitle(SearchCriteria criteria) {
        return (Specification<Movie>) (root, query, builder) -> builder.like(
                builder.lower(root.get(criteria.getKey())),
                "%" + criteria.getValue().toString().toLowerCase() + "%");
    }

    private static Specification<Movie> withTheatre(SearchCriteria criteria) {
        return (Specification<Movie>) (root, query, builder) -> builder.equal(
                root.get(criteria.getKey()), criteria.getValue().toString());
    }

    private static Specification<Movie> withGenre(SearchCriteria criteria) {
        return (Specification<Movie>) (root, query, builder) -> {
            ListJoin<Movie, Genre> join = root.joinList(Movie_.GENRES);
            query.distinct(true);
            return builder.equal(builder.lower(join.get(criteria.getKey())), criteria.getValue().toString().toLowerCase());
        };
    }

    private static Specification<Movie> withDate(SearchCriteria criteria) {
        return (Specification<Movie>) (root, query, builder) -> {
            if (criteria.getKey().startsWith("start")) {
                return builder.greaterThanOrEqualTo(root.get(Movie_.START_DATE), (LocalDate) criteria.getValue());
            } else {
                return builder.lessThanOrEqualTo(root.get(Movie_.START_DATE), (LocalDate) criteria.getValue());
            }
        };
    }

    private static Specification<Movie> withTime(SearchCriteria criteria) {
        return (Specification<Movie>) (root, query, builder) -> {
            if (criteria.getKey().startsWith("start")) {
                return builder.greaterThanOrEqualTo(root.get(Movie_.START_TIME), (LocalTime) criteria.getValue());
            } else {
                return builder.lessThanOrEqualTo(root.get(Movie_.START_TIME), (LocalTime) criteria.getValue());
            }
        };
    }
}
