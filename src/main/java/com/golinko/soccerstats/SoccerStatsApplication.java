package com.golinko.soccerstats;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class SoccerStatsApplication {

    public static void main(String[] args) {
        log.info("SoccerStatsApplication startup");
        SpringApplication.run(SoccerStatsApplication.class, args);
    }

}
