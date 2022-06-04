package com.egorzaev.dbeditor;

import android.database.Cursor;

public class CurKeeper {
    public static Cursor cursor;

    public static Cursor getCursor() {
        return cursor;
    }

    public static void setCursor(Cursor cursor) {
        CurKeeper.cursor = cursor;
    }

    public static void resetCursor() {
        CurKeeper.cursor.close();
    }
}
