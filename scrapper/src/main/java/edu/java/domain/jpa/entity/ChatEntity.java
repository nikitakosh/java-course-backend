package edu.java.domain.jpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@Entity
@Table(name = "chat")
public class ChatEntity {
    @Id
    private Long id;

    @ManyToMany
    @JoinTable(
            name = "chat_link",
            joinColumns = @JoinColumn(name = "chat_id"),
            inverseJoinColumns = @JoinColumn(name = "link_id")
    )
    @ToString.Exclude
    private List<LinkEntity> links = new ArrayList<>();
}
