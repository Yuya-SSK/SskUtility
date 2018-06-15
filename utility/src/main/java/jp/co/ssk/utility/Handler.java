package jp.co.ssk.utility;

import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

@SuppressWarnings({"unused", "WeakerAccess"})
public class Handler extends android.os.Handler {

    public Handler() {
    }

    public Handler(Looper looper) {
        super(looper);
    }

    public boolean isCurrentThread() {
        return Thread.currentThread() == getLooper().getThread();
    }

    public void sendMessage(int what) {
        sendMessage(obtainMessage(what));
    }

    public void sendMessage(int what, int arg1) {
        sendMessage(obtainMessage(what, arg1));
    }

    public void sendMessage(int what, int arg1, int arg2) {
        sendMessage(obtainMessage(what, arg1, arg2));
    }

    public void sendMessage(int what, int arg1, int arg2, @Nullable Object obj) {
        sendMessage(obtainMessage(what, arg1, arg2, obj));
    }

    public void sendMessage(int what, @Nullable Object obj) {
        sendMessage(obtainMessage(what, obj));
    }

    public void sendMessageSyncIf(@NonNull Message msg) {
        if (isCurrentThread()) {
            handleMessage(msg);
        } else {
            sendMessage(msg);
        }
    }

    public void sendMessageSyncIf(int what) {
        sendMessageSyncIf(obtainMessage(what));
    }

    public void sendMessageSyncIf(int what, int arg1) {
        sendMessageSyncIf(obtainMessage(what, arg1));
    }

    public void sendMessageSyncIf(int what, int arg1, int arg2) {
        sendMessageSyncIf(obtainMessage(what, arg1, arg2));
    }

    public void sendMessageSyncIf(int what, int arg1, int arg2, @Nullable Object obj) {
        sendMessageSyncIf(obtainMessage(what, arg1, arg2, obj));
    }

    public void sendMessageSyncIf(int what, @Nullable Object obj) {
        sendMessageSyncIf(obtainMessage(what, obj));
    }

    public void sendMessageDelayed(int what, long delayMillis) {
        sendMessageDelayed(obtainMessage(what), delayMillis);
    }

    public void sendMessageDelayed(int what, int arg1, long delayMillis) {
        sendMessageDelayed(obtainMessage(what, arg1), delayMillis);
    }

    public void sendMessageDelayed(int what, int arg1, int arg2, long delayMillis) {
        sendMessageDelayed(obtainMessage(what, arg1, arg2), delayMillis);
    }

    public void sendMessageDelayed(int what, int arg1, int arg2, @Nullable Object obj, long delayMillis) {
        sendMessageDelayed(obtainMessage(what, arg1, arg2, obj), delayMillis);
    }

    public void sendMessageDelayed(int what, @Nullable Object obj, long delayMillis) {
        sendMessageDelayed(obtainMessage(what, obj), delayMillis);
    }

    public void sendMessageAtFrontOfQueue(int what) {
        sendMessageAtFrontOfQueue(obtainMessage(what));
    }

    public void sendMessageAtFrontOfQueue(int what, int arg1) {
        sendMessageAtFrontOfQueue(obtainMessage(what, arg1));
    }

    public void sendMessageAtFrontOfQueue(int what, int arg1, int arg2) {
        sendMessageAtFrontOfQueue(obtainMessage(what, arg1, arg2));
    }

    public void sendMessageAtFrontOfQueue(int what, int arg1, int arg2, @Nullable Object obj) {
        sendMessageAtFrontOfQueue(obtainMessage(what, arg1, arg2, obj));
    }

    public void sendMessageAtFrontOfQueue(int what, @Nullable Object obj) {
        sendMessageAtFrontOfQueue(obtainMessage(what, obj));
    }

    @NonNull
    public Message obtainMessage(int what, int arg1) {
        return Message.obtain(this, what, arg1, 0);
    }
}
