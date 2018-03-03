package com.nebeek.newsstand.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

public class NewUpdateChecker {

    public static final String MINIMUM_SUPPORT_VERSION = "minimum_support_version";
    public static final String CURRENT_VERSION = "current_version";

    private OnUpdateNeededListener onUpdateNeededListener;
    private Context context;

    public interface OnUpdateNeededListener {
        void onUpdateNeeded(String updateUrl);

        void onNewUpdateAvailable(String updateUrl);
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
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            int thisVersion = packageInfo.versionCode;
            if (thisVersion < remoteConfig.getLong(MINIMUM_SUPPORT_VERSION)) {
                onUpdateNeededListener.onUpdateNeeded("https://cafebazaar.ir/app/com.bama.consumer/?l=fa");
            } else if (thisVersion < remoteConfig.getLong(CURRENT_VERSION)) {
                onUpdateNeededListener.onNewUpdateAvailable("https://cafebazaar.ir/app/com.bama.consumer/?l=fa");
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