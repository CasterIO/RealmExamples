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
import io.realm.Case;
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

                // John
                User john = realm.createObject(User.class, UUID.randomUUID().toString());
                john.setFirstName("John");
                john.setLastName("Doe");

                // Jane
                User jane = realm.createObject(User.class, UUID.randomUUID().toString());
                jane.setFirstName("Jane");
                jane.setLastName("Doe");

                // Homework
                Task homework = realm.createObject(Task.class, UUID.randomUUID().toString());
                homework.setTitle("Homework");
                homework.setCompleted(true);
                john.getTasks().add(homework);

                // Dishes
                Task dishes = realm.createObject(Task.class, UUID.randomUUID().toString());
                dishes.setTitle("Dishes");
                john.getTasks().add(dishes);
                jane.getTasks().add(dishes);

                // Trash
                Task trash= realm.createObject(Task.class, UUID.randomUUID().toString());
                trash.setTitle("Trash");
                john.getTasks().add(trash);
                jane.getTasks().add(trash);

                // Exercise
                Task exercise = realm.createObject(Task.class, UUID.randomUUID().toString());
                exercise.setTitle("Exercise");
                exercise.setCompleted(true);
                jane.getTasks().add(exercise);

            }
        });


        RealmResults<User> usersWithCompletedTasks = realm.where(User.class)
                .equalTo("tasks.isCompleted", true)

                .findAll();

        Log.d(TAG, Integer.toString(usersWithCompletedTasks.size()));


        Log.d(TAG, "Users and their completed tasks");
        for (User u : usersWithCompletedTasks)  {
            for (Task t : u.getTasks()) {
                if (t.isCompleted()) {
                    Log.d(TAG, String.format("User: %s, Task: %s", u.getFirstName(), t.getTitle()));
                }
            }
        }


        RealmResults<User> janeCompletedTasks =
                realm.where(User.class)
                .equalTo("tasks.isCompleted", true)
                .equalTo("firstName", "Jane", Case.INSENSITIVE)
                .findAll();

        Log.d(TAG, "Users named Jane and their completed tasks");
        for (User u : janeCompletedTasks)  {
            for (Task t : u.getTasks()) {
                if (t.isCompleted()) {
                    Log.d(TAG, String.format("User: %s, Task: %s", u.getFirstName(), t.getTitle()));
                }
            }
        }

        RealmResults<User> logicalOrResults =
                realm.where(User.class)
                .beginsWith("tasks.title", "t", Case.INSENSITIVE)
                .or()
                .beginsWith("tasks.title", "h", Case.INSENSITIVE)
                .findAll();

        Log.d(TAG, "Users who have tasks that have titles that start with 't' or 'h'.");
        for (User u : logicalOrResults)  {
            for (Task t : u.getTasks()) {
                if (t.getTitle().toLowerCase().startsWith("t") || t.getTitle().toLowerCase().startsWith("h")) {
                    Log.d(TAG, String.format("User: %s, Task: %s", u.getFirstName(), t.getTitle()));
                }
            }
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
