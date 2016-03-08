package com.scconsulting.database;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class Employee extends AppCompatActivity {
	
	private int result;
	private static final int RESULT_CANCELED = 99;

    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private Cursor employee = null;
    
    private int employeeId = -1;
	
	private EditText editFName;
    private EditText editLName;
    private EditText editTitle;
    private EditText editSalary;
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.employee);

		//Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		//setSupportActionBar(toolbar);
        
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
        	employeeId = extras.getInt("id");
        }
        
		dbHelper = new MySQLiteHelper(this);
        database = dbHelper.getWritableDatabase();
        
        editFName = (EditText) findViewById(R.id.editFName);
        editLName = (EditText) findViewById(R.id.editLName);
        editTitle = (EditText) findViewById(R.id.editTitle);
        editSalary = (EditText) findViewById(R.id.editSalary);

    }

	public void doCancel(View v) {
		result = RESULT_CANCELED;
		doQuit();
	}

	public void doSave(View v) {
		result = RESULT_OK;
		doQuit();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		result = RESULT_OK;

		// Edit existing employee if employeeId >= 0 
		if (employeeId > -1) {
	        if (employee != null) {
	        	employee.close();
	        }
	        String strSql = "select * from employee" +
	        		" where _id = ?" + ";";
	        String[] args = new String[]{
			  		Integer.toString(employeeId)
			};
			employee = database.rawQuery(strSql, args);
			employee.moveToFirst();
			
			editFName.setText(employee.getString(1));
			editLName.setText(employee.getString(2));
			editTitle.setText(employee.getString(3));
			editSalary.setText(employee.getString(4));
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		
		if (result == RESULT_OK) {
		
			String strSql;
			switch (employeeId) {
			case -1:
			
				strSql = "INSERT INTO " + "employee" + 
						" (firstname, " +
						"lastname, " +
						"title, " +
						"salary) " +
	    	   	    "VALUES (" + 
	    	   	    	"'" + editFName.getText() + "'" + ", " +
	    	   	    	"'" + editLName.getText() + "'" + ", " +
	    	   	    	"'" + editTitle.getText() + "'" + ", " +
	    	   	    	"'" + editSalary.getText() + "'" +
	    	   	    ");";
				
				break;
				
			default:
				
		    	strSql = "update employee set " +
		    			"firstname = " + "'" + editFName.getText() + "'" + ", " +
		    			"lastname = " + "'" + editLName.getText() + "'" + ", " +
		    			"title = " + "'" + editTitle.getText() + "'" + ", " +
		    			"salary = " + "'" + editSalary.getText() + "'" +
		    		" where " +
		    			"_id = " + employeeId + 
		    		";";
		    	
		    	break;
			}
			
			database.execSQL(strSql);
		}
	}
	
	public void doQuit() {
        Intent intent = new Intent();
        setResult(result, intent);
        finish();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		database.close();
		
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}
