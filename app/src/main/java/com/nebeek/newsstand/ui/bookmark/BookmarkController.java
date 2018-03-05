package com.nebeek.newsstand.ui.bookmark;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nebeek.newsstand.R;
import com.nebeek.newsstand.controller.base.BaseBackStackController;
import com.nebeek.newsstand.controller.base.BaseMessageListPresenter;
import com.nebeek.newsstand.data.DataRepository;
import com.nebeek.newsstand.ui.topic.MessageViewAdapter;

import butterknife.BindView;

public class BookmarkController extends BaseBackStackController implements BookmarkContract.View {

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.messages)
    RecyclerView messages;
    @BindView(R.id.empty_list_message)
    TextView emptyListMessage;

    private LinearLayoutManager layoutManager;
    private BookmarkContract.Presenter presenter;
    private MessageViewAdapter messageViewAdapter;

    public static BookmarkController newInstance() {
        return new BookmarkController();
    }

    @Override
    protected View inflateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return inflater.inflate(R.layout.controller_bookmark, container, false);
    }

    @Override
    protected void onViewBound(@NonNull View view) {
        super.onViewBound(view);

        presenter = new BookmarkPresenter(DataRepository.getInstance(), this);
        initView();
        setActive(true);
        presenter.start();
    }

    private void initView() {
        messageViewAdapter = new MessageViewAdapter(null, null, this::openWebView, null, (BaseMessageListPresenter) presenter);
        layoutManager = new LinearLayoutManager(getActivity());
        messages.setLayoutManager(layoutManager);
        messages.setAdapter(messageViewAdapter);
        layoutManager.setStackFromEnd(true);
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
    public void refreshMessagesList() {
        messageViewAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean canHandleBackStack() {
        return false;
    }

    @Override
    public void showEmptyListMessage() {
        emptyListMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmptyListMessage() {
        emptyListMessage.setVisibility(View.GONE);
    }
}
