package com.nebeek.newsstand.ui.explore;

import com.nebeek.newsstand.controller.base.MessageListPresenter;
import com.nebeek.newsstand.data.DataRepository;
import com.nebeek.newsstand.data.DataSource;
import com.nebeek.newsstand.data.local.PreferenceManager;
import com.nebeek.newsstand.data.models.TelegramMessage;
import com.nebeek.newsstand.data.models.TrackedMessages;
import com.nebeek.newsstand.data.remote.response.MessagesResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExplorePresenter extends MessageListPresenter implements ExploreContract.Presenter {

    private ExploreContract.View exploreView;
    private int offset = 0;
    private boolean isLoading = false;
    private PreferenceManager preferenceManager;

    public ExplorePresenter(ExploreContract.View exploreView, DataRepository dataRepository, PreferenceManager preferenceManager) {
        super(dataRepository);
        this.exploreView = exploreView;
        this.preferenceManager = preferenceManager;
    }

    @Override
    public void start() {
        loadOlderMessages();

//        countDownTimer = new CountDownTimer(300000, 8000) {
//            @Override
//            public void onTick(long l) {
//                if (!isLoading) {
//                    fetchNewMessages();
//                }
//            }
//
//            @Override
//            public void onFinish() {
//
//            }
//        };
//        countDownTimer.start();
    }

    private void fetchNewMessages() {
        // todo needs to be optimized, only fetch messages greater than id of last message

        dataRepository.getMessages(0, null, new DataSource.GetMessagesCallback() {
            @Override
            public void onResponse(MessagesResponse response) {

                List<TelegramMessage> newList = new ArrayList<>();
                for (TelegramMessage message : response.getResults()) {
                    boolean found = false;
                    for (TelegramMessage previous : messageList) {
                        if (previous.getId().equals(message.getId())) {
                            found = true;
                        }
                    }

                    // todo remove
                    if (!found/* || newList.size() == 0*/) {
//                        Log.d("TAG", "hhhh " + newList.size());
                        newList.add(message);
                    }
                }

                if (newList.size() > 0) {
                    messageList.addAll(messageList.size(), newList);
                    if (exploreView.isActive()) {
                        exploreView.refreshMessagesList(newList.size(), true);
                    }
                    offset += newList.size();
                }
            }

            @Override
            public void onFailure() {

            }

            @Override
            public void onNetworkFailure() {

            }
        });
    }

    @Override
    public void loadOlderMessages() {
        if (isLoading) {
            return;
        }
        isLoading = true;

        dataRepository.getMessages(offset, null, new DataSource.GetMessagesCallback() {
            @Override
            public void onResponse(MessagesResponse response) {

//                for(TelegramMessage m : response.getResults()) {
//                    Log.d("Tag", "hhh " + m.getLink());
//                }

                isLoading = false;
                Collections.reverse(response.getResults());
                messageList.addAll(0, response.getResults());
                exploreView.refreshMessagesList(response.getResults().size(), false);
                offset += 10;
            }

            @Override
            public void onFailure() {
                isLoading = false;
            }

            @Override
            public void onNetworkFailure() {
                isLoading = false;
            }
        });
    }

    @Override
    public void trackMessages(int firstItem, int lastItem) {
        TrackedMessages trackedMessages = preferenceManager.getTrackedMessages();
        if (firstItem != -1 && lastItem != -1) {
            if (firstItem < lastItem - 1) {
                for (int i = firstItem + 1; i < lastItem; i++) {
                    trackedMessages.addId(messageList.get(i).getId());
                }
            } else {
                for (int i = firstItem; i <= lastItem; i++) {
                    trackedMessages.addId(messageList.get(i).getId());
                }
            }
        }
        preferenceManager.putTrackedMessages(trackedMessages);
    }
}
