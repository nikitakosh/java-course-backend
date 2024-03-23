package edu.java.domain.jpa.repositoires;

import edu.java.domain.jpa.entity.ChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaTgChatRepository extends JpaRepository<ChatEntity, Long> {
}
