package com.nebeek.newsstand.ui.explore;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nebeek.newsstand.R;
import com.nebeek.newsstand.controller.base.BaseBackStackController;
import com.nebeek.newsstand.controller.base.BaseMessageListPresenter;
import com.nebeek.newsstand.data.DataRepository;
import com.nebeek.newsstand.ui.topic.MessageViewAdapter;

import butterknife.BindView;

public class ExploreController extends BaseBackStackController implements ExploreContract.View {

    @BindView(R.id.messages)
    RecyclerView messages;

    private MessageViewAdapter messageViewAdapter;
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

        presenter = new ExplorePresenter(this, DataRepository.getInstance());
        init();
        presenter.start();
    }

    private void init() {
        messageViewAdapter = new MessageViewAdapter(null, null, this::openWebView, null, (BaseMessageListPresenter) presenter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
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

    @Override
    public void refreshMessagesList(int messagesCount, boolean scrollToEnd) {
//        if (scrollToEnd) {
//            messages.smoothScrollToPosition(messageList.size() - 1);
//        }
        messageViewAdapter.notifyItemRangeInserted(0, messagesCount);
    }
}
