package de.stminakirchemuenchen.stminamassreservation.repositories;

import de.stminakirchemuenchen.stminamassreservation.entities.Liturgie;
import de.stminakirchemuenchen.stminamassreservation.entities.Reservation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.UUID;

public interface ReservationRepository extends CrudRepository<Reservation, UUID> {
    @Query("SELECT r FROM Reservation r WHERE r.deleted = false and r.liturgie.date >= CURRENT_TIMESTAMP ORDER BY registrationDate ASC, r.liturgie.date ASC")
    Collection<Reservation> findUpcomingReservations();

    @Query("SELECT r FROM Reservation r WHERE r.deleted = false and r.liturgie.date >= :startTime and r.liturgie.date <= :endTime ORDER BY registrationDate ASC, r.liturgie.date ASC")
    Collection<Reservation> findReservationsOfLiturgiesBetweenDates(OffsetDateTime startTime, OffsetDateTime endTime);

    @Query("SELECT r FROM Reservation r WHERE r.deleted = false and TRIM(LOWER(r.firstName)) = TRIM(LOWER(:firstName)) and TRIM(LOWER(r.lastName)) = TRIM(LOWER(:lastName)) and r.liturgie.date >= :startTime and r.liturgie.date <= :endTime ORDER BY registrationDate ASC, r.liturgie.date ASC")
    Collection<Reservation> findReservationsOfLiturgiesBetweenDatesByUserName(String firstName, String lastName, OffsetDateTime startTime, OffsetDateTime endTime);

    @Query("SELECT r FROM Reservation r WHERE r.deleted = false and TRIM(LOWER(r.firstName)) = TRIM(LOWER(:firstName)) and TRIM(LOWER(r.lastName)) = TRIM(LOWER(:lastName)) and r.liturgie.date >= CURRENT_TIMESTAMP ORDER BY registrationDate ASC, r.liturgie.date ASC")
    Collection<Reservation> findUpcomingReservationsByUserName(String firstName, String lastName);

    @Query("SELECT r FROM Reservation r WHERE r.deleted = false and r.liturgie = :liturgie")
    Collection<Reservation> findReservationsByLiturgie(Liturgie liturgie);
}
