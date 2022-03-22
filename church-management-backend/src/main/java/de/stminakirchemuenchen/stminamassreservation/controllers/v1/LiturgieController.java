package de.stminakirchemuenchen.stminamassreservation.controllers.v1;

import com.bishoysoliman.stminamassreservation.api.v1.LiturgieApi;
import com.bishoysoliman.stminamassreservation.api.v1.models.LiturgieDTO;
import com.bishoysoliman.stminamassreservation.api.v1.models.LiturgieSchedulePage;
import de.stminakirchemuenchen.stminamassreservation.services.LiturgieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/apis/v1")
public class LiturgieController implements LiturgieApi {

    @Autowired
    private LiturgieService liturgieService;

    @Override
    public ResponseEntity<LiturgieSchedulePage> liturgieGet() {
        return new ResponseEntity<>( new LiturgieSchedulePage()
                .liturgies(this.liturgieService.getUpcomingLiturgieSchedules())
                , HttpStatus.OK);
    }

    @Override
    public ResponseEntity<LiturgieDTO> liturgiePut(LiturgieDTO liturgie) {
        return new ResponseEntity<>(this.liturgieService.updateLiturgie(liturgie)
                , HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> liturgieDelete(String liturgieUUID,Boolean forceDelete) {
        return new ResponseEntity(this.liturgieService.deleteLiturgie(liturgieUUID, forceDelete), HttpStatus.NO_CONTENT);
    }
}
