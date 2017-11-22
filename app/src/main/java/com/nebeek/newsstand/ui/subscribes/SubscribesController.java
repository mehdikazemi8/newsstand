package com.nebeek.newsstand.ui.subscribes;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bluelinelabs.conductor.RouterTransaction;
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler;
import com.nebeek.newsstand.R;
import com.nebeek.newsstand.controller.base.BaseBackStackController;
import com.nebeek.newsstand.data.DataRepository;
import com.nebeek.newsstand.data.models.Topic;
import com.nebeek.newsstand.ui.topic.TopicController;
import com.nebeek.newsstand.util.listener.OnItemSelectedListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SubscribesController extends BaseBackStackController implements SubscribesContract.View {

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.topics)
    RecyclerView topics;

    private List<Topic> topicList = new ArrayList<>();
    private TopicViewAdapter adapter;
    private SubscribesContract.Presenter presenter;

    private void init() {

//        RxBus.getInstance().getEvents().subscribe(new NewSubscription())

        topics.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new TopicViewAdapter(topicList, onItemSelectedListener);
        topics.setAdapter(adapter);
    }

    private OnItemSelectedListener<Topic> onItemSelectedListener = new OnItemSelectedListener<Topic>() {
        @Override
        public void onSelect(Topic topic) {
            presenter.onKeywordSelected(topic);
        }

        @Override
        public void onDeselect(Topic object) {

        }
    };

    @Override
    protected void onViewBound(@NonNull View view) {
        super.onViewBound(view);

        init();

        setActive(true);
        presenter = new SubscribesPresenter(DataRepository.getInstance(), this);
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

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showSubscribes(List<Topic> topicList) {
        if (topicList == null) {
            return;
        }

        this.topicList.clear();
        this.topicList.addAll(topicList);
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public void showSearchUI(Topic topic) {
        getParentController().getRouter().pushController(
                RouterTransaction.with(TopicController.newInstance(topic))
                        .pushChangeHandler(new FadeChangeHandler())
                        .popChangeHandler(new FadeChangeHandler())
        );
    }
}
