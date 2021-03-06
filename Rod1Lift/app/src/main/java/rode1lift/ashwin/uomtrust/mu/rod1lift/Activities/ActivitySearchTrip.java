
package rode1lift.ashwin.uomtrust.mu.rod1lift.Activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import rode1lift.ashwin.uomtrust.mu.rod1lift.Constant.CONSTANT;
import rode1lift.ashwin.uomtrust.mu.rod1lift.DAO.RequestDAO;
import rode1lift.ashwin.uomtrust.mu.rod1lift.DTO.RequestDTO;
import rode1lift.ashwin.uomtrust.mu.rod1lift.ENUM.RequestStatus;
import rode1lift.ashwin.uomtrust.mu.rod1lift.R;
import rode1lift.ashwin.uomtrust.mu.rod1lift.Utils.ConnectivityHelper;
import rode1lift.ashwin.uomtrust.mu.rod1lift.Utils.Utils;


public class ActivitySearchTrip extends Activity {

    private AutoCompleteTextView autoFrom;
    private AutoCompleteTextView autoTo;

    private TextView txtDate, txtTime;

    private int accountId;

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";

    public static String API_KEY;

    private FrameLayout flMain;
    private RequestDTO requestDTO;
    private Calendar requestDateTime = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_trip);

        flMain = (FrameLayout) findViewById(R.id.flMain);

        API_KEY = getString(R.string.google_places_api_key);

        TextView txtMenuHeader = (TextView)findViewById(R.id.txtMenuHeader);
        txtMenuHeader.setText(getString(R.string.activity_search_trip_header));

        SharedPreferences prefs = ActivitySearchTrip.this.getSharedPreferences(CONSTANT.APP_NAME, MODE_PRIVATE);
        accountId = prefs.getInt(CONSTANT.CURRENT_ACCOUNT_ID, 1);

        autoFrom = (AutoCompleteTextView)findViewById(R.id.autoFrom);
        autoFrom.setAdapter(new ActivitySearchTrip.GooglePlacesAutocompleteAdapter(this, android.R.layout.simple_list_item_1));

        autoTo = (AutoCompleteTextView)findViewById(R.id.autoTo);
        autoTo.setAdapter(new ActivitySearchTrip.GooglePlacesAutocompleteAdapter(this, android.R.layout.simple_list_item_1));

        txtDate = (TextView)findViewById(R.id.txtDate);
        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Utils.hideKeyboard(ActivitySearchTrip.this);

                Calendar currentTime = Calendar.getInstance();
                final int mMonth = currentTime.get(Calendar.MONTH);
                final int mYear = currentTime.get(Calendar.YEAR);
                final int mDay = currentTime.get(Calendar.DAY_OF_MONTH);
                final DatePickerDialog datePickerDialogue;
                datePickerDialogue = new DatePickerDialog(ActivitySearchTrip.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        requestDateTime.set(Calendar.YEAR, year);
                        requestDateTime.set(Calendar.MONTH, monthOfYear);
                        requestDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        txtDate.setText(String.valueOf(dayOfMonth) +" "+ new DateFormatSymbols().getMonths()[monthOfYear]);

                        Utils.hideKeyboard(ActivitySearchTrip.this);

                    }
                }, mYear, mMonth, mDay);

                datePickerDialogue.getDatePicker().setMinDate(currentTime.getTimeInMillis());
                datePickerDialogue.setTitle(getString(R.string.activity_create_trip_select_date));
                datePickerDialogue.show();
            }
        });

        txtTime = (TextView)findViewById(R.id.txtTime);
        txtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(ActivitySearchTrip.this);

                Calendar currentTime = Calendar.getInstance();
                final int mHour = currentTime.get(Calendar.HOUR_OF_DAY);
                final int mMinute = currentTime.get(Calendar.MINUTE);
                final TimePickerDialog timePickerDialog;
                timePickerDialog = new TimePickerDialog(ActivitySearchTrip.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        String min =  String.valueOf(selectedMinute).length() <2? "0"+String.valueOf(selectedMinute):String.valueOf(selectedMinute);

                        txtTime.setText(String.valueOf(selectedHour)+" : "+min);

                        requestDateTime.set(Calendar.HOUR_OF_DAY, selectedHour);
                        requestDateTime.set(Calendar.MINUTE, selectedMinute);
                        requestDateTime.set(Calendar.SECOND, 0);

                        Utils.hideKeyboard(ActivitySearchTrip.this);
                    }
                }, mHour, mMinute, false);

                timePickerDialog.setTitle(getString(R.string.activity_create_trip_select_time));
                timePickerDialog.show();
            }
        });

        TextView txtSearch = (TextView)findViewById(R.id.txtDone);
        txtSearch.setText(getString(R.string.activity_search_trip_search));
        txtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validForm()){
                    if(ConnectivityHelper.isConnected(ActivitySearchTrip.this)) {

                        Date tripStartTime = requestDTO.getEventDate();
                        Date tripEndTime = tripStartTime;

                        boolean hasTrip = new RequestDAO(ActivitySearchTrip.this).getTripByDateTime(tripStartTime, tripEndTime).size() >0;

                        Intent intent = new Intent(ActivitySearchTrip.this, ActivitySearchTripResults.class);
                        intent.putExtra(CONSTANT.REQUESTDTO, requestDTO);
                        startActivity(intent);

                    }
                    else{
                        Utils.alertError(ActivitySearchTrip.this, getString(R.string.error_no_connection));
                    }
                }
            }
        });

        ImageView imgBack = (ImageView)findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });

        Intent intent = getIntent();
        if(intent != null){
            if(intent.getStringExtra(CONSTANT.SEARCH_TRIP_FROM) != null){
                autoFrom.setText(intent.getStringExtra(CONSTANT.SEARCH_TRIP_FROM));
            }

            if(intent.getStringExtra(CONSTANT.SEARCH_TRIP_TO) != null){
                autoTo.setText(intent.getStringExtra(CONSTANT.SEARCH_TRIP_TO));
            }
        }
    }


    @Override
    public void onBackPressed() {

    }


    private boolean validForm(){
        boolean validForm = true;

        if(TextUtils.isEmpty(autoFrom.getText())){
            Utils.alertError(ActivitySearchTrip.this, getResources().getString(R.string.activity_create_trip_validation_autocomplete_address));
            return false;
        }
        else if(TextUtils.isEmpty(autoTo.getText())){
            Utils.alertError(ActivitySearchTrip.this, getResources().getString(R.string.activity_create_trip_validation_autocomplete_address));
            return false;
        }
        else if(TextUtils.isEmpty(txtDate.getText()) ){
            Utils.alertError(ActivitySearchTrip.this, getResources().getString(R.string.activity_create_trip_validation_date));
            return false;
        }
        else if(TextUtils.isEmpty(txtTime.getText())){
            Utils.alertError(ActivitySearchTrip.this, getResources().getString(R.string.activity_create_trip_validation_time));
            return false;
        }
        else{
            requestDTO = new RequestDTO();
            requestDTO.setRequestStatus(RequestStatus.REQUEST_PENDING);
            requestDTO.setEventDate(requestDateTime.getTime());
            requestDTO.setPlaceTo(autoTo.getText().toString());
            requestDTO.setPlaceFrom(autoFrom.getText().toString());
        }

        return validForm;
    }

    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    // ref: https://examples.javacodegeeks.com/android/android-google-places-autocomplete-api-example/
    public static ArrayList autocomplete(String input) {
        ArrayList resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY );
            sb.append("&components=country:mu");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (Exception e) {
            return resultList;
        }  finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                String place = predsJsonArray.getJSONObject(i).getString("description");
                String[] parts = place.split(",");
                resultList.add(parts[0]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultList;
    }

    class GooglePlacesAutocompleteAdapter extends ArrayAdapter implements Filterable {
        private ArrayList resultList;

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return (String)resultList.get(index);
        }

        @Override
        public android.widget.Filter getFilter() {
            android.widget.Filter filter = new android.widget.Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    }
                    else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }

    protected void onPause(){
        super.onPause();
        flMain.setAnimation(null);
    }

    protected void onResume(){
        super.onResume();
    }
}
