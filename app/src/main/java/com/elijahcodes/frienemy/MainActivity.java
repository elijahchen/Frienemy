package com.elijahcodes.frienemy;

import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ListView contactNames;
    private static final int REQUEST_CODE_READ_CONTACTS = 1;
    private static boolean READ_CONTACTS_GRANTED = false;
    FloatingActionButton fab = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        contactNames = (ListView) findViewById(R.id.contact_names);

        int hasReadContactPermission = ContextCompat.checkSelfPermission(this, READ_CONTACTS);
        Log.d(TAG, "onCreate: checkSelfPermission = " + hasReadContactPermission);

        if(hasReadContactPermission == PackageManager.PERMISSION_GRANTED){
            Log.d(TAG, "onCreate: Permission Granted");
            READ_CONTACTS_GRANTED = true;
        } else {
            Log.d(TAG, "onCreate: Requesting Permission");
            ActivityCompat.requestPermissions(this, new String[]{READ_CONTACTS}, REQUEST_CODE_READ_CONTACTS);
        }

        //TEST
//        SQLiteDatabase sqLiteDatabase = getBaseContext().openOrCreateDatabase("sqlite-test.db", MODE_PRIVATE, null);
//        String sql = "DROP TABLE IF EXISTS contacts;";
//        Log.d(TAG, "onCreate: sql = " + sql);
//        sqLiteDatabase.execSQL(sql);
//        sql = "CREATE TABLE IF NOT EXISTS contacts(name TEXT, phone INTEGER, email TEXT);";
//        Log.d(TAG, "onCreate: sql is " + sql);
//        sqLiteDatabase.execSQL(sql);
//        sql = "INSERT INTO contacts VALUES('John', 185157126, 'john@email.com');";
//        Log.d(TAG, "onCreate: sql is " + sql);
//        sqLiteDatabase.execSQL(sql);
//        sql = "INSERT INTO contacts VALUES('Sam', 827176452, 'sam@email.eu');";
//        Log.d(TAG, "onCreate: sql is " + sql);
//        sqLiteDatabase.execSQL(sql);

//        Cursor query = sqLiteDatabase.rawQuery("SELECT * FROM contacts;", null);
//        if (query.moveToFirst()) {
//            do {
//                String name = query.getString(0);
//                int phone = query.getInt(1);
//                String email = query.getString(2);
//                Toast.makeText(this, "Name = " + name + ", phone = " + phone + ", email = " + email, Toast.LENGTH_LONG).show();
//            } while (query.moveToNext());
//        }
//        query.close();
//        sqLiteDatabase.close();

        fab = (FloatingActionButton) findViewById(R.id.fab);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: fab onClick: Starts");

                String[] projection = {ContactsContract.Contacts.DISPLAY_NAME_PRIMARY};
                ContentResolver contentResolver = getContentResolver();
                Cursor cursor = contentResolver.query(
                        ContactsContract.Contacts.CONTENT_URI,
                        projection,
                        null,
                        null,
                        ContactsContract.Contacts.DISPLAY_NAME_PRIMARY);
                if(cursor != null){
                    List<String> contacts = new ArrayList<String>();
                    while(cursor.moveToNext()){
                        contacts.add(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
                    }
                    cursor.close();
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, R.layout.contact_detail,R.id.name, contacts);
                    contactNames.setAdapter(arrayAdapter);
                }
                Log.d(TAG, "fab onClick: Ends");
            }
        });
        Log.d(TAG, "onCreate: Ends");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: Starts");
        switch(requestCode){
            case REQUEST_CODE_READ_CONTACTS:{
                // When request is cancelled, arrays will be empty
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Log.d(TAG, "onRequestPermissionsResult: Permission Granted");
                    READ_CONTACTS_GRANTED = true;
                } else
                    Log.d(TAG, "onRequestPermissionsResult: Permission Denied");
                }
//            fab.setEnabled(READ_CONTACTS_GRANTED);
            Log.d(TAG, "onRequestPermissionsResult: Ends");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
