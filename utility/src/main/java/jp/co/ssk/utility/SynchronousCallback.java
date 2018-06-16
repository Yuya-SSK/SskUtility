package jp.co.ssk.utility;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@SuppressWarnings("unused")
public final class SynchronousCallback<T> {
    private final static int DEFAULT_TIMEOUT = 10 * 1000;
    @NonNull
    private final CountDownLatch mLock;
    @Nullable
    private T mResult;

    public SynchronousCallback() {
        mLock = new CountDownLatch(1);
    }

    public void lock() {
        try {
            lock(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (TimeoutException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void lock(long timeout, @NonNull TimeUnit unit) throws InterruptedException, TimeoutException {
        mLock.await(timeout, unit);
        if (0 < mLock.getCount()) {
            throw new TimeoutException("Timeout CountDownLatch.await()");
        }
    }

    public void unlock() {
        mLock.countDown();
    }

    @Nullable
    public T getResult() {
        return mResult;
    }

    public void setResult(@Nullable T result) {
        mResult = result;
    }
}
