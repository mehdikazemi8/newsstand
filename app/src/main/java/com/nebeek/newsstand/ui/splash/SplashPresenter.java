package com.nebeek.newsstand.ui.splash;

import android.os.Handler;

import com.nebeek.newsstand.data.DataRepository;
import com.nebeek.newsstand.data.DataSource;
import com.nebeek.newsstand.data.local.PreferenceManager;
import com.nebeek.newsstand.data.models.User;
import com.nebeek.newsstand.data.remote.response.TokenResponse;

public class SplashPresenter implements SplashContract.Presenter {

    private SplashContract.View splashView;
    private PreferenceManager preferenceManager;
    private DataRepository dataRepository;

    public SplashPresenter(DataRepository dataRepository, PreferenceManager preferenceManager, SplashContract.View splashView) {
        this.dataRepository = dataRepository;
        this.preferenceManager = preferenceManager;
        this.splashView = splashView;
    }

    @Override
    public void start() {
        Handler handler = new Handler();
        handler.postDelayed(() -> doFakeRegisterIfNeeded(), 1000);
    }

    private void doFakeRegisterIfNeeded() {
        String authorization = preferenceManager.getAuthorization();
        if (authorization == null) {
            User tempUser = new User("abcdABCD@-23");

            dataRepository.registerNewUser(tempUser, new DataSource.RegisterCallback() {
                @Override
                public void onSuccess(User user) {

                    user.setPassword(tempUser.getPassword());
                    preferenceManager.putUser(user);
//                    preferenceManager.putTokenResponse(tokenResponse);
//                    dataRepository.prepareDataSource();

                    authenticateUser(user);

//                    splashView.showMainPageUI();
                }

                @Override
                public void onFailure() {
//                    splashView.showMainPageUI();
                }

                @Override
                public void onNetworkFailure() {
                    splashView.showNetworkFailureError();
                }
            });
        } else {
            splashView.showMainPageUI();
        }
    }

    private void authenticateUser(User user) {

        dataRepository.authenticateUser(user, new DataSource.AuthenticateCallback() {
            @Override
            public void onResponse(TokenResponse tokenResponse) {
                preferenceManager.putTokenResponse(tokenResponse);
                dataRepository.prepareDataSource();
                splashView.showMainPageUI();
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
