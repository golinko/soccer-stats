package com.golinko.soccerstats.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TeamScoreDto {
    @NonNull
    private TeamDto team;

    @NonNull
    private Long score;
}
