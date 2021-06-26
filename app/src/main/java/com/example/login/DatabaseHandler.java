package com.example.login;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.login.model.Message;
import com.example.login.model.MessageList;
import com.example.login.model.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    public DatabaseHandler(@Nullable Context context) {
        super(context, "messenger", null, 1);
    }

    private static User user;

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_users_table =
                "CREATE TABLE users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT UNIQUE," +
                    "password TEXT," +
                    "first_name TEXT," +
                    "last_name TEXT" +
                ")";
        String create_messages_table =
                "CREATE TABLE messages (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "sender_id INTEGER," +
                    "receiver_id INTEGER," +
                    "message TEXT," +
                    "time DATETIME," +
                    "CONSTRAINT fk_messages_sender_id FOREIGN KEY(sender_id) REFERENCES users(id)," +
                    "CONSTRAINT fk_messages_receiver_id FOREIGN KEY(receiver_id) REFERENCES users(id)" +
                ")";

        db.execSQL(create_users_table);
        db.execSQL(create_messages_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String drop_messages_table = "DROP TABLE IF EXISTS messages";
        db.execSQL(drop_messages_table);
        String drop_users_table = "DROP TABLE IF EXISTS users";
        db.execSQL(drop_users_table);
        onCreate(db);
    }

    //Đăng nhập
    public boolean login(String username, String password) {
        if (isLoggedIn())
            return false;

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        Cursor cursor = db.rawQuery(query, new String[] { username, password });
        cursor.moveToFirst();

        if (cursor.isAfterLast())
            return false;

        User user = new User(
                cursor.getInt(cursor.getColumnIndex("id")),
                cursor.getString(cursor.getColumnIndex("username")),
                cursor.getString(cursor.getColumnIndex("password")),
                cursor.getString(cursor.getColumnIndex("first_name")),
                cursor.getString(cursor.getColumnIndex("last_name"))
        );
        this.user = user;

        return true;
    }

    //Đăng xuất
    public void logout() {
        this.user = null;
    }

    //Kiểm tra đăng nhập
    public boolean isLoggedIn() {
        return this.user != null;
    }

    //Tạo tài khoản user
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("username", user.getUsername());
        values.put("password", user.getPassword());
        values.put("first_name", user.getFirstName());
        values.put("last_name", user.getLastName());

        db.insert("users", null, values);
        db.close();
    }

    //Tạo nhắn tin
    public void addMessage(int senderId, int receiverId, String message) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("sender_id", senderId);
        values.put("receiver_id", receiverId);
        values.put("message", message);
        values.put("time", System.currentTimeMillis());

        db.insert("messages", null, values);
        db.close();
    }

    //Lấy user đang đăng nhập
    public User getUser() {
        return this.user;
    }

    //Lấy user
    public User getUser(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM users WHERE id = ?";
        Cursor cursor = db.rawQuery(query, new String[] {String.valueOf(id)});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            User user = new User(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("username")),
                    cursor.getString(cursor.getColumnIndex("password")),
                    cursor.getString(cursor.getColumnIndex("first_name")),
                    cursor.getString(cursor.getColumnIndex("last_name"))
            );
            return user;
        }
        return null;
    }

    //Lấy tất cả users
    public List<User> getUsers() {
        List<User> list = new ArrayList<>();
        if (user == null)
            return list;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM users";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            User user = new User(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("username")),
                    cursor.getString(cursor.getColumnIndex("password")),
                    cursor.getString(cursor.getColumnIndex("first_name")),
                    cursor.getString(cursor.getColumnIndex("last_name"))
            );
            list.add(user);
            cursor.moveToNext();
        }
        return list;
    }

    //Gửi tin nhắn
    public void sendMessage(int receiverId, String message) {
        if (user == null)
            return;

        SQLiteDatabase db = this.getWritableDatabase();
        int senderId = this.user.getId();

        ContentValues values = new ContentValues();
        values.put("sender_id", senderId);
        values.put("receiver_id", receiverId);
        values.put("message", message);
        values.put("time", System.currentTimeMillis());

        db.insert("messages", null, values);
        db.close();
    }

    //Nhận tin nhắn
    public List<Message> getMessages(int receiverId) {
        List<Message> list = new ArrayList<>();
        if (user == null)
            return list;
        int senderId = this.user.getId();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT id, message, time, sender_id FROM messages WHERE (sender_id = ? AND receiver_id = ?) OR (sender_id = ? AND receiver_id = ?)";
        Cursor cursor = db.rawQuery(query, new String[] { Integer.toString(senderId), Integer.toString(receiverId), Integer.toString(receiverId), Integer.toString(senderId) });
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Message message = new Message(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getInt(cursor.getColumnIndex("sender_id")),
                    0,
                    cursor.getString(cursor.getColumnIndex("message")),
                    new Date(cursor.getLong(cursor.getColumnIndex("time")))
            );
            list.add(message);
            cursor.moveToNext();
        }
        return list;
    }

    //Nhân
    public List<MessageList> getMessageList() {
        List<MessageList> list = new ArrayList<>();
        if (user == null)
            return list;

        int senderId = this.user.getId();
        SQLiteDatabase db = this.getReadableDatabase();

        //Lấy tất cả id của người nhận
        String queryReceiverIds = "SELECT DISTINCT id FROM (SELECT DISTINCT receiver_id AS id, time FROM messages WHERE sender_id = ? OR receiver_id = ? UNION SELECT DISTINCT sender_id, time AS id FROM messages WHERE sender_id = ? OR receiver_id = ? ORDER BY time DESC)";
        Cursor cursorReceiverIds = db.rawQuery(queryReceiverIds, new String[] { Integer.toString(senderId), Integer.toString(senderId), Integer.toString(senderId), Integer.toString(senderId) });
        cursorReceiverIds.moveToFirst();

        //Mỗi người nhận lấy record message mới nhất và thêm vào list
        while (!cursorReceiverIds.isAfterLast()) {
            int receiverId = cursorReceiverIds.getInt(cursorReceiverIds.getColumnIndex("id"));
            if (receiverId != senderId) {
                String userInfo = "SELECT id, first_name, last_name FROM users WHERE id = ?";
                Cursor cursorUserInfo = db.rawQuery(userInfo, new String[] {String.valueOf(receiverId)});
                cursorUserInfo.moveToFirst();

                String message = "SELECT message, time " +
                        "FROM messages " +
                        "WHERE (sender_id = ? AND receiver_id = ?) OR (sender_id = ? AND receiver_id = ?) " +
                        "ORDER BY time DESC " +
                        "LIMIT 1 ";
                Cursor cursorMessage = db.rawQuery(message, new String[] {String.valueOf(senderId), String.valueOf(receiverId), String.valueOf(receiverId), String.valueOf(senderId)});
                cursorMessage.moveToFirst();
                MessageList messageList = new MessageList(
                        cursorUserInfo.getInt(cursorUserInfo.getColumnIndex("id")),
                        cursorUserInfo.getString(cursorUserInfo.getColumnIndex("first_name")),
                        cursorUserInfo.getString(cursorUserInfo.getColumnIndex("last_name")),
                        cursorMessage.getString(cursorMessage.getColumnIndex("message")),
                        new Date(cursorMessage.getLong(cursorMessage.getColumnIndex("time")))
                );
                list.add(messageList);
            }
            cursorReceiverIds.moveToNext();
        }

        return list;
    }

    public void generateSampleData() {
        if (getUser(1) != null)
            return;
        //Tạo tài khoản
        addUser(new User("jennydo", "123", "Jenny", "Đỗ"));
        addUser(new User("trucgiang", "123", "Nguyễn Trúc", "Giang"));
        addUser(new User("vanky", "123", "Bành Văn", "Kỳ"));
        addUser(new User("huumanh", "123", "Chu Hữu", "Mạnh"));

        //Gửi tin nhắn
        addMessage(1, 2, "Giang ơi");
        addMessage(1, 2, "Rảnh hông");
        addMessage(2, 1, "Sao zạ");
        addMessage(1, 2, "Đi ăn buffe");
        addMessage(1, 2, "Tối");
        addMessage(2, 1, "M mời t hả");
        addMessage(1, 2, "Uk");
        addMessage(1, 2, "Chứ sao");
        addMessage(1, 2, "6h mình đi, t chở Giang đi");
        addMessage(2, 1, "Đồng ý ^.^");
        addMessage(2, 1, "Trời tự nhiên có người mời đi ăn buffe xĩu ^o^");
        addMessage(3, 1, "Đi đâu sao chưa về");
        addMessage(3, 1, "Chờ nãy h -.-");
        addMessage(1, 3, "Đang đi ăn @@");
        addMessage(3, 1, "Về mua hộp cơm dùm Kỳ nha anh iu");
        addMessage(1, 3, "Dạ chụy");
        addMessage(1, 4, "Mạnh ơi");
        addMessage(1, 4, "Aloooo");
        addMessage(3, 2, "Má con kia m cướp chồng t à");
        addMessage(3, 2, "T qua t xử h á");
        addMessage(2, 3, "Cướp qq");
        addMessage(2, 3, "-.-!");
        addMessage(3, 2, "Liệu hồn t nha $^%$$%%");
        addMessage(2, 3, "Dô duyên, đi chơi thôi má");
        addMessage(4, 3, "Kỳ ơi");
        addMessage(4, 3, "Đi cà phê ko");
        addMessage(3, 4, "Ở đâu á");
        addMessage(4, 3, "Chỗ vườn hoa chỗ cầu dak bla hồi trc mình vô");
        addMessage(3, 4, "ok đi");
    }
}
