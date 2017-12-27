package xyz.leohan.websocketlib;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import org.java_websocket.handshake.ServerHandshake;

import java.nio.channels.NotYetConnectedException;

import static xyz.leohan.websocketlib.WebSocketService.INTENT_DATA_TIMEOUT_TIME;
import static xyz.leohan.websocketlib.WebSocketService.INTENT_DATA_URI;
import static xyz.leohan.websocketlib.WebSocketService.USER_CLOSE_CODE;

/**
 * Created by leo on 2017/6/13.
 */

public class WebSocketAndroidClient {
    private Context mContext;
    private WebSocketService.MyWebSocketServiceBinder binder;
    private String uri;
    private int timeoutTime;
    private static WebSocketAndroidClient instance;
    public static final String INTENT_WEBSOCKET_MSG = "webSocketMsg";
    private onWebSocketOpenListener mOnWebSocketOpenListener;

    public interface onWebSocketOpenListener {
        void onOpen(ServerHandshake handshakedata);
    }

    private WebSocketConnectionListener mListener = new WebSocketConnectionListener() {
        @Override
        public void onOpen(ServerHandshake handshakedata) {
            if (null != mOnWebSocketOpenListener) {
                mOnWebSocketOpenListener.onOpen(handshakedata);
            }
        }

        @Override
        public void onMessage(String message) {
            Intent intent = new Intent();
            intent.setAction(WebSocketReceiver.INTENT_ACTION_WEBSOCKET);
            intent.putExtra(INTENT_WEBSOCKET_MSG, message);
            mContext.sendBroadcast(intent);
        }

        @Override
        public void onClose(int code, String reason, boolean remote) {
            Log.i("onClose", "code:" + code + ",reason:" + reason + "remote:" + remote);
            if (code == USER_CLOSE_CODE && reason.equals("userClose")) {
                return;
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        if (null != binder) {
//                                if (binder.isConnecting()) {
//                                    break;
//                                } else {
//                                    binder.connect();
//                                    System.out.println("connecting.....");
//                                }
                            System.out.println("in loop");
                            if (binder.isClosed()) {
                                binder.connect();
                                System.out.println("connecting.....");
                            } else {
                                break;
                            }
                        }
                        try {

                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }

        @Override
        public void onError(Exception ex) {
            Log.i("websocket", ex.getMessage());
            if (binder != null) {
                binder.connect();
            }
        }
    };

    private WebSocketAndroidClient(Context context, String uri, int timeoutTime) {
        this.mContext = context;
        this.uri = uri;
        this.timeoutTime = timeoutTime;
    }

    /**
     * 连接
     */
    public void connect(onWebSocketOpenListener listener) {
        this.mOnWebSocketOpenListener = listener;
        Intent intent = new Intent(mContext, WebSocketService.class);
        intent.putExtra(INTENT_DATA_URI, uri);
        intent.putExtra(INTENT_DATA_TIMEOUT_TIME, timeoutTime);
        ServiceConnection connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                binder = (WebSocketService.MyWebSocketServiceBinder) service;
                binder.setConnectListener(mListener);
                binder.connect();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                binder.close();
            }
        };
        mContext.bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    /**
     * 初始化
     *
     * @param context 上下文
     * @param uri     websocket 地址
     */
    public static void init(Context context, String uri) {
        if (null == instance) {
            synchronized (WebSocketAndroidClient.class) {
                if (null == instance) {
                    instance = new WebSocketAndroidClient(context, uri, 0);
                }
            }
        }
    }

    /**
     * 初始化
     *
     * @param context     上下文
     * @param uri         websocket 地址
     * @param timeoutTime 超时时间
     */
    public static void init(Context context, String uri, int timeoutTime) {
        if (null == instance) {
            synchronized (WebSocketAndroidClient.class) {
                if (null == instance) {
                    instance = new WebSocketAndroidClient(context, uri, timeoutTime);
                }
            }
        }
    }

    /**
     * 发送数据
     *
     * @param msg 消息
     * @throws Exception {@link NotYetConnectedException} 当未连接时抛出异常
     */
    public void sendMsg(String msg) throws Exception {
        try {
            binder.sendMsg(msg);
        } catch (NotYetConnectedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 判断是否已连接
     *
     * @return 是否已连接
     */
    public boolean isConnected() {
        return binder.isConnecting();
    }

    /**
     * 断开连接
     */
    public void disConnect() {
        binder.close();
    }

    public static WebSocketAndroidClient getInstance() {
        if (null == instance) {
            throw new RuntimeException("WebSocketAndroidClient not init");
        }
        return instance;
    }

}
