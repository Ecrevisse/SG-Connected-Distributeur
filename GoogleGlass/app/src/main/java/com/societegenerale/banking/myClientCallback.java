package com.societegenerale.banking;

/**
 * Created by romeo on 04/10/2014.
 */
public class myClientCallback implements Client.ClientCallbacks {
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
}
