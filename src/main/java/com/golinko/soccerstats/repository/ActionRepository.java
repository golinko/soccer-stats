package com.golinko.soccerstats.repository;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.golinko.soccerstats.exceptions.SoccerStatsException;
import com.golinko.soccerstats.model.Action;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ActionRepository {
    @Value("${data.file:dataset.csv}")
    private String dataFile;

    @Value("${data.file.encoding:ISO-8859-1}")
    private String dataFileEncoding;

    @Cacheable(value = "actions")
    public List<Action> loadActions() throws SoccerStatsException {
        log.debug("loadActions()");
        try (Reader reader = getReader()) {
            CsvMapper mapper = new CsvMapper().enable(CsvParser.Feature.TRIM_SPACES);
            CsvSchema bootstrapSchema = mapper
                    .typedSchemaFor(Action.class)
                    .withHeader()
                    .withLineSeparator(",")
                    .withNullValue("NULL");
            MappingIterator<Action> readValues = mapper
                    .readerFor(Action.class)
                    .with(bootstrapSchema)
                    .readValues(reader);
            return readValues.readAll();
        } catch (Exception e) {
            log.error("Error during csv file read", e);
            throw new SoccerStatsException("Error during csv file read");
        }
    }

    private Reader getReader() throws IOException, URISyntaxException {
        log.debug("getReader()");
        Path path = Paths.get(ClassLoader.getSystemResource(dataFile).toURI());
        return Files.newBufferedReader(path, Charset.forName(dataFileEncoding));
    }
}

