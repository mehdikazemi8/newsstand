package com.nebeek.newsstand;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;

import com.annimon.stream.Stream;
import com.bluelinelabs.conductor.Conductor;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;
import com.nebeek.newsstand.data.DataRepository;
import com.nebeek.newsstand.data.local.AppDatabase;
import com.nebeek.newsstand.data.local.LocalDataSource;
import com.nebeek.newsstand.data.local.PreferenceManager;
import com.nebeek.newsstand.data.models.TelegramMessage;
import com.nebeek.newsstand.data.remote.RemoteDataSource;
import com.nebeek.newsstand.ui.splash.SplashController;
import com.nebeek.newsstand.util.NetworkHelper;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Router router;

    private void init() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        DataRepository.init(
                AppDatabase.getInMemoryDatabase(this),
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

//        List<TelegramMessage> all = AppDatabase.getInMemoryDatabase(this).messageModel().fetchAll();
//        Log.d("TAG, ", "likedCount all size " + all.size());
//        int likeCount = Stream.of(all).map(TelegramMessage::getLiked).filter(value -> value != null && value.equals(true)).toList().size();
//        Log.d("TAG, ", "likedCount " + likeCount);
//
//        for (TelegramMessage message : all) {
//            Log.d("TAG, ", "likedCount " + message.getId() + " " + message.getLiked() + " " + (message.getLiked() == null));
//        }

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
