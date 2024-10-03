package Controller;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    // Database Name and Version
    private static final String DATABASE_NAME = "myDatabase.db";
    private static final int DATABASE_VERSION = 1;
    public static final String KEY_ID = "idChoseATrouver";
    public static final String KEY_XP = "experience";

    //TABLE choseATrouverPrixJust
    public static final String TABLE_CHOSE = "choseATrouverPrixJust";
    public static final String KEY_ID_CHOSE = "idChoseATrouver";
    public static final String KEY_NOM = "nom";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_RES = "resultatPasse";
    public static final String KEY_PRIX = "prix";
    public static final String KEY_PATH_TO_PICTURE = "PathToPicture";

    //TABLE motCryptex et motPendu
    public static final String TABLE_MOTCRYPTEX = "motCryptex";
    public static final String TABLE_MOTPENDU = "motPendu";
    public static final String KEY_ID_CRYPTEX = "idMotCryptex";
    public static final String KEY_ID_PENDU = "idMotPendu";
    public static final String KEY_MOT = "mot";
    public static final String KEY_DIFF = "diff";
    public static final String KEY_ID_INDICE = "idIndice";

    //TABLE Utilisateur
    public static final String TABLE_UTILISATEUR = "Utilisateur";
    public static final String KEY_NOMUSER = "pseudoUser";
    public static final String KEY_IDUSER = "idUtilisateur";
    public static final String KEY_XP = "experience";
    public static final String KEY_RES = "resultatPasse";
    public static final String KEY_EMAIL = "email";

    //TABLE infoSecu
    public static final String TABLE_INFOSECU = "info_Secu";
    public static final String KEY_IDSECU = "idinfoSecu";
    public static final String KEY_EMAILSECU = "email";
    public static final String KEY_PASSWORD = "password";
    //TABLE SON
    public static final String TABLE_SON = "Son";
    public static final String KEY_ID_SON = "idSon";
    public static final String KEY_PATH_SON = "cheminSon";


    // Constructor
    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Called when the database is created for the first time
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_CHOSE = "CREATE TABLE " + TABLE_CHOSE + " ("
                + KEY_ID_CHOSE + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_NOM + " TEXT NOT NULL UNIQUE, "
                + KEY_PRIX + " INTEGER NOT NULL, "
                + KEY_PATH_TO_PICTURE + " TEXT NOT NULL)";
        db.execSQL(CREATE_TABLE_CHOSE);


        String infoSecu_TABLE = "CREATE TABLE infoSecu("
                + "idInfo INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "psswrd TEXT NOT NULL,"
                + "email TEXT NOT NULL,"
                + "FOREIGN KEY (idIndice) REFERENCES Indice(idIndice))";
        db.execSQL(infoSecu_TABLE);

        String CREATE_TABLE_USER = "CREATE TABLE" + TABLE_UTILISATEUR + " ("
                + KEY_IDUSER + "idUtilisateur INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NOMUSER + "username TEXT NOT NULL UNIQUE,"
                + KEY_XP + "xp INTEGER NOT NULL,"
                + KEY_EMAIL + "email TEXT NOT NULL)";
        db.execSQL(CREATE_TABLE_USER);

        String Indice_TABLE = "CREATE TABLE Indice("
                + "idIndice INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "indice TEXT NOT NULL)";
        db.execSQL(Indice_TABLE);

        String MotCryptex_TABLE = "CREATE TABLE " +TABLE_MOTCRYPTEX+ "("
                + KEY_ID_CRYPTEX +" PRIMARY KEY AUTOINCREMENT,"
                + KEY_MOT+ "TEXT NOT NULL UNIQUE,"
                + KEY_DIFF+ "int NOT NULL,"
                + "FOREIGN KEY ("+KEY_ID_INDICE+") REFERENCES Indice("+KEY_ID_INDICE+"))";
        db.execSQL(MotCryptex_TABLE);

        String MotPendu_TABLE = "CREATE TABLE "+ TABLE_MOTPENDU+"("
                + KEY_ID_PENDU+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_MOT +"TEXT NOT NULL,"
                + KEY_DIFF+" int NOT NULL,"
                + "FOREIGN KEY ("+KEY_ID_INDICE+") REFERENCES Indice("+KEY_ID_INDICE+"))";
        db.execSQL(MotPendu_TABLE);

        String Son_TABLE = "CREATE TABLE "+ TABLE_SON +"("
                +  KEY_ID_SON +"INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_PATH_SON+ " TEXT NOT NULL,"
                + "FOREIGN KEY ("+KEY_ID_INDICE+") REFERENCES Indice("+KEY_ID_INDICE+"))";
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
