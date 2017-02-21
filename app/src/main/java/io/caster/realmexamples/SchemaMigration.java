package io.caster.realmexamples;

import io.realm.DynamicRealm;
import io.realm.FieldAttribute;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

public class SchemaMigration implements RealmMigration {
    public static final long SCHEMA_VERSION = 4;

    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        RealmSchema schema = realm.getSchema();

        if (oldVersion == 0) {
            schema.get("User")
                    .addField("age", long.class);

            oldVersion++;
        }

        if (oldVersion == 1) {
            schema.get("User")
                    .removeField("age");

            oldVersion++;
        }

        if (oldVersion == 2) {
            RealmObjectSchema bookSchema = schema.create("Book")
                    .addField("id", String.class, FieldAttribute.PRIMARY_KEY)
                    .addField("title", String.class)
                    .addField("isbn", String.class);


            schema.get("User")
                    .addRealmObjectField("favoriteBook", bookSchema);

            oldVersion++;
        }

        if (oldVersion == 3) {
            // Create a dog
            RealmObjectSchema dogSchema = schema.create("Dog")
                    .addField("id", String.class, FieldAttribute.PRIMARY_KEY)
                    .addField("name", String.class, FieldAttribute.REQUIRED);

            schema.get("User")
                    .addRealmListField("dogs", dogSchema);

            oldVersion++;
        }
    }
}
