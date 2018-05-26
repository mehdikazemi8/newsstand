package com.nebeek.newsstand.event;

import java.util.concurrent.TimeUnit;

import rx.Subscription;
import rx.functions.Action1;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

public class ThrottleTrackingBus {

    private static final int THRESHOLD_MS = 4000;

    private Subject<VisibleState, VisibleState> publishSubject;
    private Subscription subscription;
    private final Action1<VisibleState> onSuccess;

    public ThrottleTrackingBus(final Action1<VisibleState> onSuccess,
                               final Action1<Throwable> onError) {
        this.onSuccess = onSuccess;
        this.publishSubject = PublishSubject.create();
        this.subscription = publishSubject
                .distinctUntilChanged()
                .throttleWithTimeout(THRESHOLD_MS, TimeUnit.MILLISECONDS)
                .subscribe(this::onCallback, onError);
    }

    public void postViewEvent(final VisibleState visibleState) {
        publishSubject.onNext(visibleState);
    }

    public void unsubscribe() {
        subscription.unsubscribe();
    }

    private void onCallback(VisibleState visibleState) {
        onSuccess.call(visibleState);
    }

    public static class VisibleState {
        final int firstCompletelyVisible;
        final int lastCompletelyVisible;

        public VisibleState(int firstCompletelyVisible,
                            int lastCompletelyVisible) {
            this.firstCompletelyVisible = firstCompletelyVisible;
            this.lastCompletelyVisible = lastCompletelyVisible;
        }

        public int getFirstCompletelyVisible() {
            return firstCompletelyVisible;
        }

        public int getLastCompletelyVisible() {
            return lastCompletelyVisible;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof VisibleState))
                return false;

            VisibleState o = (VisibleState) obj;
            return o.firstCompletelyVisible == this.firstCompletelyVisible &&
                    o.lastCompletelyVisible == this.lastCompletelyVisible;
        }

        @Override
        public int hashCode() {
            return this.firstCompletelyVisible * 37 + this.lastCompletelyVisible;
        }

        @Override
        public String toString() {
            return "toString -> " + firstCompletelyVisible + ", " + lastCompletelyVisible;
        }
    }
}