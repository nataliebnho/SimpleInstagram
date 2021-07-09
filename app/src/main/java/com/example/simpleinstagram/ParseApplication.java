package com.example.simpleinstagram;

import android.app.Application;

import com.example.simpleinstagram.models.Post;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //Register your parse models
        ParseObject.registerSubclass(Post.class);

        // set applicationId, and server server based on the values in the back4app settings.
        // any network interceptors must be added with the Configuration Builder given this syntax
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("GPFNOBgH6r4KGSg0NoOPKbQ2iYru6LWzheOvXSZi") // should correspond to Application Id env variable
                .clientKey("vJztXRxF03AnPQ7re7MMCzsjSfMgoVl3TInN63Tq")  // should correspond to Client key env variable
                .server("https://parseapi.back4app.com")
                .build());

    }
}
