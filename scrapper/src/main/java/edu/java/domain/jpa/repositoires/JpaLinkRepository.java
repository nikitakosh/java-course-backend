package edu.java.domain.jpa.repositoires;

import edu.java.domain.jpa.entity.LinkEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaLinkRepository extends JpaRepository<LinkEntity, Integer> {
    Optional<LinkEntity> findByUrl(String url);
}
