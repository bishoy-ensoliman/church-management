package de.stminakirchemuenchen.stminamassreservation.controllers.v1;

import com.bishoysoliman.stminamassreservation.api.v1.UserApi;
import com.bishoysoliman.stminamassreservation.api.v1.models.JwtDTO;
import com.bishoysoliman.stminamassreservation.api.v1.models.UserDTO;
import com.bishoysoliman.stminamassreservation.api.v1.models.UserRegistrationDTO;
import de.stminakirchemuenchen.stminamassreservation.config.JwtTokenUtil;
import de.stminakirchemuenchen.stminamassreservation.services.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/apis/v1")
public class AuthenticationController implements UserApi {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Override
    public ResponseEntity<JwtDTO> authenticate(UserDTO user) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername()
                            , user.getPassword()));
        } catch (DisabledException | BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(user.getUsername());

        GrantedAuthority authority = userDetails.getAuthorities().stream().findFirst().orElse(null);

        String role = Objects.isNull(authority) ? "" : authority.getAuthority();

        final String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtDTO().jwttoken(token)
                .username(user.getUsername())
                .role(role));
    }

    @Override
    public ResponseEntity<UserDTO> registerUser(UserRegistrationDTO user) {
        return ResponseEntity.ok(userDetailsService.save(user));
    }
}