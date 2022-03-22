package de.stminakirchemuenchen.stminamassreservation.controllers.v1;

import com.bishoysoliman.stminamassreservation.api.v1.ReservationApi;
import com.bishoysoliman.stminamassreservation.api.v1.models.ReservationDTO;
import com.bishoysoliman.stminamassreservation.api.v1.models.ReservationList;
import de.stminakirchemuenchen.stminamassreservation.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/apis/v1")
public class ReservationController implements ReservationApi {
    @Autowired
    private ReservationService reservationService;

    @Override
    public ResponseEntity<ReservationList> reservationGet(String firstName, String lastName) {
        return new ResponseEntity<>( new ReservationList()
                .reservations(this.reservationService.getUpcomingReservations(firstName, lastName))
                , HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ReservationDTO> reservationPut(ReservationDTO reservation) {
        return new ResponseEntity<>(this.reservationService.addReservation(reservation)
                , HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> reservationDelete(String uuid, String password) {
        return new ResponseEntity(this.reservationService.deleteReservation(uuid, password), HttpStatus.NO_CONTENT);
    }
}
