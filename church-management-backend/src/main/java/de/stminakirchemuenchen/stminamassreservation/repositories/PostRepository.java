package de.stminakirchemuenchen.stminamassreservation.repositories;

import de.stminakirchemuenchen.stminamassreservation.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {
}
