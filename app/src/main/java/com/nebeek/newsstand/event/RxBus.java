package com.nebeek.newsstand.event;


import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class RxBus {

    public RxBus() {
    }

    private PublishSubject<Object> subject = PublishSubject.create();

    public void send(Object o) {
        subject.onNext(o);
    }

    public Observable<Object> toObservable() {
        return subject;
    }

    private static RxBus instance = null;

    public static RxBus getInstance() {
        if (instance == null) {
            instance = new RxBus();
        }

        return instance;
    }
}
