package com.example.investhelp;


import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity {

	AutoCompleteTextView setSymbolAuto;
	Button btnAdd;
	ListView lvList;
	ArrayAdapter<String> mListData;
	Spinner country_selected;
	String[] symbols;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		//Get handle for country spinner
		country_selected = (Spinner) findViewById(R.id.spinner1);
		country_selected.setOnItemSelectedListener(new OnItemSelectedListener() 
		{
		    @Override
		    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) 
		    {
		          // set the adapter for the autoCOmpleteTextView here based on what was selected
		          if(country_selected.getSelectedItem().toString().equals("United States of America")) {
		                symbols = getResources().getStringArray(R.array.symbol_array);
		          } else if (country_selected.getSelectedItem().toString().equals("Canada")){
		                symbols = getResources().getStringArray(R.array.symbol_array_tsx);
		          }
		        //Create Adapter to hold values for the list
		  		ArrayAdapter<String> adapter = new ArrayAdapter<String> (MainActivity.this,android.R.layout.simple_dropdown_item_1line,symbols);
		  		setSymbolAuto.setThreshold(1);
		  		setSymbolAuto.setAdapter(adapter);
		    }

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		    
		});
		//Get Handle of Text
		setSymbolAuto = (AutoCompleteTextView) findViewById(R.id.auto_stocks);

		//Get Handle of List
		lvList = (ListView)findViewById(R.id.listView_main);
		mListData = new ArrayAdapter<String> (this,android.R.layout.simple_dropdown_item_1line);
		
		//Set Adapter to List
		lvList.setAdapter(mListData);
		
		//Get Button Handle
		btnAdd = (Button)findViewById(R.id.button1);
		btnAdd.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				//Enter Text into list
				if (setSymbolAuto != null){ 
				onAddItem2ListClick();
				}
				else{
					return;
				}
			}
			
		});
		
		//Item Clicker Function
		ClickCallBack();

	}
	
	protected void onAddItem2ListClick() {
		//Get text from TextView
		String value;
		value = this.setSymbolAuto.getText().toString();
		//Split string into two parts
		String[] parts = value.split(" ", 2);
		//Add Text to List adapter
		this.mListData.add(parts[0] + " " + parts[1]);
		//Tell adapter to refresh data
		mListData.notifyDataSetChanged();
		//Clear the text box
		setSymbolAuto.setText("");
		
	}
	
	private void ClickCallBack() {
		lvList = (ListView)findViewById(R.id.listView_main);
		mListData = new ArrayAdapter<String> (this,android.R.layout.simple_dropdown_item_1line);
		
		//Set Adapter to List
		lvList.setAdapter(mListData);
		lvList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View viewClicked,
					int position, long id) {
				String message;
				String name = (String) parent.getItemAtPosition(position);
				message = name + " deleted.";
				Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
				
				//delete item
				String itemSelected = parent.getItemAtPosition(position).toString();
				mListData.remove(itemSelected);
				mListData.notifyDataSetChanged();
				
				//delete item from Database
				
				return true;
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public void onBackPressed(){
		super.onBackPressed();
		System.exit(0);
	}
	
}
	
		
	
