package jp.co.ssk.sample_lib;

import android.content.Context;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import jp.co.ssk.utility.SynchronousCallback;
import jp.co.ssk.utility.Type;
import jp.co.ssk.utility.sm.State;
import jp.co.ssk.utility.sm.StateMachine;

@SuppressWarnings({"unused", "WeakerAccess"})
final class SampleStateMachine extends StateMachine {

    public interface Listener {
        default void onStateChanged(@NonNull SampleState sampleState) {}
    }

    private static final int EVT_BASE = 0x00000000;
    private static final int EVT_LOCAL_BASE = 0xff000000;

    private static final int EVT_ACTIVATE = EVT_BASE + 0x00000001;
    private static final int EVT_DEACTIVATE = EVT_BASE + 0x00000002;
    private static final int EVT_CONNECT = EVT_BASE + 0x00000003;
    private static final int EVT_DISCONNECT = EVT_BASE + 0x00000004;

    private final State mInactiveState = new InactiveState();
    private final State mActiveState = new ActiveState();
    private final State mUnconnectedState = new UnconnectedState();
    private final State mUnconnected1State = new Unconnected1State();
    private final State mUnconnected2State = new Unconnected2State();
    private final State mUnconnected3State = new Unconnected3State();
    private final State mConnectingState = new ConnectingState();
    private final State mConnecting1State = new Connecting1State();
    private final State mConnecting2State = new Connecting2State();
    private final State mConnecting3State = new Connecting3State();
    private final State mConnectedState = new ConnectedState();
    private final State mConnected1State = new Connected1State();
    private final State mConnected2State = new Connected2State();
    private final State mConnected3State = new Connected3State();
    private final State mDisconnectingState = new DisconnectingState();
    private final State mDisconnecting1State = new Disconnecting1State();
    private final State mDisconnecting2State = new Disconnecting2State();
    private final State mDisconnecting3State = new Disconnecting3State();

    @NonNull
    private final Context mContext;
    @NonNull
    private final Listener mListener;
    @NonNull
    private SampleState mSampleState;

    public SampleStateMachine(@NonNull Context context, @Nullable Looper looper, @NonNull Listener listener) {
        super(looper);
        mContext = context;
        mListener = listener;

        State defaultState = new DefaultState();
        addState(defaultState);
        addState(mInactiveState, defaultState);
        addState(mActiveState, defaultState);
        addState(mUnconnectedState, mActiveState);
        addState(mUnconnected1State, mUnconnectedState);
        addState(mUnconnected2State, mUnconnectedState);
        addState(mUnconnected3State, mUnconnectedState);
        addState(mConnectingState, mActiveState);
        addState(mConnecting1State, mConnectingState);
        addState(mConnecting2State, mConnectingState);
        addState(mConnecting3State, mConnectingState);
        addState(mConnectedState, mActiveState);
        addState(mConnected1State, mConnectedState);
        addState(mConnected2State, mConnected1State);
        addState(mConnected3State, mConnected2State);
        addState(mDisconnectingState, mActiveState);
        addState(mDisconnecting1State, mDisconnectingState);
        addState(mDisconnecting2State, mDisconnectingState);
        addState(mDisconnecting3State, mDisconnectingState);

        mSampleState = SampleState.UnknownState;
        setInitialState(mInactiveState);
        start();
    }

    public void activate() {
        sendMessage(EVT_ACTIVATE);
    }

    public void deactivate() {
        sendMessage(EVT_DEACTIVATE);
    }

    public void connect() {
        sendMessage(EVT_CONNECT);
    }

    public void disconnect() {
        sendMessage(EVT_DISCONNECT);
    }

    @NonNull
    public SampleState getSampleState() {
        final SampleState ret;
        if (getHandler().isCurrentThread()) {
            ret = mSampleState;
        } else {
            final SynchronousCallback callback = new SynchronousCallback();
            getHandler().post(() -> {
                callback.setResult(mSampleState);
                callback.unlock();
            });
            callback.lock();
            ret = Type.cast(callback.getResult());
        }
        return ret;
    }

    private void _setSampleState(@NonNull SampleState sampleState) {
        mSampleState = sampleState;
        mListener.onStateChanged(sampleState);
    }

    private class DefaultState extends State {
        @Override
        public boolean processMessage(@NonNull Message msg) {
            return StateMachine.HANDLED;
        }
    }

    private class InactiveState extends State {
        @Override
        public void enter() {
            _setSampleState(SampleState.InactiveState);
        }
        @Override
        public boolean processMessage(@NonNull Message msg) {
            boolean ret = StateMachine.NOT_HANDLED;
            switch (msg.what) {
                case EVT_ACTIVATE:
                    ret = StateMachine.HANDLED;
                    transitionTo(mUnconnected1State);
                    break;
            }
            return ret;
        }
    }

    private class ActiveState extends State {
        @Override
        public void enter() {
            _setSampleState(SampleState.ActiveState);
        }
        @Override
        public boolean processMessage(@NonNull Message msg) {
            boolean ret = StateMachine.NOT_HANDLED;
            switch (msg.what) {
                case EVT_DEACTIVATE:
                    ret = StateMachine.HANDLED;
                    transitionTo(mInactiveState);
                    break;
            }
            return ret;
        }
    }

    private class UnconnectedState extends State {
        @Override
        public void enter() {
            _setSampleState(SampleState.UnconnectedState);
        }
        @Override
        public boolean processMessage(@NonNull Message msg) {
            boolean ret = StateMachine.NOT_HANDLED;
            switch (msg.what) {
                case EVT_CONNECT:
                    ret = StateMachine.HANDLED;
                    transitionTo(mConnecting1State);
                    break;
            }
            return ret;
        }
    }

    private class Unconnected1State extends State {
        @Override
        public void enter() {
            _setSampleState(SampleState.Unconnected1State);
        }
        @Override
        public boolean processMessage(@NonNull Message msg) {
            return StateMachine.NOT_HANDLED;
        }
    }

    private class Unconnected2State extends State {
        @Override
        public void enter() {
            _setSampleState(SampleState.Unconnected2State);
        }
        @Override
        public boolean processMessage(@NonNull Message msg) {
            return StateMachine.NOT_HANDLED;
        }
    }

    private class Unconnected3State extends State {
        @Override
        public void enter() {
            _setSampleState(SampleState.Unconnected3State);
        }
        @Override
        public boolean processMessage(@NonNull Message msg) {
            return StateMachine.NOT_HANDLED;
        }
    }

    private class ConnectingState extends State {
        @Override
        public void enter() {
            _setSampleState(SampleState.ConnectingState);
        }
        @Override
        public boolean processMessage(@NonNull Message msg) {
            return StateMachine.NOT_HANDLED;
        }
    }

    private class Connecting1State extends State {
        private static final int EVT_LOCAL_1 = EVT_LOCAL_BASE + 0x01;
        @Override
        public void enter() {
            _setSampleState(SampleState.Connecting1State);
            sendMessageDelayed(EVT_LOCAL_1, 1000);
        }
        @Override
        public void exit() {
            removeMessage(EVT_LOCAL_1);
        }
        @Override
        public boolean processMessage(@NonNull Message msg) {
            boolean ret = StateMachine.NOT_HANDLED;
            switch (msg.what) {
                case EVT_LOCAL_1:
                    ret = StateMachine.HANDLED;
                    transitionTo(mConnecting2State);
                    break;
            }
            return ret;
        }
    }

    private class Connecting2State extends State {
        private static final int EVT_LOCAL_1 = EVT_LOCAL_BASE + 0x01;
        @Override
        public void enter() {
            _setSampleState(SampleState.Connecting2State);
            sendMessageDelayed(EVT_LOCAL_1, 1000);
        }
        @Override
        public void exit() {
            removeMessage(EVT_LOCAL_1);
        }
        @Override
        public boolean processMessage(@NonNull Message msg) {
            boolean ret = StateMachine.NOT_HANDLED;
            switch (msg.what) {
                case EVT_LOCAL_1:
                    ret = StateMachine.HANDLED;
                    transitionTo(mConnecting3State);
                    break;
            }
            return ret;
        }
    }

    private class Connecting3State extends State {
        private static final int EVT_LOCAL_1 = EVT_LOCAL_BASE + 0x01;
        @Override
        public void enter() {
            _setSampleState(SampleState.Connecting3State);
            sendMessageDelayed(EVT_LOCAL_1, 1000);
        }
        @Override
        public void exit() {
            removeMessage(EVT_LOCAL_1);
        }
        @Override
        public boolean processMessage(@NonNull Message msg) {
            boolean ret = StateMachine.NOT_HANDLED;
            switch (msg.what) {
                case EVT_LOCAL_1:
                    ret = StateMachine.HANDLED;
                    transitionTo(mConnected3State);
                    break;
            }
            return ret;
        }
    }

    private class ConnectedState extends State {
        @Override
        public void enter() {
            _setSampleState(SampleState.ConnectedState);
        }
        @Override
        public boolean processMessage(@NonNull Message msg) {
            return StateMachine.NOT_HANDLED;
        }
    }

    private class Connected1State extends State {
        @Override
        public void enter() {
            _setSampleState(SampleState.Connected1State);
        }
        @Override
        public boolean processMessage(@NonNull Message msg) {
            return StateMachine.NOT_HANDLED;
        }
    }

    private class Connected2State extends State {
        @Override
        public void enter() {
            _setSampleState(SampleState.Connected2State);
        }
        @Override
        public boolean processMessage(@NonNull Message msg) {
            return StateMachine.NOT_HANDLED;
        }
    }

    private class Connected3State extends State {
        @Override
        public void enter() {
            _setSampleState(SampleState.Connected3State);
        }
        @Override
        public boolean processMessage(@NonNull Message msg) {
            return StateMachine.NOT_HANDLED;
        }
    }

    private class DisconnectingState extends State {
        @Override
        public void enter() {
            _setSampleState(SampleState.DisconnectingState);
        }
        @Override
        public boolean processMessage(@NonNull Message msg) {
            return StateMachine.NOT_HANDLED;
        }
    }

    private class Disconnecting1State extends State {
        @Override
        public void enter() {
            _setSampleState(SampleState.Disconnecting1State);
        }
        @Override
        public boolean processMessage(@NonNull Message msg) {
            return StateMachine.NOT_HANDLED;
        }
    }

    private class Disconnecting2State extends State {
        @Override
        public void enter() {
            _setSampleState(SampleState.Disconnecting2State);
        }
        @Override
        public boolean processMessage(@NonNull Message msg) {
            return StateMachine.NOT_HANDLED;
        }
    }

    private class Disconnecting3State extends State {
        @Override
        public void enter() {
            _setSampleState(SampleState.Disconnecting3State);
        }
        @Override
        public boolean processMessage(@NonNull Message msg) {
            return StateMachine.NOT_HANDLED;
        }
    }
}
