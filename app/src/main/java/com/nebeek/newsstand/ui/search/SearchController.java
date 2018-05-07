package com.nebeek.newsstand.ui.search;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.bluelinelabs.conductor.RouterTransaction;
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler;
import com.nebeek.newsstand.R;
import com.nebeek.newsstand.controller.base.BaseController;
import com.nebeek.newsstand.data.DataRepository;
import com.nebeek.newsstand.data.models.Topic;
import com.nebeek.newsstand.ui.subscribes.TopicViewAdapter;
import com.nebeek.newsstand.ui.topic.TopicController;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnTextChanged;

public class SearchController extends BaseController implements SearchContract.View {

    private String lastQuery = "";
    private SearchContract.Presenter presenter;

    @BindView(R.id.search_input)
    EditText searchInput;
    @BindView(R.id.suggestions)
    RecyclerView suggestions;

    private TopicViewAdapter topicViewAdapter;
    private List<Topic> topicList = new ArrayList<>();

    @OnTextChanged(R.id.search_input)
    public void searchInputOnTextChanged() {
        String newText = searchInput.getText().toString();
        if (newText.equals(lastQuery)) {
            return;
        }

        lastQuery = newText;
        presenter.getTopics(newText);
    }

    public static SearchController newInstance() {
        return new SearchController();
    }

    @Override
    protected View inflateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return inflater.inflate(R.layout.controller_search, container, false);
    }

    @Override
    protected void onViewBound(@NonNull View view) {
        super.onViewBound(view);

        presenter = new SearchPresenter(this, DataRepository.getInstance());
        initView();
        presenter.start();
    }

    private void initView() {
        topicViewAdapter = new TopicViewAdapter(
                topicList,
                topic -> presenter.onSuggestionClicked(topic),
                R.layout.template_topic,
                false
        );
        suggestions.setLayoutManager(new LinearLayoutManager(getActivity()));
        suggestions.setAdapter(topicViewAdapter);
    }

    @Override
    public void focusOnSearchInput() {
        try {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(searchInput, InputMethodManager.SHOW_IMPLICIT);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showSuggestions(List<Topic> suggestions) {
        topicList.clear();
        topicList.addAll(suggestions);
        topicViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void clearSuggestions() {
        topicList.clear();
        topicViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void showTopicUI(Topic topic) {
        getRouter().pushController(
                RouterTransaction.with(TopicController.newInstance(topic))
                        .pushChangeHandler(new FadeChangeHandler())
                        .popChangeHandler(new FadeChangeHandler())
        );
    }
}
