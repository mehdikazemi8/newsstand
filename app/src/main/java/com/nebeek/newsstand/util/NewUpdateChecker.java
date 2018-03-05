package com.nebeek.newsstand.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

public class NewUpdateChecker {

    public static final String MINIMUM_SUPPORT_VERSION = "minimum_support_version";
    public static final String CURRENT_VERSION = "current_version";
    public static final String UPDATE_URL = "update_url";

    private OnUpdateNeededListener onUpdateNeededListener;
    private Context context;

    public interface OnUpdateNeededListener {
        void onUpdateNeeded(String updateUrl);

        void onNewUpdateAvailable(String updateUrl);

        void noUpdateStartYourFlow();
    }

    public static Builder with(@NonNull Context context) {
        return new Builder(context);
    }

    public NewUpdateChecker(@NonNull Context context,
                            OnUpdateNeededListener onUpdateNeededListener) {
        this.context = context;
        this.onUpdateNeededListener = onUpdateNeededListener;
    }

    public void check() {
        final FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();

        try {
            Log.d("TAG", "remote config " + remoteConfig.getLong(MINIMUM_SUPPORT_VERSION));
            Log.d("TAG", "remote config " + remoteConfig.getLong(CURRENT_VERSION));
            Log.d("TAG", "remote config " + remoteConfig.getString(UPDATE_URL));

            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            int thisVersion = packageInfo.versionCode;
            if (thisVersion < remoteConfig.getLong(MINIMUM_SUPPORT_VERSION)) {
                onUpdateNeededListener.onUpdateNeeded(remoteConfig.getString(UPDATE_URL));
                // todo remove
            } else if (thisVersion < remoteConfig.getLong(CURRENT_VERSION)) {
                onUpdateNeededListener.onNewUpdateAvailable(remoteConfig.getString(UPDATE_URL));
            } else {
                onUpdateNeededListener.noUpdateStartYourFlow();
            }
        } catch (PackageManager.NameNotFoundException e) {

        }
    }

    public static class Builder {
        private Context context;
        private OnUpdateNeededListener onUpdateNeededListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder onUpdateNeeded(OnUpdateNeededListener onUpdateNeededListener) {
            this.onUpdateNeededListener = onUpdateNeededListener;
            return this;
        }

        public NewUpdateChecker build() {
            return new NewUpdateChecker(context, onUpdateNeededListener);
        }

        public NewUpdateChecker check() {
            NewUpdateChecker newUpdateChecker = build();
            newUpdateChecker.check();
            return newUpdateChecker;
        }
    }
}