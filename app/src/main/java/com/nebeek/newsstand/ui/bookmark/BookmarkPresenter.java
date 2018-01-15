package com.nebeek.newsstand.ui.bookmark;

import com.nebeek.newsstand.controller.base.MessageListPresenter;
import com.nebeek.newsstand.data.DataRepository;

public class BookmarkPresenter extends MessageListPresenter implements BookmarkContract.Presenter {

    BookmarkContract.View bookmarkView;

    public BookmarkPresenter(DataRepository dataRepository, BookmarkContract.View bookmarkView) {
        super(dataRepository);
        this.bookmarkView = bookmarkView;
    }

    @Override
    public void start() {
        messageList.addAll(dataRepository.getAllBookmarked());
        bookmarkView.refreshMessagesList();
    }
}
