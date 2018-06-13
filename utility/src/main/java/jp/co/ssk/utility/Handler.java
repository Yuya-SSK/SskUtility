package jp.co.ssk.utility;

import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

@SuppressWarnings({"unused", "WeakerAccess"})
public class Handler {

    @NonNull
    private final android.os.Handler mHandler;

    public Handler() {
        this(null);
    }

    public Handler(@Nullable Looper looper) {
        mHandler = new android.os.Handler(looper) {
            @Override
            public void handleMessage(Message msg) {
                if (null != msg) {
                    jp.co.ssk.utility.Handler.this.handleMessage(msg);
                }
            }
        };
    }

    @NonNull
    public Looper getLooper() {
        return mHandler.getLooper();
    }

    public boolean isCurrentThread() {
        return Thread.currentThread() == mHandler.getLooper().getThread();
    }

    public void sendMessage(@NonNull Message msg) {
        mHandler.sendMessage(msg);
    }

    public void sendMessage(int what) {
        mHandler.sendMessage(mHandler.obtainMessage(what));
    }

    public void sendMessage(int what, int arg1) {
        mHandler.sendMessage(mHandler.obtainMessage(what, arg1));
    }

    public void sendMessage(int what, int arg1, int arg2) {
        mHandler.sendMessage(mHandler.obtainMessage(what, arg1, arg2));
    }

    public void sendMessage(int what, int arg1, int arg2, @Nullable Object obj) {
        mHandler.sendMessage(mHandler.obtainMessage(what, arg1, arg2, obj));
    }

    public void sendMessage(int what, @Nullable Object obj) {
        mHandler.sendMessage(mHandler.obtainMessage(what, obj));
    }

    public void sendMessageDelayed(@NonNull Message msg, long delayMillis) {
        mHandler.sendMessageDelayed(msg, delayMillis);
    }

    public void sendMessageDelayed(int what, long delayMillis) {
        mHandler.sendMessageDelayed(mHandler.obtainMessage(what), delayMillis);
    }

    public void sendMessageDelayed(int what, int arg1, long delayMillis) {
        mHandler.sendMessageDelayed(mHandler.obtainMessage(what, arg1), delayMillis);
    }

    public void sendMessageDelayed(int what, int arg1, int arg2, long delayMillis) {
        mHandler.sendMessageDelayed(mHandler.obtainMessage(what, arg1, arg2), delayMillis);
    }

    public void sendMessageDelayed(int what, int arg1, int arg2, @Nullable Object obj, long delayMillis) {
        mHandler.sendMessageDelayed(mHandler.obtainMessage(what, arg1, arg2, obj), delayMillis);
    }

    public void sendMessageDelayed(int what, @Nullable Object obj, long delayMillis) {
        mHandler.sendMessageDelayed(mHandler.obtainMessage(what, obj), delayMillis);
    }

    public void sendMessageAtFrontOfQueue(@NonNull Message msg) {
        mHandler.sendMessageAtFrontOfQueue(msg);
    }

    public void sendMessageAtFrontOfQueue(int what) {
        mHandler.sendMessageAtFrontOfQueue(mHandler.obtainMessage(what));
    }

    public void sendMessageAtFrontOfQueue(int what, int arg1) {
        mHandler.sendMessageAtFrontOfQueue(mHandler.obtainMessage(what, arg1));
    }

    public void sendMessageAtFrontOfQueue(int what, int arg1, int arg2) {
        mHandler.sendMessageAtFrontOfQueue(mHandler.obtainMessage(what, arg1, arg2));
    }

    public void sendMessageAtFrontOfQueue(int what, int arg1, int arg2, @Nullable Object obj) {
        mHandler.sendMessageAtFrontOfQueue(mHandler.obtainMessage(what, arg1, arg2, obj));
    }

    public void sendMessageAtFrontOfQueue(int what, @Nullable Object obj) {
        mHandler.sendMessageAtFrontOfQueue(mHandler.obtainMessage(what, obj));
    }

    public boolean hasMessage(int what) {
        return mHandler.hasMessages(what);
    }

    public void removeMessage(int what) {
        mHandler.removeMessages(what);
    }

    public void post(@NonNull Runnable r) {
        mHandler.post(r);
    }

    public void postDelayed(@NonNull Runnable r, long delayMillis) {
        mHandler.postDelayed(r, delayMillis);
    }

    public void removeCallbacks(@NonNull Runnable r) {
        mHandler.removeCallbacks(r);
    }

    @NonNull
    public Message obtainMessage() {
        return mHandler.obtainMessage();
    }

    protected void handleMessage(@NonNull Message msg) {
    }

    @NonNull
    private Message obtainMessage(int what, int arg1) {
        return Message.obtain(mHandler, what, arg1, 0);
    }
}
