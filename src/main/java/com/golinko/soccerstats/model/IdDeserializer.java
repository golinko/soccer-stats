package com.golinko.soccerstats.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.Optional;

/**
 * Deserializer for ID value from Long. There is no ID with value 0, such value will be deserialized to null
 */
public class IdDeserializer extends StdDeserializer<Long> {

    // this is required for deserializer initialization
    public IdDeserializer() {
        this(null);
    }

    private IdDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Long deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        Long result = jp.readValueAs(Long.class);

        return Optional.of(result).filter(r -> !r.equals(0L)).orElse(null);
    }
}
