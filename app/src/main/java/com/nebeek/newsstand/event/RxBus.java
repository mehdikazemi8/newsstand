package com.nebeek.newsstand.event;


import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class RxBus {

    private static RxBus instance = null;

    private PublishSubject<Object> subject = PublishSubject.create();

    public static RxBus getInstance() {
        if (instance == null) {
            instance = new RxBus();
        }

        return instance;
    }

    public Observable<Object> getEvents() {
        return subject;
    }
}
