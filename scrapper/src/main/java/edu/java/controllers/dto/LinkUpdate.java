package edu.java.controllers.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LinkUpdate {
    @NotEmpty
    private String url;
    @NotEmpty
    private String description;
    @NotEmpty
    private List<Long> tgChatIds;


}
