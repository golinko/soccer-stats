package com.golinko.soccerstats.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MatchInfoDto {
    @NonNull
    private MatchDto match;

    @NonNull
    private List<MatchPersonDto> away;

    @NonNull
    private List<MatchPersonDto> home;

    @NonNull
    private List<MatchPersonDto> other;
}
