package com.douglas.poornomore;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static String DATABASE_NAME = "PoorNoMore.db";
    private static int DATABASE_VERSION = 1;
    private static String T1 = "CLIENTS";
    private static String T2 = "TRANSACTIONS";
    private static String T3 = "SAVINGS";
    private static String T1_C1 = "ClientID";
    private static String T1_C2 = "Name";
    private static String T1_C3 = "Email";
    private static String T1_C4 = "MonthlyIncome";
    private static String T1_C5 = "DailyLimit";
    private static String T1_C6 = "CurrentLimit";
    private static String T1_C7 = "RunDate";
    private static String T2_C1 = "TransID";
    private static String T2_C2 = "ClientID";
    private static String T2_C3 = "Date";
    private static String T2_C4 = "Name";
    private static String T2_C5 = "Category";
    private static String T2_C6 = "Amount";
    private static String T3_C1 = "ClientID";
    private static String T3_C2 = "Category";
    private static String T3_C3 = "Amount";

    private static String clientID;

    private SQLiteDatabase sqldb = this.getWritableDatabase();

    DatabaseHelper(Context context)  {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String T1Query = "CREATE TABLE " + T1 + "(" +
                T1_C1 + " INTEGER PRIMARY KEY," +
                T1_C2 + " TEXT NOT NULL," +
                T1_C3 + " TEXT NOT NULL UNIQUE," +
                T1_C4 + " REAL NOT NULL," +
                T1_C5 + " REAL NOT NULL," +
                T1_C6 + " REAL NOT NULL," +
                T1_C7 + " TEXT NOT NULL)";

        String T2Query = "CREATE TABLE " + T2 + "(" +
                T2_C1 + " INTEGER PRIMARY KEY," +
                T2_C2 + " INTEGER NOT NULL," +
                T2_C3 + " TEXT NOT NULL," +
                T2_C4 + " TEXT NOT NULL," +
                T2_C5 + " TEXT NOT NULL," +
                T2_C6 + " REAL NOT NULL," +
                "FOREIGN KEY(" + T2_C2 + ") REFERENCES " + T1 + "(" + T1_C1 + "))";

        String T3Query = "CREATE TABLE " + T3 + "(" +
                T3_C1 + " INTEGER NOT NULL," +
                T3_C2 + " TEXT NOT NULL," +
                T3_C3 + " REAL NOT NULL," +
                "PRIMARY KEY(" + T3_C1 + "," + T3_C2 + ")," +
                "FOREIGN KEY(" + T3_C1 + ") REFERENCES " + T1 + "(" + T1_C1 + "))";

        db.execSQL(T1Query);
        db.execSQL(T2Query);
        db.execSQL(T3Query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + T1);
        db.execSQL("DROP TABLE IF EXISTS " + T2);
        db.execSQL("DROP TABLE IF EXISTS " + T3);
        onCreate(db);
    }

    // login method, returns true if email valid and sets clientID
    boolean login(String email) {
        String [] col = {T1_C1};
        String [] args = {email};
        Cursor c = sqldb.query(T1, col, T1_C3 + "=?", args, null, null, null);

        if (c.getCount() > 0) {
            c.moveToNext();
            clientID = c.getString(0);
            c.close();
            return true;
        }
        else return false;
    }

    boolean logout() {
        clientID = "0";
        return true;
    }

    // add client (must run login, addSavings then addTrans("New Account","Records",0, new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA).format(new Date()))
    boolean addClient(String name, String email, double inc) {
        Calendar cal = Calendar.getInstance();
        int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        double lim = inc / days;

        ContentValues v = new ContentValues();
        v.put(T1_C2, name);
        v.put(T1_C3, email);
        v.put(T1_C4, inc);
        v.put(T1_C5, lim);
        v.put(T1_C6, lim);
        v.put(T1_C7, new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA).format(cal.getTime()));

        long r = sqldb.insert(T1, null, v);

        return (r > 0);
    }

    // add transaction, pass date as 'yyyy-MM-dd', then run updLimit
    boolean addTrans( String date, String name, String cat, double amt) {
        ContentValues v = new ContentValues();
        v.put(T2_C2, clientID);
        v.put(T2_C3, date);
        v.put(T2_C4, name);
        v.put(T2_C5, cat);
        v.put(T2_C6, amt);

        long r = sqldb.insert(T2, null, v);

        return (r > 0 );
    }

    // add Savings account, run after addClient
    boolean addSavings(String cat) {
        ContentValues v = new ContentValues();
        v.put(T3_C1, clientID);
        v.put(T3_C2, cat);
        v.put(T3_C3, 0);

        long r = sqldb.insert(T3, null, v);

        return (r > 0);
    }

    // updates the remaining daily limit, run after addTrans
    boolean updLimit(double amt) {
        Cursor c = sqldb.query(T1,new String[] {T1_C6},T1_C1+"=?", new String[] {clientID},null,null,null);
        c.moveToNext();
        double limit = c.getDouble(0) - amt;
        c.close();
        ContentValues v = new ContentValues();
        v.put(T1_C6, limit);
        long r = sqldb.update(T1,v,T1_C1+"=?",new String[] {clientID});

        return (r > 0);
    }

    // update monthly income
    boolean updIncome(double inc) {
        int days = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
        double lim = inc / days;

        ContentValues v = new ContentValues();
        v.put(T1_C4, inc);
        v.put(T1_C5, lim);

        long r = sqldb.update(T1, v, T1_C1 + "=?", new String[] {clientID});

        return (r > 0);
    }

    //get client info
    Cursor getClient() {
        return sqldb.query(T1, new String [] {T1_C2,T1_C3,T1_C4},T1_C1 + "=?", new String [] {clientID},null,null,null);
    }

    // get remaining limit
    Cursor getLimit() {
        return sqldb.query(T1, new String[] {T1_C6}, T1_C1 + "=?", new String[] {clientID},null,null,null);
    }

    // get transactions for date 'yyyy-MM-dd'
    Cursor getTrans(String date) {
        String [] col = {T2_C1,T2_C3,T2_C4,T2_C5,T2_C6};
        String [] args = {clientID,date};
        return sqldb.query(T2, col,T2_C2 + "=? AND " + T2_C3 + "=?", args,null,null, T2_C1);
    }


    // get transactions for month 'yyyy-MM'
    Cursor getTransMo(String date) {
        String [] col = {T2_C1,T2_C3,T2_C4,T2_C5,T2_C6};
        String [] args = {clientID,date+"%"};
        return sqldb.query(T2, col,T2_C2 + "=? AND " + T2_C3 + " LIKE ?", args,null,null, T2_C1);
    }

    // get transactions for month 'yyyy-MM' with Descending order
    Cursor getTransMoExpense(String date) {
        String [] col = {T2_C1,T2_C3,T2_C4,T2_C5,T2_C6};
        String [] args = {clientID,date+"%"};
        return sqldb.query(T2, col,T2_C2 + "=? AND " + T2_C3 + " LIKE ?", args,null,null, T2_C3 + " DESC, " + T2_C1 + " DESC");
    }

    // delete transaction by transID
    Boolean delTrans(String transID) {
        long r = sqldb.delete(T2, T2_C1 + "=?", new String[] {transID});
        return (r > 0);
    }

    // get total savings
    Cursor getSavings() {
        return sqldb.query(T3,new String[] {T3_C2,T3_C3}, T3_C1 + "=?", new String[] {clientID}, null, null,null);
    }

    // get account amount
    Cursor getSavingsAmt(String cat) {
        return sqldb.query(T3,new String[] {T3_C3}, T3_C1 + "=? AND " + T3_C2 + "=?", new String[] {clientID,cat}, null, null,null);
    }

    // get savings for date 'yyyy-MM'
    Cursor getTotSavings(String date) {
        return sqldb.query(T3, new String[] {"SUM(" + T3_C3 + ")"},T3_C1 + "=?", new String[] {clientID},null,null, null);
    }

    // move savings into categories
    boolean moveSavings(String fromCat, String toCat, double amt) {
        String query = "UPDATE " + T3 + " SET " + T3_C3 + "=" + T3_C3 + "-" + amt + " WHERE " + T3_C1 + "=" + clientID + " AND " + T3_C2 + "='" + fromCat + "'";
        sqldb.execSQL(query);

        Cursor c = sqldb.query(T3, new String[] {T3_C3},T3_C1 + "=? AND " + T3_C2 + "=?", new String[] {clientID, toCat},null,null,null);
        if (c.getCount() > 0) {
            c.moveToNext();
            amt += c.getDouble(0);
        }
        c.close();

        ContentValues v = new ContentValues();
        v.put(T3_C1, clientID);
        v.put(T3_C2, toCat);
        v.put(T3_C3, amt);
        long r = sqldb.replace(T3,null,v);

        return (r > 0);
    }

    // runs once per day, updates savings and sets daily limit
    boolean onOpen() {
        Cursor c = sqldb.rawQuery("SELECT " + T1_C5 + "," +T1_C7 + " FROM " + T1 + " WHERE " + T1_C1 + "=" + clientID, null);
        c.moveToNext();
        Double lim = c.getDouble(0);
        String lastRun = c.getString(1);
        c.close();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
        String today = sdf.format(new Date());

        if (!lastRun.equals(today)) {
            double diff = 0;
            Calendar lastDate = Calendar.getInstance();
            Calendar curDate = Calendar.getInstance();
            try {
                lastDate.setTime(sdf.parse(lastRun));
                curDate.setTime(sdf.parse(today));
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }

            while (!lastDate.equals(curDate)) {
                Double limit = lim;
                String date = sdf.format(lastDate.getTime());
                Cursor c1 = sqldb.query(T2, new String[] {"SUM("+T2_C6+")"}, T2_C1+"=? AND "+T2_C3+"=?", new String[] {clientID,date},null,null,null);
                c1.moveToNext();
                double amt = c1.getDouble(0);
                c1.close();
                if (amt < limit) {
                    limit -= amt + diff;
                    diff = 0;
                    String query = "UPDATE " + T3 + " SET " + T3_C3 + "=" + T3_C3 + "+" + limit + " WHERE " + T3_C1 + "=" + clientID + " AND " + T3_C2 + "='Uncategorized'";
                    sqldb.execSQL(query);
                } else {
                    diff = amt - limit;
                }
                lastDate.add(Calendar.DAY_OF_YEAR, 1);
            }

            lim -= diff;
            ContentValues v = new ContentValues();
            v.put(T1_C6, lim);
            v.put(T1_C7, today);
            sqldb.update(T1, v, T1_C1 + "=?", new String[] {clientID});
            return true;
        }
        return false;
    }
}