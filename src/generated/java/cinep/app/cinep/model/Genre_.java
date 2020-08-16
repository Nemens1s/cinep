package cinep.app.cinep.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Genre.class)
public abstract class Genre_ {

	public static volatile SingularAttribute<Genre, Movie> movie;
	public static volatile SingularAttribute<Genre, String> description;
	public static volatile SingularAttribute<Genre, Integer> id;

	public static final String MOVIE = "movie";
	public static final String DESCRIPTION = "description";
	public static final String ID = "id";

}

