package chat.rocket.android.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Message extends AbstractModel{
    public static final String TABLE_NAME = "message";

    public String roomId;
    public String userId;
    public String content;
    public String timestamp;

    private static class DBAccessor extends AbstractModelDBAccessor<Message> {

        protected DBAccessor(SQLiteDatabase db) {
            super(db, TABLE_NAME);
        }

        @Override
        protected Message createModel(Cursor c) {
            return Message.createFromCursor(c);
        }

        @Override
        protected ContentValues createContentValue(Message instance) {
            ContentValues values = createInitContentValue(instance);
            values.put("room_id", instance.roomId);
            values.put("user_id", instance.userId);
            values.put("content", instance.content);
            values.put("timestamp", instance.timestamp);
            return values;
        }

        @Override
        protected void updateTable(int oldVersion, int newVersion) {
            int updateVersion = oldVersion;

            if (updateVersion < 2) {
                mDb.beginTransaction();
                try {
                    dropTable();
                    mDb.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                            " (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                            " id TEXT UNIQUE NOT NULL," +
                            " room_id TEXT," +
                            " user_id TEXT," +
                            " content TEXT," +
                            " timestamp TEXT);\n");

                    mDb.setTransactionSuccessful();
                }
                finally {
                    mDb.endTransaction();
                }

                updateVersion = 2;
            }
        }
    }

    public static Message createFromCursor(Cursor c) {
        Message m = new Message();
        initID(m, c);
        m.roomId = c.getString(c.getColumnIndex("room_id"));
        m.userId = c.getString(c.getColumnIndex("user_id"));
        m.content = c.getString(c.getColumnIndex("content"));
        m.timestamp = c.getString(c.getColumnIndex("timestamp"));
        return m;
    }

    public static void updateTable(SQLiteDatabase db, int oldVersion, int newVersion) {
        new DBAccessor(db).updateTable(oldVersion, newVersion);
    }

    public static void dropTable(SQLiteDatabase db) {
        new DBAccessor(db).dropTable();
    }

    public static Message get(SQLiteDatabase db, String selection, String[] selectionArgs) {
        return new DBAccessor(db).get(selection, selectionArgs,null);
    }

    public static Message get(SQLiteDatabase db, long _id) {
        return new DBAccessor(db).get(_id);
    }

    public static Message getById(SQLiteDatabase db, String id) {
        return new DBAccessor(db).getByID(id);
    }

    public long put(SQLiteDatabase db) {
        return new DBAccessor(db).put(this);
    }

    public int delete(SQLiteDatabase db) {
        return new DBAccessor(db).delete(this);
    }

}