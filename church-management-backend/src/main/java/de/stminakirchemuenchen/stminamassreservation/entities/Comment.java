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
public class Comment {
    @Id
    @Type(type = "pg-uuid")
    private UUID id = UUID.randomUUID();

    private String text;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_post_user"))
    private UserEntity createdBy;

    @Column(name = "creation_date", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime creationDate;

    @Column(name = "last_update_date", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime lastUpdateDate;
}
