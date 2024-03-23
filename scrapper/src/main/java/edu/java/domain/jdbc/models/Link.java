package edu.java.domain.jdbc.models;

import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Link {
    private Integer id;
    private String url;
    private OffsetDateTime updatedAt;
    private OffsetDateTime createdAt;
    private String commitMessage;
    private String commitSHA;
    private Long answerId;
    private String answerOwner;
}
