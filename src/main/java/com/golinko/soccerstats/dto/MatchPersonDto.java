package com.golinko.soccerstats.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MatchPersonDto {
    @NonNull
    private PersonDto person;

    @NonNull
    private String personFunction;

    private TeamDto team;

    private Long squadNumber;
}

