package cinep.app.cinep.service.parsers;

import cinep.app.cinep.model.Movie;
import org.springframework.stereotype.Service;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class XmlReaderService {

    public List<Movie> parseAllXml(String url) {
        List<Movie> movies = new ArrayList<>();
        Movie movie = null;
        XMLInputFactory factory = XMLInputFactory.newInstance();

        try {
            XMLEventReader xmlEventReader = factory.createXMLEventReader(new URL(url).openStream());
            while (xmlEventReader.hasNext()) {
                movie = getMovie(movies, movie, xmlEventReader);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return movies;
    }


    private Movie getMovie(List<Movie> movies, Movie movie, XMLEventReader xmlEventReader) throws XMLStreamException {
        XMLEvent xmlEvent = xmlEventReader.nextEvent();
        if (xmlEvent.isStartElement()) {
            StartElement startElement = xmlEvent.asStartElement();
            if (startElement.getName().getLocalPart().equalsIgnoreCase("show")) {
                movie = new Movie();

            } else if (startElement.getName().getLocalPart().equalsIgnoreCase("id")) {
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
                movies.add(movie);
            }
        }
        return movie;
    }
}
