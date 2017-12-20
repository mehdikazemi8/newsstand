package com.nebeek.newsstand.ui.explore;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.RouterTransaction;
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler;
import com.nebeek.newsstand.R;
import com.nebeek.newsstand.controller.base.BaseBackStackController;
import com.nebeek.newsstand.data.DataRepository;
import com.nebeek.newsstand.data.models.Snippet;
import com.nebeek.newsstand.ui.topic.SnippetViewAdapter;
import com.nebeek.newsstand.ui.webview.WebViewController;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ExploreController extends BaseBackStackController implements ExploreContract.View {

    @BindView(R.id.messages)
    RecyclerView messages;

    private List<Snippet> messageList = new ArrayList<>();
    private SnippetViewAdapter snippetViewAdapter;
    private ExploreContract.Presenter presenter;

    public static ExploreController getInstance() {
        return new ExploreController();
    }

    @Override
    public boolean canHandleBackStack() {
        return false;
    }

    @Override
    protected View inflateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return inflater.inflate(R.layout.controller_explore, container, false);
    }

    @Override
    protected void onViewBound(@NonNull View view) {
        super.onViewBound(view);

        init();

        presenter = new ExplorePresenter(this, DataRepository.getInstance());
        presenter.start();
    }

    private void init() {
        snippetViewAdapter = new SnippetViewAdapter(messageList, this::openWebView);
        messages.setLayoutManager(new LinearLayoutManager(getActivity()));
        messages.setAdapter(snippetViewAdapter);
    }

    @Override
    public void showMessages(List<Snippet> messageList) {
        this.messageList.clear();
        this.messageList.addAll(messageList);
        snippetViewAdapter.notifyDataSetChanged();
    }
}
