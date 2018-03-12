package com.nebeek.newsstand.controller;

import com.nebeek.newsstand.controller.base.BaseView;

public interface BaseMessageView extends BaseView {
    void openTelegramWithSpecificUrl(String url);
}
