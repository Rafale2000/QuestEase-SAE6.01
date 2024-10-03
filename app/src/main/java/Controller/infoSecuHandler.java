package Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import Model.bdd.infoSecu;


public class infoSecuHandler extends MyDatabaseHelper{

    private SQLiteOpenHelper dbHelper;


    public infoSecuHandler(Context context) {
        super(context);
        dbHelper = new MyDatabaseHelper(context);
    }

    public int addInfoSecu (infoSecu I){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_IDSECU, I.getId());
        values.put(KEY_EMAILSECU, I.getEmail());
        values.put(KEY_PASSWORD, I.getPsswrd());

        long insertId = db.insert(TABLE_UTILISATEUR, null, values);

        return (int) insertId;
    }

    public infoSecu getInfoSecu(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_INFOSECU, new String[] { KEY_IDSECU,
                        KEY_EMAILSECU, KEY_PASSWORD}, KEY_IDSECU + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        assert cursor != null;
        return new infoSecu(cursor.getInt(0),
                cursor.getString(1), cursor.getString(2));

        }


    /**
     * supprime une chose à trouver
     * @param I
     */
    public void deleteSecu(infoSecu I) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TABLE_INFOSECU, KEY_IDSECU + " = ?",
                new String[] { String.valueOf(I.getId()) });
        db.close();
    }

    public int updateSecu(infoSecu I){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_EMAILSECU,I.getEmail());
        values.put(KEY_PASSWORD,I.getPsswrd());
        long updateId=db.update(TABLE_INFOSECU,values,
                KEY_IDSECU + " = ?",
                new String[]{String.valueOf(I.getId())});
        db.close();
        return (int) updateId;
    }

}
