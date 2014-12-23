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
package net.javacrumbs.futureconverter.java8common;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

class SimpleCompletionStage<T> implements CompletionStage<T> {
    static final Executor SAME_THREAD_EXECUTOR = Runnable::run;

    private final ListenableCallbackRegistry<T> callbackRegistry = new ListenableCallbackRegistry<>();

    public void success(T result) {
        callbackRegistry.success(result);
    }

    public void failure(Throwable e) {
        callbackRegistry.failure(e);
    }

    @Override
    public <U> CompletionStage<U> thenApply(Function<? super T, ? extends U> fn) {
        return thenApplyAsync(fn, SAME_THREAD_EXECUTOR);
    }

    @Override
    public <U> CompletionStage<U> thenApplyAsync(Function<? super T, ? extends U> fn) {
        return null;
    }

    @Override
    public <U> CompletionStage<U> thenApplyAsync(Function<? super T, ? extends U> fn, Executor executor) {
        SimpleCompletionStage<U> newCompletionStage = newSimpleCompletionStage();
        callbackRegistry.addSuccessCallback(result -> {
            try {
                newCompletionStage.success(fn.apply(result));
            } catch (Throwable e) {
                newCompletionStage.failure(wrapException(e));
            }
        }, executor);
        addStandardFailureCallback(newCompletionStage::failure, executor);
        return newCompletionStage;
    }

    @Override
    public CompletionStage<Void> thenAccept(Consumer<? super T> action) {
        return thenAcceptAsync(action, SAME_THREAD_EXECUTOR);
    }

    @Override
    public CompletionStage<Void> thenAcceptAsync(Consumer<? super T> action) {
        return null;
    }

    @Override
    public CompletionStage<Void> thenAcceptAsync(Consumer<? super T> action, Executor executor) {
        SimpleCompletionStage<Void> newCompletionStage = newSimpleCompletionStage();
        callbackRegistry.addSuccessCallback(result -> {
            try {
                action.accept(result);
                newCompletionStage.success(null);
            } catch (Throwable e) {
                newCompletionStage.failure(wrapException(e));
            }
        }, executor);
        addStandardFailureCallback(newCompletionStage::failure, executor);
        return newCompletionStage;
    }

    @Override
    public CompletionStage<Void> thenRun(Runnable action) {
        return null;
    }

    @Override
    public CompletionStage<Void> thenRunAsync(Runnable action) {
        return null;
    }

    @Override
    public CompletionStage<Void> thenRunAsync(Runnable action, Executor executor) {
        return null;
    }

    @Override
    public <U, V> CompletionStage<V> thenCombine(CompletionStage<? extends U> other, BiFunction<? super T, ? super U, ? extends V> fn) {
        return thenCombineAsync(other, fn, SAME_THREAD_EXECUTOR);
    }

    @Override
    public <U, V> CompletionStage<V> thenCombineAsync(CompletionStage<? extends U> other, BiFunction<? super T, ? super U, ? extends V> fn) {
        return null;
    }

    @Override
    public <U, V> CompletionStage<V> thenCombineAsync(CompletionStage<? extends U> other, BiFunction<? super T, ? super U, ? extends V> fn, Executor executor) {
        SimpleCompletionStage<V> newCompletionStage = newSimpleCompletionStage();
        callbackRegistry.addSuccessCallback(result1 -> {
            try {
                other.thenAccept(result2 -> newCompletionStage.success(fn.apply(result1, result2)));
            } catch (Throwable e) {
                newCompletionStage.failure(wrapException(e));
            }
        }, executor);
        addStandardFailureCallback(newCompletionStage::failure, executor);
        return newCompletionStage;
    }

    @Override
    public <U> CompletionStage<Void> thenAcceptBoth(CompletionStage<? extends U> other, BiConsumer<? super T, ? super U> action) {
        return null;
    }

    @Override
    public <U> CompletionStage<Void> thenAcceptBothAsync(CompletionStage<? extends U> other, BiConsumer<? super T, ? super U> action) {
        return null;
    }

    @Override
    public <U> CompletionStage<Void> thenAcceptBothAsync(CompletionStage<? extends U> other, BiConsumer<? super T, ? super U> action, Executor executor) {
        return null;
    }

    @Override
    public CompletionStage<Void> runAfterBoth(CompletionStage<?> other, Runnable action) {
        return null;
    }

    @Override
    public CompletionStage<Void> runAfterBothAsync(CompletionStage<?> other, Runnable action) {
        return null;
    }

    @Override
    public CompletionStage<Void> runAfterBothAsync(CompletionStage<?> other, Runnable action, Executor executor) {
        return null;
    }

    @Override
    public <U> CompletionStage<U> applyToEither(CompletionStage<? extends T> other, Function<? super T, U> fn) {
        return null;
    }

    @Override
    public <U> CompletionStage<U> applyToEitherAsync(CompletionStage<? extends T> other, Function<? super T, U> fn) {
        return null;
    }

    @Override
    public <U> CompletionStage<U> applyToEitherAsync(CompletionStage<? extends T> other, Function<? super T, U> fn, Executor executor) {
        return null;
    }

    @Override
    public CompletionStage<Void> acceptEither(CompletionStage<? extends T> other, Consumer<? super T> action) {
        return null;
    }

    @Override
    public CompletionStage<Void> acceptEitherAsync(CompletionStage<? extends T> other, Consumer<? super T> action) {
        return null;
    }

    @Override
    public CompletionStage<Void> acceptEitherAsync(CompletionStage<? extends T> other, Consumer<? super T> action, Executor executor) {
        return null;
    }

    @Override
    public CompletionStage<Void> runAfterEither(CompletionStage<?> other, Runnable action) {
        return null;
    }

    @Override
    public CompletionStage<Void> runAfterEitherAsync(CompletionStage<?> other, Runnable action) {
        return null;
    }

    @Override
    public CompletionStage<Void> runAfterEitherAsync(CompletionStage<?> other, Runnable action, Executor executor) {
        return null;
    }

    @Override
    public <U> CompletionStage<U> thenCompose(Function<? super T, ? extends CompletionStage<U>> fn) {
        return null;
    }

    @Override
    public <U> CompletionStage<U> thenComposeAsync(Function<? super T, ? extends CompletionStage<U>> fn) {
        return null;
    }

    @Override
    public <U> CompletionStage<U> thenComposeAsync(Function<? super T, ? extends CompletionStage<U>> fn, Executor executor) {
        return null;
    }

    @Override
    public CompletionStage<T> exceptionally(Function<Throwable, ? extends T> fn) {
        SimpleCompletionStage<T> newCompletionStage = newSimpleCompletionStage();
        callbackRegistry.addSuccessCallback(newCompletionStage::success, SAME_THREAD_EXECUTOR);
        callbackRegistry.addFailureCallback(t -> newCompletionStage.success(fn.apply(t)), SAME_THREAD_EXECUTOR);
        return newCompletionStage;
    }

    @Override
    public CompletionStage<T> whenComplete(BiConsumer<? super T, ? super Throwable> action) {
        return whenCompleteAsync(action, SAME_THREAD_EXECUTOR);
    }

    @Override
    public CompletionStage<T> whenCompleteAsync(BiConsumer<? super T, ? super Throwable> action) {
        return null;
    }

    @Override
    public CompletionStage<T> whenCompleteAsync(BiConsumer<? super T, ? super Throwable> action, Executor executor) {
        SimpleCompletionStage<T> newCompletionStage = newSimpleCompletionStage();
        callbackRegistry.addSuccessCallback(result -> {
            action.accept(result, null);
            newCompletionStage.success(result);
        }, executor);
        callbackRegistry.addFailureCallback(e -> {
            action.accept(null, e);
            newCompletionStage.failure(wrapException(e));
        }, executor);
        return newCompletionStage;
    }

    @Override
    public <U> CompletionStage<U> handle(BiFunction<? super T, Throwable, ? extends U> fn) {
        return null;
    }

    @Override
    public <U> CompletionStage<U> handleAsync(BiFunction<? super T, Throwable, ? extends U> fn) {
        return null;
    }

    @Override
    public <U> CompletionStage<U> handleAsync(BiFunction<? super T, Throwable, ? extends U> fn, Executor executor) {
        return null;
    }

    @Override
    public CompletableFuture<T> toCompletableFuture() {
        return null;
    }

    private void addStandardFailureCallback(Consumer<Throwable> onError, Executor executor) {
        callbackRegistry.addFailureCallback(t -> onError.accept(wrapException(t)), executor);
    }

    private final Throwable wrapException(Throwable e) {
        if (e instanceof CompletionException) {
            return e;
        } else {
            return new CompletionException(e);
        }
    }

    private <R> SimpleCompletionStage<R> newSimpleCompletionStage() {
        return new SimpleCompletionStage<>();
    }
}
