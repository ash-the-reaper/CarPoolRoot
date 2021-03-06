package rode1lift.ashwin.uomtrust.mu.rod1lift.Activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.maps.model.LatLng;
import com.sdsmdg.harjot.crollerTest.Croller;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import rode1lift.ashwin.uomtrust.mu.rod1lift.AsyncTask.AsyncDriverCreateOrUpdateRequest;
import rode1lift.ashwin.uomtrust.mu.rod1lift.AsyncTask.AsyncDriverDeleteRequest;
import rode1lift.ashwin.uomtrust.mu.rod1lift.AsyncTask.AsyncUpdateAccount;
import rode1lift.ashwin.uomtrust.mu.rod1lift.Constant.CONSTANT;
import rode1lift.ashwin.uomtrust.mu.rod1lift.DAO.AccountDAO;
import rode1lift.ashwin.uomtrust.mu.rod1lift.DAO.CarDAO;
import rode1lift.ashwin.uomtrust.mu.rod1lift.DAO.RequestDAO;
import rode1lift.ashwin.uomtrust.mu.rod1lift.DTO.AccountDTO;
import rode1lift.ashwin.uomtrust.mu.rod1lift.DTO.CarDTO;
import rode1lift.ashwin.uomtrust.mu.rod1lift.DTO.ManageRequestDTO;
import rode1lift.ashwin.uomtrust.mu.rod1lift.DTO.RequestDTO;
import rode1lift.ashwin.uomtrust.mu.rod1lift.DTO.RequestObject;
import rode1lift.ashwin.uomtrust.mu.rod1lift.ENUM.RequestStatus;
import rode1lift.ashwin.uomtrust.mu.rod1lift.R;
import rode1lift.ashwin.uomtrust.mu.rod1lift.Utils.ConnectivityHelper;
import rode1lift.ashwin.uomtrust.mu.rod1lift.Utils.Utils;


public class ActivityCreateTrip extends Activity {


    private TextView txtPrice;
    private AutoCompleteTextView autoFrom;
    private AutoCompleteTextView autoTo;
    private TextView txtDate, txtTime;
    private EditText txtContact, txtSeatAvailable;
    private Croller croller;

    private Calendar requestDateTime = null;
    private RequestDTO requestDTO = new RequestDTO();

    private int accountId;
    private AccountDTO accountDTO;
    private CarDTO carDTO;

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";

    public static String API_KEY;
    private RequestObject requestObject = null;

    private boolean newTrip = true;
    private FloatingActionMenu fabMenu;

    private FrameLayout flMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trip);

        flMain = (FrameLayout) findViewById(R.id.flMain);

        API_KEY = getString(R.string.google_places_api_key);

        TextView txtMenuHeader = (TextView)findViewById(R.id.txtMenuHeader);
        txtMenuHeader.setText(getString(R.string.activity_create_trip_header));

        accountId = Utils.getCurrentAccount(ActivityCreateTrip.this);
        accountDTO = new AccountDAO(ActivityCreateTrip.this).getAccountById(accountId);

        txtPrice = (TextView)findViewById(R.id.txtPrice);
        txtPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityCreateTrip.this, PickerActivityTripPrice.class);
                intent.putExtra(CONSTANT.TRIP_PRICE, txtPrice.getText().toString());
                startActivityForResult(intent, CONSTANT.CREATE_TRIP_ACTIVITY);
            }
        });

        String from = getIntent().getStringExtra(CONSTANT.CREATE_TRIP_FROM), to = getIntent().getStringExtra(CONSTANT.CREATE_TRIP_TO);

        autoFrom = (AutoCompleteTextView)findViewById(R.id.autoFrom);
        autoFrom.setAdapter(new GooglePlacesAutocompleteAdapter(this, android.R.layout.simple_list_item_1));
        if(from != null && !TextUtils.isEmpty(from))
            autoFrom.setText(from);

        autoTo = (AutoCompleteTextView)findViewById(R.id.autoTo);
        autoTo.setAdapter(new GooglePlacesAutocompleteAdapter(this, android.R.layout.simple_list_item_1));
        if(to != null && !TextUtils.isEmpty(to))
            autoTo.setText(to);

        txtContact = (EditText) findViewById(R.id.txtContact);
        if(accountDTO != null && accountDTO.getPhoneNum() != null && accountDTO.getPhoneNum().toString().length() >=6)
            txtContact.setText(accountDTO.getPhoneNum().toString());

        txtSeatAvailable = (EditText) findViewById(R.id.txtSeatAvailable);
        carDTO = new CarDAO(ActivityCreateTrip.this).getCarByAccountID(accountId);
        if(carDTO != null && carDTO.getNumOfPassenger() != null)
            txtSeatAvailable.setText(carDTO.getNumOfPassenger().toString());

        txtDate = (TextView)findViewById(R.id.txtDate);
        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Utils.hideKeyboard(ActivityCreateTrip.this);

                Calendar currentTime = Calendar.getInstance();
                if(requestDateTime != null){
                    currentTime.setTime(requestDateTime.getTime());
                }
                else
                    requestDateTime = Calendar.getInstance();

                final int mMonth = currentTime.get(Calendar.MONTH);
                final int mYear = currentTime.get(Calendar.YEAR);
                final int mDay = currentTime.get(Calendar.DAY_OF_MONTH);
                final DatePickerDialog datePickerDialogue;
                datePickerDialogue = new DatePickerDialog(ActivityCreateTrip.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        requestDateTime.set(Calendar.YEAR, year);
                        requestDateTime.set(Calendar.MONTH, monthOfYear);
                        requestDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        txtDate.setText(String.valueOf(dayOfMonth) +" "+ new DateFormatSymbols().getMonths()[monthOfYear]);

                        Utils.hideKeyboard(ActivityCreateTrip.this);

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
                Utils.hideKeyboard(ActivityCreateTrip.this);

                Calendar currentTime = Calendar.getInstance();
                if(requestDateTime != null){
                    currentTime.setTime(requestDateTime.getTime());
                }
                else
                    requestDateTime = Calendar.getInstance();

                final int mHour = currentTime.get(Calendar.HOUR_OF_DAY);
                final int mMinute = currentTime.get(Calendar.MINUTE);
                final TimePickerDialog timePickerDialog;
                timePickerDialog = new TimePickerDialog(ActivityCreateTrip.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        String min =  String.valueOf(selectedMinute).length() <2? "0"+String.valueOf(selectedMinute):String.valueOf(selectedMinute);

                        txtTime.setText(String.valueOf(selectedHour)+" : "+min);

                        requestDateTime.set(Calendar.HOUR_OF_DAY, selectedHour);
                        requestDateTime.set(Calendar.MINUTE, selectedMinute);
                        requestDateTime.set(Calendar.SECOND, 0);

                        Utils.hideKeyboard(ActivityCreateTrip.this);
                    }
                }, mHour, mMinute, false);

                timePickerDialog.setTitle(getString(R.string.activity_create_trip_select_time));
                timePickerDialog.show();
            }
        });

        slider();

        requestObject = (RequestObject)getIntent().getSerializableExtra(CONSTANT.REQUEST_OBJECT);

        ImageView imgBack = (ImageView)findViewById(R.id.imgBack);

        TextView txtDone = (TextView)findViewById(R.id.txtDone);
        txtDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validForm()){
                    if(ConnectivityHelper.isConnected(ActivityCreateTrip.this)) {

                        String source = autoFrom.getText().toString();
                        String destination = autoTo.getText().toString();
                        Context context = ActivityCreateTrip.this;

                        LatLng gSource = Utils.getLatLngFromAddress(context, source);
                        LatLng gDestination = Utils.getLatLngFromAddress(context, destination);

                        getUrl(gSource, gDestination);

                        //new AsyncDriverCreateOrUpdateRequest(ActivityCreateTrip.this, true).execute(requestDTO);
                    }
                    else{
                        Utils.alertError(ActivityCreateTrip.this, getString(R.string.error_no_connection));
                    }
                }
            }
        });

        if(requestObject != null && requestObject.getRequestDTO() != null && requestObject.getRequestDTO().getRequestId() != null){

            requestDTO = requestObject.getRequestDTO();
            autoFrom.setText(requestDTO.getPlaceFrom());
            autoTo.setText(requestDTO.getPlaceTo());

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(requestDTO.getEventDate());

            txtDate.setText(String.valueOf(calendar.getTime().getDate()) +" "+ new DateFormatSymbols().getMonths()[calendar.getTime().getMonth()]);

            int selectedMinute = calendar.getTime().getMinutes();
            int selectedHour = calendar.getTime().getHours();
            String min =  String.valueOf(selectedMinute).length() <2? "0"+String.valueOf(selectedMinute):String.valueOf(selectedMinute);
            txtTime.setText(String.valueOf(selectedHour)+" : "+min);

            String phoneNum = new AccountDAO(ActivityCreateTrip.this).getAccountById(accountId).getPhoneNum().toString();
            txtContact.setText(phoneNum);

            requestDateTime = Calendar.getInstance();
            requestDateTime.setTime(requestDTO.getEventDate());

            croller.setProgress((requestDTO.getPrice()/5));

            imgBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

            fabMenu = (FloatingActionMenu)findViewById(R.id.fabMenu);
            fabMenu.setVisibility(View.VISIBLE);

            FloatingActionButton fabViewDetails = (FloatingActionButton)findViewById(R.id.fabViewDetails);
            fabViewDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fabMenu.close(true);
                    Intent intent = new Intent(ActivityCreateTrip.this, ActivityDriverTripDetails.class);
                    RequestObject requestObject = (RequestObject)getIntent().getSerializableExtra(CONSTANT.REQUEST_OBJECT);
                    List<ManageRequestDTO> manageRequestDTOList = requestObject.getManageRequestDTOList();
                    if(manageRequestDTOList != null && manageRequestDTOList.size() >0) {
                        intent.putExtra(CONSTANT.REQUEST_OBJECT, requestObject);
                        startActivity(intent);
                    }
                    else{
                        Utils.alertError(ActivityCreateTrip.this, getString(R.string.activity_create_trip_price_no_client));
                    }
                }
            });

            FloatingActionButton fabDeleteTrip = (FloatingActionButton)findViewById(R.id.fabDeleteTrip);
            fabDeleteTrip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertCancel(!newTrip);
                }
            });

            List<ManageRequestDTO> manageRequestDTOList = requestObject.getManageRequestDTOList();
            if(manageRequestDTOList != null && manageRequestDTOList.size() >0){
                Utils.animateLayout(flMain);

                autoFrom.setFocusable(false);
                autoFrom.setFocusableInTouchMode(false);
                autoFrom.setEnabled(false);
                autoTo.setFocusable(false);
                autoTo.setFocusableInTouchMode(false);
                autoTo.setEnabled(false);
                txtDate.setEnabled(false);
                txtTime.setEnabled(false);
                txtPrice.setEnabled(false);
                txtSeatAvailable.setEnabled(false);
                txtContact.setEnabled(false);
                croller.setEnabled(false);
                txtDone.setEnabled(false);
                txtDone.setVisibility(View.INVISIBLE);
            }
        }
        else{
            imgBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertCancel(newTrip);
                }
            });
        }
    }

    private void alertCancel(final boolean newTrip) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ActivityCreateTrip.this);

        alertDialog.setTitle(getString(R.string.activity_create_trip_confirm_delete));
        alertDialog.setMessage(getString(R.string.activity_create_trip_confirm_delete_message));

        alertDialog.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                if(!newTrip){
                    Intent intent = getIntent();
                    if(ConnectivityHelper.isConnected(ActivityCreateTrip.this)) {
                        new AsyncDriverDeleteRequest(ActivityCreateTrip.this, intent).execute(requestDTO);
                    }
                    else{
                        Utils.alertError(ActivityCreateTrip.this, getString(R.string.error_no_connection));
                    }

                    fabMenu.close(true);
                }
                else {
                    finish();
                }
            }
        });

        alertDialog.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        if(requestObject == null)
            alertCancel(newTrip);
        else{
            finish();
        }
    }

    private void slider(){
        //https://android-arsenal.com/details/1/5079
        croller = (Croller) findViewById(R.id.croller);
        croller.setIndicatorWidth(12);

        croller.setBackCircleColor(Color.TRANSPARENT);
        croller.setMainCircleColor(Color.TRANSPARENT);

        croller.setMax(20);
        croller.setStartOffset(45);
        croller.setIsContinuous(false);
        croller.setLabelSize(40);
        croller.setLabelColor(Color.WHITE);
        croller.setProgressPrimaryColor(getResources().getColor(R.color.white));
        croller.setIndicatorColor(getResources().getColor(R.color.white));
        croller.setProgressSecondaryColor(getResources().getColor(R.color.black));
        croller.setLabel(getString(R.string.activity_create_trip_price));
        croller.setProgressPrimaryCircleSize(7f);
        croller.setProgressSecondaryCircleSize(5f);

        croller = (Croller) findViewById(R.id.croller);
        croller.setOnProgressChangedListener(new Croller.onProgressChangedListener() {
            @Override
            public void onProgressChanged(int progress) {
                txtPrice.setText(String.valueOf(progress*5));
            }
        });

    }

    private boolean validForm(){
        boolean validForm = true;

        if(TextUtils.isEmpty(autoFrom.getText())){
            Utils.alertError(ActivityCreateTrip.this, getResources().getString(R.string.activity_create_trip_validation_autocomplete_address));
            return false;
        }
        else if(TextUtils.isEmpty(autoTo.getText())){
            Utils.alertError(ActivityCreateTrip.this, getResources().getString(R.string.activity_create_trip_validation_autocomplete_address));
            return false;
        }
        else if(TextUtils.isEmpty(txtDate.getText()) ){
            Utils.alertError(ActivityCreateTrip.this, getResources().getString(R.string.activity_create_trip_validation_date));
            return false;
        }
        else if(TextUtils.isEmpty(txtTime.getText())){
            Utils.alertError(ActivityCreateTrip.this, getResources().getString(R.string.activity_create_trip_validation_time));
            return false;
        }
        else if(TextUtils.isEmpty(txtSeatAvailable.getText().toString())){
            Utils.alertError(ActivityCreateTrip.this, getResources().getString(R.string.activity_create_trip_validation_seat_available));
            return false;
        }
        else if(TextUtils.isEmpty(txtContact.getText().toString())){
            Utils.alertError(ActivityCreateTrip.this, getResources().getString(R.string.activity_create_trip_validation_contact_detail));
            return false;
        }
        else if(txtContact.getText().toString().length() >=9 || txtContact.getText().toString().length() <=6){
            Utils.alertError(ActivityCreateTrip.this, getResources().getString(R.string.activity_create_trip_validation_contact_detail_length));
            return false;
        }

        int numOfPassenger = carDTO.getNumOfPassenger();
        int numOfPassengerEntered = Integer.parseInt(txtSeatAvailable.getText().toString());
        if( numOfPassengerEntered> numOfPassenger || numOfPassengerEntered <1){
            String message = getResources().getString(R.string.activity_create_trip_validation_seat_available_length) +" - "+numOfPassenger;
            Utils.alertError(ActivityCreateTrip.this, message);
            return false;
        }


        if(validForm){
            if(accountDTO != null && accountDTO.getPhoneNum() == null){
                accountDTO.setPhoneNum(Integer.parseInt(txtContact.getText().toString()));
                new AccountDAO(ActivityCreateTrip.this).saveOrUpdateAccount(accountDTO);
                new AsyncUpdateAccount(ActivityCreateTrip.this).execute(accountDTO);
            }
            else if(!accountDTO.getPhoneNum().equals(Integer.valueOf(txtContact.getText().toString()))){
                accountDTO.setPhoneNum(Integer.parseInt(txtContact.getText().toString()));
                new AccountDAO(ActivityCreateTrip.this).saveOrUpdateAccount(accountDTO);
                new AsyncUpdateAccount(ActivityCreateTrip.this).execute(accountDTO);
            }

            requestDTO.setAccountId(accountId);
            requestDTO.setPlaceFrom(autoFrom.getText().toString());
            requestDTO.setPlaceTo(autoTo.getText().toString());
            requestDTO.setEventDate(requestDateTime.getTime());
            requestDTO.setRequestStatus(RequestStatus.REQUEST_PENDING);
            requestDTO.setPrice(Integer.parseInt(txtPrice.getText().toString()));
            requestDTO.setSeatAvailable(Integer.parseInt(txtSeatAvailable.getText().toString()));

            Date date = new Date();
            if(requestDTO.getDateCreated() == null)
                requestDTO.setDateCreated(date);

            requestDTO.setDateUpdated(date);
        }
        else{
            Utils.vibrate(ActivityCreateTrip.this);
        }

        return validForm;
    }

    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CONSTANT.CREATE_TRIP_ACTIVITY) {
            String tripPrice = data.getStringExtra(CONSTANT.TRIP_PRICE);

            txtPrice.setText(tripPrice);
            croller.setProgress(Integer.parseInt(tripPrice)/5);
        }

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


    private void getUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";

        String googleMapDirectionKey = getResources().getString(R.string.google_direction_api_key);

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode +"&key="+googleMapDirectionKey;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        FetchUrl fetchUrl = new FetchUrl();
        fetchUrl.execute(url);
    }

    // Fetches data from url passed
    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DataParser parser = new DataParser();
                // Starts parsing data
                parser.parse(jObject);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {

            Date tripStartTime = requestDTO.getEventDate();
            Date tripEndTime = requestDTO.getTripEndTime();

            boolean hasTrip = new RequestDAO(ActivityCreateTrip.this).getTripByDateTime(tripStartTime, tripEndTime).size() >0;

            if(hasTrip){
                String message = getResources().getString(R.string.error_already_has_trip);
                Utils.alertError(ActivityCreateTrip.this, message);
            }
            else {
                new AsyncDriverCreateOrUpdateRequest(ActivityCreateTrip.this, true).execute(requestDTO);
            }
        }
    }

    public class DataParser {

        /** Receives a JSONObject and returns a list of lists containing latitude and longitude */
        public List<List<HashMap<String,String>>> parse(JSONObject jObject){

            List<List<HashMap<String, String>>> routes = new ArrayList<>() ;
            JSONArray jRoutes;
            JSONArray jLegs;
            JSONObject durationObject;
            String sDuration;

            Long duration = 0L;

            try {

                jRoutes = jObject.getJSONArray("routes");

                /** Traversing all routes */
                for(int i=0;i<jRoutes.length();i++){
                    jLegs = ( (JSONObject)jRoutes.get(i)).getJSONArray("legs");

                    durationObject = jLegs.getJSONObject(0);
                    sDuration = durationObject.getJSONObject("duration").getString("text");
                    String number = sDuration.replaceAll("[\\D]", "");

                    duration = duration+Long.parseLong(number);
                    Log.e("Duration", duration.toString());

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                if(duration != null && duration >0) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(requestDTO.getEventDate().getTime());
                    cal.add(Calendar.MINUTE, duration.intValue());
                    requestDTO.setTripEndTime(cal.getTime());
                }
                else{
                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(requestDTO.getEventDate().getTime());
                    cal.add(Calendar.MINUTE, 60);
                    requestDTO.setTripEndTime(cal.getTime());
                }
            }

            return routes;
        }
    }
}
