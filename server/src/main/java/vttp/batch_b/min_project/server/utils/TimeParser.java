package vttp.batch_b.min_project.server.utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TimeParser {
    
    //PT13H20M
    public static String parseDuration(String duration) {
        Duration d = Duration.parse(duration);
    
        long hours = d.toHours();
        long minutes = d.toMinutesPart();
    
        return hours + " h" + minutes + " min";
    }

    //2025-03-23T14:15:00
    public static String parseDateTime(String dateTime, String timezone) {
        LocalDateTime localDateTime = LocalDateTime.parse(dateTime);
    
        ZonedDateTime zonedDateTime = localDateTime
            .atZone(ZoneId.of("UTC"))
            .withZoneSameInstant(ZoneId.of(timezone));
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy\nh:mm a");
        
        return zonedDateTime.format(formatter);
    }
}
