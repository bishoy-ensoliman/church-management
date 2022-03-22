package de.stminakirchemuenchen.stminamassreservation.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserReservation {
    @Id
    @Type(type = "pg-uuid")
    private UUID id = UUID.randomUUID();

    private String username;
    private String date;
    private String place;
    private String numberOfPlaces;
}
