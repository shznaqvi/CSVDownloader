package edu.aku.hassannaqvi.csvdownloader.core;


import edu.aku.hassannaqvi.csvdownloader.interfaces.WordsContract.WordsTable;

public final class CreateTable {

    public static final String DATABASE_NAME = "vocapp.db";
    public static final String PROJECT_NAME = "Vocabulary";
    public static final int DATABASE_VERSION = 1;


    public static final String SQL_CREATE_WORDS = "CREATE TABLE "
            + WordsTable.TABLE_NAME + "("
            + WordsTable.COLUMN_ID + " INTEGER PRIMARY KEY,"
            + WordsTable.COLUMN_WORD + " TEXT,"
            + WordsTable.COLUMN_TRANS + " TEXT,"
            + WordsTable.COLUMN_S1 + " TEXT,"
            + WordsTable.COLUMN_S2 + " TEXT,"
            + WordsTable.COLUMN_S3 + " TEXT,"
            + WordsTable.COLUMN_GRADE + " TEXT,"
            + WordsTable.COLUMN_CATEGORY + " TEXT,"
            + WordsTable.COLUMN_VIEWS + " INTEGER DEFAULT 0,"
            + WordsTable.COLUMN_BOOKMARK + " TEXT DEFAULT \"0\","
            + WordsTable.COLUMN_POS + " TEXT"


            + " );";
/*
    public static final String SQL_CREATE_FORMSSF = "CREATE TABLE "
            + FormsSFTable.TABLE_NAME + "("
            + FormsSFTable.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + FormsSFTable.COLUMN_PROJECT_NAME + " TEXT,"
            + FormsSFTable.COLUMN_DEVICEID + " TEXT,"
            + FormsSFTable.COLUMN_DEVICETAGID + " TEXT,"
            + FormsSFTable.COLUMN_SYSDATE + " TEXT,"
            + FormsSFTable.COLUMN_UID + " TEXT,"
            + FormsSFTable.COLUMN_GPSLAT + " TEXT,"
            + FormsSFTable.COLUMN_GPSLNG + " TEXT,"
            + FormsSFTable.COLUMN_GPSDATE + " TEXT,"
            + FormsSFTable.COLUMN_GPSACC + " TEXT,"
            + FormsSFTable.COLUMN_APPVERSION + " TEXT,"
            + FormsSFTable.COLUMN_SF + " TEXT,"
//            + FormsSFTable.COLUMN_ENDINGDATETIME + " TEXT,"
           */
/* + FormsSFTable.COLUMN_ISTATUS + " TEXT,"
            + FormsSFTable.COLUMN_ISTATUS96x + " TEXT,"*//*

            + FormsSFTable.COLUMN_SYNCED + " TEXT,"
            + FormsSFTable.COLUMN_SYNCED_DATE + " TEXT"
            + " );";

    public static final String SQL_CREATE_FORMSEN = "CREATE TABLE "
            + FormsS3Table.TABLE_NAME + "("
            + FormsS3Table.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + FormsS3Table.COLUMN_PROJECT_NAME + " TEXT,"
            + FormsS3Table.COLUMN_DEVICEID + " TEXT,"
            + FormsS3Table.COLUMN_DEVICETAGID + " TEXT,"
            + FormsS3Table.COLUMN_SYSDATE + " TEXT,"
            + FormsS3Table.COLUMN_UID + " TEXT,"
            + FormsS3Table.COLUMN_GPSLAT + " TEXT,"
            + FormsS3Table.COLUMN_GPSLNG + " TEXT,"
            + FormsS3Table.COLUMN_GPSDATE + " TEXT,"
            + FormsS3Table.COLUMN_GPSACC + " TEXT,"
            + FormsS3Table.COLUMN_APPVERSION + " TEXT,"
            + FormsS3Table.COLUMN_EN + " TEXT,"
//            + FormsS3Table.COLUMN_ENDINGDATETIME + " TEXT,"
           */
/* + FormsSFTable.COLUMN_ISTATUS + " TEXT,"
            + FormsSFTable.COLUMN_ISTATUS96x + " TEXT,"*//*

            + FormsS3Table.COLUMN_SYNCED + " TEXT,"
            + FormsS3Table.COLUMN_SYNCED_DATE + " TEXT"
            + " );";

    public static final String SQL_CREATE_FORMSWF = "CREATE TABLE "
            + FormsWFTable.TABLE_NAME + "("
            + FormsWFTable.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + FormsWFTable.COLUMN_PROJECT_NAME + " TEXT,"
            + FormsWFTable.COLUMN_DEVICEID + " TEXT,"
            + FormsWFTable.COLUMN_DEVICETAGID + " TEXT,"
            + FormsWFTable.COLUMN_SYSDATE + " TEXT,"
            + FormsWFTable.COLUMN_UID + " TEXT,"
            + FormsWFTable.COLUMN_GPSLAT + " TEXT,"
            + FormsWFTable.COLUMN_GPSLNG + " TEXT,"
            + FormsWFTable.COLUMN_GPSDATE + " TEXT,"
            + FormsWFTable.COLUMN_GPSACC + " TEXT,"
            + FormsWFTable.COLUMN_APPVERSION + " TEXT,"
            + FormsWFTable.COLUMN_SWFA01 + " TEXT,"
            + FormsWFTable.COLUMN_SWFA02 + " TEXT,"
            + FormsWFTable.COLUMN_SWFA03 + " TEXT,"
            + FormsWFTable.COLUMN_SWFA04 + " TEXT,"
            + FormsWFTable.COLUMN_SWFA05 + " TEXT,"
            + FormsWFTable.COLUMN_SWFB01 + " TEXT,"
            + FormsWFTable.COLUMN_SWFB02 + " TEXT,"
            + FormsWFTable.COLUMN_SWFC + " TEXT,"
            + FormsWFTable.COLUMN_SWFD + " TEXT,"
            + FormsWFTable.COLUMN_SWFE + " TEXT,"
            + FormsWFTable.COLUMN_SWFF + " TEXT,"
//            + FormsWFTable.COLUMN_ENDINGDATETIME + " TEXT,"
           */
/* + FormsWFTable.COLUMN_ISTATUS + " TEXT,"
            + FormsWFTable.COLUMN_ISTATUS96x + " TEXT,"*//*

            + FormsWFTable.COLUMN_SYNCED + " TEXT,"
            + FormsWFTable.COLUMN_SYNCED_DATE + " TEXT"
            + " );";

    public static final String SQL_CREATE_USERS = "CREATE TABLE " + UsersTable.TABLE_NAME + "("
            + UsersTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + UsersTable.COLUMN_USERNAME + " TEXT,"
            + UsersTable.COLUMN_PASSWORD + " TEXT,"
            + UsersTable.COLUMN_FULLNAME + " TEXT"
            + " );";

    public static final String SQL_CREATE_DISTRICTS = "CREATE TABLE " + DistrictsTable.TABLE_NAME + "("
            + DistrictsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + DistrictsTable.COLUMN_DIST_ID + " TEXT,"
            + DistrictsTable.COLUMN_ADMIN_UNIT + " TEXT,"
            + DistrictsTable.COLUMN_PROV + " TEXT"
            + " );";

    public static final String SQL_CREATE_VERSIONAPP = "CREATE TABLE " + VersionAppTable.TABLE_NAME + " (" +
            VersionAppTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            VersionAppTable.COLUMN_VERSION_CODE + " TEXT, " +
            VersionAppTable.COLUMN_VERSION_NAME + " TEXT, " +
            VersionAppTable.COLUMN_PATH_NAME + " TEXT " +
            ");";

    public static final String SQL_CREATE_BL_RANDOM = "CREATE TABLE " + BLRandomTable.TABLE_NAME + "("
            + BLRandomTable.COLUMN_ID + " TEXT,"
            + BLRandomTable.COLUMN_P_CODE + " TEXT,"
            + BLRandomTable.COLUMN_EB_CODE + " TEXT,"
            + BLRandomTable.COLUMN_LUID + " TEXT,"
            + BLRandomTable.COLUMN_HH + " TEXT,"
            + BLRandomTable.COLUMN_STRUCTURE_NO + " TEXT,"
            + BLRandomTable.COLUMN_FAMILY_EXT_CODE + " TEXT,"
            + BLRandomTable.COLUMN_HH_HEAD + " TEXT,"
            + BLRandomTable.COLUMN_CONTACT + " TEXT,"
            + BLRandomTable.COLUMN_HH_SELECTED_STRUCT + " TEXT,"
            + BLRandomTable.COLUMN_RANDOMDT + " TEXT,"
            + BLRandomTable.COLUMN_SNO_HH + " TEXT );";
*/

/*    public static final String SQL_ALTER_CHILD_TABLE = "ALTER TABLE " +
            ChildTable.TABLE_NAME + " ADD COLUMN " +
            ChildTable.COLUMN_SYSDATE + " TEXT";*/
}
