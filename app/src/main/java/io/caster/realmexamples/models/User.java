package io.caster.realmexamples.models;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class User extends RealmObject {

    @PrimaryKey
    private String id;
    private String firstName;
    private String lastName;
    private Task task;
    private RealmList<Task> upcomingTasks;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public RealmList<Task> getUpcomingTasks() {
        return upcomingTasks;
    }

    public void setUpcomingTasks(RealmList<Task> upcomingTasks) {
        this.upcomingTasks = upcomingTasks;
    }

}
