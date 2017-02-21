package io.caster.realmexamples.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Book extends RealmObject {

    @PrimaryKey
    private String id;

    private String title;
    private String isbn;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}
