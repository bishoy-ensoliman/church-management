package de.stminakirchemuenchen.stminamassreservation.repositories;

import de.stminakirchemuenchen.stminamassreservation.entities.Liturgie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.UUID;

public interface LiturgieRepository extends JpaRepository<Liturgie, UUID> {

    @Query("SELECT l FROM Liturgie l WHERE l.deleted = false and l.date >= CURRENT_TIMESTAMP ORDER BY date ASC")
    Collection<Liturgie> findUpcomingLiturgies();

    @Query("SELECT l FROM Liturgie l WHERE l.deleted = false and l.date = :date")
    Liturgie findByDate(OffsetDateTime date);
}
