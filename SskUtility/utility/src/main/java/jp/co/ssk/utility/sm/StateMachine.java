package jp.co.ssk.utility.sm;

import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;

import jp.co.ssk.utility.Handler;
import jp.co.ssk.utility.SynchronousCallback;
import jp.co.ssk.utility.Type;

@SuppressWarnings({"all"})
public abstract class StateMachine {

    protected static final boolean HANDLED = State.HANDLED;
    protected static final boolean NOT_HANDLED = State.NOT_HANDLED;

    @NonNull
    private final Handler mHandler;
    @NonNull
    private final HashMap<State, StateInfo> mStateInfoMap = new HashMap<>();
    @NonNull
    private final Deque<StateInfo> mStateStack = new ArrayDeque<>();
    @NonNull
    private final LinkedList<Message> mDeferredMessages = new LinkedList<>();
    @Nullable
    private State mInitialState;
    @Nullable
    private State mDestState;
    @Nullable
    private Message mCurrentMessage;

    protected StateMachine() {
        this(null);
    }

    protected StateMachine(@Nullable Looper looper) {
        if (null == looper) {
            HandlerThread thread = new HandlerThread(getName());
            thread.start();
            looper = thread.getLooper();
        }
        mHandler = new Handler(looper) {
            @Override
            protected void handleMessage(@NonNull Message msg) {
                _handleMessage(msg);
            }
        };
    }

    @NonNull
    protected Handler getHandler() {
        return mHandler;
    }

    protected void addState(@NonNull final State state) {
        addState(state, null);
    }

    protected void addState(@NonNull final State state, @Nullable final State parent) {
        if (mHandler.isCurrentThread()) {
            _addState(state, parent);
        } else {
            mHandler.post(() -> _addState(state, parent));
        }
    }

    protected void setInitialState(@NonNull final State state) {
        if (mHandler.isCurrentThread()) {
            mInitialState = state;
        } else {
            mHandler.post(() -> mInitialState = state);
        }
    }

    protected void start() {
        if (mHandler.isCurrentThread()) {
            _start();
        } else {
            mHandler.post(this::_start);
        }
    }

    protected void transitionTo(@NonNull final State state) {
        if (mHandler.isCurrentThread()) {
            mDestState = state;
        } else {
            mHandler.post(() -> mDestState = state);
        }
    }

    @Nullable
    protected final Message getCurrentMessage() {
        final Message ret;
        if (mHandler.isCurrentThread()) {
            ret = mCurrentMessage;
        } else {
            final SynchronousCallback callback = new SynchronousCallback();
            mHandler.post(() -> {
                callback.setResult(mCurrentMessage);
                callback.unlock();
            });
            callback.lock();
            ret = Type.cast(callback.getResult());
        }
        return ret;
    }

    @NonNull
    protected State getCurrentState() {
        final State ret;
        if (mHandler.isCurrentThread()) {
            ret = mStateStack.peekFirst().state;
        } else {
            final SynchronousCallback callback = new SynchronousCallback();
            mHandler.post(() -> {
                callback.setResult(mStateStack.peekFirst().state);
                callback.unlock();
            });
            callback.lock();
            ret = Type.cast(callback.getResult());
            if (null == ret) {
                throw new UnknownError("null == ret");
            }
        }
        return ret;
    }

    protected void sendMessage(int what) {
        mHandler.sendMessage(what);
    }

    protected void sendMessage(int what, int arg1) {
        mHandler.sendMessage(what, arg1);
    }

    protected void sendMessage(int what, int arg1, int arg2) {
        mHandler.sendMessage(what, arg1, arg2);
    }

    protected void sendMessage(int what, int arg1, int arg2, @Nullable Object obj) {
        mHandler.sendMessage(what, arg1, arg2, obj);
    }

    protected void sendMessage(int what, @Nullable Object obj) {
        mHandler.sendMessage(what, obj);
    }

    protected void sendMessageDelayed(int what, long delayMillis) {
        mHandler.sendMessageDelayed(what, delayMillis);
    }

    protected void sendMessageDelayed(int what, int arg1, long delayMillis) {
        mHandler.sendMessageDelayed(what, arg1, delayMillis);
    }

    protected void sendMessageDelayed(int what, int arg1, int arg2, long delayMillis) {
        mHandler.sendMessageDelayed(what, arg1, arg2, delayMillis);
    }

    protected void sendMessageDelayed(int what, int arg1, int arg2, @Nullable Object obj, long delayMillis) {
        mHandler.sendMessageDelayed(what, arg1, arg2, obj, delayMillis);
    }

    protected void sendMessageDelayed(int what, @Nullable Object obj, long delayMillis) {
        mHandler.sendMessageDelayed(what, obj, delayMillis);
    }

    protected boolean hasMessage(int what) {
        return mHandler.hasMessage(what);
    }

    protected void removeMessage(int what) {
        mHandler.removeMessage(what);
    }

    protected final void deferMessage(@NonNull final Message msg) {
        if (mHandler.isCurrentThread()) {
            _deferMessage(msg);
        } else {
            mHandler.post(() -> _deferMessage(msg));
        }
    }

    protected final void removeDeferredMessage(final int what) {
        if (mHandler.isCurrentThread()) {
            _removeDeferredMessage(what);
        } else {
            mHandler.post(() -> _removeDeferredMessage(what));
        }
    }

    @NonNull
    private StateInfo _addState(@NonNull State state, @Nullable State parent) {
        if (mStateInfoMap.containsKey(state)) {
            throw new RuntimeException("State already added.");
        }
        StateInfo parentStateInfo = null;
        if (parent != null) {
            parentStateInfo = mStateInfoMap.get(parent);
            if (parentStateInfo == null) {
                parentStateInfo = _addState(parent, null);
            }
        }
        StateInfo stateInfo = new StateInfo();
        stateInfo.state = state;
        stateInfo.parentStateInfo = parentStateInfo;
        stateInfo.active = false;
        return mStateInfoMap.put(state, stateInfo);
    }

    private void _start() {
        if (mInitialState == null) {
            throw new RuntimeException("Unset initial state.");
        }
        _performTransitions(mInitialState);
    }

    private void _performTransitions(@NonNull State destState) {
        StateInfo tempStateInfo = mStateInfoMap.get(destState);
        State foundRootState = null;
        Deque<StateInfo> destStateDeque = new ArrayDeque<>();
        while (tempStateInfo != null) {
            if (tempStateInfo.active) {
                foundRootState = tempStateInfo.state;
                break;
            }
            destStateDeque.offerFirst(tempStateInfo);
            tempStateInfo = tempStateInfo.parentStateInfo;
        }
        while (null != (tempStateInfo = mStateStack.peekFirst())) {
            if (foundRootState != null && foundRootState == tempStateInfo.state) {
                break;
            }
            Log.i(getName(), "invokeExitMethods: " + tempStateInfo.state.getName());
            tempStateInfo.state.exit();
            tempStateInfo.active = false;
            mStateStack.pollFirst();
        }
        for (StateInfo stateInfo : destStateDeque) {
            Log.i(getName(), "invokeEnterMethods: " + stateInfo.state.getName());
            stateInfo.state.enter();
            stateInfo.active = true;
            mStateStack.offerFirst(stateInfo);
        }
        _moveDeferredMessageAtFrontOfQueue();
    }

    private void _processMessage(Message msg) {
        for (StateInfo stateInfo : mStateStack) {
            Log.i(getName(), "processMsg: " + stateInfo.state.getName() + String.format(Locale.US, " msg.what=0x%08x", msg.what));
            if (stateInfo.state.processMessage(msg)) {
                break;
            }
        }
    }

    private void _handleMessage(@NonNull Message msg) {
        mCurrentMessage = msg;
        _processMessage(msg);
        if (mDestState != null) {
            _performTransitions(mDestState);
            mDestState = null;
        }
    }

    private void _moveDeferredMessageAtFrontOfQueue() {
        for (Message message : mDeferredMessages) {
            mHandler.sendMessageAtFrontOfQueue(message);
        }
        mDeferredMessages.clear();
    }

    private void _deferMessage(@NonNull Message msg) {
        Message newMsg = mHandler.obtainMessage();
        newMsg.copyFrom(msg);
        mDeferredMessages.add(newMsg);
    }

    private void _removeDeferredMessage(int what) {
        mDeferredMessages.removeIf(msg -> msg.what == what);
    }

    @NonNull
    private String getName() {
        return getClass().getSimpleName();
    }

    private static class StateInfo {
        private State state;
        private StateInfo parentStateInfo;
        private boolean active;

        @Override
        public String toString() {
            String str = "{";
            str += "state=" + state;
            if (parentStateInfo != null) {
                str += ", parent=" + parentStateInfo.state;
            }
            str = str + ", active=" + active;
            str += '}';
            return str;
        }
    }
}
