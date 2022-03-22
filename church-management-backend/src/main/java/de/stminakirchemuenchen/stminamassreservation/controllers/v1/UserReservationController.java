package de.stminakirchemuenchen.stminamassreservation.controllers.v1;

import com.bishoysoliman.stminamassreservation.api.v1.UserReservationApi;
import com.bishoysoliman.stminamassreservation.api.v1.models.ImportUserReservationsResponse;
import com.bishoysoliman.stminamassreservation.api.v1.models.LiturgieSchedulePage;
import com.bishoysoliman.stminamassreservation.api.v1.models.UserReservationDTO;
import com.bishoysoliman.stminamassreservation.api.v1.models.UserReservationList;
import de.stminakirchemuenchen.stminamassreservation.services.UserReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/apis/v1")
public class UserReservationController implements UserReservationApi {

    @Autowired
    private UserReservationService userReservationService;

    public ResponseEntity<UserReservationList> userReservationGet(String username) {
        return new ResponseEntity<>( new UserReservationList()
                .reservations(this.userReservationService.getUserReservations(username))
                , HttpStatus.OK);
    }


    public ResponseEntity<ImportUserReservationsResponse> userReservationImportPost(MultipartFile upfile) {
        return new ResponseEntity<>(this.userReservationService.importUserReservations(upfile)
                , HttpStatus.OK);
    }
}
