package io.caster.realmexamples;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class RealmExamplesApplication extends Application {
    private RealmConfiguration realmConfiguration;

    @Override
    public void onCreate() {
        super.onCreate();

        // Configure Realm for the application
        realmConfiguration = new RealmConfiguration.Builder(this)
                .name("examples.realm")
                .build();
        Realm.deleteRealm(realmConfiguration); //Deletes the realm,
        // use when you want a clean slate.

        // Make this Realm the default
        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
