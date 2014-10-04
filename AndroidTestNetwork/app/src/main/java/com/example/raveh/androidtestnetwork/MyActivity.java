package com.example.raveh.androidtestnetwork;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MyActivity extends Activity {

    TextView textResponse;
    EditText editTextAddress, editTextPort;
    Button buttonConnect, buttonClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        editTextAddress = (EditText)findViewById(R.id.address);
        editTextPort = (EditText)findViewById(R.id.port);
        buttonConnect = (Button)findViewById(R.id.connect);
        buttonClear = (Button)findViewById(R.id.clear);
        textResponse = (TextView)findViewById(R.id.response);

        buttonConnect.setOnClickListener(buttonConnectOnClickListener);

        buttonClear.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                textResponse.setText("");
            }});
    }

    OnClickListener buttonConnectOnClickListener =
            new OnClickListener(){

                @Override
                public void onClick(View arg0) {
                    Client myClientTask = new Client(new Client.ClientCallbacks() {
                        @Override
                        public void callbackReceiveUniqueId(int uniqueId) {
                            textResponse.setText(textResponse.getText() + " *Receive unique id : " + Integer.toString(uniqueId));
                        }

                        @Override
                        public void callbackReceiveIdOk() {
                            textResponse.setText(textResponse.getText() + " *Receive id ok ! ");
                        }

                        @Override
                        public void callbackReceiveAmountOk() {
                            textResponse.setText(textResponse.getText() + " *Receive amount ok ! ");
                        }
                    });
                    myClientTask.execute();
                    myClientTask.SendCode(4242);
                    //myClientTask.Stop();

                }};
}
