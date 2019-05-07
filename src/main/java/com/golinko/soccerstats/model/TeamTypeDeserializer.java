package com.golinko.soccerstats.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.Optional;

/**
 * Deserializer for team type string value to {@link TeamType} object.
 */
public class TeamTypeDeserializer extends StdDeserializer<TeamType> {

    // this is required for deserializer initialization
    public TeamTypeDeserializer() {
        this(null);
    }

    private TeamTypeDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public TeamType deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String result = jp.readValueAs(String.class);

        return Optional.of(result)
                .filter(r -> !r.isEmpty())
                .map(r -> TeamType.valueOf(r.toUpperCase()))
                .orElse(null);
    }
}
