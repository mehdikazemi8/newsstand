package com.nebeek.newsstand.data;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.nebeek.newsstand.data.remote.request.FCMRequest;

public class AutoValueGsonTypeAdapterFactory implements TypeAdapterFactory {

    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        Class<? super T> rawType = type.getRawType();

        if (rawType.equals(FCMRequest.class)) {
            return (TypeAdapter<T>) FCMRequest.typeAdapter(gson);
        }

        return null;
    }
}