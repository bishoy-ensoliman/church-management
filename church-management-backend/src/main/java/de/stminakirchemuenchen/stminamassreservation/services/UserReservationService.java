package de.stminakirchemuenchen.stminamassreservation.services;

import com.bishoysoliman.stminamassreservation.api.v1.models.ImportUserReservationsResponse;
import com.bishoysoliman.stminamassreservation.api.v1.models.UserReservationDTO;
import de.stminakirchemuenchen.stminamassreservation.entities.UserReservation;
import de.stminakirchemuenchen.stminamassreservation.repositories.UserReservationRepository;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserReservationService {

    @Autowired
    private UserReservationRepository userReservationRepository;


    public List<UserReservationDTO> getUserReservations(String username) {
        return this.userReservationRepository.findByName(username)
                .stream().map( userReservation -> new UserReservationDTO()
                        .date(userReservation.getDate())
                        .username(userReservation.getUsername())
                        .place(userReservation.getPlace())
                        .numberOfPlaces(userReservation.getNumberOfPlaces())
        ).collect(Collectors.toList());
    }

    @Secured("ROLE_ADMIN")
    public ImportUserReservationsResponse importUserReservations(MultipartFile upfile) {
        ImportUserReservationsResponse importResponse = new ImportUserReservationsResponse();
        int numSucceededInserts = 0;
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(upfile.getInputStream()))) {
            while (reader.ready()) {
                String line = reader.readLine();
                String[] tokens = line.split(";", -1);
                UserReservation userReservation = new UserReservation();
                userReservation.setUsername(tokens[0].trim());
                userReservation.setDate(tokens[1]);
                userReservation.setPlace(tokens[2]);
                userReservation.setNumberOfPlaces(tokens[3]);
                this.userReservationRepository.save(userReservation);
                numSucceededInserts++;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        importResponse.setImportedCount(numSucceededInserts);
        return importResponse;
    }
}
