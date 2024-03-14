package edu.java.clients.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record RepoResponse(
        @JsonProperty("id") String id,
        @JsonProperty("full_name") String fullName,
        @JsonProperty("updated_at") OffsetDateTime updatedAt
) {

}
