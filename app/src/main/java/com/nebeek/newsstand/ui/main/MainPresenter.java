package com.nebeek.newsstand.ui.main;


import android.util.Log;

import com.nebeek.newsstand.data.DataRepository;
import com.nebeek.newsstand.data.DataSource;
import com.nebeek.newsstand.data.local.PreferenceManager;

public class MainPresenter implements MainContract.Presenter {
    private PreferenceManager preferenceManager;
    private DataRepository dataRepository;
    private MainContract.View mainView;

    public MainPresenter(PreferenceManager preferenceManager, DataRepository dataRepository, MainContract.View mainView) {
        this.preferenceManager = preferenceManager;
        this.dataRepository = dataRepository;
        this.mainView = mainView;
    }

    @Override
    public void start() {
        sendFCMToServer();
    }

    private void sendFCMToServer() {
        if (preferenceManager.getFcmID() == null) {
            return;
        }

        String token = preferenceManager.getFcmID().instanceId();
        DataRepository.getInstance().sendFcmIDToServer(token, new DataSource.SendFcmIDCallback() {
            @Override
            public void onSuccess() {
                Log.d("TAG", "Refreshed token hhhh: " + token);
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
