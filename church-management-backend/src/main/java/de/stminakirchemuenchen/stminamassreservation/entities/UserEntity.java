package de.stminakirchemuenchen.stminamassreservation.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    @Id
    @Type(type = "pg-uuid")
    private UUID id = UUID.randomUUID();

    private String username;

    private String password;

    @ElementCollection
    private List<String> roles;

    private boolean enabled;

    private String firstName;
    private String lastName;
    private String email;
}
