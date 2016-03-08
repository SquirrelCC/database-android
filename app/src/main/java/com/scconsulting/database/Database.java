package com.scconsulting.database;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class Database extends AppCompatActivity {

    private SQLiteDatabase database;
    private ArrayAdapter<String> adapter;
    private List<String> list;
    private ListView emplListView;
    private Cursor employee;
    private MySQLiteHelper dbHelper;
    private int employeeId = -1;
    private int listPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.database);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        dbHelper = new MySQLiteHelper(this);
        database = dbHelper.getReadableDatabase();

        emplListView = (ListView) findViewById( R.id.emplList );
        emplListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CheckedTextView textView = (CheckedTextView) view;
                textView.toggle();    // Set the clicked line checked if it was not checked, and vice versa.

                listPosition = position;
                employee.moveToPosition(position);
                employeeId = employee.getInt(0);
            }
        });
    }

    public void doNew(View v) {
        Intent intent = new Intent(this, Employee.class);
        startActivityForResult(intent, 0);
    }

    public void doEdit(View v) {

        if (employeeId >= 0) {

            Intent intent = new Intent(this, Employee.class);
            intent.putExtra("id", employeeId);
            startActivityForResult(intent, 0);

        }
        else {
            Toast.makeText(Database.this, Database.this.getString(R.string.not_selected), Toast.LENGTH_LONG).show();
        }
    }

    public void doDelete(View v) {
        list.remove(listPosition);
        adapter.notifyDataSetChanged();
        uncheckAll(emplListView);

		/*
		 * Delete employee record
		 */
        String strSql = "delete from employee" +
                " where _id = " + employeeId +
                ";";
        database.execSQL(strSql);
        strSql = null;

        employeeQuery();
        employeeId = -1;
        listPosition = -1;
    }

    private void employeeQuery() {
        if (employee != null) {
            employee.close();
        }
        String strSql = "select * from employee;";
        employee = database.rawQuery(strSql, null);
        employee.moveToFirst();
    }

    private void uncheckAll(ViewGroup vg) {
        for (int i = 0; i < vg.getChildCount(); i++) {
            CheckedTextView view = (CheckedTextView) vg.getChildAt(i);
            view.setChecked(false);
        }
    }

    public String padLeft(String s, int n) {
        return String.format("%1$" + n + "s", s);
    }

    public void setupList() {

        list = new ArrayList<String>();
        while (!employee.isAfterLast()) {

            // Put employee ID, first name, last name into each list entry
            list.add( padLeft(employee.getString(0), 5 ) + ".  " +
                    employee.getString(1) + " " + employee.getString(2));
            employee.moveToNext();
        }

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_checked, list);

        emplListView.setAdapter(adapter);
        uncheckAll(emplListView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the tool bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_database, menu);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();

        employeeQuery();
        setupList();
        employeeId = -1;
        listPosition = -1;
    }

    @Override
    public void onPause() {
        super.onPause();

        if (employee != null) {
            employee.close();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        database.close();
    }

}
