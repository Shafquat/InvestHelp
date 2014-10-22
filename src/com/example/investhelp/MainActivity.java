package com.example.investhelp;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
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

import com.gegsy.investhelp.R;

public class MainActivity extends Activity {

	
	static String yahooStockInfo1 = "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.quote%20where%20symbol%20in%20(%22";
	static String yahooStockInfo2 = "%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=";
	static String stockSymbol = "";
	static String stockLastTradePriceOnly = "";
	static String stockChange = "";	
	AutoCompleteTextView setSymbolAuto;
	Button btnAdd;
	ListView lvList;
	ArrayAdapter<String> mListData;
	Spinner country_selected;
	String[] symbols;
	ProgressDialog dialog;
	
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
				dialog = ProgressDialog.show(MainActivity.this,"Downloading","Please Wait");
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
		//String stockValue;
		//stockValue = this.setSymbolAuto.getText().toString();

		//run JSON parsing on selected stock
		// Call for doInBackground() in MyAsyncTask to be executed
		new MyAsyncTask().execute();
		
		//this.mListData.add(stockValue);
		//Tell adapter to refresh data
		//mListData.notifyDataSetChanged();
		//Clear the text box
		//setSymbolAuto.setText("");
		
	}
	
	// Use AsyncTask if you need to perform background tasks, but also need
	// to change components on the GUI. Put the background operations in
	// doInBackground. Put the GUI manipulation code in onPostExecute

	private class MyAsyncTask extends AsyncTask<String, String, String> {

		protected String doInBackground(String... arg0) {

			// HTTP Client that supports streaming uploads and downloads
			DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
			
			// Define that I want to use the POST method to grab data from
			// the provided URL
			//Get Handle of Text
			setSymbolAuto = (AutoCompleteTextView) findViewById(R.id.auto_stocks);
			String stockValue = setSymbolAuto.getText().toString();
			String[] separatedStockValue = stockValue.split(" ");
			HttpPost httppost = new HttpPost(yahooStockInfo1 + separatedStockValue[0] + yahooStockInfo2);
			
			// Web service used is defined
			httppost.setHeader("Content-type", "application/json");

			// Used to read data from the URL
			InputStream inputStream = null;
			
			// Will hold the whole all the data gathered from the URL
			String result = null;

			try {
				
				// Get a response if any from the web service
				HttpResponse response = httpclient.execute(httppost);        
				
				// The content from the requested URL along with headers, etc.
				HttpEntity entity = response.getEntity();

				// Get the main content from the URL
				inputStream = entity.getContent();
				
				// JSON is UTF-8 by default
				// BufferedReader reads data from the InputStream until the Buffer is full
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
				
				// Will store the data
				StringBuilder theStringBuilder = new StringBuilder();

				String line = null;
				
				// Read in the data from the Buffer untilnothing is left
				while ((line = reader.readLine()) != null)
				{
					
					// Add data from the buffer to the StringBuilder
					theStringBuilder.append(line + "\n");
				}
				
				// Store the complete data in result
				result = theStringBuilder.toString();

			} catch (Exception e) { 
				e.printStackTrace();
			}
			finally {
				
				// Close the InputStream when you're done with it
				try{if(inputStream != null)inputStream.close();}
				catch(Exception e){}
			}

			// Holds Key Value pairs from a JSON source
			JSONObject jsonObject;
			try {
				// Print out all the data read in
				// Log.v("JSONParser RESULT ", result);

				// Get the root JSONObject
				jsonObject = new JSONObject(result);

				// Get the JSON object named query
				JSONObject queryJSONObject = jsonObject.getJSONObject("query");
				
				// Get the JSON object named results inside of the query object
				JSONObject resultsJSONObject = queryJSONObject.getJSONObject("results");
				
				// Get the JSON object named quote inside of the results object
				JSONObject quoteJSONObject = resultsJSONObject.getJSONObject("quote");
				
				// Get the JSON Strings in the quote object
				stockSymbol = quoteJSONObject.getString("symbol");
				stockLastTradePriceOnly = quoteJSONObject.getString("LastTradePriceOnly");
				stockChange = quoteJSONObject.getString("Change");
			}
			catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return result;

		}		protected void onPostExecute(String result){
			
			mListData.add(stockSymbol + " : " + stockLastTradePriceOnly + " " + stockChange);
			//Tell adapter to refresh data
			mListData.notifyDataSetChanged();
			//Clear the text box
			setSymbolAuto.setText("");
			dialog.dismiss();
		}}


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
				String[] nameSeparated = name.split(":");
				message = nameSeparated[0] + " deleted.";
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
	
		
	
