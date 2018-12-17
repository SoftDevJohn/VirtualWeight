package com.costigan.virtualweight;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import org.joda.time.LocalDate;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.format.DateTimeFormatter;

/**
 * Used to support the conversion of LocalDate to Gson date
 */
class GsonLocalDateAdapter implements JsonSerializer<LocalDate> {

    public JsonElement serialize(LocalDate date, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(date.toString("yyyy-MM-dd"));
    }
}