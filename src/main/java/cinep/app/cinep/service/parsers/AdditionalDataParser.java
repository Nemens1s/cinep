package cinep.app.cinep.service.parsers;

import cinep.app.cinep.config.ApiConfig;
import cinep.app.cinep.pojo.MovieData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdditionalDataParser {

    private final ApiConfig config;

    private final String EST = "EST";
    private final String RUS = "RUS";
    private final String ENG = "ENG";


    @Autowired
    public AdditionalDataParser(ApiConfig config) {
        this.config = config;
    }

    public Map<String, MovieData> getData() {
        Map<String, MovieData> dataMap = new HashMap<>();
        List<MovieData> estData = parseData(config.getEst(), EST);
        updateMap(dataMap, estData, EST);
        List<MovieData> engData = parseData(config.getEng(), ENG);
        updateMap(dataMap, engData, ENG);
        List<MovieData> rusData = parseData(config.getRus(), RUS);
        updateMap(dataMap, rusData, RUS);
        return dataMap;
    }


    private List<MovieData> parseData(String url, String dataType) {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        List<MovieData> data = new ArrayList<>();
        try {
            XMLEventReader xmlEventReader = factory.createXMLEventReader(new URL(url).openStream());
            while (xmlEventReader.hasNext()) {
                MovieData movieData = new MovieData();
                while (xmlEventReader.hasNext()) {
                    XMLEvent xmlEvent = xmlEventReader.nextEvent();
                    if (xmlEvent.isStartElement()) {
                        StartElement startElement = xmlEvent.asStartElement();
                        if (startElement.getName().getLocalPart().equalsIgnoreCase("OriginalTitle")) {
                            xmlEvent = xmlEventReader.nextEvent();
                            String title = xmlEvent.asCharacters().getData();
                            title = title.replaceAll("\\(.*", "").replaceAll("3D", "")
                                    .replaceAll("2D", "");
                            movieData.setOriginalTitle(title.trim());
                        } else if (startElement.getName().getLocalPart().equalsIgnoreCase("title")) {
                            xmlEvent = xmlEventReader.nextEvent();
                            String title = xmlEvent.asCharacters().getData();
                            title = title.replaceAll("\\(.*", "").replaceAll("3D", "")
                                    .replaceAll("2D", "");
                            if (dataType.equalsIgnoreCase(EST)) {
                                movieData.setEstTitle(title);
                            } else if (dataType.equalsIgnoreCase(ENG)) {
                                movieData.setEngTitle(title);
                            } else {
                                movieData.setRusTitle(title);
                            }
                        } else if (startElement.getName().getLocalPart().equalsIgnoreCase("Genres")) {
                            xmlEvent = xmlEventReader.nextEvent();
                            String genres = xmlEvent.asCharacters().getData();
                            if (dataType.equalsIgnoreCase(EST)) {
                                movieData.setEstGenres(genres);
                            } else if (dataType.equalsIgnoreCase(ENG)) {
                                movieData.setEngGenres(genres);
                            } else {
                                movieData.setRusGenres(genres);
                            }
                        }
                    }
                    if (xmlEvent.isEndElement()) {
                        EndElement endElement = xmlEvent.asEndElement();
                        if (endElement.getName().getLocalPart().equalsIgnoreCase("show")) {
                            data.add(movieData);
                            break;
                        }
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    private void updateMap(Map<String, MovieData> dataMap, List<MovieData> data, String dataType) {
        for (MovieData movie : data) {
            if (dataMap.containsKey(movie.getOriginalTitle())) {
                MovieData movieData = dataMap.get(movie.getOriginalTitle());
                if (dataType.equalsIgnoreCase(ENG)) {
                    movieData.setEngTitle(movie.getEngTitle());
                    movieData.setEngGenres(movie.getEngGenres());
                } else if (dataType.equalsIgnoreCase(RUS)) {
                    movieData.setRusTitle(movie.getRusTitle());
                    movieData.setRusGenres(movie.getRusGenres());
                }
                dataMap.replace(movieData.getOriginalTitle(), movieData);
            } else {
                dataMap.put(movie.getOriginalTitle(), movie);
            }
        }
    }


}
