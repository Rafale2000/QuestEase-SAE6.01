package Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import Model.bdd.Utilisateur;

public class UtilisateurHandler extends MyDatabaseHelper {

    private SQLiteOpenHelper dbHelper;


    public UtilisateurHandler(Context context) {
        super(context);
        dbHelper = new MyDatabaseHelper(context);
    }

    public Utilisateur addUtilisateur (Utilisateur U){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, U.getUsername());
        values.put(KEY_NOM, U.getUsername());
        values.put(KEY_XP, U.getXp());
        values.put(KEY_RES, U.getResultatPasse());
        values.put(KEY_EMAIL, U.getEmail());
    }

    public Utilisateur getUtilisateur(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CHOSE, new String[] { KEY_ID,
                        KEY_NOM, KEY_XP, KEY_RES, KEY_EMAIL}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        assert cursor != null;
        return new Utilisateur(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getInt(2), cursor.getString(3),
                cursor.getString(4));

    }

    /**
     * supprime une chose à trouver
     * @param Util
     */
    public void deleteUtil(Utilisateur Util) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TABLE_CHOSE, KEY_ID + " = ?",
                new String[] { String.valueOf(Util.getId()) });
        db.close();
    }

    public int updateUtil(Utilisateur Util){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NOM,Util.getUsername());
        values.put(KEY_XP,Util.getXp());
        values.put(KEY_RES,Util.getResultatPasse());
        values.put(KEY_EMAIL,Util.getEmail());
        long updateId=db.update(TABLE_CHOSE,values,
                KEY_ID + " = ?",
                new String[]{String.valueOf(Util.getId())});
        db.close();
        return (int) updateId;
    }
}
