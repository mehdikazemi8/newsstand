package com.nebeek.newsstand.ui.search;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nebeek.newsstand.R;
import com.nebeek.newsstand.controller.base.BaseController;
import com.nebeek.newsstand.data.DataRepository;
import com.nebeek.newsstand.data.models.Keyword;
import com.nebeek.newsstand.data.models.Snippet;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SearchController extends BaseController implements SearchContract.View {

    @BindView(R.id.keyword_content)
    TextView keywordContent;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.snippets)
    RecyclerView snippets;
    @BindView(R.id.add_button)
    TextView addButton;

    private List<Snippet> snippetList = new ArrayList<>();
    private SearchContract.Presenter presenter;
    private SnippetViewAdapter snippetViewAdapter;
    private String keyword;
    private Keyword keywordObject;

    public static SearchController newInstance(String keyword) {
        SearchController instance = new SearchController();
        instance.keyword = keyword;
        instance.keywordObject = null;
        return instance;
    }

    public static SearchController newInstance(Keyword keywordObject) {
        SearchController instance = new SearchController();
        instance.keyword = null;
        instance.keywordObject = keywordObject;
        return instance;
    }

    // todo, check and plus is hardcoded!
    private void initView() {
        if (keyword == null) {
            keyword = keywordObject.getContent();

            if (keywordObject.getInLibrary()) {
                addButton.setText("check");
            } else {
                addButton.setText("plus");
            }
        }

        keywordContent.setText(keyword);
        snippetViewAdapter = new SnippetViewAdapter(snippetList);
        snippets.setLayoutManager(new LinearLayoutManager(getActivity()));
        snippets.setAdapter(snippetViewAdapter);
    }

    @Override
    protected void onViewBound(@NonNull View view) {
        super.onViewBound(view);

        initView();

        setActive(true);
        presenter = new SearchPresenter(keyword, this, DataRepository.getInstance());

        if(keywordObject != null) {
            presenter.setKeywordObject(keywordObject);
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
    public void showSearchResults(List<Snippet> snippetList) {
        this.snippetList.clear();
        this.snippetList.addAll(snippetList);
        this.snippetViewAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.back_button)
    public void backOnClick() {
        getRouter().popController(this);
    }

    @OnClick(R.id.add_button)
    public void addOnClick() {
        if (addButton.getText().toString().equals("plus")) {
            presenter.addToLibrary();
        } else {
            presenter.removeFromLibrary();
        }
    }

    @Override
    public void changePlusToCheck() {
        addButton.setText("check");
    }

    @Override
    public void changeCheckToPlus() {
        addButton.setText("plus");
    }
}
