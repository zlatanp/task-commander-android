package com.example.android.taskcommander.util;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Tea on 5/5/2018.
 */

public class SessionHandler {
    public static String loggedUid(){
        FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
        return fbUser.getUid();
    }


    public static String loggedEmail(){
        FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
        return fbUser.getEmail();
    }
}
