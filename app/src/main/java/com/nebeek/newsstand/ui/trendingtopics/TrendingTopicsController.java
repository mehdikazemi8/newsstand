package com.nebeek.newsstand.ui.trendingtopics;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anton46.collectionitempicker.CollectionPicker;
import com.anton46.collectionitempicker.Item;
import com.nebeek.newsstand.R;
import com.nebeek.newsstand.controller.base.BaseController;
import com.nebeek.newsstand.data.DataRepository;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class TrendingTopicsController extends BaseController implements TrendingTopicsContract.View {

    @BindView(R.id.topic_item_picker)
    CollectionPicker topicItemPicker;

    TrendingTopicsContract.Presenter presenter;

    @Override
    protected View inflateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return inflater.inflate(R.layout.controller_trending_topics, container, false);
    }

    @Override
    protected void onViewBound(@NonNull View view) {
        super.onViewBound(view);

        initView();

        presenter = new TrendingTopicsPresenter(this, DataRepository.getInstance());
        presenter.start();
    }

    private void initView() {
        List<Item> items = new ArrayList<>();
        items.add(new Item("item1", "پرسپولیس"));
        items.add(new Item("item1", "استقلال"));
        items.add(new Item("item1", "رئال مادرید"));
        items.add(new Item("item1", "بارسلونا"));
        items.add(new Item("item1", "اتلتیکومادرید"));
        items.add(new Item("item1", "منچستریونایتد"));
        items.add(new Item("item1", "منچسترسیتی"));
        items.add(new Item("item1", "چلسی"));
        items.add(new Item("item1", "آرسنال"));
        items.add(new Item("item1", "لیورپول"));
        items.add(new Item("item1", "تاتنهام"));
        items.add(new Item("item1", "بایرن مونیخ"));
        items.add(new Item("item1", "بروسیادورتموند"));


        topicItemPicker.setItems(items);
        topicItemPicker.setOnItemClickListener((item, position) -> {
//            topicItemPicker.setItems(new ArrayList<>());

            new Handler().postDelayed(() -> {
                List<Item> items222 = new ArrayList<>();
                items222.add(new Item("item1", "پرسپولیس"));
                items222.add(new Item("item1", "استقلال"));
                items222.add(new Item("item1", "رئال مادرید"));
                items222.add(new Item("item1", "بارسلونا"));
                items222.add(new Item("item1", "اتلتیکومادرید"));
                items222.add(new Item("item1", "منچستریونایتد"));
                items222.add(new Item("item1", "منچسترسیتی"));
                items222.add(new Item("item1", "چلسی"));
                items222.add(new Item("item1", "آرسنال"));
                items222.add(new Item("item1", "لیورپول"));
                items222.add(new Item("item1", "تاتنهام"));
                items222.add(new Item("item1", "بایرن مونیخ"));
                items222.add(new Item("item1", "بروسیادورتموند"));
                topicItemPicker.setItems(items222);
            }, 1000);
        });

    }
}
