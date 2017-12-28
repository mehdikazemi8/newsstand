package com.nebeek.newsstand.ui.explore;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nebeek.newsstand.R;
import com.nebeek.newsstand.controller.base.BaseBackStackController;
import com.nebeek.newsstand.data.DataRepository;
import com.nebeek.newsstand.data.models.Snippet;
import com.nebeek.newsstand.ui.topic.SnippetViewAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;

public class ExploreController extends BaseBackStackController implements ExploreContract.View {

    @BindView(R.id.messages)
    RecyclerView messages;

    private List<Snippet> messageList = new ArrayList<>();
    private SnippetViewAdapter snippetViewAdapter;
    private ExploreContract.Presenter presenter;
    private boolean isLoading;

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
        isLoading = false;
        snippetViewAdapter = new SnippetViewAdapter(messageList, this::openWebView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        messages.setLayoutManager(layoutManager);
        messages.setAdapter(snippetViewAdapter);

        messages.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (isLoading) {
                    return;
                }

                if (layoutManager.findFirstVisibleItemPosition() <= 4) {
                    isLoading = true;
                    loadMore();
                }
//                Log.d("TAG", "abcd " + newState + " " + layoutManager.findFirstVisibleItemPosition());
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

//                Log.d("TAG", "abcd 222 " + dx + " " + dy);
            }
        });
    }

    private void loadMore() {
        presenter.loadOlderMessages();
    }

    @Override
    public void showMessages(List<Snippet> messageList, boolean scrollToEnd) {
        isLoading = false;
        Collections.reverse(messageList);
        this.messageList.addAll(0, messageList);
        if (scrollToEnd) {
            messages.smoothScrollToPosition(messageList.size() - 1);
        }
        snippetViewAdapter.notifyItemRangeInserted(0, messageList.size());
    }
}
