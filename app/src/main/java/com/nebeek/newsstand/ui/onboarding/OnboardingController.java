package com.nebeek.newsstand.ui.onboarding;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.anton46.collectionitempicker.CollectionPicker;
import com.anton46.collectionitempicker.Item;
import com.bluelinelabs.conductor.RouterTransaction;
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler;
import com.nebeek.newsstand.R;
import com.nebeek.newsstand.controller.base.BaseController;
import com.nebeek.newsstand.data.DataRepository;
import com.nebeek.newsstand.data.models.Topic;
import com.nebeek.newsstand.ui.main.MainController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class OnboardingController extends BaseController implements OnboardingContract.View {

    @BindView(R.id.topic_item_picker)
    CollectionPicker topicItemPicker;
    @BindView(R.id.next_page)
    Button nextPage;
    @BindView(R.id.page_number)
    TextView pageNumber;
    @BindView(R.id.khob)
    Button khob;
    @BindView(R.id.info)
    TextView info;

    private int currentWordIdx = 0;
    private String[] words;

    @OnClick(R.id.khob)
    public void onKhobClick() {
        presenter.fetchNextInfo();
    }

    @OnClick(R.id.next_page)
    public void nextPageOnClick() {
        topicItemPicker.setItems(new ArrayList<>());
        topicItemPicker.drawItemView();
        presenter.fetchNextPage(Stream.of(topicItemPicker.getCheckedItems().entrySet()).map(Map.Entry::getKey).toList());
    }

    OnboardingContract.Presenter presenter;

    @Override
    protected View inflateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return inflater.inflate(R.layout.controller_onboarding_topics, container, false);
    }

    @Override
    protected void onViewBound(@NonNull View view) {
        super.onViewBound(view);

        presenter = new OnboardingPresenter(
                this,
                DataRepository.getInstance(),
                getActivity().getResources().getStringArray(R.array.onboarding_info)
        );
        presenter.start();
    }

    @Override
    public void showOnboardingTopics(List<Topic> topics) {
        nextPage.setVisibility(View.VISIBLE);

        List<Item> onboardingTopicItems = new ArrayList<>();
        for (Topic topic : topics) {
            onboardingTopicItems.add(new Item(topic.getId(), topic.getNames().get(0).getFa()));
        }

        topicItemPicker.setItems(onboardingTopicItems);
        topicItemPicker.drawItemView();
//        topicItemPicker.setOnItemClickListener((item, position) -> {
//
//        });
    }

    @Override
    public void setPageNumber(int pageNumberInt, int allPagesCount) {
        pageNumber.setText(getActivity().getString(R.string.page_number, pageNumberInt, allPagesCount));
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
    public void showInfo(String infoStr) {
        khob.setVisibility(View.INVISIBLE);
        info.setText("");
        info.setVisibility(View.VISIBLE);
        currentWordIdx = 0;
        words = infoStr.split(" ");
        showNextWord();
    }

    private void showNextWord() {
        if (currentWordIdx == words.length) {
            khob.setVisibility(View.VISIBLE);
        } else {
            info.setText(getActivity().getString(R.string.info_template, info.getText().toString(), words[currentWordIdx++]));
            new Handler().postDelayed(this::showNextWord, 200);
        }
    }

    @Override
    public void makeInfoAndKhobInvisible() {
        info.setVisibility(View.INVISIBLE);
        khob.setVisibility(View.INVISIBLE);
    }
}
