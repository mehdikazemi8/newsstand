package com.nebeek.newsstand.ui.bookmark;

import com.nebeek.newsstand.controller.base.MessageListPresenter;
import com.nebeek.newsstand.data.DataRepository;
import com.nebeek.newsstand.event.NewBookmarkAdded;
import com.nebeek.newsstand.event.PlayButtonClicked;
import com.nebeek.newsstand.event.RxBus;

public class BookmarkPresenter extends MessageListPresenter implements BookmarkContract.Presenter {

    BookmarkContract.View bookmarkView;

    public BookmarkPresenter(DataRepository dataRepository, BookmarkContract.View bookmarkView) {
        super(dataRepository);
        this.bookmarkView = bookmarkView;
    }

    @Override
    public void start() {
        // todo
        messageList.clear();
        messageList.addAll(dataRepository.getAllBookmarked());

        if (messageList.size() == 0) {
            bookmarkView.showEmptyListMessage();
        } else {
            bookmarkView.hideEmptyListMessage();
            bookmarkView.refreshMessagesList();
        }

        RxBus.getInstance().toObservable().subscribe(object -> {
            if (object instanceof NewBookmarkAdded) {
                start();
            } else if (object instanceof PlayButtonClicked) {
                bookmarkView.openTelegramWithSpecificUrl(
                        ((PlayButtonClicked) object).getUrl()
                );
            }
        });
    }
}
