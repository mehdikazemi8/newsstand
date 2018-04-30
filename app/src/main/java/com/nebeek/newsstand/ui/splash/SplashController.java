package com.nebeek.newsstand.ui.splash;

import android.content.ActivityNotFoundException;
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
import com.nebeek.newsstand.customview.ConfirmDialog;
import com.nebeek.newsstand.data.DataRepository;
import com.nebeek.newsstand.data.local.PreferenceManager;
import com.nebeek.newsstand.ui.main.MainController;
import com.nebeek.newsstand.ui.onboarding.OnboardingController;
import com.nebeek.newsstand.util.GlobalToast;
import com.nebeek.newsstand.util.NewUpdateChecker;

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
    public void showOnboardingTopicsUI() {
        OnboardingController onboardingController = new OnboardingController();
        getRouter().replaceTopController(
                RouterTransaction.with(onboardingController)
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

        ConfirmDialog dialog = new ConfirmDialog(
                getActivity(),
                getActivity().getString(R.string.new_force_update),
                getActivity().getString(R.string.ok),
                getActivity().getString(R.string.dont_want_to_update),
                view -> redirectStore(updateUrl),
                view -> getActivity().finish()
        );
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    public void onNewUpdateAvailable(String updateUrl) {
        ConfirmDialog dialog = new ConfirmDialog(
                getActivity(),
                getActivity().getString(R.string.new_update_available),
                getActivity().getString(R.string.ok),
                getActivity().getString(R.string.lets_update_later),
                view -> redirectStore(updateUrl),
                view -> presenter.start()
        );
        dialog.setCancelable(false);
        dialog.show();
    }

    private void redirectStore(String updateUrl) {
        try {
            final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            getActivity().finish();
        } catch (ActivityNotFoundException exception) {
            final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl));
            startActivity(intent);
            getActivity().finish();
        }
    }

    @Override
    public void noUpdateStartYourFlow() {
        presenter.start();
    }
}
