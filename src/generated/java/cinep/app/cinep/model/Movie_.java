package cinep.app.cinep.model;

import java.time.LocalDate;
import java.time.LocalTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Movie.class)
public abstract class Movie_ {

	public static volatile SingularAttribute<Movie, String> theatreAuditorium;
	public static volatile SingularAttribute<Movie, Integer> durationInMinutes;
	public static volatile SingularAttribute<Movie, String> showUrl;
	public static volatile SingularAttribute<Movie, String> productionYear;
	public static volatile SingularAttribute<Movie, String> userRating;
	public static volatile SingularAttribute<Movie, String> theatre;
	public static volatile SingularAttribute<Movie, String> originalTitle;
	public static volatile ListAttribute<Movie, Genre> genres;
	public static volatile SingularAttribute<Movie, String> estonianTitle;
	public static volatile SingularAttribute<Movie, LocalTime> startTime;
	public static volatile SingularAttribute<Movie, Long> id;
	public static volatile SingularAttribute<Movie, String> russianTitle;
	public static volatile SingularAttribute<Movie, LocalDate> startDate;
	public static volatile SingularAttribute<Movie, String> englishTitle;

	public static final String THEATRE_AUDITORIUM = "theatreAuditorium";
	public static final String DURATION_IN_MINUTES = "durationInMinutes";
	public static final String SHOW_URL = "showUrl";
	public static final String PRODUCTION_YEAR = "productionYear";
	public static final String USER_RATING = "userRating";
	public static final String THEATRE = "theatre";
	public static final String ORIGINAL_TITLE = "originalTitle";
	public static final String GENRES = "genres";
	public static final String ESTONIAN_TITLE = "estonianTitle";
	public static final String START_TIME = "startTime";
	public static final String ID = "id";
	public static final String RUSSIAN_TITLE = "russianTitle";
	public static final String START_DATE = "startDate";
	public static final String ENGLISH_TITLE = "englishTitle";

}

