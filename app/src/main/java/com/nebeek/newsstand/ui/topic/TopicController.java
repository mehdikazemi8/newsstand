package com.nebeek.newsstand.ui.topic;

import android.content.Intent;
import android.net.Uri;
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
import com.nebeek.newsstand.customview.SquareImageView;
import com.nebeek.newsstand.data.DataRepository;
import com.nebeek.newsstand.data.models.Topic;
import com.nebeek.newsstand.data.remote.ApiService;
import com.nebeek.newsstand.util.GlobalToast;
import com.nebeek.newsstand.util.imagehandler.GlideApp;

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
    @BindView(R.id.photo)
    SquareImageView photo;

    private int PAGE_SIZE = 10;
    private LinearLayoutManager layoutManager;
    private TopicContract.Presenter presenter;
    private MessageViewAdapter messageViewAdapter;
    private String keyword;
    private Topic topicObject;
    private List<Topic> relatedTopics;

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

        try {
            GlideApp.with(getActivity()).load(ApiService.BASE_URL + topicObject.getImages().get(0).getImages().get(0).getData())
                    .placeholder(R.drawable.loading_circle)
                    .circleCrop()
                    .into(photo);
        } catch (Exception e) {
            GlideApp.with(getActivity()).load("")
                    .placeholder(R.drawable.loading_circle)
                    .circleCrop()
                    .into(photo);
        }

        keywordContent.setText(keyword);

        // todo, send null presenter?
        messageViewAdapter = new MessageViewAdapter(topicObject, relatedTopics, this::openWebView, this::showTopicControllerUI, (BaseMessageListPresenter) presenter);
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

        if (topicObject == null || topicObject.getRelations() == null) {
            continueAfterRelatedTopics();
        } else {
            List<List<Object>> request = new ArrayList<>();
            for (String id : topicObject.getRelations()) {
                List<Object> push = new ArrayList<>();
                push.add(id);
                push.add(0);
                request.add(push);
            }
            presenter.fetchRelatedTopics(request);
        }
    }

    private void continueAfterRelatedTopics() {
        initView();

        setActive(true);

        if (topicObject != null) {
            presenter.setTopicObject(topicObject);
        }
        presenter.start();
    }

    @Override
    protected View inflateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return inflater.inflate(R.layout.controller_topic, container, false);
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        try {
            progressBar.setVisibility(View.INVISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void refreshMessagesList(int messagesCount, boolean pushedAtEnd) {
        // todo :( must be handled in AdapterClass not here
//        if (snippetList.size() == 0 && items.size() > 0) {
//            items.add(items.size() - 1, items.get(items.size() - 1));
//        }

        messageViewAdapter.notifyItemRangeInserted(0, messagesCount);
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

    @Override
    public void refreshRelatedTopics(List<Topic> topicList) {
        relatedTopics = topicList;
        continueAfterRelatedTopics();
    }

    @Override
    public void openTelegramWithSpecificUrl(String url) {
        try {
            final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            intent.setPackage("org.telegram.messenger");
            startActivity(intent);
        } catch (Exception e) {
            final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        }
    }
}
