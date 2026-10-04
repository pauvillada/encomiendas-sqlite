package com.hfad.encomiendas;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Encomiendas.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "encomienda";

    public static final String COL_ID = "ID";
    public static final String COL_DESCRIPCION = "DESCRIPCION";
    public static final String COL_PESO = "PESO";
    public static final String COL_REM_NOMBRE = "REMITENTE_NOMBRE";
    public static final String COL_REM_TELEFONO = "REMITENTE_TELEFONO";
    public static final String COL_DEST_NOMBRE = "DESTINATARIO_NOMBRE";
    public static final String COL_DEST_TELEFONO = "DESTINATARIO_TELEFONO";
    public static final String COL_DEST_DIRECCION = "DESTINATARIO_DIRECCION";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_DESCRIPCION + " TEXT, "
                + COL_PESO + " REAL, "
                + COL_REM_NOMBRE + " TEXT, "
                + COL_REM_TELEFONO + " TEXT, "
                + COL_DEST_NOMBRE + " TEXT, "
                + COL_DEST_TELEFONO + " TEXT, "
                + COL_DEST_DIRECCION + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Inserta una encomienda. Devuelve true si se guardó bien.
    public boolean insertData(String descripcion, double peso,
                              String remNombre, String remTelefono,
                              String destNombre, String destTelefono,
                              String destDireccion) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_DESCRIPCION, descripcion);
        values.put(COL_PESO, peso);
        values.put(COL_REM_NOMBRE, remNombre);
        values.put(COL_REM_TELEFONO, remTelefono);
        values.put(COL_DEST_NOMBRE, destNombre);
        values.put(COL_DEST_TELEFONO, destTelefono);
        values.put(COL_DEST_DIRECCION, destDireccion);

        long result = db.insert(TABLE_NAME, null, values);
        return result != -1;
    }

    // Devuelve todas las encomiendas guardadas.
    public Cursor getAllData() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }
}