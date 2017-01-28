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

                // Joe
                User joe = realm.createObject(User.class, UUID.randomUUID().toString());
                joe.setFirstName("Joe");
                joe.setLastName("Smith");

                // Jenny
                User jenny = realm.createObject(User.class, UUID.randomUUID().toString());
                jenny.setFirstName("Jenny");
                jenny.setLastName("Smith");

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
                exercise.setTitle("exercise");
                exercise.setCompleted(true);
                jane.getTasks().add(exercise);

                // Eliminate Clutter
                Task eliminateClutter = realm.createObject(Task.class, UUID.randomUUID().toString());
                eliminateClutter.setTitle("Eliminate Clutter");
                joe.getTasks().add(eliminateClutter);

                // Work on Report
                Task workOnReport = realm.createObject(Task.class, UUID.randomUUID().toString());
                workOnReport.setTitle("Work on TPS Report");
                jenny.getTasks().add(workOnReport);

            }
        });

        RealmResults<User> users =
                realm.where(User.class)
                        .contains("firstName", "J", Case.INSENSITIVE)
                        .beginGroup()
                            .beginsWith("tasks.title", "E", Case.INSENSITIVE)
                            .or()
                            .beginsWith("tasks.title", "W", Case.INSENSITIVE)
                        .endGroup()
                        .findAll();


        for (User u : users) {
            Log.d(TAG, String.format("User: %s", u.getFirstName()));
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
