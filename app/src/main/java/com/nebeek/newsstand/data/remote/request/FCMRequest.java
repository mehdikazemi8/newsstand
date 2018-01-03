package com.nebeek.newsstand.data.remote.request;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.nebeek.newsstand.data.models.BaseModel;

@AutoValue
public abstract class FCMRequest extends BaseModel {
    public abstract String instanceId();

    @Nullable
    public abstract String type();

    @Nullable
    public abstract String uniqueID();

    @Nullable
    public abstract Boolean sendToServer();

    public static Builder builder() {
        return new AutoValue_FCMRequest.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder instanceId(String fcm);

        public abstract Builder sendToServer(Boolean sendToServer);

        public abstract Builder type(String type);

        public abstract Builder uniqueID(String uniqueID);

        public abstract FCMRequest build();
    }

    public static TypeAdapter<FCMRequest> typeAdapter(Gson gson) {
        return new AutoValue_FCMRequest.GsonTypeAdapter(gson);
    }
}
