package edu.java.bot.controllers.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LinkUpdate {
    @NotNull
    private Integer id;
    @NotEmpty
    private String url;
    @NotEmpty
    private String description;
    @NotEmpty
    private List<Integer> tgChatIds;


}
