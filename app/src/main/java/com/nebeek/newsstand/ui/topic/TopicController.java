package com.nebeek.newsstand.ui.topic;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bluelinelabs.conductor.RouterTransaction;
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler;
import com.nebeek.newsstand.R;
import com.nebeek.newsstand.controller.base.BaseController;
import com.nebeek.newsstand.controller.base.BaseMessageListPresenter;
import com.nebeek.newsstand.data.DataRepository;
import com.nebeek.newsstand.data.models.Topic;
import com.nebeek.newsstand.util.GlobalToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class TopicController extends BaseController implements TopicContract.View {

    @BindView(R.id.keyword_content)
    TextView keywordContent;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.messages)
    RecyclerView messages;
    @BindView(R.id.add_button)
    TextView addButton;

    private int PAGE_SIZE = 10;
    private LinearLayoutManager layoutManager;
    private TopicContract.Presenter presenter;
    private MessageViewAdapter messageViewAdapter;
    private String keyword;
    private Topic topicObject;

    public static TopicController newInstance(String keyword) {
        TopicController instance = new TopicController();
        instance.keyword = keyword;
        instance.topicObject = null;
        return instance;
    }

    public static TopicController newInstance(Topic topicObject) {

        Log.d("TAG", "newInstance " + topicObject.serialize());

        TopicController instance = new TopicController();
        instance.keyword = null;
        instance.topicObject = topicObject;
        return instance;
    }

    // todo, check and plus is hardcoded!
    private void initView() {
        if (keyword == null) {
            keyword = topicObject.getNames().get(0).getFa();

            if (topicObject.getInLibrary()) {
                addButton.setText(getResources().getString(R.string.icon_check_circle));
            } else {
                addButton.setText(getResources().getString(R.string.icon_add_circle));
            }
        }

        keywordContent.setText(keyword);

        List<Topic> topics = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            topics.add(topicObject);
        }

        // todo, send null presenter?
        messageViewAdapter = new MessageViewAdapter(topicObject, topics, this::openWebView, this::showTopicControllerUI, (BaseMessageListPresenter) presenter);
        layoutManager = new LinearLayoutManager(getActivity());
        messages.setLayoutManager(layoutManager);
        messages.setAdapter(messageViewAdapter);
        layoutManager.setStackFromEnd(true);

        messages.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (layoutManager.findFirstVisibleItemPosition() <= 4) {
                    presenter.loadOlderMessages();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private void showTopicControllerUI(Topic toOpenTopic) {
        getRouter().pushController(
                RouterTransaction.with(TopicController.newInstance(toOpenTopic))
                        .pushChangeHandler(new FadeChangeHandler())
                        .popChangeHandler(new FadeChangeHandler())
        );
    }

    @Override
    protected void onViewBound(@NonNull View view) {
        super.onViewBound(view);

        presenter = new TopicPresenter(topicObject, this, DataRepository.getInstance());

        initView();

        setActive(true);

        if (topicObject != null) {
            presenter.setTopicObject(topicObject);
        }
        presenter.start();
    }

    @Override
    protected View inflateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return inflater.inflate(R.layout.controller_search, container, false);
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void refreshMessagesList(int messagesCount, boolean scrollToEnd) {
        // todo :( must be handled in AdapterClass not here
//        if (snippetList.size() == 0 && items.size() > 0) {
//            items.add(items.size() - 1, items.get(items.size() - 1));
//        }

        messageViewAdapter.notifyItemRangeInserted(0, messagesCount);

//        Log.d("TAG", "scrollToEnd " + scrollToEnd);
//        if (scrollToEnd) {
//            messages.smoothScrollToPosition(snippetList.size() - 1);
//        }
    }

    @OnClick(R.id.back_button)
    public void backOnClick() {
        getRouter().popController(this);
    }

    @OnClick(R.id.add_button)
    public void addOnClick() {
        if (addButton.getText().toString().equals(getResources().getString(R.string.icon_add_circle))) {
            presenter.subscribeToTopic();
        } else {
            presenter.removeFromLibrary();
        }
    }

    @Override
    public void changePlusToCheck() {
        addButton.setText(getResources().getString(R.string.icon_check_circle));
    }

    @Override
    public void changeCheckToPlus() {
        addButton.setText(getResources().getString(R.string.icon_add_circle));
    }

    @Override
    public void showAddToLibraryError() {
        GlobalToast.showError(getActivity());
    }
}
