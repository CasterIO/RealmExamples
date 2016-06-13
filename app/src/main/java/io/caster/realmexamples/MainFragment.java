package io.caster.realmexamples;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.UUID;

import io.caster.realmexamples.models.Task;
import io.caster.realmexamples.models.User;
import io.realm.Realm;
import io.realm.RealmResults;


public class MainFragment extends Fragment {

    private static final String TAG = MainFragment.class.getSimpleName();

    private Realm realm;

    public MainFragment() {
    }

    public static MainFragment newInstance() {
        Bundle args = new Bundle();
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        realm = Realm.getDefaultInstance();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // Only create a user if we don't have one.
                if (realm.where(User.class).count() == 0) {
                    User u = realm.createObject(User.class);
                    u.setFirstName("Donn");
                    u.setLastName("Felker");
                    u.setId(UUID.randomUUID().toString());
                }
            }
        });

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                User u = realm.where(User.class).findFirst();
                Task t = realm.createObject(Task.class);
                t.setTitle("Test Task");
                t.setDescription("Foo Bar");
                u.getTasks().add(t);
            }
        });

        User u = realm.where(User.class).findFirst();
        Log.d(TAG, u.getTasks().get(0).getTitle() + " size - " + Integer.toString(u.getTasks().size()));

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                User u = realm.where(User.class).findFirst();
                Task t1 = realm.createObject(Task.class);
                t1.setTitle("Exercise More");
                u.getTasks().add(t1);

                Task t2 = realm.createObject(Task.class);
                t2.setTitle("No More Chips!");
                u.getTasks().add(t2);

                Task t3= realm.createObject(Task.class);
                t3.setTitle("Buy Veggies!");
                u.getTasks().add(t3);
            }
        });

        Log.d(TAG, Integer.toString(u.getTasks().size()));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
