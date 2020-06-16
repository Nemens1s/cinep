package cinep.app.cinep.model;

import lombok.Getter;

@Getter
public enum Cinema {


    FORUM("https://www.forumcinemas.ee/xml/Schedule/?area=1008&dt=",
            "https://www.forumcinemas.ee/xml/ScheduleDates/");
//    APOLLO("https://www.apollokino.ee/xml/Schedule/?dt=",
//            "https://www.apollokino.ee/xml/ScheduleDates/"),
//    T1("https://api.cinamonkino.com/xml/Schedule/?id=9989&lang=en-US", ""),
//    KOSMOS("https://api.cinamonkino.com/xml/Schedule/?id=9999&lang=en-US", ""),
//    VIIMSI("http://www.viimsikino.ee/xml/Schedule/?dt=","http://www.viimsikino.ee/xml/ScheduleDates/"),
//    ARTIS("https://www.kino.ee/xml/Schedule/?dt=","https://www.kino.ee/xml/ScheduleDates/");

    private final String scheduleUrl;
    private final String datesUrl;


    Cinema(String scheduleUrl, String dateUrl) {
        this.scheduleUrl = scheduleUrl;
        this.datesUrl = dateUrl;
    }


}
