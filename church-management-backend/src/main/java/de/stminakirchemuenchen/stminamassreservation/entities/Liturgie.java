package de.stminakirchemuenchen.stminamassreservation.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Liturgie {
    @Id
    @Type(type = "pg-uuid")
    private UUID id = UUID.randomUUID();

    private String description;

    private int churchCount;
    private int salaCount;

    private int maxChurchCount;
    private int maxSalaCount;

    @Column(name = "date", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime date;

    private boolean deleted = false;
}
