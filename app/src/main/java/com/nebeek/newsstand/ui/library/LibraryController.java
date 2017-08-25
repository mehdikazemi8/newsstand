package com.nebeek.newsstand.ui.library;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nebeek.newsstand.R;
import com.nebeek.newsstand.controller.base.BaseBackStackController;

public class LibraryController extends BaseBackStackController implements LibraryContract.View {

    private LibraryContract.Presenter presenter;

    @Override
    protected void onViewBound(@NonNull View view) {
        super.onViewBound(view);

        setActive(true);
        presenter = new LibraryPresenter();
        presenter.start();
    }

    @Override
    protected View inflateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return inflater.inflate(R.layout.controller_library, container, false);
    }

    @Override
    public boolean canHandleBackStack() {
        return false;
    }
}
