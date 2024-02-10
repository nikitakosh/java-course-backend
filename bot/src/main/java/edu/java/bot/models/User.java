package edu.java.bot.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "chat_id")
    private Long chatID;
    @Column(name = "name")
    private String name;
    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private UserState state;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_links",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "link_id")
    )
    private List<Link> links;
}
