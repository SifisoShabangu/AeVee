package com.example.commadersherpard.map;

import android.util.Log;

import microsoft.aspnet.signalr.client.ErrorCallback;
import microsoft.aspnet.signalr.client.InvalidStateException;
import microsoft.aspnet.signalr.client.LogLevel;
import microsoft.aspnet.signalr.client.Logger;
import microsoft.aspnet.signalr.client.SignalRFuture;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import microsoft.aspnet.signalr.client.transport.ClientTransport;
import microsoft.aspnet.signalr.client.transport.ServerSentEventsTransport;


public class HubConnectionFactory
{
    private static HubConnectionFactory mInstance= null;
    private HubConnection mConnection;
    private HubProxy serverHub;

    protected HubConnectionFactory(){}

    public static synchronized HubConnectionFactory getInstance(){
        if(null == mInstance){
            mInstance = new HubConnectionFactory();
        }
        return mInstance;
    }

    public HubConnection getHubConnection() {
        return mConnection;
    }

    public HubProxy getServerHub() {
        return serverHub;
    }

    public SignalRFuture<Void> connect(String url) {
        final SignalRFuture<Void> future = new SignalRFuture<Void>();
        createObjects(url, future);

        return future;
    }

    public void createObjects(String url, final SignalRFuture<Void> future){

        mConnection = new HubConnection(url, "", true, new Logger() {

            @Override
            public void log(String message, LogLevel level) {
                if (level == LogLevel.Critical) {
                    Log.d("SignalR", level.toString() + ": " + message);
                }
            }
        });

        try {
            serverHub = mConnection.createHubProxy(Globals.hub);
        } catch (InvalidStateException e) {
            Log.d("SignalR", "Error getting creating proxy: " + e.toString());
            future.triggerError(e);
        }

        ClientTransport transport = new ServerSentEventsTransport(mConnection.getLogger());
        SignalRFuture<Void> connectionFuture = mConnection.start(transport);

        mConnection.connected(new Runnable() {
            @Override
            public void run() {

                future.setResult(null);
            }
        });

        mConnection.error(new ErrorCallback() {
            @Override
            public void onError(Throwable error) {
                Log.d("SignalR", "Connection error: " + error.toString());

                if (!future.isDone()) {
                    future.triggerError(error);
                }
            }
        });
    }

    public void disconnect() {
        serverHub = null;
        mConnection.stop();
    }
}
