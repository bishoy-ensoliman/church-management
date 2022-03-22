package de.stminakirchemuenchen.stminamassreservation.services;

import com.bishoysoliman.stminamassreservation.api.v1.models.ReservationDTO;
import de.stminakirchemuenchen.stminamassreservation.entities.Liturgie;
import de.stminakirchemuenchen.stminamassreservation.entities.Reservation;
import de.stminakirchemuenchen.stminamassreservation.enums.Places;
import de.stminakirchemuenchen.stminamassreservation.repositories.LiturgieRepository;
import de.stminakirchemuenchen.stminamassreservation.repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private LiturgieRepository liturgieRepository;

    @Autowired
    private EmailServiceImpl emailService;

    @Secured("ROLE_ADMIN")
    public List<ReservationDTO> getAllUpcomingReservations() {
        return this.reservationRepository.findUpcomingReservations().stream()
                .map(this::mapReservationToDTO)
                .collect(Collectors.toList());
    }

    public ReservationDTO addReservation(ReservationDTO reservationDTO) {

        String massUUID = reservationDTO.getLiturgieUUID();
        if (!StringUtils.hasText(massUUID)) {
            return reservationDTO.approved(false);
        }

        Liturgie liturgie = this.liturgieRepository.findById(UUID.fromString(massUUID))
                .orElse(null);
        if (Objects.isNull(liturgie)) {
            return reservationDTO.approved(false);
        }

        Collection<Reservation> nextTwoWeeksReservations =
                this.reservationRepository.findReservationsOfLiturgiesBetweenDatesByUserName(
                        reservationDTO.getFirstName().trim(),
                        reservationDTO.getLastName().trim(),
                        liturgie.getDate(),
                        liturgie.getDate().plusDays(13));

        if(!CollectionUtils.isEmpty(nextTwoWeeksReservations)){
            return reservationDTO.approved(false);
        }

        Collection<Reservation> prevTwoWeeksReservations =
                this.reservationRepository.findReservationsOfLiturgiesBetweenDatesByUserName(
                        reservationDTO.getFirstName().trim(),
                        reservationDTO.getLastName().trim(),
                        liturgie.getDate().minusDays(13),
                        liturgie.getDate());
        if(!CollectionUtils.isEmpty(prevTwoWeeksReservations)){
            return reservationDTO.approved(false);
        }

        String selectedPlace = reservationDTO.getPlace();
        int numOfPeople = reservationDTO.getNumberOfPeople();

        int newChurchCount = liturgie.getChurchCount() + numOfPeople;
        int newSalaCount = liturgie.getSalaCount() + numOfPeople;

        if (selectedPlace.equals(Places.KIRCHE.name())
                && newChurchCount <= liturgie.getMaxChurchCount()) {
            liturgie.setChurchCount(newChurchCount);
        } else if (selectedPlace.equals(Places.SAAL.name())
                && newSalaCount <= liturgie.getMaxSalaCount()) {
            liturgie.setSalaCount(newSalaCount);
        } else {
            return reservationDTO.approved(false);
        }

        this.liturgieRepository.save(liturgie);


        Reservation reservation = new Reservation();
        reservation.setApproved(true);
        reservation.setEmail(reservationDTO.getEmail().trim());
        reservation.setMobile(reservationDTO.getMobile().trim());
        reservation.setFirstName(reservationDTO.getFirstName().trim());
        reservation.setLastName(reservationDTO.getLastName().trim());
        reservation.setNumberOfPeople(numOfPeople);
        reservation.setRegistrationDate(OffsetDateTime.now());
        reservation.setPlace(selectedPlace);
        reservation.setLiturgie(liturgie);
        reservation.setPassword(reservationDTO.getPassword().trim());
        this.reservationRepository.save(reservation);
        reservationDTO.setApproved(true);
        reservationDTO.setRegistrationDate(reservation.getRegistrationDate());
//        try {
//            emailService.sendRegistrationEmail(reservationDTO.getEmail()
//                    , "Liturgie Reservation Successful"
//                    , MessageFormat.format("Your Reservation on {0} is approved."
//                            , liturgie.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")))
//                            , liturgie.getDate());
//        } catch (MessagingException exception){
//            System.out.println("Failed sending confirmation email");
//        }
        return reservationDTO;
    }

    private ReservationDTO mapReservationToDTO(Reservation reservation) {
        return new ReservationDTO().approved(reservation.isApproved())
                .email(reservation.getEmail())
                .liturgieDate(reservation.getLiturgie().getDate())
                .liturgieUUID(reservation.getLiturgie().getId().toString())
                .uuid(reservation.getId().toString())
                .firstName(reservation.getFirstName())
                .lastName(reservation.getLastName())
                .mobile(reservation.getMobile())
                .numberOfPeople(reservation.getNumberOfPeople())
                .registrationDate(reservation.getRegistrationDate())
                .place(reservation.getPlace());
    }

    public List<ReservationDTO> getUpcomingReservations(String firstName, String lastName) {
        if (!StringUtils.hasText(firstName) && !StringUtils.hasText(lastName)) {
            return getAllUpcomingReservations();
        }
        return this.reservationRepository.findUpcomingReservationsByUserName(firstName.trim(), lastName.trim()).stream()
                .map(this::mapReservationToDTO)
                .collect(Collectors.toList());
    }

    public Void deleteReservation(String uuid, String password) {
        Reservation reservation = this.reservationRepository.findById(UUID.fromString(uuid)).orElse(null);
        if (reservation != null && reservation.getPassword().equals(password.trim())) {
            reservation.setDeleted(true);
            this.reservationRepository.save(reservation);
            Liturgie liturgie = reservation.getLiturgie();
            if(reservation.getPlace().equals(Places.KIRCHE.name())){
                liturgie.setChurchCount(liturgie.getChurchCount() - reservation.getNumberOfPeople());
            } else if(reservation.getPlace().equals(Places.SAAL.name())){
                liturgie.setSalaCount(liturgie.getSalaCount() - reservation.getNumberOfPeople());
            }
            this.liturgieRepository.save(liturgie);
        }
        return null;
    }
}
