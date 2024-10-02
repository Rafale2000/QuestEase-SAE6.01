package bdd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    // Database Name and Version
    private static final String DATABASE_NAME = "myDatabase.db";
    private static final int DATABASE_VERSION = 1;

    // Constructor
    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Called when the database is created for the first time
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Define your table structure here
        String CREATE_TABLE = "CREATE TABLE choseATrouverPrixJust ("
                + "idChoseATrouver INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name TEXT, "
                + "prix int,"
                + "PathToPicture TEXT)";
        db.execSQL(CREATE_TABLE);

        String infoSecu_TABLE = "CREATE TABLE infoSecu("
                + "idInfo INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "psswrd TEXT NOT NULL,"
                + "email TEXT NOT NULL,"
                + "FOREIGN KEY (idIndice) REFERENCES Indice(idIndice))";
        db.execSQL(infoSecu_TABLE);

        String utilisateur_TABLE = "CREATE TABLE Utilisateur("
                + "idUtilisateur INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "psswrd TEXT NOT NULL,"
                + "email TEXT NOT NULL)";
        db.execSQL(utilisateur_TABLE);

        String Indice_TABLE = "CREATE TABLE Indice("
                + "idIndice INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "indice TEXT NOT NULL)";
        db.execSQL(Indice_TABLE);

        String MotCryptex_TABLE = "CREATE TABLE motCryptex("
                + "idCryptex INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "mot TEXT NOT NULL,"
                + "difficulte int NOT NULL,"
                + "FOREIGN KEY (idIndice) REFERENCES Indice(idIndice))";
        db.execSQL(MotCryptex_TABLE);

        String MotPendu_TABLE = "CREATE TABLE motPendu("
                + "idPendu INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "mot TEXT NOT NULL,"
                + "difficulte int NOT NULL,"
                + "FOREIGN KEY (idIndice) REFERENCES Indice(idIndice))";
        db.execSQL(MotPendu_TABLE);

        String Son_TABLE = "CREATE TABLE son("
                + "idSon INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "pathToSound TEXT NOT NULL,"
                + "FOREIGN KEY (idIndice) REFERENCES Indice(idIndice))";
        db.execSQL(Son_TABLE);

        String Resultat_TABLE = "CREATE TABLE Resultat("
                + "idResultat INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "mot TEXT NOT NULL,"
                + "difficulte int NOT NULL)";
        db.execSQL(Resultat_TABLE);
    }

    // Called when the database needs to be upgraded
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the old table and create a new one
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }
}
