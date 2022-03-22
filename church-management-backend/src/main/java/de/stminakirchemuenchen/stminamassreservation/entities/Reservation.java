package de.stminakirchemuenchen.stminamassreservation.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {
    @Id
    @Type(type = "pg-uuid")
    private UUID id = UUID.randomUUID();

    private String lastName;
    private String firstName;
    private String password;
    private String email;
    private String mobile;
    private int numberOfPeople;
    private String place;
    private boolean approved = false;

    @Column(name = "registration_date", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime registrationDate;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_reservation_liturgie"))
    private Liturgie liturgie;

    private boolean deleted = false;
}
