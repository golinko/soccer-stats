package com.golinko.soccerstats.exceptions;

import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static java.lang.String.format;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class SoccerStatsException extends Exception {

    public SoccerStatsException(@NonNull String message) {
        super(message);
    }

    @Override
    public String toString() {
        return format("Message: %s", getMessage());
    }
}
