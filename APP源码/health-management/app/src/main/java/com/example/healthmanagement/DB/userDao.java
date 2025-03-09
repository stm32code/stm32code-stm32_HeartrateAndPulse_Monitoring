package com.example.healthmanagement.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户操作
 */
public class userDao {
    private SQLiteOpenHelper helper;
    private SQLiteDatabase db;

    public userDao(Context context) {
        helper = new DBopenHelper(context);
    }

    /**
     * 修改数据类型
     */
    public enum updataType{
        PASW,ST,NAME; //密码，步数，姓名
    }
    /**
     * 添加用户
     * @param u
     * @return
     */
    public boolean addUser(User u){
        try{
            db = helper.getWritableDatabase();
            ContentValues Values = new ContentValues();
            Values.put("u_name",u.getName());
            Values.put("u_password",u.getPassword());
            Values.put("u_id",u.getId());
            db.insert("user",null,Values);
            db.close();
            return true;
        }catch (Exception e){
            Log.e("数据库添加",e.toString());
            return false;
        }
    }

    /**
     * 更新数据
     * @param id
     * @param type
     * @param data
     * @return
     */
    public boolean updateUser(int id,updataType type,String data){
        try{
            db=helper.getReadableDatabase();
            String sql;
            switch(type){
                case ST:
                    sql = "update user set u_mb ='"+data+"'where u_id='"+id+"';";
                    break;
                case NAME:
                    sql = "update user set u_name ='"+data+"'where u_id='"+id+"';";
                    break;
                case PASW:
                    sql = "update user set u_password ='"+data+"'where u_id='"+id+"';";
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + type);
            }
            db.execSQL(sql);
            db.close();
            return true;
        }catch (Exception e){
            Log.e("数据库更新",e.toString());
            return false;
        }
    }

    /***
     * 根据用户ID查找
     * @param id
     * @return
     */
    public User findUser(int id){
        try{
            db=helper.getReadableDatabase();
            Cursor cursor = db.rawQuery("select * from user where u_id='" + id +"';", null);
            if (cursor.getCount() !=0){
                User user=new User();
                if(cursor.moveToNext()){
                    user.setName(cursor.getString(cursor.getColumnIndex("u_name")));
                    user.setPassword(cursor.getString(cursor.getColumnIndex("u_password")));
                    user.setId(cursor.getInt(cursor.getColumnIndex("u_id")));
                    user.setStep(cursor.getInt(cursor.getColumnIndex("u_mb")));
                }
                db.close();
                return user;
            }else{
                db.close();
                return null;
            }
        }catch (Exception e){
            Log.e("数据库查找(单)",e.toString());
            return null;
        }

    }

    /**
     * 根据用户名查找；如果传入参数为null 则会返回所有用户信息
     * @param name
     * @return
     */
    public List<User> findUser(@Nullable String name){
        try{
            db=helper.getReadableDatabase();
            String sql;
            if(name == null)
                sql = "select * from user;";
            else
                sql = "select * from user where u_name = '"+name+"'";
            Cursor cursor = db.rawQuery(sql, null);
            if(cursor.getCount() != 0){
                List<User> userList = new ArrayList<>();
                while (cursor.moveToNext()){
                    User user=new User();
                    user.setName(cursor.getString(cursor.getColumnIndex("u_name")));
                    user.setPassword(cursor.getString(cursor.getColumnIndex("u_password")));
                    user.setId(cursor.getInt(cursor.getColumnIndex("u_id")));
                    user.setStep(cursor.getInt(cursor.getColumnIndex("u_mb")));
                    userList.add(user);
                }
                db.close();
                return userList;
            }else {
                db.close();
                return null;
            }

        }catch (Exception e){
            Log.e("数据库查找(多)",e.toString());
            return null;
        }
    }

    /**
     * 删除指定用户
     * @param id
     * @return
     */
    public boolean deleteUser(int id){
        try{
            db = helper.getWritableDatabase();
            String sql = "delete from `user` where u_id = '"+id+"'";
            db.execSQL(sql);
            db.close();
            return true;
        }catch (Exception e){
            Log.e("数据库删除",e.toString());
            return false;
        }
    }
}
