package com.example.picar.database.migration;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.migration.Migration;


/*
*
* Classe Exemple de migration  ( Modification de colonne dans la base de donnée )
*
* */



public class Migration2_3 extends Migration {

    /**
     * Creates a new migration between {@code startVersion} and {@code endVersion}.
     *
     * @param startVersion The start version of the database.
     * @param endVersion The end version of the database after this migration is applied.
     */
    public Migration2_3(int startVersion, int endVersion) {
        super(startVersion, endVersion);
    }

    public static Migration2_3 newInstance() {
        return new Migration2_3(2, 3);
    }

    @Override
    public void migrate(SupportSQLiteDatabase database) {
        database.execSQL("ALTER TABLE feedEntrys ADD COLUMN favourite INTEGER DEFAULT 0 ");
    }
}
