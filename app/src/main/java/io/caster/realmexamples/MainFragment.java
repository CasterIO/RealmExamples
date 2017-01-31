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
import io.realm.RealmQuery;
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
            }
        });

        Log.d(TAG, "User has a task: " + (u.getTask() == null ? "false" : "true"));


        // Create a list of users so we can interact with them.
        for (int i = 0; i < 10; i++) {
            final int temp = i;
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    User user = realm.createObject(User.class, UUID.randomUUID().toString());
                    user.setFirstName("First" + temp);
                    user.setLastName("Last" + temp);
                }
            });
        }

        RealmResults<User> users = realm.where(User.class).findAll();
        Log.d(TAG, "Number of users: " + users.size());

        final User firstUser = users.get(0); // The first user in the list.

        // Create a bunch of tasks
        for (int i = 0; i < 10; i++) {
            final int temp = i;
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Task task = realm.createObject(Task.class, UUID.randomUUID().toString());
                    task.setTitle("TempTitle" + temp);
                    firstUser.getUpcomingTasks().add(task);
                }
            });
        }

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                firstUser.getUpcomingTasks().deleteFirstFromRealm();
            }
        });


        Log.d(TAG, "firstUser breakpoint");

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                firstUser.getUpcomingTasks().deleteLastFromRealm();
            }
        });

        Log.d(TAG, "firstUser second breakpoint");

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                firstUser.getUpcomingTasks().deleteFromRealm(1);
            }
        });

        Log.d(TAG, "firstUser third breakpoint");

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                firstUser.getUpcomingTasks().deleteAllFromRealm();
            }
        });

        Log.d(TAG, "firstUser fourth breakpoint");

        RealmResults<Task> tasks = realm.where(Task.class).findAll();

        Log.d(TAG, "number of tasks: " + tasks.size());

        // delete from realm results.
        tasks.deleteAllFromRealm();

        // Recreate the tasks
        // Create a bunch of tasks
        for (int i = 0; i < 10; i++) {
            final int temp = i;
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Task task = realm.createObject(Task.class, UUID.randomUUID().toString());
                    task.setTitle("TempTitle" + temp);
                    firstUser.getUpcomingTasks().add(task);
                }
            });
        }

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                firstUser.getUpcomingTasks().remove(2);
            }
        });

        Log.d(TAG, "number of tasks: " + firstUser.getUpcomingTasks().size());

        RealmResults<Task> allTasks = realm.where(Task.class).findAll();

        Log.d(TAG, "number of tasks in all result: " + allTasks.size());

        realm.delete(Task.class);

        RealmResults<Task> allTasks2 = realm.where(Task.class).findAll();

        Log.d(TAG, "number of tasks in all result after delete: " + allTasks2.size());

        // delete all data in the realm
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.deleteAll();
                // all data in realm is now gone
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
