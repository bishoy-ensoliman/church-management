package de.stminakirchemuenchen.stminamassreservation.services;

import com.bishoysoliman.stminamassreservation.api.v1.models.UserDTO;
import com.bishoysoliman.stminamassreservation.api.v1.models.UserRegistrationDTO;
import de.stminakirchemuenchen.stminamassreservation.entities.UserEntity;
import de.stminakirchemuenchen.stminamassreservation.repositories.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity =
                this.userEntityRepository.findByUsername(username);
        if (userEntity == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        if (!userEntity.isEnabled()) {
            throw new UsernameNotFoundException("User with username: " + username+ " is disabled");
        }

        if (CollectionUtils.isEmpty(userEntity.getRoles())) {
            throw new UsernameNotFoundException("User with username: " + username+ " is not approved");
        }

        return new User(userEntity.getUsername(), userEntity.getPassword(),
                this.getAuthorities(userEntity));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(UserEntity user) {
        return user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
    }

    public UserDTO save(UserRegistrationDTO user) {
        UserEntity userEntity = this.userEntityRepository.findByUsername(user.getUsername());
        if(!Objects.isNull(userEntity))
            return new UserDTO();
        UserEntity newUserEntity = new UserEntity();
        newUserEntity.setUsername(user.getUsername());
        newUserEntity.setPassword(bcryptEncoder.encode(user.getPassword()));
        newUserEntity.setRoles(new ArrayList<>());
        this.userEntityRepository.save(newUserEntity);
        return new UserDTO()
                .username(newUserEntity.getUsername())
                .password("******");
    }
}
