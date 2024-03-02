package edu.java.bot.controllers.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LinkUpdate {
    @NotNull
    private Integer id;
    @NotEmpty
    private URI url;
    @NotEmpty
    private String description;
    @NotEmpty
    private List<Integer> tgChatIds;


}
