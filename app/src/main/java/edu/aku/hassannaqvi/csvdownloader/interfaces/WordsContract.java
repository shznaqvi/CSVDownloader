package edu.aku.hassannaqvi.csvdownloader.interfaces;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class WordsContract {

    public static String CONTENT_AUTHORITY = "edu.aku.hassannaqvi.csvdownloader";

    public static abstract class WordsTable implements BaseColumns {
        public static final String TABLE_NAME = "words";
        public static final String COLUMN_NAME_NULLABLE = "NULLHACK";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_WORD = "word";
        public static final String COLUMN_TRANS = "trans";
        public static final String COLUMN_S1 = "sentcol1";
        public static final String COLUMN_S2 = "sentcol2";
        public static final String COLUMN_S3 = "sentcol3";
        public static final String COLUMN_GRADE = "grade";
        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_POS = "pos";  // Part of Speech
        public static final String COLUMN_VIEWS = "views";  // Part of Speech

        public static String PATH = "words";
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH;
        public static Uri CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY)
                .buildUpon().appendPath(PATH).build();
        public static String SERVER_PATH = "sync.php";


        public static String getWordsFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static Uri buildUriWithId(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}