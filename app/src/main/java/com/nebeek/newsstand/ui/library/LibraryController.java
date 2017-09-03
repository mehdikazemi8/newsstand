package com.nebeek.newsstand.ui.library;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bluelinelabs.conductor.RouterTransaction;
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler;
import com.nebeek.newsstand.R;
import com.nebeek.newsstand.controller.base.BaseBackStackController;
import com.nebeek.newsstand.data.DataRepository;
import com.nebeek.newsstand.data.models.Keyword;
import com.nebeek.newsstand.ui.search.SearchController;
import com.nebeek.newsstand.util.listener.OnItemSelectedListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class LibraryController extends BaseBackStackController implements LibraryContract.View {

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.keywords)
    RecyclerView keywords;

    private List<Keyword> keywordList = new ArrayList<>();
    private KeywordViewAdapter adapter;
    private LibraryContract.Presenter presenter;

    private void init() {
        keywords.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        adapter = new KeywordViewAdapter(keywordList, onItemSelectedListener);
        keywords.setAdapter(adapter);
    }

    private OnItemSelectedListener<Keyword> onItemSelectedListener = new OnItemSelectedListener<Keyword>() {
        @Override
        public void onSelect(Keyword keyword) {
            presenter.onKeywordSelected(keyword);
        }

        @Override
        public void onDeselect(Keyword object) {

        }
    };

    @Override
    protected void onViewBound(@NonNull View view) {
        super.onViewBound(view);

        init();

        setActive(true);
        presenter = new LibraryPresenter(DataRepository.getInstance(), this);
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
    public void showKeywords(List<Keyword> keywordList) {
        Log.d("TAG", "abcd " + keywordList.size());
        this.keywordList.clear();
        this.keywordList.addAll(keywordList);
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public void showSearchUI(Keyword keyword) {
        getParentController().getRouter().pushController(
                RouterTransaction.with(SearchController.newInstance(keyword))
                        .pushChangeHandler(new FadeChangeHandler())
                        .popChangeHandler(new FadeChangeHandler())
        );
    }
}
