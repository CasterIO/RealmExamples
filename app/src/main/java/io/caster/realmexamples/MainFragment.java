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
import io.realm.RealmList;
import io.realm.RealmObject;
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

        final User u = realm.where(User.class).findFirst();


        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                User u = realm.where(User.class).findFirst();
                Task t = realm.createObject(Task.class);
                t.setId(UUID.randomUUID().toString());
                t.setTitle("Take out Recycling");
                t.setDescription("Do it before morning!");
                u.setTask(t);
            }
        });

        // Output the current task.
        Log.d(TAG, "Task Title: " + u.getTask().getTitle());

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                User user = realm.where(User.class).findFirst();
                Task task = user.getTask();
                user.setTask(null);
                RealmObject.deleteFromRealm(task);
                user.deleteFromRealm();
            }
        });

        Log.d(TAG, "User has a task: " + (u.getTask() == null ? "false" : "true"));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
