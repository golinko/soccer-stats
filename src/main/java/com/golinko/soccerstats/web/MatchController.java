package com.golinko.soccerstats.web;

import com.golinko.soccerstats.dto.MatchDto;
import com.golinko.soccerstats.dto.MatchInfoDto;
import com.golinko.soccerstats.dto.MatchTableDto;
import com.golinko.soccerstats.exceptions.SoccerStatsException;
import com.golinko.soccerstats.service.MatchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/match", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RequiredArgsConstructor
@Api(value = "Soccer statistics API")
public class MatchController {

    @NonNull
    private final MatchService matchService;

    @ApiOperation(value = "Get list of matches with the information about match dates and competition", response = List.class)
    @GetMapping
    public List<MatchDto> getMatches() throws SoccerStatsException {
        log.debug("getMatches()");
        return matchService.getMatchList();
    }

    @ApiOperation(value = "Get match tables with the information about the team scores", response = List.class)
    @GetMapping("/table")
    public List<MatchTableDto> getMatchTables() throws SoccerStatsException {
        log.debug("getMatchTables()");
        return matchService.getMatchTables();
    }

    @ApiOperation(value = "Get match details by id with information about persons participating in the match", response = List.class)
    @GetMapping("/{matchId}")
    public MatchInfoDto getMatchInfo(@ApiParam(value = "Match id from which match info object will retrieve", example = "2174508", required = true)
                                     @PathVariable Long matchId) throws SoccerStatsException {
        log.debug("getMatchInfo({})", matchId);
        return matchService.getMatchInfo(matchId);
    }
}
