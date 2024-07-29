package db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;
import androidx.core.database.sqlite.SQLiteDatabaseKt;

import entity.UserInfo;
import kotlin.Suppress;

public class UserDbHelper extends SQLiteOpenHelper {


    private static UserDbHelper sHelper;
    private static final String DB_NAME="user.db";
    private static final int VERSION =1;

    public UserDbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory,@Nullable Integer version){
        super(context,name,factory,version);
    }
    //创建单例，供使用调用该类里面的增删改查的方法
    public synchronized static UserDbHelper getInstance(Context context){
        if(null==sHelper){
            sHelper = new UserDbHelper(context,DB_NAME,null,VERSION);
        }
        return sHelper;
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        //创建user_table表
        db.execSQL("create table user_table(user_id integer primary key autoincrement," +//use_id 是自增长
                "username text," +       //用户名
                "password text," +      //密码
                "nickname text" +      //注册类型，0----用户，1----管理员
                ")");



    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase,int i,int i1){

    }

    /**
     * 登录  //就是查询的逻辑
     */
    @SuppressLint("Range")
    public UserInfo login(String username) {
        SQLiteDatabase db = getReadableDatabase();
        UserInfo userInfo = null;
        String sql = "select user_id,username,password,nickname from user_table where username=?";
        String[] selectionArgs = {username};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        if (cursor.moveToNext()) {
            int user_id = cursor.getInt(cursor.getColumnIndex("user_id"));
            String name = cursor.getString(cursor.getColumnIndex("username"));
            String password = cursor.getString(cursor.getColumnIndex("password"));
            String nickname = cursor.getString(cursor.getColumnIndex("nickname"));
            userInfo = new UserInfo(user_id, username, password, nickname);


        }
        cursor.close();
        db.close();
        return userInfo;
    }

    /**
     * 注册
     */
    public int register(String username,String password,String nickname){
        //获取SQLiteDatabase实例
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        //填充占位符
        values.put("username",username);
        values.put("password",password);
        values.put("nickname",nickname);
        String nullColumnHack = "values(null,?,?,?)";
        //执行
        int insert = (int) db.insert("user_table",nullColumnHack,values);
        db.close();
        return insert;

    }


}

