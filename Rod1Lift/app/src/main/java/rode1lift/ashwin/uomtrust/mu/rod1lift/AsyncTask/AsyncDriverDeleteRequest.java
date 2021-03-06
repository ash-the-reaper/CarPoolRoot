package rode1lift.ashwin.uomtrust.mu.rod1lift.AsyncTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rode1lift.ashwin.uomtrust.mu.rod1lift.Adapter.DriverRequestAdapterPending;
import rode1lift.ashwin.uomtrust.mu.rod1lift.Adapter.DriverRequestUserAcceptedAdapter;
import rode1lift.ashwin.uomtrust.mu.rod1lift.DAO.ManageRequestDAO;
import rode1lift.ashwin.uomtrust.mu.rod1lift.DAO.RequestDAO;
import rode1lift.ashwin.uomtrust.mu.rod1lift.DTO.RequestDTO;
import rode1lift.ashwin.uomtrust.mu.rod1lift.DTO.RequestObject;
import rode1lift.ashwin.uomtrust.mu.rod1lift.ENUM.RequestStatus;
import rode1lift.ashwin.uomtrust.mu.rod1lift.R;
import rode1lift.ashwin.uomtrust.mu.rod1lift.Utils.Utils;
import rode1lift.ashwin.uomtrust.mu.rod1lift.WebService.WebService;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Ashwin on 03-Jun-17.
 */

public class AsyncDriverDeleteRequest extends AsyncTask<RequestDTO, Void ,Boolean > {

    private Context context;
    private ProgressDialog progressDialog;
    private RequestDTO requestDTO;
    private DriverRequestAdapterPending driverRequestAdapterPending;
    private List<RequestObject> requestObjectList;
    private Intent intent;

    private DriverRequestUserAcceptedAdapter driverRequestUserAcceptedAdapter;

    public AsyncDriverDeleteRequest(final Context context, DriverRequestAdapterPending driverRequestAdapterPending, List<RequestObject> requestObjectList) {
        this.context = context;
        this.driverRequestAdapterPending = driverRequestAdapterPending;
        this.requestObjectList = requestObjectList;
    }

    public AsyncDriverDeleteRequest(final Context context, DriverRequestUserAcceptedAdapter driverRequestUserAcceptedAdapter, List<RequestObject> requestObjectList) {
        this.context = context;
        this.driverRequestUserAcceptedAdapter = driverRequestUserAcceptedAdapter;
        this.requestObjectList = requestObjectList;
    }

    public AsyncDriverDeleteRequest(final Context context, Intent intent) {
        this.context = context;
        this.intent = intent;
    }

    @Override
    protected void onPreExecute() {
        String message = context.getString(R.string.async_driver_delete_trip_delete);
        progressDialog = Utils.progressDialogue(context,message);
        progressDialog.show();
    }


    @Override
    protected Boolean doInBackground(RequestDTO... params) {
        JSONObject postData = new JSONObject();
        requestDTO = params[0];

        HttpURLConnection httpURLConnection = null;

        try{
            postData.put("requestId", requestDTO.getRequestId());

            if(requestDTO.getManageRequestId() != null)
                postData.put("manageRequestId", requestDTO.getManageRequestId());

            String url;

            if(requestDTO.getRequestStatus() == RequestStatus.REQUEST_PENDING)
                url = WebService.API_DRIVER_DELETE_PENDING_REQUEST;
            else
                url = WebService.API_DRIVER_DELETE_CLIENT_REQUEST;

            httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
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

            return builder.toString().contains("true");

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
    protected void onPostExecute(Boolean deleted){
        super.onPostExecute(deleted);

        if(deleted != null && deleted) {
            if(driverRequestAdapterPending != null) {
                new RequestDAO(context).deleteRequest(requestDTO.getRequestId());

                List<RequestObject> newRequestObjectList = new ArrayList<>();
                for (RequestObject r : requestObjectList) {
                    if (!r.getRequestDTO().getRequestId().equals(requestDTO.getRequestId()))
                        newRequestObjectList.add(r);
                }
                List<Boolean> b = driverRequestAdapterPending.getConfirmDelete();
                Collections.fill(b, Boolean.FALSE);

                driverRequestAdapterPending.setConfirmDelete(b);
                driverRequestAdapterPending.setRequestObjectList(newRequestObjectList);
                driverRequestAdapterPending.notifyDataSetChanged();
            }
            else if (driverRequestUserAcceptedAdapter != null) {

                new ManageRequestDAO(context).deleteManageRequest(requestDTO.getManageRequestId());
                new RequestDAO(context).deleteRequest(requestDTO.getRequestId());

                List<RequestObject> newRequestObjectList = new ArrayList<>();

                List<Boolean> falseList = new ArrayList<>();

                for(int x = 0; x < requestObjectList.size(); x++){
                    RequestObject r = requestObjectList.get(x);
                    if (!r.getManageRequestDTOList().get(0).getManageRequestId().equals(requestDTO.getManageRequestId())) {
                        newRequestObjectList.add(r);
                        falseList.add(false);
                    }
                }


                driverRequestUserAcceptedAdapter.setConfirmDelete(falseList);
                driverRequestUserAcceptedAdapter.setRequestObjectList(newRequestObjectList);
                driverRequestUserAcceptedAdapter.notifyDataSetChanged();
            }
            else{
                ((Activity)context).setResult(RESULT_OK, intent);
                ((Activity)context).finish();
            }
        }
        else{
            Utils.alertError(context, context.getString(R.string.error_server));
        }
    }
}
