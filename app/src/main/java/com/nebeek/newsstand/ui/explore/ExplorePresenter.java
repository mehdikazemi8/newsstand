package com.nebeek.newsstand.ui.explore;

import android.os.CountDownTimer;
import android.util.Log;

import com.nebeek.newsstand.controller.base.MessageListPresenter;
import com.nebeek.newsstand.data.DataRepository;
import com.nebeek.newsstand.data.DataSource;
import com.nebeek.newsstand.data.models.TelegramMessage;
import com.nebeek.newsstand.data.remote.response.MessagesResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExplorePresenter extends MessageListPresenter implements ExploreContract.Presenter {

    private ExploreContract.View exploreView;
    private int offset = 0;
    private boolean isLoading = false;

    private CountDownTimer countDownTimer;

    public ExplorePresenter(ExploreContract.View exploreView, DataRepository dataRepository) {
        super(dataRepository);
        this.exploreView = exploreView;
    }

    @Override
    public void start() {

        loadOlderMessages();

        countDownTimer = new CountDownTimer(300000, 8000) {
            @Override
            public void onTick(long l) {
                if (!isLoading) {
                    fetchNewMessages();
                }
            }

            @Override
            public void onFinish() {

            }
        };
        countDownTimer.start();
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
                    if (!found || newList.size() == 0) {
                        Log.d("TAG", "hhhh " + newList.size());
                        newList.add(message);
                    }
                }

                messageList.addAll(messageList.size(), newList);
                if (exploreView.isActive()) {
                    exploreView.refreshMessagesList(newList.size(), true);
                }
                offset += newList.size();
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
    protected void finalize() throws Throwable {
        super.finalize();

        countDownTimer.cancel();
    }
}
