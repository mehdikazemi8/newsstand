package com.nebeek.newsstand.ui.splash;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bluelinelabs.conductor.RouterTransaction;
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler;
import com.nebeek.newsstand.R;
import com.nebeek.newsstand.controller.base.BaseController;
import com.nebeek.newsstand.data.DataRepository;
import com.nebeek.newsstand.data.local.PreferenceManager;
import com.nebeek.newsstand.ui.main.MainController;
import com.nebeek.newsstand.ui.trendingtopics.TrendingTopicsController;
import com.nebeek.newsstand.util.NewUpdateChecker;
import com.nebeek.newsstand.util.GlobalToast;

import butterknife.BindView;

public class SplashController extends BaseController implements SplashContract.View,
        NewUpdateChecker.OnUpdateNeededListener {

    @BindView(R.id.text)
    TextView text;

    private SplashContract.Presenter presenter;

    @Override
    protected View inflateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return inflater.inflate(R.layout.controller_splash, container, false);
    }

    @Override
    protected void onViewBound(@NonNull View view) {
        super.onViewBound(view);

        setActive(true);
        presenter = new SplashPresenter(DataRepository.getInstance(), PreferenceManager.getInstance(getActivity()), this);

        NewUpdateChecker.with(getActivity()).onUpdateNeeded(this).check();
    }

    @Override
    public void showMainPageUI() {
        MainController mainController = new MainController();
        getRouter().setRoot(
                RouterTransaction.with(mainController)
                        .pushChangeHandler(new FadeChangeHandler())
                        .popChangeHandler(new FadeChangeHandler())
        );
    }

    @Override
    public void showTrendingTopicsUI() {
        TrendingTopicsController trendingTopicsController = new TrendingTopicsController();
        getRouter().replaceTopController(
                RouterTransaction.with(trendingTopicsController)
                        .pushChangeHandler(new FadeChangeHandler())
                        .popChangeHandler(new FadeChangeHandler())
        );
    }

    @Override
    public void showNetworkFailureError() {
        GlobalToast.showNetworkFailureError(getActivity());
    }

    @Override
    public void onUpdateNeeded(String updateUrl) {
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle("آپدیت جدید اومده")
                .setMessage("برای ادامه باید آپدیت کنی وگرنه اپلیکیشن کار نمیکنه :(")
                .setPositiveButton("اوکی", (dialogInterface, i) -> {
                    redirectStore(updateUrl);
                }).setNegativeButton("نمی خوام", (dialogInterface, i) -> {
                    getActivity().finish();
                }).create();
        dialog.show();
    }

    @Override
    public void onNewUpdateAvailable(String updateUrl) {
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle("آپدیت جدید اومده")
                .setMessage("نظرت چیه بریم برای آپدیت؟")
                .setPositiveButton("اوکی", (dialogInterface, i) -> {
                    redirectStore(updateUrl);
                }).setNegativeButton("بی خیال بذا بعدا", (dialogInterface, i) -> {
                    presenter.start();
                }).create();
        dialog.show();
    }

    private void redirectStore(String updateUrl) {
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
