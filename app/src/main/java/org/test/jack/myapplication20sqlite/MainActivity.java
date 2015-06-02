package org.test.jack.myapplication20sqlite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;


public class MainActivity extends ActionBarActivity {

    SQLiteDatabase contactsDB;

    private Button createDatabase,deleteDatabase,addContact,
            deleteContact,getContacts;
    private EditText nameEditText,emailEditText,idEditText,contactsEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createDatabase = (Button) findViewById(R.id.createDatabase);
        deleteDatabase = (Button) findViewById(R.id.deleteDatabase);
        addContact     = (Button) findViewById(R.id.addContact);
        deleteContact  = (Button) findViewById(R.id.deleteContact);
        getContacts    = (Button) findViewById(R.id.getContacts);

        nameEditText   = (EditText)findViewById(R.id.nameEditText);
        emailEditText  = (EditText)findViewById(R.id.emailEditText);
        idEditText     = (EditText)findViewById(R.id.idEditText);
        contactsEditText= (EditText)findViewById(R.id.contactsEditText);



    }

    public void createDatabase(View view) {

        try{

            contactsDB = this.openOrCreateDatabase("MyContacts",MODE_PRIVATE,null);//null is db error handler function

            contactsDB.execSQL("create table if not exists contacts (" +
                    "id integer primary key, name text ,email text);");

            //first use the getApplicationContext().getDatabasePath("MyContacts.db"); not work use the below
            //one works 
            File databasePath = getApplicationContext().getDatabasePath("MyContacts");


            //???
            if (databasePath.exists()){

                Toast.makeText(this,"Database created"+databasePath.toString(),Toast.LENGTH_SHORT).show();

            }else{

                Toast.makeText(this,"Database missing"+databasePath.toString(),Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){

            Log.e("Create database Exception",e.getMessage());

        }

        addContact.setClickable(true);
        getContacts.setClickable(true);
        deleteDatabase.setClickable(true);
        deleteContact.setClickable(true);


    }
    public void deleteDatabase(View view) {

        this.deleteDatabase("MyContacts");
    }
    public void addContact(View view) {

        String name = nameEditText.getText().toString();
        String email= emailEditText.getText().toString();


        contactsDB.execSQL("insert into contacts (name ,email) values ('"+name+"','"+email+"');");


    }
    public void deleteContact(View view) {

        String id = idEditText.getText().toString();

        int intID = Integer.parseInt(id);

        contactsDB.execSQL("delete from contacts where id = '"+intID+"';");

    }
    public void getContacts(View view) {

        Cursor cursor = contactsDB.rawQuery("select * from contacts;",null);

        int idColumn = cursor.getColumnIndex("id");
        int nameColumn = cursor.getColumnIndex("name");
        int emailColumn= cursor.getColumnIndex("email");

        cursor.moveToFirst();

        String contactsList = "";

        Log.e("-*-*-*-------------------------",String.valueOf(cursor.getColumnCount()));

        //first use the cursor != null not work so using the cursor.getCount() works
        if((cursor.getCount() >0 )&&(cursor.getColumnCount() >0)){

            do{
                String id = cursor.getString(idColumn);
                String name = cursor.getString(nameColumn);
                String email = cursor.getString(emailColumn);

                contactsList = contactsList + id +" : "+name+" : "+email+" : "+"\n";


            }while(cursor.moveToNext());

        }
        contactsEditText.setText(contactsList);



    }

    @Override
    protected void onDestroy() {

        contactsDB.close();
        super.onDestroy();
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
