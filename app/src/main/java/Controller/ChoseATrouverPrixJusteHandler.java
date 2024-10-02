package Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import Model.bdd.MotCryptex;

public class ChoseATrouverPrixJusteHandler {
    private SQLiteOpenHelper dbHelper;
    public MotCryptexHandler(Context context){
        super(context);
        dbHelper = new MyDatabaseHelper(context);
    }

    public int addMoteCrypter (MotCryptex motCryptex){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, motCryptex.getMot());
        values.put(KEY_NOM, motCryptex.getMot());
}
