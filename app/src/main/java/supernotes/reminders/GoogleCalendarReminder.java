package supernotes.reminders;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

import java.io.IOException;
import java.security.GeneralSecurityException;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;


public class GoogleCalendarReminder {
    private static final Logger LOGGER = LoggerFactory.getLogger(GoogleCalendarReminder.class);
    public static void addEventToCalendar(String noteContent, String startDateTime) {
        try {
            Calendar service = GoogleCalendarService.getCalendarService();
            Event event = new Event()
                    .setSummary("Reminder: " + noteContent)
                    .setDescription("Note content: " + noteContent);

            DateTime start = new DateTime(startDateTime);

            // Convertir la DateTime en LocalDateTime
            Instant instant = Instant.ofEpochMilli(start.getValue());
            LocalDateTime startDate = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

            // Ajouter une heure à la date de début pour obtenir la date de fin
            LocalDateTime endDate = startDate.plusHours(1); // Ajouter une heure

            // Convertir la date de fin en DateTime
            DateTime end = new DateTime(endDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());

            EventDateTime startEventDateTime = new EventDateTime().setDateTime(start);
            EventDateTime endEventDateTime = new EventDateTime().setDateTime(end);

            event.setStart(startEventDateTime);
            event.setEnd(endEventDateTime);

            Event createdEvent = service.events().insert("primary", event).execute();
            LOGGER.info("Event created: {} ",createdEvent.getHtmlLink());
        } catch (IOException | GeneralSecurityException e) {
            LOGGER.error("Une erreur s'est produite.", e);
        }
    }
}
