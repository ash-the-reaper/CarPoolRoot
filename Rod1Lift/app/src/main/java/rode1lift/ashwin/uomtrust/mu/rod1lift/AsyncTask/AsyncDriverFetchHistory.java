package rode1lift.ashwin.uomtrust.mu.rod1lift.AsyncTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rode1lift.ashwin.uomtrust.mu.rod1lift.Adapter.DriverHistoryAdapter;
import rode1lift.ashwin.uomtrust.mu.rod1lift.DTO.AccountDTO;
import rode1lift.ashwin.uomtrust.mu.rod1lift.DTO.ManageRequestDTO;
import rode1lift.ashwin.uomtrust.mu.rod1lift.DTO.RequestDTO;
import rode1lift.ashwin.uomtrust.mu.rod1lift.DTO.RequestObject;
import rode1lift.ashwin.uomtrust.mu.rod1lift.ENUM.RequestStatus;
import rode1lift.ashwin.uomtrust.mu.rod1lift.R;
import rode1lift.ashwin.uomtrust.mu.rod1lift.Utils.Utils;
import rode1lift.ashwin.uomtrust.mu.rod1lift.WebService.WebService;

/**
 * Created by Ashwin on 03-Jun-17.
 */

public class AsyncDriverFetchHistory extends AsyncTask<Void, Void ,List<RequestObject >> {

    private Context context;
    private ProgressDialog progressDialog;
    private RecyclerView recyclerView;

    public AsyncDriverFetchHistory(final Context context, RecyclerView recyclerView ) {
        this.context = context;
        this.recyclerView = recyclerView;
    }

    @Override
    protected void onPreExecute() {
        String message = context.getString(R.string.async_driver_fetch_history);
        progressDialog = Utils.progressDialogue(context,message);
        progressDialog.show();
    }


    @Override
    protected List<RequestObject> doInBackground(Void... params) {
        JSONObject postData = new JSONObject();

        HttpURLConnection httpURLConnection = null;

        try{

            int userId = Utils.getCurrentAccount(context);

            postData.put("accountId", userId);

            httpURLConnection = (HttpURLConnection) new URL(WebService.API_DRIVER_GET_HISTORY_LIST).openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            httpURLConnection.setRequestProperty("Accept", "application/json;charset=UTF-8");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.connect();


            DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
            wr.writeBytes(postData.toString());
            wr.flush();
            wr.close();

            InputStream responseStream = httpURLConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(responseStream));

            final StringBuilder builder = new StringBuilder();
            String inputLine;

            while ((inputLine = reader.readLine()) != null) {
                builder.append(inputLine).append("\n");
            }

            JSONArray jsonMain = new JSONArray(builder.toString());

            List<RequestObject> requestObjectList = new ArrayList<>();

            for(int x = 0; x<jsonMain.length(); x++){
                JSONObject jsonObjectList = jsonMain.getJSONObject(x);
                JSONObject jsonObjectRequest = jsonObjectList.getJSONObject("requestDTO");

                RequestObject requestObject = new RequestObject();

                RequestDTO newRequestDTO = new RequestDTO();
                newRequestDTO.setAccountId(jsonObjectRequest.getInt("accountId"));
                newRequestDTO.setRequestId(jsonObjectRequest.getInt("requestId"));
                newRequestDTO.setSeatAvailable(jsonObjectRequest.getInt("seatAvailable"));
                newRequestDTO.setEventDate(new Date(jsonObjectRequest.getLong("eventDate")));
                newRequestDTO.setPlaceFrom(jsonObjectRequest.getString("placeFrom"));
                newRequestDTO.setPlaceTo(jsonObjectRequest.getString("placeTo"));
                newRequestDTO.setPrice(jsonObjectRequest.getInt("price"));

                if(jsonObjectRequest.has("tripDuration")&& !jsonObjectRequest.isNull("tripDuration"))
                    newRequestDTO.setTripEndTime(new Date(jsonObjectRequest.getLong("tripDuration")));


                List<ManageRequestDTO> manageRequestDTOList = new ArrayList<>();
                JSONArray jsonManageRequestMain = jsonObjectList.getJSONArray("manageRequestDTOList");
                for(int y = 0; y<jsonManageRequestMain.length(); y++){
                    JSONObject jsonObjectManageRequest = jsonManageRequestMain.getJSONObject(y);

                    ManageRequestDTO manageRequestDTO = new ManageRequestDTO();
                    manageRequestDTO.setManageRequestId(jsonObjectManageRequest.getInt("manageRequestId"));
                    manageRequestDTO.setSeatRequested(jsonObjectManageRequest.getInt("seatRequested"));
                    manageRequestDTO.setAccountId(jsonObjectManageRequest.getInt("accountId"));
                    manageRequestDTO.setCarId(jsonObjectManageRequest.getInt("carId"));
                    manageRequestDTO.setDateCreated(new Date(jsonObjectManageRequest.getLong("dateCreated")));
                    manageRequestDTO.setDateUpdated(new Date(jsonObjectManageRequest.getLong("dateUpdated")));
                    manageRequestDTO.setRequestId(jsonObjectManageRequest.getInt("requestId"));
                    RequestStatus r = RequestStatus.valueOf(jsonObjectManageRequest.getString("requestStatus"));
                    manageRequestDTO.setRequestStatus(r);

                    manageRequestDTOList.add(manageRequestDTO);
                }


                List<AccountDTO> accountDTOList = new ArrayList<>();
                JSONArray jsonAccountMain = jsonObjectList.getJSONArray("accountDTOList");
                for(int y = 0; y<jsonAccountMain.length(); y++){
                    JSONObject jsonObjectAccount = jsonAccountMain.getJSONObject(y);

                    AccountDTO account = new AccountDTO();
                    account.setAccountId(jsonObjectAccount.getInt("accountId"));
                    account.setFullName(jsonObjectAccount.getString("fullName"));
                    account.setPhoneNum(jsonObjectAccount.getInt("phoneNum"));
                    account.setProfilePicture(Base64.decode(jsonObjectAccount.getString("sProfilePicture"), Base64.DEFAULT));

                    if(jsonObjectAccount.has("rating") && !jsonObjectAccount.isNull("rating")) {
                        account.setRating(jsonObjectAccount.getDouble("rating"));
                        account.setRatingCount(jsonObjectAccount.getInt("ratingCount"));
                    }

                    accountDTOList.add(account);
                }

                requestObject.setRequestDTO(newRequestDTO);
                requestObject.setManageRequestDTOList(manageRequestDTOList);
                requestObject.setAccountDTOList(accountDTOList);

                requestObjectList.add(requestObject);
            }

            return requestObjectList;

        }catch (Exception e){
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }

            if(progressDialog != null && progressDialog.isShowing())
                progressDialog.dismiss();
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<RequestObject> requestObjectList){
        super.onPostExecute(requestObjectList);

        if(progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();

        if(requestObjectList != null && requestObjectList.size() >0) {
            DriverHistoryAdapter driverHistoryAdapter = new DriverHistoryAdapter(context, requestObjectList);
            recyclerView.setAdapter(driverHistoryAdapter);
        }

        /*else  {
            Utils.alertError(context, context.getString(R.string.error_server));
        }*/
    }
}
