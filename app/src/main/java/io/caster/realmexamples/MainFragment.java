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

        User u = realm.where(User.class).findFirst();


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

        // Create 5 users with 5 tasks
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                User u = realm.createObject(User.class);
                u.setFirstName("Bill");
                u.setLastName("Smith");
                u.setId(UUID.randomUUID().toString());

                Task t = realm.createObject(Task.class);
                t.setId(UUID.randomUUID().toString());
                t.setTitle("Go cycling");
                t.setDescription("Have fun too!");
                u.setTask(t);
            }
        });

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                User u = realm.createObject(User.class);
                u.setFirstName("Boskar");
                u.setLastName("Lints");
                u.setId(UUID.randomUUID().toString());

                Task t = realm.createObject(Task.class);
                t.setId(UUID.randomUUID().toString());
                t.setTitle("Buy new shoes");
                t.setDescription("Brown ones, to match the belt.");
                t.setCompleted(true); // Aha! Already did this one!
                u.setTask(t);
            }
        });

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                User u = realm.createObject(User.class);
                u.setFirstName("Suzi");
                u.setLastName("Lopez");
                u.setId(UUID.randomUUID().toString());

                Task t = realm.createObject(Task.class);
                t.setId(UUID.randomUUID().toString());
                t.setTitle("Review pull request for release.");
                t.setDescription("Take your time, it is important!");
                u.setTask(t);
            }
        });

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                User u = realm.createObject(User.class);
                u.setFirstName("Warren");
                u.setLastName("Chu");
                u.setId(UUID.randomUUID().toString());

                Task t = realm.createObject(Task.class);
                t.setId(UUID.randomUUID().toString());
                t.setTitle("Finish Budget");
                t.setDescription("Finalize runway calculations too.");
                u.setTask(t);
            }
        });

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                User u = realm.createObject(User.class);
                u.setFirstName("Wendy");
                u.setLastName("Jackson");
                u.setId(UUID.randomUUID().toString());

                Task t = realm.createObject(Task.class);
                t.setId(UUID.randomUUID().toString());
                t.setTitle("Finish Marketing Plan");
                t.setDescription("Include social account metrics in calculations.");
                u.setTask(t);

            }
        });

        RealmResults<Task> tasks = realm.where(Task.class)
                .beginsWith("title", "Finish")
                .findAll();

        Log.d(TAG, "Tasks that have a title starting with 'Finish': " + Integer.toString(tasks.size()));

        // Find all users who have a task title that starts with 'Finish'
        RealmResults<User> users = realm.where(User.class)
                .beginsWith("task.title", "Finish")
                .findAll();


        Log.d(TAG, "Users found that have a task title starting with 'Finish': " + Integer.toString(users.size()));

        // Find all users who have a tasks that are completed.
        RealmResults<User> usersWithCompletedTasks = realm.where(User.class)
                .equalTo("task.isCompleted", true)
                .findAll();

        Log.d(TAG, "Number of users with completed tasks: " + Integer.toString(usersWithCompletedTasks.size()));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
