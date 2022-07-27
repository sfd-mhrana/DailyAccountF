package com.example.dailyaccount.databasesetup;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountModel extends SQLiteOpenHelper {

    public AccountModel(@Nullable Context context) {
        super(context, AllParameter.DB_NAME, null,AllParameter.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
       String mytable= "CREATE TABLE "+AllParameter.DB_TABLE +"("+
               AllParameter.KEY_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"+
               AllParameter.KEY_DETAILS +" VARCHAR NOT NULL,"+
               AllParameter.KEY_AMOUNT+" VARCHAR NOT NULL,"+
               AllParameter.KEY_STATUS +"  VARCHAR NOT NULL,`"+
               AllParameter.KEY_DATE +"` VARCHAR NOT NULL)";
        sqLiteDatabase.execSQL(mytable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS "+AllParameter.DB_TABLE);
        onCreate(sqLiteDatabase);
    }

    public int addMyData(AccountData accountData){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values =new ContentValues();
        values.put(AllParameter.KEY_DETAILS,accountData.getDetails());
        values.put(AllParameter.KEY_AMOUNT,accountData.getAmount());
        values.put(AllParameter.KEY_STATUS,accountData.getStatus());
        values.put(AllParameter.KEY_DATE,accountData.getDate());
        db.insert(AllParameter.DB_TABLE,null,values);
        db.close();
        return 1;
    }

    public int updateMyData(AccountData accountData){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values =new ContentValues();
        values.put(AllParameter.KEY_DETAILS,accountData.getDetails());
        values.put(AllParameter.KEY_AMOUNT,accountData.getAmount());
        values.put(AllParameter.KEY_STATUS,accountData.getStatus());
        return db.update(AllParameter.DB_TABLE,values,AllParameter.KEY_ID + " = ? ",
                new String[]{String.valueOf(accountData.getId())});

    }

    public List<AccountData> getAlldata(String date){
        List<AccountData> alldata=new ArrayList<AccountData>();
        String selectqurey= "SELECT * FROM "+ AllParameter.DB_TABLE +" WHERE "+AllParameter.KEY_DATE+"='"+date+"';";
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery(selectqurey,null);
        if(cursor.moveToFirst()) {
            do{
                AccountData st=new AccountData();
                st.setId(Integer.parseInt(cursor.getString(0)));
                st.setDetails(cursor.getString(1));
                st.setAmount(cursor.getString(2));
                st.setStatus(cursor.getString(3));
                st.setDate(cursor.getString(4));
                alldata.add(st);
            }while (cursor.moveToNext());
        }

        return alldata;
    }

    public List<Map> getDailyinMonth(String month) {

        List<Map> alldata = new ArrayList<>();

        int totalDevit=0,totalCredit=0;



        String selectqurey =
                "SELECT itemdate,SUM(devamou) as devamou,SUM(creamou) as creamou  from (SELECT " +
                    "(CASE WHEN devdate is null THEN credate ELSE devdate END)as itemdate, " +
                    "(CASE WHEN devAmount is null THEN 0 ELSE devAmount END)as devamou, " +
                    "(CASE WHEN creAmount  is null THEN 0 ELSE creAmount  END)as creamou " +
                    "FROM ( " +
                    "SELECT devdate,devAmount,credate,creAmount FROM ( " +
                    "SELECT "+AllParameter.KEY_DATE+" as devdate,SUM("+AllParameter.KEY_AMOUNT+") as devAmount FROM "+AllParameter.DB_TABLE+" WHERE "+AllParameter.KEY_STATUS+" ='Devit' and "+AllParameter.KEY_DATE+" LIKE '%"+month+"%' GROUP BY "+AllParameter.KEY_DATE+
                    ") as devT LEFT JOIN  ( " +
                    "SELECT "+AllParameter.KEY_DATE+" as credate,SUM("+AllParameter.KEY_AMOUNT+") as creAmount FROM "+AllParameter.DB_TABLE+" WHERE "+AllParameter.KEY_STATUS+" ='Credit' and "+AllParameter.KEY_DATE+" LIKE '%"+month+"%' GROUP BY " +AllParameter.KEY_DATE+
                    ") as creT on devdate = credate " +
                    "UNION " +
                    "SELECT devdate,devAmount,credate,creAmount FROM ( " +
                    "SELECT "+AllParameter.KEY_DATE+" as credate,SUM("+AllParameter.KEY_AMOUNT+") as creAmount FROM "+AllParameter.DB_TABLE+" WHERE "+AllParameter.KEY_STATUS+" ='Credit' and "+AllParameter.KEY_DATE+" LIKE '%"+month+"%' GROUP BY "+AllParameter.KEY_DATE+
                    ") as creT LEFT  JOIN  ( " +
                    "SELECT "+AllParameter.KEY_DATE+" as devdate,SUM("+AllParameter.KEY_AMOUNT+") as devAmount FROM "+AllParameter.DB_TABLE+" WHERE "+AllParameter.KEY_STATUS+" ='Devit' and "+AllParameter.KEY_DATE+" LIKE '%"+month+"%' GROUP BY "+AllParameter.KEY_DATE+
                    ") as devT on devdate = credate )as lastT) as allt GROUP BY itemdate;";

        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery(selectqurey,null);
        if(cursor.moveToFirst()) {
            do{
                Map mydata=new HashMap();
                mydata.put("details", cursor.getString(0));
                mydata.put("devit", cursor.getString(1));
                mydata.put("credit", cursor.getString(2));
                totalDevit+=Integer.parseInt(cursor.getString(1));
                totalCredit+=Integer.parseInt(cursor.getString(2));
                alldata.add(mydata);
            }while (cursor.moveToNext());
        }
        Map total=new HashMap();
        total.put("totalDevit",totalDevit);
        total.put("totalCredit",totalCredit);
        total.put("selectedMonth",month);

        alldata.add(total);

        return alldata;
    }

    public List<Map> getMonthlyinYear(String year){

        List<Map> alldata = new ArrayList<>();

        int totalDevit=0,totalCredit=0;




        String selectqurey =
                "SELECT itemdate,SUM(devamou) as devamou,SUM(creamou) as creamou  from (SELECT " +
                        "(CASE WHEN devdate is null THEN credate ELSE devdate END)as itemdate, " +
                        "(CASE WHEN devAmount is null THEN 0 ELSE devAmount END)as devamou, " +
                        "(CASE WHEN creAmount  is null THEN 0 ELSE creAmount  END)as creamou " +
                        "FROM ( " +
                        "SELECT devdate,devAmount,credate,creAmount FROM ( " +
                        "SELECT "+AllParameter.KEY_DATE+" as devdate,SUM("+AllParameter.KEY_AMOUNT+") as devAmount FROM "+AllParameter.DB_TABLE+" WHERE "+AllParameter.KEY_STATUS+" ='Devit' and "+AllParameter.KEY_DATE+" LIKE '%"+year+"%' GROUP BY SUBSTR("+AllParameter.KEY_DATE+",1,7)"+
                        ") as devT LEFT JOIN  ( " +
                        "SELECT "+AllParameter.KEY_DATE+" as credate,SUM("+AllParameter.KEY_AMOUNT+") as creAmount FROM "+AllParameter.DB_TABLE+" WHERE "+AllParameter.KEY_STATUS+" ='Credit' and "+AllParameter.KEY_DATE+" LIKE '%"+year+"%' GROUP BY SUBSTR(" +AllParameter.KEY_DATE+",1,7)"+
                        ") as creT on devdate = credate " +
                        "UNION " +
                        "SELECT devdate,devAmount,credate,creAmount FROM ( " +
                        "SELECT "+AllParameter.KEY_DATE+" as credate,SUM("+AllParameter.KEY_AMOUNT+") as creAmount FROM "+AllParameter.DB_TABLE+" WHERE "+AllParameter.KEY_STATUS+" ='Credit' and "+AllParameter.KEY_DATE+" LIKE '%"+year+"%' GROUP BY SUBSTR("+AllParameter.KEY_DATE+",1,7)"+
                        ") as creT LEFT  JOIN  ( " +
                        "SELECT "+AllParameter.KEY_DATE+" as devdate,SUM("+AllParameter.KEY_AMOUNT+") as devAmount FROM "+AllParameter.DB_TABLE+" WHERE "+AllParameter.KEY_STATUS+" ='Devit' and "+AllParameter.KEY_DATE+" LIKE '%"+year+"%' GROUP BY SUBSTR("+AllParameter.KEY_DATE+",1,7)"+
                        ") as devT on devdate = credate )as lastT) as allt GROUP BY SUBSTR(itemdate,1,7);";

        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery(selectqurey,null);
        if(cursor.moveToFirst()) {
            do{
                Map mydata=new HashMap();
                mydata.put("details", cursor.getString(0));
                mydata.put("devit", cursor.getString(1));
                mydata.put("credit", cursor.getString(2));
                totalDevit+=Integer.parseInt(cursor.getString(1));
                totalCredit+=Integer.parseInt(cursor.getString(2));
                alldata.add(mydata);
            }while (cursor.moveToNext());
        }
        Map total=new HashMap();
        total.put("totalDevit",totalDevit);
        total.put("totalCredit",totalCredit);
        total.put("selectedYear",year);

        alldata.add(total);

        return alldata;
    }

    public List<Map> getYearlyinAllData(){

        List<Map> alldata = new ArrayList<>();

        int totalDevit=0,totalCredit=0;


        String selectqurey =
                "SELECT itemdate,SUM(devamou) as devamou,SUM(creamou) as creamou  from (SELECT " +
                        "(CASE WHEN devdate is null THEN credate ELSE devdate END)as itemdate, " +
                        "(CASE WHEN devAmount is null THEN 0 ELSE devAmount END)as devamou, " +
                        "(CASE WHEN creAmount  is null THEN 0 ELSE creAmount  END)as creamou " +
                        "FROM ( " +
                        "SELECT devdate,devAmount,credate,creAmount FROM ( " +
                        "SELECT "+AllParameter.KEY_DATE+" as devdate,SUM("+AllParameter.KEY_AMOUNT+") as devAmount FROM "+AllParameter.DB_TABLE+" WHERE "+AllParameter.KEY_STATUS+" ='Devit' GROUP BY SUBSTR("+AllParameter.KEY_DATE+",1,4)"+
                        ") as devT LEFT JOIN  ( " +
                        "SELECT "+AllParameter.KEY_DATE+" as credate,SUM("+AllParameter.KEY_AMOUNT+") as creAmount FROM "+AllParameter.DB_TABLE+" WHERE "+AllParameter.KEY_STATUS+" ='Credit' GROUP BY SUBSTR(" +AllParameter.KEY_DATE+",1,4)"+
                        ") as creT on devdate = credate " +
                        "UNION " +
                        "SELECT devdate,devAmount,credate,creAmount FROM ( " +
                        "SELECT "+AllParameter.KEY_DATE+" as credate,SUM("+AllParameter.KEY_AMOUNT+") as creAmount FROM "+AllParameter.DB_TABLE+" WHERE "+AllParameter.KEY_STATUS+" ='Credit' GROUP BY SUBSTR("+AllParameter.KEY_DATE+",1,4)"+
                        ") as creT LEFT  JOIN  ( " +
                        "SELECT "+AllParameter.KEY_DATE+" as devdate,SUM("+AllParameter.KEY_AMOUNT+") as devAmount FROM "+AllParameter.DB_TABLE+" WHERE "+AllParameter.KEY_STATUS+" ='Devit' GROUP BY SUBSTR("+AllParameter.KEY_DATE+",1,4)"+
                        ") as devT on devdate = credate )as lastT) as allt GROUP BY SUBSTR(itemdate,1,4);";

        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery(selectqurey,null);
        if(cursor.moveToFirst()) {
            do{
                Map mydata=new HashMap();
                mydata.put("details", cursor.getString(0));
                mydata.put("devit", cursor.getString(1));
                mydata.put("credit", cursor.getString(2));
                totalDevit+=Integer.parseInt(cursor.getString(1));
                totalCredit+=Integer.parseInt(cursor.getString(2));
                alldata.add(mydata);
            }while (cursor.moveToNext());
        }
        Map total=new HashMap();
        total.put("totalDevit",totalDevit);
        total.put("totalCredit",totalCredit);

        alldata.add(total);

        return alldata;
    }


}
