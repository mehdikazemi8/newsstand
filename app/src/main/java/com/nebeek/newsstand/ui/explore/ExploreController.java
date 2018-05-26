package com.nebeek.newsstand.ui.explore;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nebeek.newsstand.R;
import com.nebeek.newsstand.controller.base.BaseBackStackController;
import com.nebeek.newsstand.controller.base.BaseMessageListPresenter;
import com.nebeek.newsstand.data.DataRepository;
import com.nebeek.newsstand.data.local.PreferenceManager;
import com.nebeek.newsstand.event.RxUtil;
import com.nebeek.newsstand.event.ThrottleTrackingBus;
import com.nebeek.newsstand.ui.topic.MessageViewAdapter;

import butterknife.BindView;

public class ExploreController extends BaseBackStackController implements ExploreContract.View {

    @BindView(R.id.messages)
    RecyclerView messages;

    private ThrottleTrackingBus trackingBus;
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
    protected void onAttach(@NonNull View view) {
        super.onAttach(view);
        trackingBus = new ThrottleTrackingBus(this::onTrackViewResponse, RxUtil.logError());
    }

    @Override
    protected void onDetach(@NonNull View view) {
        super.onDetach(view);
        trackingBus.unsubscribe();
    }

    @Override
    protected void onViewBound(@NonNull View view) {
        super.onViewBound(view);

        presenter = new ExplorePresenter(this, DataRepository.getInstance(), PreferenceManager.getInstance(getActivity()));
        init();
        setActive(true);
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

                final ThrottleTrackingBus.VisibleState visibleStateFinal = new ThrottleTrackingBus.VisibleState(layoutManager.findFirstVisibleItemPosition(), layoutManager.findLastVisibleItemPosition());
                trackingBus.postViewEvent(visibleStateFinal);
            }
        });
    }

    @Override
    public void refreshMessagesList(int messagesCount, boolean pushedAtEnd) {
        if (pushedAtEnd) {
//            GlobalToast.makeToast(getActivity(), "pushed", Toast.LENGTH_SHORT);
            messageViewAdapter.notifyItemRangeInserted(messages.getAdapter().getItemCount(), messagesCount);
        } else {
            messageViewAdapter.notifyItemRangeInserted(0, messagesCount);
        }
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

    void onTrackViewResponse(ThrottleTrackingBus.VisibleState visibleState) {
        Log.d("TAG", "Received to be tracked: " + visibleState.toString());
        presenter.trackMessages(visibleState.getFirstCompletelyVisible(), visibleState.getLastCompletelyVisible());
//        TrackingEvent trackingEvent = new TrackingEvent()
//                .put(KEY_ACTION, getString(R.string.tracking_view_catalog_items))
//                .put(KEY_CATEGORY, getString(R.string.tracking_cat_interaction))
//                .put(KEY_LABEL, visibleState.toString());
//        tracking.sendEvent(trackingEvent);
    }
}
