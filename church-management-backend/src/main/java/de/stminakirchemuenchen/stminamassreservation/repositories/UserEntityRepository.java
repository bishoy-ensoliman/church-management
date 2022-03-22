package de.stminakirchemuenchen.stminamassreservation.repositories;

import de.stminakirchemuenchen.stminamassreservation.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserEntityRepository extends JpaRepository<UserEntity, UUID> {
    UserEntity findByUsername(String username);
}
