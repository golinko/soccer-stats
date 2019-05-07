package com.golinko.soccerstats.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MatchTableDto {
    @NonNull
    private MatchDto match;

    @NonNull
    private TeamScoreDto home;

    @NonNull
    private TeamScoreDto away;
}
