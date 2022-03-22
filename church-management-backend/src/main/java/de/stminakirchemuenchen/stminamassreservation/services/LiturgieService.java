package de.stminakirchemuenchen.stminamassreservation.services;

import com.bishoysoliman.stminamassreservation.api.v1.models.LiturgieDTO;
import de.stminakirchemuenchen.stminamassreservation.entities.Liturgie;
import de.stminakirchemuenchen.stminamassreservation.entities.Reservation;
import de.stminakirchemuenchen.stminamassreservation.exceptions.LiturgieReservedAlreadyException;
import de.stminakirchemuenchen.stminamassreservation.repositories.LiturgieRepository;
import de.stminakirchemuenchen.stminamassreservation.repositories.ReservationRepository;
import io.jsonwebtoken.lang.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class LiturgieService {
    @Autowired
    private LiturgieRepository liturgieRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    private LiturgieDTO addLiturgie(LiturgieDTO liturgieDTO) {
        if(Objects.isNull(this.liturgieRepository.findByDate(liturgieDTO.getDate()))){
            Liturgie liturgie = new Liturgie();
            liturgie.setDescription(liturgieDTO.getDescription());
            liturgie.setChurchCount(0);
            liturgie.setMaxChurchCount(liturgieDTO.getMaxChurchCount());
            liturgie.setSalaCount(0);
            liturgie.setMaxSalaCount(liturgieDTO.getMaxSalaCount());
            liturgie.setDate(liturgieDTO.getDate());
            this.liturgieRepository.save(liturgie);
            liturgieDTO.setUuid(liturgie.getId().toString());
        }
        return liturgieDTO;
    }

    public List<LiturgieDTO> getUpcomingLiturgieSchedules() {
        return this.liturgieRepository.findUpcomingLiturgies().stream()
                .map(liturgie -> new LiturgieDTO()
                        .description(liturgie.getDescription())
                        .churchCount(liturgie.getChurchCount())
                        .salaCount(liturgie.getSalaCount())
                        .date(liturgie.getDate())
                        .maxChurchCount(liturgie.getMaxChurchCount())
                        .maxSalaCount(liturgie.getMaxSalaCount())
                        .uuid(liturgie.getId().toString()))
                .collect(Collectors.toList());
    }

    @Secured("ROLE_ADMIN")
    public Void deleteLiturgie(String liturgieUUID, Boolean forceDelete) {
        if(!StringUtils.hasText(liturgieUUID)){
            return null;
        }

        Liturgie liturgie = this.liturgieRepository.getById(UUID.fromString(liturgieUUID));
        Collection<Reservation> reservations = this.reservationRepository.findReservationsByLiturgie(liturgie);

        if(Collections.isEmpty(reservations)){
            liturgie.setDeleted(true);
            this.liturgieRepository.save(liturgie);
            return null;
        }

        if(!forceDelete){
            throw new LiturgieReservedAlreadyException();
        }

        //Todo notify people already booked
        reservations.stream().forEach(reservation -> reservation.setDeleted(true));
        this.reservationRepository.saveAll(reservations);
        liturgie.setDeleted(true);
        this.liturgieRepository.delete(liturgie);
        return null;
    }

    @Secured("ROLE_ADMIN")
    public LiturgieDTO updateLiturgie(LiturgieDTO liturgieDTO) {
        if(!StringUtils.hasText(liturgieDTO.getUuid()))
            return this.addLiturgie(liturgieDTO);

        Liturgie liturgie = this.liturgieRepository
                    .findById(UUID.fromString(liturgieDTO.getUuid())).orElse(null);

        if(Objects.isNull(liturgie)){
            return this.addLiturgie(liturgieDTO);
        }


        liturgie.setDescription(liturgieDTO.getDescription());
        liturgie.setMaxChurchCount(liturgieDTO.getMaxChurchCount());
        liturgie.setMaxSalaCount(liturgieDTO.getMaxSalaCount());
        liturgie.setDate(liturgieDTO.getDate());
        this.liturgieRepository.save(liturgie);

        return liturgieDTO;
    }
}
