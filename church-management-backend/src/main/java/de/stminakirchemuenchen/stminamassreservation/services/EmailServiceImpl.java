package de.stminakirchemuenchen.stminamassreservation.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import sun.security.util.IOUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Component
public class EmailServiceImpl {

    @Autowired
    private JavaMailSender emailSender;

    public void sendSimpleMessage(String to, String subject, String text, String attachmentFileName, String attachmentFileContent) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("noreply@stminakirchemuenchen.de");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, true);


        helper.addAttachment(attachmentFileName, new InputStreamSource() {
            @Override
            public InputStream getInputStream() throws IOException {
                InputStream stream = new ByteArrayInputStream(attachmentFileContent.getBytes());
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return stream;
            }
        });
        emailSender.send(message);
    }

    public void sendRegistrationEmail(String to, String subject, String text, OffsetDateTime liturgieStartTime) throws MessagingException {

        MimeMessage message = emailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("noreply@stminakirchemuenchen.de");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text);

        String startDate = liturgieStartTime.atZoneSameInstant(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("YYYYMMdd"))
                + "T" +
                liturgieStartTime.atZoneSameInstant(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("HHmmss"))+"Z";
        String endDate = liturgieStartTime.atZoneSameInstant(ZoneOffset.UTC).plusHours(2).format(DateTimeFormatter.ofPattern("YYYYMMdd"))
                + "T" +
                liturgieStartTime.atZoneSameInstant(ZoneOffset.UTC).plusHours(2).format(DateTimeFormatter.ofPattern("HHmmss"))+"Z";

        String icalEvent = "BEGIN:VCALENDAR\n" +
                "VERSION:2.0\n" +
                "PRODID:-//stminakirchemuenchen.de//Reservation Service\n" +
                "BEGIN:VEVENT\n" +
                "DTSTAMP:"+startDate+"\n" +
                "DTSTART:"+startDate+"\n" +
                "DTEND:"+endDate+"\n" +
                "SUMMARY:Liturgie St Mina Kirche München\n" +
                "END:VEVENT\n" +
                "END:VCALENDAR";



        helper.addAttachment("liturgie event.ics", new InputStreamSource() {
            @Override
            public InputStream getInputStream() throws IOException {
                InputStream stream = new ByteArrayInputStream(icalEvent.getBytes());
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return stream;
            }
        });

        emailSender.send(message);
    }
}
