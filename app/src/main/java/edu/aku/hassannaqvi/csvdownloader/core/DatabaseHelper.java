package edu.aku.hassannaqvi.csvdownloader.core;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import edu.aku.hassannaqvi.csvdownloader.interfaces.WordsContract.WordsTable;
import edu.aku.hassannaqvi.csvdownloader.models.Words;

import static edu.aku.hassannaqvi.csvdownloader.core.CreateTable.DATABASE_NAME;
import static edu.aku.hassannaqvi.csvdownloader.core.CreateTable.DATABASE_VERSION;
import static edu.aku.hassannaqvi.csvdownloader.core.CreateTable.SQL_CREATE_WORDS;


/**
 * Created by hassan.naqvi on 11/30/2016.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private final String TAG = "DatabaseHelper";
    private final Context mContext;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_WORDS);
/*        db.execSQL(SQL_CREATE_USERS);

        db.execSQL(SQL_CREATE_FORMSSF);
        db.execSQL(SQL_CREATE_FORMSWF);
        db.execSQL(SQL_CREATE_FORMSEN);
        db.execSQL(SQL_CREATE_BL_RANDOM);
        db.execSQL(SQL_CREATE_DISTRICTS);
        db.execSQL(SQL_CREATE_VERSIONAPP);*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Toast.makeText(mContext, "Upgrading Database to version " + newVersion + "...", Toast.LENGTH_SHORT).show();
        Toast.makeText(mContext, "Upgrading Database to version " + newVersion + "...", Toast.LENGTH_SHORT).show();

        switch (oldVersion) {
            case 1:
                //     db.execSQL(SQL_CREATE_FORMSWF);
                Toast.makeText(mContext, "Follow-up Table Created!", Toast.LENGTH_SHORT).show();
            case 2:
//                db.execSQL(SQL_ALTER_FORMS_A05CODE);
                //               db.execSQL(SQL_ALTER_FORMS_A08);
        }
    }

   /* public Integer syncVersionApp(JSONObject VersionList) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(VersionAppContract.VersionAppTable.TABLE_NAME, null, null);
        long count = 0;
        try {
            JSONObject jsonObjectCC = ((JSONArray) VersionList.get(VersionAppContract.VersionAppTable.COLUMN_VERSION_PATH)).getJSONObject(0);
            VersionApp Vc = new VersionApp();
            Vc.Sync(jsonObjectCC);

            ContentValues values = new ContentValues();

            values.put(VersionAppContract.VersionAppTable.COLUMN_PATH_NAME, Vc.getPathname());
            values.put(VersionAppContract.VersionAppTable.COLUMN_VERSION_CODE, Vc.getVersioncode());
            values.put(VersionAppContract.VersionAppTable.COLUMN_VERSION_NAME, Vc.getVersionname());

            count = db.insert(VersionAppContract.VersionAppTable.TABLE_NAME, null, values);
            if (count > 0) count = 1;

        } catch (Exception ignored) {
        } finally {
            db.close();
        }

        return (int) count;
    }

    public VersionApp getVersionApp() {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = null;
        String[] columns = {
                VersionAppTable._ID,
                VersionAppTable.COLUMN_VERSION_CODE,
                VersionAppTable.COLUMN_VERSION_NAME,
                VersionAppTable.COLUMN_PATH_NAME
        };

        String whereClause = null;
        String[] whereArgs = null;
        String groupBy = null;
        String having = null;

        String orderBy = null;

        VersionApp allVC = new VersionApp();
        try {
            c = db.query(
                    VersionAppTable.TABLE_NAME,  // The table to query
                    columns,                   // The columns to return
                    whereClause,               // The columns for the WHERE clause
                    whereArgs,                 // The values for the WHERE clause
                    groupBy,                   // don't group the rows
                    having,                    // don't filter by row groups
                    orderBy                    // The sort order
            );
            while (c.moveToNext()) {
                allVC.hydrate(c);
            }
        } finally {
            if (c != null) {
                c.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return allVC;
    }*/


    public int syncWords(JSONObject words) {
        SQLiteDatabase db = this.getWritableDatabase();
        //db.delete(WordsTable.TABLE_NAME, null, null);
        int insertCount = 0;
        try {


            Words word = new Words();
            word.Hydrate(words);
            //      if(!WordExists(word.getId())) {
            ContentValues values = new ContentValues();

            values.put(WordsTable.COLUMN_ID, word.getId());
            values.put(WordsTable.COLUMN_WORD, word.getWord());
            values.put(WordsTable.COLUMN_TRANS, word.getTrans());
            values.put(WordsTable.COLUMN_S1, word.getSentcol1());
            values.put(WordsTable.COLUMN_S2, word.getSentcol2());
            values.put(WordsTable.COLUMN_S3, word.getSentcol3());
            values.put(WordsTable.COLUMN_GRADE, word.getWord());
            values.put(WordsTable.COLUMN_CATEGORY, word.getWord());
            values.put(WordsTable.COLUMN_POS, word.getWord());
            long rowID = db.insert(WordsTable.TABLE_NAME, null, values);
            if (rowID != -1) insertCount++;
            //   }


        } catch (Exception e) {
            Log.d(TAG, "syncWords: " + e);
            db.close();
        } finally {
            db.close();
        }
        return insertCount;
    }

    public boolean WordExists(String id) throws SQLException {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor mCursor = db.rawQuery("SELECT * FROM " + WordsTable.TABLE_NAME + " WHERE " + WordsTable.COLUMN_ID + "=?", new String[]{id});
        if (mCursor != null) {
            return mCursor.getCount() > 0;
        }
        return false;
    }

    public String GetBookmark(String id) throws SQLException {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor mCursor = db.rawQuery("SELECT " + WordsTable.COLUMN_BOOKMARK + " FROM " + WordsTable.TABLE_NAME + " WHERE " + WordsTable.COLUMN_ID + "=?", new String[]{id});
        mCursor.moveToFirst();
        if (mCursor != null) {
            //  Toast.makeText(mContext, "BM =" + mCursor.getString(mCursor.getColumnIndex(WordsTable.COLUMN_BOOKMARK)), Toast.LENGTH_SHORT).show();
            return mCursor.getString(mCursor.getColumnIndex(WordsTable.COLUMN_BOOKMARK));
        }
        return "0";
    }

    public boolean SetBookmark(String id, String bm) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor mCursor = db.rawQuery("UPDATE " + WordsTable.TABLE_NAME + " SET " + WordsTable.COLUMN_BOOKMARK + " = ? WHERE " + WordsTable.COLUMN_ID + "=?", new String[]{bm, id});
        mCursor.moveToFirst();
        if (mCursor != null) {
            Toast.makeText(mContext, "SBM: " + mCursor.getCount(), Toast.LENGTH_SHORT).show();
            return mCursor.getCount() > 0;
        }
        Toast.makeText(mContext, "SBM: false", Toast.LENGTH_SHORT).show();

        return false;
    }

    public String getViews(String id) throws SQLException {
        SQLiteDatabase db = this.getReadableDatabase();
        Log.d(TAG, "getViews: " + id);
        Cursor mCursor = db.rawQuery("SELECT " + WordsTable.COLUMN_VIEWS +
                " FROM " + WordsTable.TABLE_NAME +
                " WHERE " + WordsTable.COLUMN_ID + "=?", new String[]{id});
        if (mCursor != null) {
            mCursor.moveToFirst();
            return mCursor.getString(mCursor.getColumnIndex(WordsTable.COLUMN_VIEWS));

        }
        return "0";
    }

    public boolean incrementViews(String id) throws SQLException {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor mCursor = db.rawQuery("UPDATE " + WordsTable.TABLE_NAME + " SET " + WordsTable.COLUMN_VIEWS + " = " + WordsTable.COLUMN_VIEWS + " + 1 WHERE " + WordsTable.COLUMN_ID + "=?", new String[]{id});
        if (mCursor != null) {
            return mCursor.getCount() > 0;
        }
        return false;
    }

    public List<Words> getAllWords(Boolean bm) throws JSONException {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = null;
        String[] columns = {
                WordsTable.COLUMN_ID,
                WordsTable.COLUMN_WORD,
                WordsTable.COLUMN_TRANS,
                WordsTable.COLUMN_S1,
                WordsTable.COLUMN_S2,
                WordsTable.COLUMN_S3,
                WordsTable.COLUMN_GRADE,
                WordsTable.COLUMN_CATEGORY,
                WordsTable.COLUMN_POS,
                WordsTable.COLUMN_BOOKMARK
        };

        String whereClause;
        String[] whereArgs;
        String groupBy = null;
        String having = null;

        String orderBy =
                WordsTable.COLUMN_VIEWS + " ASC";

        if (!bm) {
            whereClause = null;
            whereArgs = null;
        } else {
            whereClause = WordsTable.COLUMN_BOOKMARK + "=?";
            whereArgs = new String[]{"1"};
        }

        List<Words> allWords = new ArrayList<Words>();
        try {
            c = db.query(
                    WordsTable.TABLE_NAME,  // The table to query
                    columns,                   // The columns to return
                    whereClause,               // The columns for the WHERE clause
                    whereArgs,                 // The values for the WHERE clause
                    groupBy,                   // don't group the rows
                    having,                    // don't filter by row groups
                    orderBy                    // The sort order
            );
            while (c.moveToNext()) {
                Words words = new Words();
                allWords.add(words.Hydrate(c));
            }
        } finally {
            if (c != null) {
                c.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return allWords;
    }




/*
    public boolean Login(String username, String password) throws SQLException {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor mCursor = db.rawQuery("SELECT * FROM " + UsersTable.TABLE_NAME + " WHERE " + UsersTable.COLUMN_USERNAME + "=? AND " + UsersTable.COLUMN_PASSWORD + "=?", new String[]{username, password});
        if (mCursor != null) {
            return mCursor.getCount() > 0;
        }
        return false;
    }

    public boolean checkUsers() throws SQLException {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor mCursor = db.rawQuery("SELECT * FROM " + UsersTable.TABLE_NAME, null, null);
        if (mCursor != null) {
            return mCursor.getCount() > 0;
        }
        return false;
    }


    public Long addFormSL(FormsSL formsl) {

        // Gets the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

// Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(FormsSLContract.FormsSLTable.COLUMN_PROJECT_NAME, formsl.getProjectName());
        values.put(FormsSLTable.COLUMN_UID, formsl.get_UID());
        values.put(FormsSLTable.COLUMN_SYSDATE, formsl.getSysdate());
        //     values.put(FormsSLTable.COLUMN_ISTATUS, formsl.getIstatus());
        //    values.put(FormsSLTable.COLUMN_ISTATUS96x, formsl.getIstatus96x());
        *//*values.put(FormsSLTable.COLUMN_ENDINGDATETIME, formsl.getEndingdatetime());*//*
        values.put(FormsSLTable.COLUMN_GPSLAT, formsl.getGpsLat());
        values.put(FormsSLTable.COLUMN_GPSLNG, formsl.getGpsLng());
        values.put(FormsSLTable.COLUMN_GPSDATE, formsl.getGpsDT());
        values.put(FormsSLTable.COLUMN_GPSACC, formsl.getGpsAcc());
        values.put(FormsSLTable.COLUMN_DEVICETAGID, formsl.getDevicetagID());
        values.put(FormsSLTable.COLUMN_DEVICEID, formsl.getDeviceID());
        values.put(FormsSLTable.COLUMN_APPVERSION, formsl.getAppversion());

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                FormsSLTable.TABLE_NAME,
                FormsSLTable.COLUMN_NAME_NULLABLE,
                values);
        return newRowId;
    }

    public Long addFormSF(FormsSF formSF) {

        // Gets the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

// Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(FormsSFTable.COLUMN_PROJECT_NAME, formSF.getProjectName());
        values.put(FormsSFTable.COLUMN_UID, formSF.get_UID());
        values.put(FormsSFTable.COLUMN_SYSDATE, formSF.getSysdate());
    *//*    values.put(FormsSFTable.COLUMN_ISTATUS, formSF.getIstatus());
        values.put(FormsSFTable.COLUMN_ISTATUS96x, formSF.getIstatus96x());*//*
//        values.put(FormsSFTable.COLUMN_ENDINGDATETIME, formSF.getEndingdatetime());
        values.put(FormsSFTable.COLUMN_GPSLAT, formSF.getGpsLat());
        values.put(FormsSFTable.COLUMN_GPSLNG, formSF.getGpsLng());
        values.put(FormsSFTable.COLUMN_GPSDATE, formSF.getGpsDT());
        values.put(FormsSFTable.COLUMN_GPSACC, formSF.getGpsAcc());
        values.put(FormsSFTable.COLUMN_DEVICETAGID, formSF.getDevicetagID());
        values.put(FormsSFTable.COLUMN_DEVICEID, formSF.getDeviceID());
        values.put(FormsSFTable.COLUMN_APPVERSION, formSF.getAppversion());

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                FormsSFTable.TABLE_NAME,
                FormsSFTable.COLUMN_NAME_NULLABLE,
                values);
        return newRowId;
    }

    public Long addFormS3(FormsEN formS3) {

        // Gets the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

// Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(FormsS3Table.COLUMN_PROJECT_NAME, formS3.getProjectName());
        values.put(FormsS3Table.COLUMN_UID, formS3.get_UID());
        values.put(FormsS3Table.COLUMN_SYSDATE, formS3.getSysdate());
    *//*    values.put( FormsS3Table.COLUMN_ISTATUS, formSF.getIstatus());
        values.put( FormsS3Table.COLUMN_ISTATUS96x, formSF.getIstatus96x());*//*
//        values.put( FormsS3Table.COLUMN_ENDINGDATETIME, formSF.getEndingdatetime());
        values.put(FormsS3Table.COLUMN_GPSLAT, formS3.getGpsLat());
        values.put(FormsS3Table.COLUMN_GPSLNG, formS3.getGpsLng());
        values.put(FormsS3Table.COLUMN_GPSDATE, formS3.getGpsDT());
        values.put(FormsS3Table.COLUMN_GPSACC, formS3.getGpsAcc());
        values.put(FormsS3Table.COLUMN_DEVICETAGID, formS3.getDevicetagID());
        values.put(FormsS3Table.COLUMN_DEVICEID, formS3.getDeviceID());
        values.put(FormsS3Table.COLUMN_APPVERSION, formS3.getAppversion());
        values.put(FormsS3Table.COLUMN_EN, formS3.getSecEN());

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                FormsS3Table.TABLE_NAME,
                FormsS3Table.COLUMN_NAME_NULLABLE,
                values);
        return newRowId;
    }

    public Long addFormWF(FormsWF formWF) {

        // Gets the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

// Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(FormsWFTable.COLUMN_PROJECT_NAME, formWF.getProjectName());
        values.put(FormsWFTable.COLUMN_UID, formWF.get_UID());
        values.put(FormsWFTable.COLUMN_SYSDATE, formWF.getSysdate());
    *//*    values.put(FormsWFTable.COLUMN_ISTATUS, formSF.getIstatus());
        values.put(FormsWFTable.COLUMN_ISTATUS96x, formSF.getIstatus96x());*//*
//        values.put(FormsWFTable.COLUMN_ENDINGDATETIME, formSF.getEndingdatetime());
        values.put(FormsWFTable.COLUMN_GPSLAT, formWF.getGpsLat());
        values.put(FormsWFTable.COLUMN_GPSLNG, formWF.getGpsLng());
        values.put(FormsWFTable.COLUMN_GPSDATE, formWF.getGpsDT());
        values.put(FormsWFTable.COLUMN_GPSACC, formWF.getGpsAcc());
        values.put(FormsWFTable.COLUMN_DEVICETAGID, formWF.getDevicetagID());
        values.put(FormsWFTable.COLUMN_DEVICEID, formWF.getDeviceID());
        values.put(FormsWFTable.COLUMN_APPVERSION, formWF.getAppversion());
        values.put(FormsWFTable.COLUMN_SWFA01, formWF.getsWFA01());
        values.put(FormsWFTable.COLUMN_SWFA02, formWF.getsWFA02());
        values.put(FormsWFTable.COLUMN_SWFA03, formWF.getsWFA03());
        values.put(FormsWFTable.COLUMN_SWFA04, formWF.getsWFA04());
        values.put(FormsWFTable.COLUMN_SWFA05, formWF.getsWFA05());
        values.put(FormsWFTable.COLUMN_SWFB01, formWF.getsWFB01());
        values.put(FormsWFTable.COLUMN_SWFB02, formWF.getsWFB02());
        values.put(FormsWFTable.COLUMN_SWFC, formWF.getsWFC());
        values.put(FormsWFTable.COLUMN_SWFD, formWF.getsWFD());
        values.put(FormsWFTable.COLUMN_SWFE, formWF.getsWFE());
        values.put(FormsWFTable.COLUMN_SWFF, formWF.getsWFF());

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                FormsWFTable.TABLE_NAME,
                FormsWFTable.COLUMN_NAME_NULLABLE,
                values);
        return newRowId;
    }


    public int updateWordViews() {
        SQLiteDatabase db = this.getReadableDatabase();

// New value for one column
        ContentValues values = new ContentValues();
        values.put(WordsTable.COLUMN_VIEWS, WordsTable.COLUMN_VIEWS);

// Which row to update, based on the ID
        String selection = FormsSLTable._ID + " = ?";
        String[] selectionArgs = {String.valueOf(MainApp.formsSL.get_ID())};

        int count = db.update(FormsSLTable.TABLE_NAME,
                values,
                selection,
                selectionArgs);
        return count;
    }

    public int updateFormSFID() {
        SQLiteDatabase db = this.getReadableDatabase();

// New value for one column
        ContentValues values = new ContentValues();
        values.put(FormsSLTable.COLUMN_UID, MainApp.formsSF.get_UID());

// Which row to update, based on the ID
        String selection = FormsSLTable._ID + " = ?";
        String[] selectionArgs = {String.valueOf(MainApp.formsSF.get_ID())};

        int count = db.update(FormsSLTable.TABLE_NAME,
                values,
                selection,
                selectionArgs);
        return count;
    }


    public Collection<FormsSL> getAllFormsSL() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = null;
        String[] columns = {
                FormsSLTable._ID,
                FormsSLTable.COLUMN_UID,
                FormsSLTable.COLUMN_SYSDATE,
*//*
                FormsSLTable.COLUMN_ISTATUS,
*//*
                FormsSLTable.COLUMN_SL,
                FormsSLTable.COLUMN_GPSLAT,
                FormsSLTable.COLUMN_GPSLNG,
                FormsSLTable.COLUMN_GPSDATE,
                FormsSLTable.COLUMN_GPSACC,
                FormsSLTable.COLUMN_DEVICETAGID,
                FormsSLTable.COLUMN_DEVICEID,
                FormsSLTable.COLUMN_APPVERSION,

        };
        String whereClause = null;
        String[] whereArgs = null;
        String groupBy = null;
        String having = null;

        String orderBy =
                FormsSLTable.COLUMN_ID + " ASC";

        Collection<FormsSL> allFormsSL = new ArrayList<FormsSL>();
        try {
            c = db.query(
                    FormsSLTable.TABLE_NAME,  // The table to query
                    columns,                   // The columns to return
                    whereClause,               // The columns for the WHERE clause
                    whereArgs,                 // The values for the WHERE clause
                    groupBy,                   // don't group the rows
                    having,                    // don't filter by row groups
                    orderBy                    // The sort order
            );
            while (c.moveToNext()) {
                FormsSL formsSL = new FormsSL();
                allFormsSL.add(formsSL.Hydrate(c));
            }
        } finally {
            if (c != null) {
                c.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return allFormsSL;
    }

    public Collection<FormsSF> getAllFormsSF() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = null;
        String[] columns = {
                FormsSFTable._ID,
                FormsSFTable.COLUMN_UID,
                FormsSFTable.COLUMN_SYSDATE,
*//*
                FormsSFTable.COLUMN_ISTATUS,
*//*
                FormsSFTable.COLUMN_SF,
                FormsSFTable.COLUMN_GPSLAT,
                FormsSFTable.COLUMN_GPSLNG,
                FormsSFTable.COLUMN_GPSDATE,
                FormsSFTable.COLUMN_GPSACC,
                FormsSFTable.COLUMN_DEVICETAGID,
                FormsSFTable.COLUMN_DEVICEID,
                FormsSFTable.COLUMN_APPVERSION,

        };
        String whereClause = null;
        String[] whereArgs = null;
        String groupBy = null;
        String having = null;

        String orderBy =
                FormsSFTable.COLUMN_ID + " ASC";

        Collection<FormsSF> allFormsSF = new ArrayList<FormsSF>();
        try {
            c = db.query(
                    FormsSFTable.TABLE_NAME,  // The table to query
                    columns,                   // The columns to return
                    whereClause,               // The columns for the WHERE clause
                    whereArgs,                 // The values for the WHERE clause
                    groupBy,                   // don't group the rows
                    having,                    // don't filter by row groups
                    orderBy                    // The sort order
            );
            while (c.moveToNext()) {
                FormsSF formsSF = new FormsSF();
                allFormsSF.add(formsSF.Hydrate(c));
            }
        } finally {
            if (c != null) {
                c.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return allFormsSF;
    }

    public Collection<FormsEN> getAllFormsS3() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = null;
        String[] columns = {
                FormsS3Table._ID,
                FormsS3Table.COLUMN_UID,
                FormsS3Table.COLUMN_SYSDATE,
*//*
                 FormsS3Table.COLUMN_ISTATUS,
*//*
                FormsS3Table.COLUMN_EN,
                FormsS3Table.COLUMN_GPSLAT,
                FormsS3Table.COLUMN_GPSLNG,
                FormsS3Table.COLUMN_GPSDATE,
                FormsS3Table.COLUMN_GPSACC,
                FormsS3Table.COLUMN_DEVICETAGID,
                FormsS3Table.COLUMN_DEVICEID,
                FormsS3Table.COLUMN_APPVERSION,

        };
        String whereClause = null;
        String[] whereArgs = null;
        String groupBy = null;
        String having = null;

        String orderBy =
                FormsS3Table.COLUMN_ID + " ASC";

        Collection<FormsEN> allFormsEN = new ArrayList<FormsEN>();
        try {
            c = db.query(
                    FormsS3Table.TABLE_NAME,  // The table to query
                    columns,                   // The columns to return
                    whereClause,               // The columns for the WHERE clause
                    whereArgs,                 // The values for the WHERE clause
                    groupBy,                   // don't group the rows
                    having,                    // don't filter by row groups
                    orderBy                    // The sort order
            );
            while (c.moveToNext()) {
                FormsEN formsEN = new FormsEN();
                allFormsEN.add(formsEN.Hydrate(c));
            }
        } finally {
            if (c != null) {
                c.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return allFormsEN;
    }

    public Collection<FormsWF> getAllFormsWF() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = null;
        String[] columns = {
                FormsWFTable._ID,
                FormsWFTable.COLUMN_UID,
                FormsWFTable.COLUMN_SYSDATE,
*//*
                FormsWFTable.COLUMN_ISTATUS,
*//*
                FormsWFTable.COLUMN_SWFA01,
                FormsWFTable.COLUMN_SWFA02,
                FormsWFTable.COLUMN_SWFA03,
                FormsWFTable.COLUMN_SWFA04,
                FormsWFTable.COLUMN_SWFA05,
                FormsWFTable.COLUMN_SWFB01,
                FormsWFTable.COLUMN_SWFB02,
                FormsWFTable.COLUMN_SWFC,
                FormsWFTable.COLUMN_SWFD,
                FormsWFTable.COLUMN_SWFE,
                FormsWFTable.COLUMN_SWFF,
                FormsWFTable.COLUMN_GPSLAT,
                FormsWFTable.COLUMN_GPSLNG,
                FormsWFTable.COLUMN_GPSDATE,
                FormsWFTable.COLUMN_GPSACC,
                FormsWFTable.COLUMN_DEVICETAGID,
                FormsWFTable.COLUMN_DEVICEID,
                FormsWFTable.COLUMN_APPVERSION,

        };
        String whereClause = null;
        String[] whereArgs = null;
        String groupBy = null;
        String having = null;

        String orderBy =
                FormsWFTable.COLUMN_ID + " ASC";

        Collection<FormsWF> allFormsWF = new ArrayList<FormsWF>();
        try {
            c = db.query(
                    FormsWFTable.TABLE_NAME,  // The table to query
                    columns,                   // The columns to return
                    whereClause,               // The columns for the WHERE clause
                    whereArgs,                 // The values for the WHERE clause
                    groupBy,                   // don't group the rows
                    having,                    // don't filter by row groups
                    orderBy                    // The sort order
            );
            while (c.moveToNext()) {
                FormsWF formsWF = new FormsWF();
                allFormsWF.add(formsWF.Hydrate(c));
            }
        } finally {
            if (c != null) {
                c.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return allFormsWF;
    }


    public Collection<FormsSF> checkFormsSFExist() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = null;
        String[] columns = {
                FormsSFTable._ID,
                FormsSFTable.COLUMN_UID,
                FormsSFTable.COLUMN_SYSDATE,
//                FormsSFTable.COLUMN_ISTATUS,
                FormsSFTable.COLUMN_SF,
                FormsSFTable.COLUMN_GPSLAT,
                FormsSFTable.COLUMN_GPSLNG,
                FormsSFTable.COLUMN_GPSDATE,
                FormsSFTable.COLUMN_GPSACC,
                FormsSFTable.COLUMN_DEVICETAGID,
                FormsSFTable.COLUMN_DEVICEID,
                FormsSFTable.COLUMN_APPVERSION,

        };
        String whereClause = null;
        String[] whereArgs = null;
        String groupBy = null;
        String having = null;

        String orderBy =
                FormsSFTable.COLUMN_ID + " ASC";

        Collection<FormsSF> allFormsSF = new ArrayList<FormsSF>();
        try {
            c = db.query(
                    FormsSFTable.TABLE_NAME,  // The table to query
                    columns,                   // The columns to return
                    whereClause,               // The columns for the WHERE clause
                    whereArgs,                 // The values for the WHERE clause
                    groupBy,                   // don't group the rows
                    having,                    // don't filter by row groups
                    orderBy                    // The sort order
            );
            while (c.moveToNext()) {
                FormsSF formsSF = new FormsSF();
                allFormsSF.add(formsSF.Hydrate(c));
            }
        } finally {
            if (c != null) {
                c.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return allFormsSF;
    }

    public Collection<FormsEN> checkFormsS3Exist() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = null;
        String[] columns = {
                FormsS3Table._ID,
                FormsS3Table.COLUMN_UID,
                FormsS3Table.COLUMN_SYSDATE,
//                 FormsS3Table.COLUMN_ISTATUS,
                FormsS3Table.COLUMN_EN,
                FormsS3Table.COLUMN_GPSLAT,
                FormsS3Table.COLUMN_GPSLNG,
                FormsS3Table.COLUMN_GPSDATE,
                FormsS3Table.COLUMN_GPSACC,
                FormsS3Table.COLUMN_DEVICETAGID,
                FormsS3Table.COLUMN_DEVICEID,
                FormsS3Table.COLUMN_APPVERSION,

        };
        String whereClause = null;
        String[] whereArgs = null;
        String groupBy = null;
        String having = null;

        String orderBy =
                FormsS3Table.COLUMN_ID + " ASC";

        Collection<FormsEN> allFormsEN = new ArrayList<FormsEN>();
        try {
            c = db.query(
                    FormsS3Table.TABLE_NAME,  // The table to query
                    columns,                   // The columns to return
                    whereClause,               // The columns for the WHERE clause
                    whereArgs,                 // The values for the WHERE clause
                    groupBy,                   // don't group the rows
                    having,                    // don't filter by row groups
                    orderBy                    // The sort order
            );
            while (c.moveToNext()) {
                FormsEN formsEN = new FormsEN();
                allFormsEN.add(formsEN.Hydrate(c));
            }
        } finally {
            if (c != null) {
                c.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return allFormsEN;
    }

    public Collection<FormsWF> checkFormsWFExist() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = null;
        String[] columns = {
                FormsWFTable._ID,
                FormsWFTable.COLUMN_UID,
                FormsWFTable.COLUMN_SYSDATE,
//              FormsWFTable.COLUMN_ISTATUS,
                FormsWFTable.COLUMN_SWFA01,
                FormsWFTable.COLUMN_SWFA02,
                FormsWFTable.COLUMN_SWFA03,
                FormsWFTable.COLUMN_SWFA04,
                FormsWFTable.COLUMN_SWFA05,
                FormsWFTable.COLUMN_SWFB01,
                FormsWFTable.COLUMN_SWFB02,
                FormsWFTable.COLUMN_SWFC,
                FormsWFTable.COLUMN_SWFD,
                FormsWFTable.COLUMN_SWFE,
                FormsWFTable.COLUMN_SWFF,
                FormsWFTable.COLUMN_GPSLAT,
                FormsWFTable.COLUMN_GPSLNG,
                FormsWFTable.COLUMN_GPSDATE,
                FormsWFTable.COLUMN_GPSACC,
                FormsWFTable.COLUMN_DEVICETAGID,
                FormsWFTable.COLUMN_DEVICEID,
                FormsWFTable.COLUMN_APPVERSION,

        };
        String whereClause = null;
        String[] whereArgs = null;
        String groupBy = null;
        String having = null;

        String orderBy =
                FormsWFTable.COLUMN_ID + " ASC";

        Collection<FormsWF> allFormsWF = new ArrayList<FormsWF>();
        try {
            c = db.query(
                    FormsWFTable.TABLE_NAME,  // The table to query
                    columns,                   // The columns to return
                    whereClause,               // The columns for the WHERE clause
                    whereArgs,                 // The values for the WHERE clause
                    groupBy,                   // don't group the rows
                    having,                    // don't filter by row groups
                    orderBy                    // The sort order
            );
            while (c.moveToNext()) {
                FormsWF formsWF = new FormsWF();
                allFormsWF.add(formsWF.Hydrate(c));
            }
        } finally {
            if (c != null) {
                c.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return allFormsWF;
    }


    public Collection<FormsSL> getUnsyncedFormsSL() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = null;
        String[] columns = {
                FormsSLTable._ID,
                FormsSLTable.COLUMN_UID,
                FormsSLTable.COLUMN_SYSDATE,
                *//*               FormsSLTable.COLUMN_ISTATUS,
                               FormsSLTable.COLUMN_ISTATUS96x,
                             FormsSLTable.COLUMN_ENDINGDATETIME,  *//*
                FormsSLTable.COLUMN_SL,
                FormsSLTable.COLUMN_GPSLAT,
                FormsSLTable.COLUMN_GPSLNG,
                FormsSLTable.COLUMN_GPSDATE,
                FormsSLTable.COLUMN_GPSACC,
                FormsSLTable.COLUMN_DEVICETAGID,
                FormsSLTable.COLUMN_DEVICEID,
                FormsSLTable.COLUMN_APPVERSION,
        };

        String whereClause = FormsSLTable.COLUMN_SYNCED + " is null OR " + FormsSLTable.COLUMN_SYNCED + " == '' ";
        // String whereClause = null;
        String[] whereArgs = null;
        String groupBy = null;
        String having = null;
        String orderBy = FormsSLTable.COLUMN_ID + " ASC";

        Collection<FormsSL> allFormsSL = new ArrayList<>();
        try {
            c = db.query(
                    FormsSLTable.TABLE_NAME,  // The table to query
                    columns,                   // The columns to return
                    whereClause,               // The columns for the WHERE clause
                    whereArgs,                 // The values for the WHERE clause
                    groupBy,                   // don't group the rows
                    having,                    // don't filter by row groups
                    orderBy                    // The sort order
            );
            while (c.moveToNext()) {
                Log.d(TAG, "getUnsyncedForms: " + c.getCount());
                FormsSL formsSL = new FormsSL();
                allFormsSL.add(formsSL.Hydrate(c));
            }
        } catch (SQLiteException e) {

            Toast.makeText(mContext, "ERROR: " + e.getMessage(), Toast.LENGTH_SHORT).show();

        } finally {
            if (c != null) {
                c.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return allFormsSL;
    }

    public Collection<FormsSF> getUnsyncedFormsSF() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = null;
        String[] columns = {
                FormsSFTable._ID,
                FormsSFTable.COLUMN_UID,
                FormsSFTable.COLUMN_SYSDATE,
//                FormsSFTable.COLUMN_ISTATUS,
                //               FormsSFTable.COLUMN_ISTATUS96x,
//                FormsSFTable.COLUMN_ENDINGDATETIME,
                FormsSFTable.COLUMN_SF,
                FormsSFTable.COLUMN_GPSLAT,
                FormsSFTable.COLUMN_GPSLNG,
                FormsSFTable.COLUMN_GPSDATE,
                FormsSFTable.COLUMN_GPSACC,
                FormsSFTable.COLUMN_DEVICETAGID,
                FormsSFTable.COLUMN_DEVICEID,
                FormsSFTable.COLUMN_APPVERSION,
        };

        String whereClause = FormsSFTable.COLUMN_SYNCED + " is null OR " + FormsSFTable.COLUMN_SYNCED + " == '' ";
        // String whereClause = null;
        String[] whereArgs = null;
        String groupBy = null;
        String having = null;
        String orderBy = FormsSFTable.COLUMN_ID + " ASC";

        Collection<FormsSF> allFormsSF = new ArrayList<>();
        try {
            c = db.query(
                    FormsSFTable.TABLE_NAME,  // The table to query
                    columns,                   // The columns to return
                    whereClause,               // The columns for the WHERE clause
                    whereArgs,                 // The values for the WHERE clause
                    groupBy,                   // don't group the rows
                    having,                    // don't filter by row groups
                    orderBy                    // The sort order
            );
            while (c.moveToNext()) {
                Log.d(TAG, "getUnsyncedForms: " + c.getCount());
                FormsSF formsSF = new FormsSF();
                allFormsSF.add(formsSF.Hydrate(c));
            }
        } catch (SQLiteException e) {

            Toast.makeText(mContext, "ERROR: " + e.getMessage(), Toast.LENGTH_SHORT).show();

        } finally {
            if (c != null) {
                c.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return allFormsSF;
    }

    public Collection<FormsEN> getUnsyncedFormsS3() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = null;
        String[] columns = {
                FormsS3Table._ID,
                FormsS3Table.COLUMN_UID,
                FormsS3Table.COLUMN_SYSDATE,
//                 FormsS3Table.COLUMN_ISTATUS,
                //                FormsS3Table.COLUMN_ISTATUS96x,
//                 FormsS3Table.COLUMN_ENDINGDATETIME,
                FormsS3Table.COLUMN_EN,
                FormsS3Table.COLUMN_GPSLAT,
                FormsS3Table.COLUMN_GPSLNG,
                FormsS3Table.COLUMN_GPSDATE,
                FormsS3Table.COLUMN_GPSACC,
                FormsS3Table.COLUMN_DEVICETAGID,
                FormsS3Table.COLUMN_DEVICEID,
                FormsS3Table.COLUMN_APPVERSION,
        };

        String whereClause = FormsS3Table.COLUMN_SYNCED + " is null OR " + FormsS3Table.COLUMN_SYNCED + " == '' ";
        // String whereClause = null;
        String[] whereArgs = null;
        String groupBy = null;
        String having = null;
        String orderBy = FormsS3Table.COLUMN_ID + " ASC";

        Collection<FormsEN> allFormsEN = new ArrayList<>();
        try {
            c = db.query(
                    FormsS3Table.TABLE_NAME,  // The table to query
                    columns,                   // The columns to return
                    whereClause,               // The columns for the WHERE clause
                    whereArgs,                 // The values for the WHERE clause
                    groupBy,                   // don't group the rows
                    having,                    // don't filter by row groups
                    orderBy                    // The sort order
            );
            while (c.moveToNext()) {
                Log.d(TAG, "getUnsyncedForms: " + c.getCount());
                FormsEN formsEN = new FormsEN();
                allFormsEN.add(formsEN.Hydrate(c));
            }
        } catch (SQLiteException e) {

            Toast.makeText(mContext, "ERROR: " + e.getMessage(), Toast.LENGTH_SHORT).show();

        } finally {
            if (c != null) {
                c.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return allFormsEN;
    }

    public Collection<FormsWF> getUnsyncedFormsWF() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = null;
        String[] columns = {
                FormsWFTable._ID,
                FormsWFTable.COLUMN_UID,
                FormsWFTable.COLUMN_SYSDATE,
//                FormsWFTable.COLUMN_ISTATUS,
                //               FormsWFTable.COLUMN_ISTATUS96x,
//                FormsWFTable.COLUMN_ENDINGDATETIME,
                FormsWFTable.COLUMN_SWFA01,
                FormsWFTable.COLUMN_SWFA02,
                FormsWFTable.COLUMN_SWFA03,
                FormsWFTable.COLUMN_SWFA04,
                FormsWFTable.COLUMN_SWFA05,
                FormsWFTable.COLUMN_SWFB01,
                FormsWFTable.COLUMN_SWFB02,
                FormsWFTable.COLUMN_SWFC,
                FormsWFTable.COLUMN_SWFD,
                FormsWFTable.COLUMN_SWFE,
                FormsWFTable.COLUMN_SWFF,
                FormsWFTable.COLUMN_GPSLAT,
                FormsWFTable.COLUMN_GPSLNG,
                FormsWFTable.COLUMN_GPSDATE,
                FormsWFTable.COLUMN_GPSACC,
                FormsWFTable.COLUMN_DEVICETAGID,
                FormsWFTable.COLUMN_DEVICEID,
                FormsWFTable.COLUMN_APPVERSION,
        };

        String whereClause = FormsWFTable.COLUMN_SYNCED + " is null OR " + FormsWFTable.COLUMN_SYNCED + " == '' ";
        // String whereClause = null;
        String[] whereArgs = null;
        String groupBy = null;
        String having = null;
        String orderBy = FormsWFTable.COLUMN_ID + " ASC";

        Collection<FormsWF> allFormsWF = new ArrayList<>();
        try {
            c = db.query(
                    FormsWFTable.TABLE_NAME,  // The table to query
                    columns,                   // The columns to return
                    whereClause,               // The columns for the WHERE clause
                    whereArgs,                 // The values for the WHERE clause
                    groupBy,                   // don't group the rows
                    having,                    // don't filter by row groups
                    orderBy                    // The sort order
            );
            while (c.moveToNext()) {
                Log.d(TAG, "getUnsyncedForms: " + c.getCount());
                FormsWF formsWF = new FormsWF();
                allFormsWF.add(formsWF.Hydrate(c));
            }
        } catch (SQLiteException e) {

            Toast.makeText(mContext, "ERROR: " + e.getMessage(), Toast.LENGTH_SHORT).show();

        } finally {
            if (c != null) {
                c.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return allFormsWF;
    }


    public Collection<FormsSL> getTodayFormsSL(String sysdate) {

        // String sysdate =  spDateT.substring(0, 8).trim()
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = null;
        String[] columns = {
                FormsSLTable._ID,
                FormsSLTable.COLUMN_UID,
                FormsSLTable.COLUMN_SYSDATE,
//                FormsSLTable.COLUMN_ISTATUS,
                FormsSLTable.COLUMN_SYNCED,

        };
        String whereClause = FormsSLTable.COLUMN_SYSDATE + " Like ? ";
        String[] whereArgs = new String[]{"%" + sysdate + " %"};
//        String[] whereArgs = new String[]{"%" + spDateT.substring(0, 8).trim() + "%"};
        String groupBy = null;
        String having = null;

        String orderBy =
                FormsSLTable.COLUMN_ID + " ASC";

        Collection<FormsSL> allFormsSL = new ArrayList<>();
        try {
            c = db.query(
                    FormsSLTable.TABLE_NAME,  // The table to query
                    columns,                   // The columns to return
                    whereClause,               // The columns for the WHERE clause
                    whereArgs,                 // The values for the WHERE clause
                    groupBy,                   // don't group the rows
                    having,                    // don't filter by row groups
                    orderBy                    // The sort order
            );
            while (c.moveToNext()) {
                FormsSL formsSL = new FormsSL();
                formsSL.set_ID(c.getString(c.getColumnIndex(FormsSLTable.COLUMN_ID)));
                formsSL.set_UID(c.getString(c.getColumnIndex(FormsSLTable.COLUMN_UID)));
                formsSL.setSysdate(c.getString(c.getColumnIndex(FormsSLTable.COLUMN_SYSDATE)));
//                formsSL.setIstatus(c.getString(c.getColumnIndex(FormsSLTable.COLUMN_ISTATUS)));
                formsSL.setSynced(c.getString(c.getColumnIndex(FormsSLTable.COLUMN_SYNCED)));
                allFormsSL.add(formsSL);
            }
        } catch (SQLiteException e) {


        } finally {
            if (c != null) {
                c.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return allFormsSL;
    }

    public Collection<FormsSF> getTodayFormsSF(String sysdate) {

        // String sysdate =  spDateT.substring(0, 8).trim()
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = null;
        String[] columns = {
                FormsSFTable._ID,
                FormsSFTable.COLUMN_UID,
                FormsSFTable.COLUMN_SYSDATE,
//                FormsSFTable.COLUMN_ISTATUS,
                FormsSFTable.COLUMN_SYNCED,

        };
        String whereClause = FormsSFTable.COLUMN_SYSDATE + " Like ? ";
        String[] whereArgs = new String[]{"%" + sysdate + " %"};
//        String[] whereArgs = new String[]{"%" + spDateT.substring(0, 8).trim() + "%"};
        String groupBy = null;
        String having = null;

        String orderBy =
                FormsSFTable.COLUMN_ID + " ASC";

        Collection<FormsSF> allFormsSF = new ArrayList<>();
        try {
            c = db.query(
                    FormsSFTable.TABLE_NAME,  // The table to query
                    columns,                   // The columns to return
                    whereClause,               // The columns for the WHERE clause
                    whereArgs,                 // The values for the WHERE clause
                    groupBy,                   // don't group the rows
                    having,                    // don't filter by row groups
                    orderBy                    // The sort order
            );
            while (c.moveToNext()) {
                FormsSF formsSF = new FormsSF();
                formsSF.set_ID(c.getString(c.getColumnIndex(FormsSFTable.COLUMN_ID)));
                formsSF.set_UID(c.getString(c.getColumnIndex(FormsSFTable.COLUMN_UID)));
                formsSF.setSysdate(c.getString(c.getColumnIndex(FormsSFTable.COLUMN_SYSDATE)));
                //               formsSF.setIstatus(c.getString(c.getColumnIndex(FormsSFTable.COLUMN_ISTATUS)));
                formsSF.setSynced(c.getString(c.getColumnIndex(FormsSFTable.COLUMN_SYNCED)));
                allFormsSF.add(formsSF);
            }
        } catch (SQLiteException e) {

            //      db.rawQuery(SQL_ALTER_FORMS_A05CODE, null);

        } finally {
            if (c != null) {
                c.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return allFormsSF;
    }

    public Collection<FormsEN> getTodayFormsS3(String sysdate) {

        // String sysdate =  spDateT.substring(0, 8).trim()
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = null;
        String[] columns = {
                FormsS3Table._ID,
                FormsS3Table.COLUMN_UID,
                FormsS3Table.COLUMN_SYSDATE,
//                 FormsS3Table.COLUMN_ISTATUS,
                FormsS3Table.COLUMN_SYNCED,

        };
        String whereClause = FormsS3Table.COLUMN_SYSDATE + " Like ? ";
        String[] whereArgs = new String[]{"%" + sysdate + " %"};
//        String[] whereArgs = new String[]{"%" + spDateT.substring(0, 8).trim() + "%"};
        String groupBy = null;
        String having = null;

        String orderBy =
                FormsS3Table.COLUMN_ID + " ASC";

        Collection<FormsEN> allFormsEN = new ArrayList<>();
        try {
            c = db.query(
                    FormsS3Table.TABLE_NAME,  // The table to query
                    columns,                   // The columns to return
                    whereClause,               // The columns for the WHERE clause
                    whereArgs,                 // The values for the WHERE clause
                    groupBy,                   // don't group the rows
                    having,                    // don't filter by row groups
                    orderBy                    // The sort order
            );
            while (c.moveToNext()) {
                FormsEN formsEN = new FormsEN();
                formsEN.set_ID(c.getString(c.getColumnIndex(FormsS3Table.COLUMN_ID)));
                formsEN.set_UID(c.getString(c.getColumnIndex(FormsS3Table.COLUMN_UID)));
                formsEN.setSysdate(c.getString(c.getColumnIndex(FormsS3Table.COLUMN_SYSDATE)));
                //               formsSF.setIstatus(c.getString(c.getColumnIndex( FormsS3Table.COLUMN_ISTATUS)));
                formsEN.setSynced(c.getString(c.getColumnIndex(FormsS3Table.COLUMN_SYNCED)));
                allFormsEN.add(formsEN);
            }
        } catch (SQLiteException e) {

            //      db.rawQuery(SQL_ALTER_FORMS_A05CODE, null);

        } finally {
            if (c != null) {
                c.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return allFormsEN;
    }

    public Collection<FormsWF> getTodayFormsWF(String sysdate) {

        // String sysdate =  spDateT.substring(0, 8).trim()
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = null;
        String[] columns = {
                FormsWFTable._ID,
                FormsWFTable.COLUMN_UID,
                FormsWFTable.COLUMN_SYSDATE,
//                FormsWFTable.COLUMN_ISTATUS,
                FormsWFTable.COLUMN_SYNCED,

        };
        String whereClause = FormsWFTable.COLUMN_SYSDATE + " Like ? ";
        String[] whereArgs = new String[]{"%" + sysdate + " %"};
//        String[] whereArgs = new String[]{"%" + spDateT.substring(0, 8).trim() + "%"};
        String groupBy = null;
        String having = null;

        String orderBy =
                FormsWFTable.COLUMN_ID + " ASC";

        Collection<FormsWF> allFormsWF = new ArrayList<>();
        try {
            c = db.query(
                    FormsWFTable.TABLE_NAME,  // The table to query
                    columns,                   // The columns to return
                    whereClause,               // The columns for the WHERE clause
                    whereArgs,                 // The values for the WHERE clause
                    groupBy,                   // don't group the rows
                    having,                    // don't filter by row groups
                    orderBy                    // The sort order
            );
            while (c.moveToNext()) {
                FormsWF formsWF = new FormsWF();
                formsWF.set_ID(c.getString(c.getColumnIndex(FormsWFTable.COLUMN_ID)));
                formsWF.set_UID(c.getString(c.getColumnIndex(FormsWFTable.COLUMN_UID)));
                formsWF.setSysdate(c.getString(c.getColumnIndex(FormsWFTable.COLUMN_SYSDATE)));
                //               formsWF.setIstatus(c.getString(c.getColumnIndex(FormsWFTable.COLUMN_ISTATUS)));
                formsWF.setSynced(c.getString(c.getColumnIndex(FormsWFTable.COLUMN_SYNCED)));
                allFormsWF.add(formsWF);
            }
        } catch (SQLiteException e) {

            //      db.rawQuery(SQL_ALTER_FORMS_A05CODE, null);

        } finally {
            if (c != null) {
                c.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return allFormsWF;
    }
    
    *//*public Collection<FormsSL> getFormsByCluster(String cluster) {

        // String sysdate =  spDateT.substring(0, 8).trim()
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = null;
        String[] columns = {
                FormsTable._ID,
                FormsTable.COLUMN_UID,
                FormsTable.COLUMN_SYSDATE,
                FormsTable.COLUMN_ISTATUS,
                FormsTable.COLUMN_SYNCED,

        };
       // String whereClause = FormsTable.COLUMN_REFNO + " = ? ";
        String whereClause = null;
        //String[] whereArgs = new String[]{cluster};
        String[] whereArgs =null;
//        String[] whereArgs = new String[]{"%" + spDateT.substring(0, 8).trim() + "%"};
        String groupBy = null;
        String having = null;

        String orderBy =
                FormsTable.COLUMN_ID + " ASC";

        Collection<FormsSL> allForms = new ArrayList<>();
        try {
            c = db.query(
                    FormsTable.TABLE_NAME,  // The table to query
                    columns,                   // The columns to return
                    whereClause,               // The columns for the WHERE clause
                    whereArgs,                 // The values for the WHERE clause
                    groupBy,                   // don't group the rows
                    having,                    // don't filter by row groups
                    orderBy                    // The sort order
            );
            while (c.moveToNext()) {
                FormsSL form = new FormsSL();
                form.set_ID(c.getString(c.getColumnIndex(FormsTable.COLUMN_ID)));
                form.set_UID(c.getString(c.getColumnIndex(FormsTable.COLUMN_UID)));
                form.setSysdate(c.getString(c.getColumnIndex(FormsTable.COLUMN_SYSDATE)));
                form.setIstatus(c.getString(c.getColumnIndex(FormsTable.COLUMN_ISTATUS)));
                form.setSynced(c.getString(c.getColumnIndex(FormsTable.COLUMN_SYNCED)));
                allForms.add(form);
            }
        } finally {
            if (c != null) {
                c.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return allForms;
    }

    public ArrayList<FormsSL> getUnclosedForms() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = null;
        String[] columns = {
                FormsTable._ID,
                FormsTable.COLUMN_UID,
                FormsTable.COLUMN_SYSDATE,
                FormsTable.COLUMN_ISTATUS,
                FormsTable.COLUMN_SYNCED,
        };
        String whereClause = FormsTable.COLUMN_ISTATUS + " = ''";
        String[] whereArgs = null;
//        String[] whereArgs = new String[]{"%" + spDateT.substring(0, 8).trim() + "%"};
        String groupBy = null;
        String having = null;
        String orderBy = FormsTable.COLUMN_ID + " ASC";
        ArrayList<FormsSL> allForms = new ArrayList<>();
        try {
            c = db.query(
                    FormsTable.TABLE_NAME,  // The table to query
                    columns,                   // The columns to return
                    whereClause,               // The columns for the WHERE clause
                    whereArgs,                 // The values for the WHERE clause
                    groupBy,                   // don't group the rows
                    having,                    // don't filter by row groups
                    orderBy                    // The sort order
            );
            while (c.moveToNext()) {
                FormsSL form = new FormsSL();
                form.set_ID(c.getString(c.getColumnIndex(FormsTable.COLUMN_ID)));
                form.set_UID(c.getString(c.getColumnIndex(FormsTable.COLUMN_UID)));
                form.setSysdate(c.getString(c.getColumnIndex(FormsTable.COLUMN_SYSDATE)));
                form.setIstatus(c.getString(c.getColumnIndex(FormsTable.COLUMN_ISTATUS)));
                form.setSynced(c.getString(c.getColumnIndex(FormsTable.COLUMN_SYNCED)));
                allForms.add(form);
            }
        } catch (SQLiteException e) {


        } finally {
            if (c != null) {
                c.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return allForms;
    }*//*

    public int updateEndingSL() {
        SQLiteDatabase db = this.getReadableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        *//*values.put(FormsSLTable.COLUMN_ISTATUS, MainApp.formsSL.getIstatus());
        values.put(FormsSLTable.COLUMN_ISTATUS96x, MainApp.formsSL.getIstatus96x());
        values.put(FormsSLTable.COLUMN_ENDINGDATETIME, MainApp.formsSL.getEndingdatetime());*//*

        // Which row to update, based on the ID
        String selection = FormsSLTable.COLUMN_ID + " =? ";
        String[] selectionArgs = {String.valueOf(MainApp.formsSL.get_ID())};

        return db.update(FormsSLTable.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    public int updateEndingSF() {
        SQLiteDatabase db = this.getReadableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
*//*        values.put(FormsSFTable.COLUMN_ISTATUS, MainApp.formsSF.getIstatus());
        values.put(FormsSFTable.COLUMN_ISTATUS96x, MainApp.formsSF.getIstatus96x());*//*
//        values.put(FormsSFTable.COLUMN_ENDINGDATETIME, MainApp.formsSF.getEndingdatetime());

        // Which row to update, based on the ID
        String selection = FormsSFTable.COLUMN_ID + " =? ";
        String[] selectionArgs = {String.valueOf(MainApp.formsSF.get_ID())};

        return db.update(FormsSFTable.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    public int updateEndingS3() {
        SQLiteDatabase db = this.getReadableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
*//*        values.put(FormsSFTable.COLUMN_ISTATUS, MainApp.formsSF.getIstatus());
        values.put(FormsSFTable.COLUMN_ISTATUS96x, MainApp.formsSF.getIstatus96x());*//*
//        values.put(FormsSFTable.COLUMN_ENDINGDATETIME, MainApp.formsSF.getEndingdatetime());

        // Which row to update, based on the ID
        String selection = FormsS3Table.COLUMN_ID + " =? ";
        String[] selectionArgs = {String.valueOf(MainApp.formsEN.get_ID())};

        return db.update(FormsS3Table.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    public int updateEndingWF() {
        SQLiteDatabase db = this.getReadableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
*//*        values.put(FormsSFTable.COLUMN_ISTATUS, MainApp.formsSF.getIstatus());
        values.put(FormsSFTable.COLUMN_ISTATUS96x, MainApp.formsSF.getIstatus96x());*//*
//        values.put(FormsSFTable.COLUMN_ENDINGDATETIME, MainApp.formsSF.getEndingdatetime());

        // Which row to update, based on the ID
        String selection = FormsWFTable.COLUMN_ID + " =? ";
        String[] selectionArgs = {String.valueOf(MainApp.formsWF.get_ID())};

        return db.update(FormsWFTable.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }


  *//*  //Get FormsSL already exist
    public FormsSL getFilledFormSL(String district, String refno) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = null;
        String[] columns = {
                FormsTable._ID,
                FormsTable.COLUMN_UID,
                FormsTable.COLUMN_SYSDATE,
                FormsTable.COLUMN_ISTATUS,
                FormsTable.COLUMN_ISTATUS96x,
                FormsTable.COLUMN_ENDINGDATETIME,
                FormsTable.COLUMN_SF,
                FormsTable.COLUMN_SL,
                FormsTable.COLUMN_GPSLAT,
                FormsTable.COLUMN_GPSLNG,
                FormsTable.COLUMN_GPSDATE,
                FormsTable.COLUMN_GPSACC,
                FormsTable.COLUMN_DEVICETAGID,
                FormsTable.COLUMN_DEVICEID,
                FormsTable.COLUMN_APPVERSION
        };

//        String whereClause = "(" + FormsTable.COLUMN_ISTATUS + " is null OR " + FormsTable.COLUMN_ISTATUS + "='') AND " + FormsTable.COLUMN_CLUSTERCODE + "=? AND " + FormsTable.COLUMN_HHNO + "=?";
        //String whereClause = FormsTable.COLUMN_A05 + "=? AND " + FormsTable.COLUMN_REFNO + "=?";
        //String[] whereArgs = {district, refno};
        String whereClause = null;
        String[] whereArgs = null;
        String groupBy = null;
        String having = null;
        String orderBy = FormsTable._ID + " ASC";
        FormsSL allForms = null;
        try {
            c = db.query(
                    FormsTable.TABLE_NAME,  // The table to query
                    columns,                   // The columns to return
                    whereClause,               // The columns for the WHERE clause
                    whereArgs,                 // The values for the WHERE clause
                    groupBy,                   // don't group the rows
                    having,                    // don't filter by row groups
                    orderBy                    // The sort order
            );
            while (c.moveToNext()) {
                allForms = new FormsSL().Hydrate(c);
            }
        } finally {
            if (c != null) {
                c.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return allForms;
    }*//*

    //Generic update FormColumn
    public int updatesFormsSLColumn(String column, String value) {
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(column, value);

        String selection = FormsSLTable._ID + " =? ";
        String[] selectionArgs = {String.valueOf(MainApp.formsSL.get_ID())};

        return db.update(FormsSLTable.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    //Generic update FormColumn
    public int updatesFormsSFColumn(String column, String value) {
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(column, value);

        String selection = FormsSFTable._ID + " =? ";
        String[] selectionArgs = {String.valueOf(MainApp.formsSF.get_ID())};

        return db.update(FormsSFTable.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    //Generic update FormColumn
    public int updatesFormsS3Column(String column, String value) {
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(column, value);

        String selection = FormsS3Table._ID + " =? ";
        String[] selectionArgs = {String.valueOf(MainApp.formsEN.get_ID())};

        return db.update(FormsS3Table.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    //Generic update FormColumn
    public int updatesFormsWFColumn(String column, String value) {
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(column, value);

        String selection = FormsWFTable._ID + " =? ";
        String[] selectionArgs = {String.valueOf(MainApp.formsWF.get_ID())};

        return db.update(FormsWFTable.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }


    // ANDROID DATABASE MANAGER
    public ArrayList<Cursor> getData(String Query) {
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[]{"message"};
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2 = new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);

        try {
            String maxQuery = Query;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);

            //add value to cursor2
            Cursor2.addRow(new Object[]{"Success"});

            alc.set(1, Cursor2);
            if (null != c && c.getCount() > 0) {

                alc.set(0, c);
                c.moveToFirst();

                return alc;
            }
            return alc;
        } catch (SQLException sqlEx) {
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[]{"" + sqlEx.getMessage()});
            alc.set(1, Cursor2);
            return alc;
        } catch (Exception ex) {

            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[]{"" + ex.getMessage()});
            alc.set(1, Cursor2);
            return alc;
        }
    }


    //Generic Un-Synced Forms
    public void updateSyncedFormsSL(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

// New value for one column
        ContentValues values = new ContentValues();
        values.put(FormsSLTable.COLUMN_SYNCED, true);
        values.put(FormsSLTable.COLUMN_SYNCED_DATE, new Date().toString());

// Which row to update, based on the title
        String where = FormsSLTable.COLUMN_ID + " = ?";
        String[] whereArgs = {id};

        int count = db.update(
                FormsSLTable.TABLE_NAME,
                values,
                where,
                whereArgs);
    }

    //Generic Un-Synced Forms
    public void updateSyncedFormsSF(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

// New value for one column
        ContentValues values = new ContentValues();
        values.put(FormsSFTable.COLUMN_SYNCED, true);
        values.put(FormsSFTable.COLUMN_SYNCED_DATE, new Date().toString());

// Which row to update, based on the title
        String where = FormsSFTable.COLUMN_ID + " = ?";
        String[] whereArgs = {id};

        int count = db.update(
                FormsSFTable.TABLE_NAME,
                values,
                where,
                whereArgs);
    }

    //Generic Un-Synced Forms
    public void updateSyncedFormsEN(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

// New value for one column
        ContentValues values = new ContentValues();
        values.put(FormsS3Table.COLUMN_SYNCED, true);
        values.put(FormsS3Table.COLUMN_SYNCED_DATE, new Date().toString());

// Which row to update, based on the title
        String where = FormsS3Table.COLUMN_ID + " = ?";
        String[] whereArgs = {id};

        int count = db.update(
                FormsS3Table.TABLE_NAME,
                values,
                where,
                whereArgs);
    }

    //Generic Un-Synced Forms
    public void updateSyncedFormsWF(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

// New value for one column
        ContentValues values = new ContentValues();
        values.put(FormsWFTable.COLUMN_SYNCED, true);
        values.put(FormsWFTable.COLUMN_SYNCED_DATE, new Date().toString());

// Which row to update, based on the title
        String where = FormsWFTable.COLUMN_ID + " = ?";
        String[] whereArgs = {id};

        int count = db.update(
                FormsWFTable.TABLE_NAME,
                values,
                where,
                whereArgs);
    }
*/
}