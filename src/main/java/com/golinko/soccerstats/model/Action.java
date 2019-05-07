package com.golinko.soccerstats.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Date;

@Data
@NoArgsConstructor
@JsonPropertyOrder({"actionId", "competition", "matchId", "matchDate", "actionName", "matchPeriod", "actionStart", "actionEnd", "teamType", "teamId", "teamName", "personId", "personName", "personFunction", "squadNumber", "actionReason", "actionInfo", "subPersonId", "subPersonName"})
public class Action {

    @NonNull
    private Long actionId;

    @NonNull
    private String competition;

    @NonNull
    private Long matchId;

    @NonNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy HH:mm")
    private Date matchDate;

    @NonNull
    private String actionName;

    private String matchPeriod;

    private Long actionStart;

    private Long actionEnd;

    @JsonDeserialize(using = TeamTypeDeserializer.class)
    private TeamType teamType;

    @JsonDeserialize(using = IdDeserializer.class)
    private Long teamId;

    private String teamName;

    @JsonDeserialize(using = IdDeserializer.class)
    private Long personId;

    private String personName;

    private String personFunction;

    @JsonDeserialize(using = IdDeserializer.class)
    private Long squadNumber;

    private String actionReason;

    private String actionInfo;

    @JsonDeserialize(using = IdDeserializer.class)
    private Long subPersonId;

    private String subPersonName;
}
