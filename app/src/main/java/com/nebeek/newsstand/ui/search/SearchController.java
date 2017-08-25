package com.nebeek.newsstand.ui.search;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.nebeek.newsstand.R;
import com.nebeek.newsstand.controller.base.BaseController;
import com.nebeek.newsstand.data.DataRepository;
import com.nebeek.newsstand.data.models.Snippet;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SearchController extends BaseController implements SearchContract.View {

    @BindView(R.id.search_view)
    SearchView searchView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.snippets)
    RecyclerView snippets;

    private List<Snippet> snippetList = new ArrayList<>();
    private SearchContract.Presenter presenter;
    private SnippetViewAdapter snippetViewAdapter;

    public static SearchController newInstance() {
        return new SearchController();
    }

    private void initView() {
        snippetViewAdapter = new SnippetViewAdapter(snippetList);
        snippets.setLayoutManager(new LinearLayoutManager(getActivity()));
        snippets.setAdapter(snippetViewAdapter);
    }

    @Override
    protected void onViewBound(@NonNull View view) {
        super.onViewBound(view);

        initView();

        setActive(true);
        presenter = new SearchPresenter(this, DataRepository.getInstance());
        searchView.setOnQueryTextListener(presenter);
        presenter.start();
    }

    @Override
    protected View inflateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return inflater.inflate(R.layout.controller_search, container, false);
    }

    @Override
    public void focusOnSearchView() {
        // todo: doesn't work
        searchView.focusSearch(View.FOCUS_DOWN);
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
        snippetViewAdapter.notifyDataSetChanged();
    }
}
