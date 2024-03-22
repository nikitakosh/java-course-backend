package edu.java.domain.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@Entity
@Table(name = "link")
public class LinkEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "url")
    private String url;
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
    @Column(name = "created_at")
    private OffsetDateTime createdAt;
    @Column(name = "commit_message")
    private String commitMessage;
    @Column(name = "commit_sha")
    private String commitSha;
    @Column(name = "answer_id")
    private Long answerId;
    @Column(name = "answer_owner")
    private String answerOwner;
    @ManyToMany(mappedBy = "links")
    @ToString.Exclude
    private List<ChatEntity> chats = new ArrayList<>();

    public void addChat(ChatEntity chat) {
        this.getChats().add(chat);
        chat.getLinks().add(this);
    }

    public void removeChat(ChatEntity chat) {
        chat.getLinks().remove(this);
        chats.remove(chat);
    }
}
