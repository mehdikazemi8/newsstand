package com.nebeek.newsstand.data.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.nebeek.newsstand.data.MyAdapterFactory;

import java.lang.reflect.Type;

public class BaseModel {
    public static <T> T deserialize(String json, Type type) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .registerTypeAdapterFactory(MyAdapterFactory.create())
                .create();
        try {
            return gson.fromJson(json, type);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String serialize() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
