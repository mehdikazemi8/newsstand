package com.nebeek.newsstand.ui.temp;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nebeek.newsstand.R;
import com.nebeek.newsstand.controller.base.BaseBackStackController;

public class TempController extends BaseBackStackController {

    @Override
    public boolean canHandleBackStack() {
        return false;
    }

    @Override
    protected View inflateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return inflater.inflate(R.layout.controller_explore, container, false);
    }
}
