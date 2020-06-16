package cinep.app.cinep.service.parsers;

import org.springframework.stereotype.Service;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ScheduleDatesParser {

    public List<String> getDates (String url) {
        if (url.equals("")) {
            return new ArrayList<>(Collections.singletonList(""));
        }
        List<String> dates = new ArrayList<>();
        XMLInputFactory factory = XMLInputFactory.newInstance();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        try {
            XMLEventReader xmlEventReader = factory.createXMLEventReader(new URL(url).openStream());
            while (xmlEventReader.hasNext()) {
                XMLEvent xmlEvent = xmlEventReader.nextEvent();
                if (xmlEvent.isStartElement()) {
                    StartElement startElement = xmlEvent.asStartElement();
                    if (startElement.getName().getLocalPart().equalsIgnoreCase("dateTime")) {
                        xmlEvent = xmlEventReader.nextEvent();
                        String dateString = xmlEvent.asCharacters().toString();
                        LocalDate date = LocalDate.parse(dateString, formatter);
                        dates.add(date.toString());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dates;
    }

}
