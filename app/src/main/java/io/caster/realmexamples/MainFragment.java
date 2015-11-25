package io.caster.realmexamples;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Date;
import java.util.UUID;

import io.caster.realmexamples.models.Task;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;


public class MainFragment extends Fragment {

    private Realm realm;
    private LinearLayout viewContainer;

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
        viewContainer = (LinearLayout)v.findViewById(R.id.container);
        v.findViewById(R.id.do_work).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Change something
                realm.beginTransaction();
                Task task = realm.where(Task.class).findFirst();
                task.setTitle("Foo Bar");
                realm.commitTransaction();
            }
        });
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        realm = Realm.getDefaultInstance();

        // create some tasks
        realm.beginTransaction();
        Task t = realm.createObject(Task.class);
        t.setId(UUID.randomUUID().toString());
        t.setTitle("Goodbye" + new Date().getTime());
        t.setDescription("This is a description");
        realm.commitTransaction();

        RealmResults<Task> tasks = realm.allObjects(Task.class)
                .where()
                .contains("title","Goodbye")
                .findAll()          ;
        for(Task task : tasks) {
            Log.d("Realm", String.format("ID: %s, Title: %s, Desc: %s",
                    task.getId(), task.getTitle(), task.getDescription()));

            TextView tv = new TextView(getActivity());
            tv.setText(task.getTitle());
            viewContainer.addView(tv);
        }

        realm.addChangeListener(realmChangeListener);



    }

    RealmChangeListener realmChangeListener = new RealmChangeListener() {
        @Override
        public void onChange() {
            Log.d("Realm", "The realm has been updated");
            // Some data has changed.

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        // prevent memory leaks.
        realm.removeAllChangeListeners();
        realm.close();
    }
}
