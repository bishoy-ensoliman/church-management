package de.stminakirchemuenchen.stminamassreservation.scheduledtasks;

import de.stminakirchemuenchen.stminamassreservation.entities.Reservation;
import de.stminakirchemuenchen.stminamassreservation.repositories.ReservationRepository;
import de.stminakirchemuenchen.stminamassreservation.services.EmailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.mail.MessagingException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class SendAdminEmail {
    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    private EmailServiceImpl emailService;

    @Scheduled(cron = "0 0 22 * * *", zone = "Europe/Berlin")
    public void sendAbounaNamesEmailBeforeMass() {
        List<Reservation> reservations =
                (List<Reservation>) this.reservationRepository.findReservationsOfLiturgiesBetweenDates(
                        OffsetDateTime.now(), OffsetDateTime.now().plusDays(1)
                );
        if(!CollectionUtils.isEmpty(reservations)) {
            StringBuilder csvFile = new StringBuilder();
            StringBuilder htmlTable = new StringBuilder();
            htmlTable.append("<table style=\"width:100%\">\n" +
                    "  <tr>\n" +
                    "    <th>Name</th>\n" +
                    "    <th>Email</th>\n" +
                    "    <th>Mobile</th>\n" +
                    "    <th>Zahl</th>\n" +
                    "    <th>Ort</th>\n" +
                    "    <th>Registerierung Datum</th>\n" +
                    "    <th>Liturgie</th>\n" +
                    "    <th>Liturgie Datum</th>\n" +
                    "  </tr>\n");
            csvFile.append("Name,Email,Mobile,Zahl,Ort,Registerierung Datum,Liturgie,Liturgie Datum\r\n");
            reservations.stream().forEach(reservation -> {
                csvFile.append(reservation.getFirstName() + " " + reservation.getLastName()
                        + "," + reservation.getEmail()
                        + "," + reservation.getMobile()
                        + "," + reservation.getNumberOfPeople()
                        + "," + reservation.getPlace()
                        + "," + reservation.getRegistrationDate().format(DateTimeFormatter.ofPattern("dd-MM-YYYY HH:mm:ss"))
                        + "," + reservation.getLiturgie().getDescription()
                        + "," + reservation.getLiturgie().getDate().format(DateTimeFormatter.ofPattern("dd-MM-YYYY HH:mm:ss")) + "\n");
                htmlTable.append("  <tr>\n" +
                        "    <td>" + reservation.getFirstName() + " " + reservation.getLastName() + "</td>\n" +
                        "    <td>" + reservation.getEmail() + "</td>\n" +
                        "    <td>" + reservation.getMobile() + "</td>\n" +
                        "    <td>" + reservation.getNumberOfPeople() + "</td>\n" +
                        "    <td>" + reservation.getPlace() + "</td>\n" +
                        "    <td>" + reservation.getRegistrationDate().format(DateTimeFormatter.ofPattern("dd-MM-YYYY HH:mm:ss")) + "</td>\n" +
                        "    <td>" + reservation.getLiturgie().getDescription() + "</td>\n" +
                        "    <td>" + reservation.getLiturgie().getDate().format(DateTimeFormatter.ofPattern("dd-MM-YYYY HH:mm:ss")) + "</td>\n" +
                        "  </tr>\n");
            });
            htmlTable.append("</table>");
            try {
                this.emailService.sendSimpleMessage("bishoy.ensoliman@gmail.com"
                        , "Morgen Liturgie Reservierungen"
                        , htmlTable.toString()
                        , "Liturgie Reservierung "
                                + OffsetDateTime.now().toString() + ".csv", csvFile.toString());
            } catch (MessagingException exception) {
                System.out.println("Failed sending confirmation email");
            }
        }
    }
}
