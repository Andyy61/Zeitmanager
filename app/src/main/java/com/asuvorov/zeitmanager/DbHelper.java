package com.asuvorov.zeitmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ASuvorov on 06.06.2016.
 *
 */
public class DbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "zeitmanager.DB";
    private static final int DATABASE_VERSION = 2;
    private SQLiteDatabase sqLiteDatabase;

    Context context;
    private static final String CREATE_QUERY =
            "CREATE TABLE " + DBInformation.TABLENAME +
                    "(" + DBInformation.DATE + " TEXT,"
                    + DBInformation.WOCHENNUMMER + " TEXT,"
                    + DBInformation.WOCHENTAG + " TEXT,"
                    + DBInformation.KOMMEN + " TEXT,"
                    + DBInformation.PAUSE + " TEXT,"
                    + DBInformation.GEHEN + " TEXT);";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.e("DATABASE OPERATIONS", "Database created / opened...");
        this.context = context;
        sqLiteDatabase = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_QUERY);
        Log.e("DATABASE OPERATIONS", "Database created...");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addInformation(String datum, String wochennummer, String wochentag, String kommen, String pause, String gehen) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(DBInformation.DATE, datum);
        contentValues.put(DBInformation.WOCHENNUMMER, wochennummer);
        contentValues.put(DBInformation.WOCHENTAG, wochentag);
        contentValues.put(DBInformation.KOMMEN, kommen);
        contentValues.put(DBInformation.PAUSE, pause);
        contentValues.put(DBInformation.GEHEN, gehen);

        sqLiteDatabase.insert(DBInformation.TABLENAME, null, contentValues);

    }

    public int updateDay(String datum, String wochennummer, String wochentag, String kommen, String pause, String gehen) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBInformation.DATE, datum);
        contentValues.put(DBInformation.WOCHENNUMMER, wochennummer);
        contentValues.put(DBInformation.WOCHENTAG, wochentag);
        contentValues.put(DBInformation.KOMMEN, kommen);
        contentValues.put(DBInformation.PAUSE, pause);
        contentValues.put(DBInformation.GEHEN, gehen);

        String selection = DBInformation.DATE + " = ?";

        String[] selection_arg = {datum};

       return sqLiteDatabase.update(DBInformation.TABLENAME, contentValues, selection, selection_arg);
    }

    public Cursor getInformation(String datum) {
        Cursor cursor;
        String[] projections = {DBInformation.DATE, DBInformation.WOCHENNUMMER, DBInformation.WOCHENTAG, DBInformation.KOMMEN, DBInformation.PAUSE, DBInformation.GEHEN};
        String selection = DBInformation.DATE + " like ?";


        String[] selection_arg = {datum};


        cursor = sqLiteDatabase.query(DBInformation.TABLENAME, projections, selection, selection_arg, null, null, null);
        return cursor;
    }

    public Cursor getWeek(String datum) {
        Cursor cursor = null;
        try {

            String[] projections = {DBInformation.DATE, DBInformation.WOCHENNUMMER, DBInformation.WOCHENTAG, DBInformation.KOMMEN, DBInformation.PAUSE, DBInformation.GEHEN};

            SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY);
            Calendar cal = Calendar.getInstance();

            cal.setTime(df.parse(datum));

            SimpleDateFormat weekFormat = new SimpleDateFormat("w", Locale.GERMANY);
            String weekDay = weekFormat.format(cal.getTime());

            String selection = DBInformation.WOCHENNUMMER + " = ?";
            String[] selection_arg = {weekDay};

            cursor = sqLiteDatabase.query(DBInformation.TABLENAME, projections, selection, selection_arg, null, null, null);

        } catch (ParseException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return cursor;
    }

    public void setDaysOfWeek(Date date)
    {
        for(int i = 2; i < 7; i++)
        {
            try {
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                cal.set(Calendar.DAY_OF_WEEK, i);
                SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY);
                String dayDate = df.format(cal.getTime());

                SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE", Locale.GERMANY);
                String weekDay = dateFormat.format(cal.getTime());

                SimpleDateFormat weekFormat = new SimpleDateFormat("w", Locale.GERMANY);
                String weekOfYear = weekFormat.format(cal.getTime());

                addInformation(dayDate, weekOfYear, weekDay, "06:30", "01:25", "15:45");
            }
            catch (Exception e)
            {
                Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }

    public int updateKommen(String date, String kommen)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBInformation.DATE    , date);
        contentValues.put(DBInformation.KOMMEN  , kommen);


        String selection = DBInformation.DATE +" = ?";

        String[] selection_arg = {date};

        return sqLiteDatabase.update(DBInformation.TABLENAME,contentValues,selection,selection_arg);
    }
    public int updatePause(String date, String pause)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBInformation.DATE    , date);
        contentValues.put(DBInformation.PAUSE  , pause);


        String selection = DBInformation.DATE +" = ?";

        String[] selection_arg = {date};

        return sqLiteDatabase.update(DBInformation.TABLENAME,contentValues,selection,selection_arg);
    }
    public int updateGehen(String date, String gehen)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBInformation.DATE    , date);
        contentValues.put(DBInformation.GEHEN  , gehen);



        String selection = DBInformation.DATE +" = ?";

        String[] selection_arg = {date};
        String a = "0";
        return sqLiteDatabase.update(DBInformation.TABLENAME,contentValues,selection,selection_arg);
    }
}