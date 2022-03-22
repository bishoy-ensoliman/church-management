package de.stminakirchemuenchen.stminamassreservation.repositories;

import de.stminakirchemuenchen.stminamassreservation.entities.UserReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface UserReservationRepository extends JpaRepository<UserReservation, UUID> {
    @Query("SELECT ur FROM UserReservation ur WHERE LOWER(ur.username) = LOWER(:username)")
    List<UserReservation> findByName(String username);
}
