package com.nebeek.newsstand.fcm;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.nebeek.newsstand.data.DataRepository;
import com.nebeek.newsstand.data.DataSource;
import com.nebeek.newsstand.data.local.PreferenceManager;
import com.nebeek.newsstand.data.remote.request.FCMRequest;

public class FirebaseIDService extends FirebaseInstanceIdService {
    private static final String TAG = "FirebaseIDService";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // TODO: Implement this method to send any registration to your app's servers.
        sendRegistrationToServer(refreshedToken);
    }

    /**
     * Persist token to third-party servers.
     * <p>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.

        PreferenceManager preferenceManager = PreferenceManager.getInstance(this);
        FCMRequest fcmRequest = preferenceManager.getFcmID();
        if (fcmRequest == null || !token.equals(fcmRequest.fcmID())) { // must send to server

            if (preferenceManager.getTokenResponse() == null) {
                // save and send later
                fcmRequest = FCMRequest.builder().fcmID(token).sendToServer(true).build();
                preferenceManager.putFcmID(fcmRequest);
            } else {
                // save and send now
                fcmRequest = FCMRequest.builder().fcmID(token).sendToServer(false).build();
                preferenceManager.putFcmID(fcmRequest);

                DataRepository.getInstance().sendFcmIDToServer(token, new DataSource.SendFcmIDCallback() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "Refreshed token hhhh: " + token);
                    }

                    @Override
                    public void onFailure() {

                    }

                    @Override
                    public void onNetworkFailure() {

                    }
                });
            }
        }
    }
}