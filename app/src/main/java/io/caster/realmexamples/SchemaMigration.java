package io.caster.realmexamples;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

public class SchemaMigration implements RealmMigration {
    public static final long SCHEMA_VERSION = 2;

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

    }
}
