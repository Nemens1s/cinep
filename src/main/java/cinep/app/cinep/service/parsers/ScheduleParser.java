package cinep.app.cinep.service.parsers;

import cinep.app.cinep.model.Cinema;
import cinep.app.cinep.model.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ScheduleParser {

    private final ScheduleDatesParser datesParser;


    @Autowired
    public ScheduleParser(ScheduleDatesParser datesParser) {
        this.datesParser = datesParser;
    }

    public List<Movie> getMoviesFromAPI() {
        List<Movie> listOfMovies = new ArrayList<>();
        List<Cinema> cinemas = new ArrayList<>(Arrays.asList(Cinema.values()));
        cinemas.forEach(cinema -> listOfMovies.addAll(parseSchedule(cinema)));
        listOfMovies.sort(Comparator.comparing(Movie::getStartTime));
        return listOfMovies;
    }


    private List<Movie> parseSchedule(Cinema cinema) {
        List<Movie> movies = new ArrayList<>();
        List<String> dates = datesParser.getDates(cinema.getDatesUrl());
        dates.forEach(date -> movies.addAll(getSchedule(cinema.getScheduleUrl() + date)));
        return movies;
    }

    private List<Movie> getSchedule(String url) {
        List<Movie> movies = new ArrayList<>();
        XMLInputFactory factory = XMLInputFactory.newInstance();
        try {
            XMLEventReader xmlEventReader = factory.createXMLEventReader(new URL(url).openStream());
            while (xmlEventReader.hasNext()) {
                Movie movie = getMovie(xmlEventReader);
                if (movie.getOriginalTitle() != null) {
                    movies.add(movie);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return movies;
    }

    private Movie getMovie(XMLEventReader xmlEventReader) {
        Movie movie = new Movie();
        while (xmlEventReader.hasNext()) {
            try {
                XMLEvent xmlEvent = xmlEventReader.nextEvent();
                if (xmlEvent.isStartElement()) {
                    StartElement startElement = xmlEvent.asStartElement();
                    if (startElement.getName().getLocalPart().equalsIgnoreCase("id")) {
                        xmlEvent = xmlEventReader.nextEvent();
                        movie.setId(Long.parseLong(xmlEvent.asCharacters().getData()));
                    } else if (startElement.getName().getLocalPart().equalsIgnoreCase("OriginalTitle")) {
                        xmlEvent = xmlEventReader.nextEvent();
                        String title = xmlEvent.asCharacters().getData();
                        title = title.replaceAll("\\(.*", "");
                        title = title.replaceAll("3D", "");
                        title = title.replaceAll("2D", "");
                        movie.setOriginalTitle(title.trim());
                    } else if (startElement.getName().getLocalPart().equalsIgnoreCase("theatre")) {
                        xmlEvent = xmlEventReader.nextEvent();
                        movie.setTheatre(xmlEvent.asCharacters().getData());
                    } else if (startElement.getName().getLocalPart().equalsIgnoreCase("TheatreAuditorium")) {
                        xmlEvent = xmlEventReader.nextEvent();
                        movie.setTheatreAuditorium(xmlEvent.asCharacters().getData());
                    } else if (startElement.getName().getLocalPart().equalsIgnoreCase("LengthInMinutes")) {
                        xmlEvent = xmlEventReader.nextEvent();
                        movie.setDurationInMinutes(Integer.parseInt(xmlEvent.asCharacters().getData()));
                    } else if (startElement.getName().getLocalPart().equalsIgnoreCase("ShowURL")) {
                        xmlEvent = xmlEventReader.nextEvent();
                        movie.setShowUrl(xmlEvent.asCharacters().getData());
                    } else if (startElement.getName().getLocalPart().equalsIgnoreCase("dttmShowStart")) {
                        xmlEvent = xmlEventReader.nextEvent();

                        LocalDateTime d = LocalDateTime.parse(xmlEvent.asCharacters().getData());
                        movie.setStartDate(d.toLocalDate());
                        movie.setStartTime(d.toLocalTime());
                    } else if (startElement.getName().getLocalPart().equalsIgnoreCase("ProductionYear")) {
                        xmlEvent = xmlEventReader.nextEvent();
                        movie.setProductionYear(xmlEvent.asCharacters().getData());
                    } else if (startElement.getName().getLocalPart().equalsIgnoreCase("title")) {
                        xmlEvent = xmlEventReader.nextEvent();
                        movie.setEstonianTitle(xmlEvent.asCharacters().getData());
                    }

                }
                if (xmlEvent.isEndElement()) {
                    EndElement endElement = xmlEvent.asEndElement();
                    if (endElement.getName().getLocalPart().equalsIgnoreCase("show")) {
                        if (movie.getShowUrl() == null) {
                            if (movie.getTheatre().equalsIgnoreCase("T1")) {
                                movie.setShowUrl("https://cinamonkino.com/t1/en");
                            } else {
                                movie.setShowUrl("https://cinamonkino.com/kosmos/en");
                            }
                        }
                        break;
                    }
                }
            } catch (XMLStreamException e) {
                e.printStackTrace();
            }
        }
        return movie;
    }
}
