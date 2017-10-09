package com.nebeek.newsstand;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Conductor;


import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;
import com.google.auto.value.AutoValue;
import com.nebeek.newsstand.data.DataRepository;
import com.nebeek.newsstand.data.DataSource;
import com.nebeek.newsstand.data.local.LocalDataSource;
import com.nebeek.newsstand.data.local.PreferenceManager;
import com.nebeek.newsstand.data.models.Comment;
import com.nebeek.newsstand.data.remote.RemoteDataSource;
import com.nebeek.newsstand.ui.splash.SplashController;
import com.nebeek.newsstand.util.NetworkHelper;

public class MainActivity extends AppCompatActivity {
    private Router router;

    private void init() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        DataRepository.init(
                RemoteDataSource.getInstance(PreferenceManager.getInstance(this)),
                new LocalDataSource(),
                new NetworkHelper(this)
        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        pushRootController(savedInstanceState);
    }

    private void pushRootController(Bundle savedInstanceState) {
        ViewGroup container = (ViewGroup) findViewById(R.id.controller_container);
        router = Conductor.attachRouter(this, container, savedInstanceState);
        if (!router.hasRootController()) {
            router.setRoot(RouterTransaction.with(new SplashController()));
        }
    }

    @Override
    public void onBackPressed() {
        if (!router.handleBack()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        router.onActivityResult(requestCode, resultCode, data);
    }
}
