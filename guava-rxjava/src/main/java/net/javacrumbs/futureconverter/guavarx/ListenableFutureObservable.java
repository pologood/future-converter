/**
 * Copyright 2009-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.javacrumbs.futureconverter.guavarx;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import rx.Observable;
import rx.Subscriber;

/**
 * Wraps  {@link com.google.common.util.concurrent.ListenableFuture} as {@link rx.Observable}.
 * The original future is NOT canceled upon unsubscribe.
 *
 * @param <T>
 */
class ListenableFutureObservable<T> extends Observable<T> {
    private final ListenableFuture<T> listenableFuture;

    ListenableFutureObservable(ListenableFuture<T> listenableFuture) {
        super(onSubscribe(listenableFuture));
        this.listenableFuture = listenableFuture;
    }

    private static <T> OnSubscribe<T> onSubscribe(final ListenableFuture<T> listenableFuture) {
        return new Observable.OnSubscribe<T>() {
            @Override
            public void call(final Subscriber<? super T> subscriber) {
                Futures.addCallback(listenableFuture, new FutureCallback<T>() {
                    @Override
                    public void onSuccess(T result) {
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onNext(result);
                            subscriber.onCompleted();
                        }
                    }
                    @Override
                    public void onFailure(Throwable t) {
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onError(t);
                        }
                    }
                });
            }
        };
    }

    public ListenableFuture<T> getListenableFuture() {
        return listenableFuture;
    }
}
