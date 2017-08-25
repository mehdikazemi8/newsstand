package com.nebeek.newsstand.controller.base;

import android.widget.TextView;


import com.nebeek.newsstand.R;

import butterknife.BindView;
import butterknife.OnClick;

public abstract class HeaderController extends BaseController {

    @BindView(R.id.template_header_left_back_button)
    public TextView headerLeftBackButton;

    @BindView(R.id.template_header_back_button)
    public TextView headerBackButton;

    @OnClick(R.id.template_header_back_button)
    public void backOnClick() {
        getRouter().popController(this);
    }

    @OnClick(R.id.template_header_left_back_button)
    public void leftBackOnClick() {
        getRouter().popController(this);
    }
}
