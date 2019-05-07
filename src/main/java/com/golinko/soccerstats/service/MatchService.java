package com.golinko.soccerstats.service;

import com.golinko.soccerstats.dto.MatchDto;
import com.golinko.soccerstats.dto.MatchInfoDto;
import com.golinko.soccerstats.dto.MatchTableDto;
import com.golinko.soccerstats.exceptions.NotFoundException;
import com.golinko.soccerstats.exceptions.SoccerStatsException;
import com.golinko.soccerstats.model.Action;
import com.golinko.soccerstats.repository.ActionRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
@Slf4j
@RequiredArgsConstructor
public class MatchService {

    @NonNull
    private final ActionRepository repository;

    @NonNull
    private final DataConverter dataConverter;

    public List<MatchDto> getMatchList() throws SoccerStatsException {
        log.debug("getMatchList()");
        return repository.loadActions().stream()
                .filter(byNonNullTeamId())
                .filter(distinctByKey(Action::getMatchId))
                .map(dataConverter::convertToMatchDto)
                .distinct()
                .collect(toList());
    }

    public List<MatchTableDto> getMatchTables() throws SoccerStatsException {
        log.debug("getMatchTables()");
        return repository.loadActions().stream()
                .filter(byNonNullTeamId())
                .collect(groupingBy(dataConverter::convertToMatchDto))
                .entrySet().stream()
                .map(entry -> dataConverter.convertToMatchTableDto(entry.getKey(), entry.getValue()).orElse(null))
                .filter(Objects::nonNull)
                .collect(toList());
    }

    public MatchTableDto getMatchTable(Long matchId) throws SoccerStatsException {
        log.debug("getMatchTable({})", matchId);
        return getMatchTables().stream()
                .filter(m -> m.getMatch().getMatchId().equals(matchId))
                .findAny()
                .orElseThrow(() -> new NotFoundException(String.format("Match[%d] is not found", matchId)));
    }

    public MatchInfoDto getMatchInfo(Long matchId) throws SoccerStatsException {
        log.debug("getMatchInfo()");
        return repository.loadActions().stream()
                .filter(a -> a.getMatchId().equals(matchId))
                .filter(a -> Objects.nonNull(a.getPersonId()))
                .filter(a -> Objects.nonNull(a.getPersonFunction()))
                .collect(groupingBy(dataConverter::convertToMatchDto))
                .entrySet().stream()
                .map(entry -> dataConverter.convertToMatchInfoDto(entry.getKey(), entry.getValue()))
                .findAny()
                .orElseThrow(() -> new NotFoundException(String.format("Match[%d] is not found", matchId)));
    }

    private Predicate<Action> byNonNullTeamId() {
        return a -> Objects.nonNull(a.getTeamId());
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }
}
