package com.example.cartalkpush;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class FireBaseinstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onTokenRefresh() {      //핸드폰에서 가져오는 난수

        String token = FirebaseInstanceId.getInstance().getToken();
        Log.e(TAG, token);

        sendRegistrationToServer(token);

    }

    private void sendRegistrationToServer (String token) {

    }
}
