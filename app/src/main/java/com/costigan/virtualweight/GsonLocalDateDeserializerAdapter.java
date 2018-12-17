package com.costigan.virtualweight;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.lang.reflect.Type;

/**
 * Deserialize the Loacl date from gson
 */
public class GsonLocalDateDeserializerAdapter implements JsonDeserializer<LocalDate> {

    @Override
    public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String readDate = json.getAsString();
        LocalDate desDate;
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
        desDate = LocalDate.parse(readDate, formatter);

        return desDate;
    }
}


