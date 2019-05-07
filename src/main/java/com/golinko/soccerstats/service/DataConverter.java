package com.golinko.soccerstats.service;

import com.golinko.soccerstats.dto.*;
import com.golinko.soccerstats.exceptions.NotFoundException;
import com.golinko.soccerstats.model.Action;
import com.golinko.soccerstats.model.TeamType;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class DataConverter {
    @NonNull
    private final ModelMapper modelMapper;

    MatchDto convertToMatchDto(Action action) {
        return modelMapper.map(action, MatchDto.class);
    }

    Optional<MatchTableDto> convertToMatchTableDto(MatchDto match, List<Action> actions) {
        try {
            return Optional.of(MatchTableDto.builder()
                    .match(match)
                    .home(findTeamScore(actions, TeamType.HOME))
                    .away(findTeamScore(actions, TeamType.AWAY))
                    .build());
        } catch (NotFoundException e) {
            // This is not a good practice of handling exceptions, but here this should not actually happen
            log.error("Cannot convert to MatchTableDto", e);
            return Optional.empty();
        }
    }

    MatchInfoDto convertToMatchInfoDto(MatchDto match, List<Action> actions) {
        return MatchInfoDto.builder()
                .match(match)
                .home(findPersons(actions, TeamType.HOME))
                .away(findPersons(actions, TeamType.AWAY))
                .other(findPersons(actions, null))
                .build();
    }

    private List<MatchPersonDto> findPersons(List<Action> actions, TeamType teamType) {
        return actions.stream()
                .filter(Optional.ofNullable(teamType)
                        .map(this::byTeamType)
                        .orElseGet(this::byNoTeam))
                .map(this::convertToMatchPersonDto)
                .distinct()
                .collect(Collectors.toList());
    }

    private TeamScoreDto findTeamScore(List<Action> actions, TeamType teamType) throws NotFoundException {
        return TeamScoreDto.builder()
                .team(findTeam(actions, teamType))
                .score(findScore(actions, teamType))
                .build();
    }

    private TeamDto findTeam(List<Action> actions, TeamType teamType) throws NotFoundException {
        return actions.stream()
                .filter(byTeamType(teamType))
                .map(this::convertToTeamDto)
                .findAny()
                .orElseThrow(() -> new NotFoundException(String.format("No %s team was found", teamType)));
    }

    private Long findScore(List<Action> actions, TeamType teamType) {
        return actions.stream()
                .filter(byTeamType(teamType))
                .filter(a -> "Goal".equals(a.getActionName()))
                .count();
    }

    private TeamDto convertToTeamDto(Action action) {
        return modelMapper.map(action, TeamDto.class);
    }

    private MatchPersonDto convertToMatchPersonDto(Action action) {
        return modelMapper.map(action, MatchPersonDto.class);
    }

    private Predicate<Action> byTeamType(TeamType teamType) {
        return a -> teamType.equals(a.getTeamType());
    }

    private Predicate<Action> byNoTeam() {
        return a -> Objects.isNull(a.getTeamType());
    }
}
