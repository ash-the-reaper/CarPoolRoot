package rode1lift.ashwin.uomtrust.mu.rod1lift.AsyncTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import rode1lift.ashwin.uomtrust.mu.rod1lift.Activities.LoginActivity;
import rode1lift.ashwin.uomtrust.mu.rod1lift.Activities.MainActivity;
import rode1lift.ashwin.uomtrust.mu.rod1lift.Constant.Const;
import rode1lift.ashwin.uomtrust.mu.rod1lift.DAO.AccountDAO;
import rode1lift.ashwin.uomtrust.mu.rod1lift.DAO.CarDAO;
import rode1lift.ashwin.uomtrust.mu.rod1lift.DTO.AccountDTO;
import rode1lift.ashwin.uomtrust.mu.rod1lift.DTO.CarDTO;
import rode1lift.ashwin.uomtrust.mu.rod1lift.ENUM.AccountRole;
import rode1lift.ashwin.uomtrust.mu.rod1lift.R;
import rode1lift.ashwin.uomtrust.mu.rod1lift.Utils.Utils;
import rode1lift.ashwin.uomtrust.mu.rod1lift.WebService.WebService;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Ashwin on 03-Jun-17.
 */

public class AsyncCreateAccount extends AsyncTask<AccountDTO, Void ,AccountDTO> {

    private Context context;
    private ProgressDialog progressDialog;

    public AsyncCreateAccount(final Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = Utils.progressDialogue(context, "Creating your account");
        progressDialog.show();
    }


    @Override
    protected AccountDTO doInBackground(AccountDTO... params) {
        JSONObject postData = new JSONObject();
        AccountDTO accountDTO = params[0];
        HttpURLConnection httpURLConnection = null;

        try{
            postData.put("firstName", accountDTO.getFirstName());
            postData.put("lastName", accountDTO.getLastName());
            postData.put("email", accountDTO.getEmail());
            postData.put("accountRole", accountDTO.getAccountRole().getValue());
            postData.put("accountStatus", accountDTO.getAccountStatus().getValue());

            if(accountDTO.getFacebookId() != null)
                postData.put("facebookId", accountDTO.getFacebookId());

            if(accountDTO.getGoogleId() != null)
                postData.put("googleId", accountDTO.getGoogleId());

            if(accountDTO.getDateCreated() != null)
                postData.put("dateCreated", accountDTO.getDateCreated().getTime());

            if(accountDTO.getDateUpdated() != null)
                postData.put("dateUpdated", accountDTO.getDateUpdated().getTime());



            httpURLConnection = (HttpURLConnection) new URL(WebService.API_CREATE_ACCOUNT).openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            httpURLConnection.setRequestProperty("Accept", "application/json; charset=utf-8");
            httpURLConnection.setRequestProperty("accept-charset", "UTF-8");
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

            JSONObject jsonObject = new JSONObject(builder.toString());
            accountDTO.setAccountId(jsonObject.getInt("accountId"));

            return accountDTO;

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
    protected void onPostExecute(AccountDTO accountDTO){
        super.onPostExecute(accountDTO);

        if(accountDTO != null && accountDTO.getAccountId() > 0) {
            new AccountDAO(context).updateAccountIdFromWS(accountDTO.getAccountId());

            SharedPreferences.Editor editor = context.getSharedPreferences(Const.appName, MODE_PRIVATE).edit();
            editor.putBoolean(Const.login, true);
            editor.putInt(Const.currentAccountId, accountDTO.getAccountId());
            editor.commit();


            if (accountDTO.getAccountRole() == AccountRole.DRIVER) {
                CarDTO carDetailsDTO = new CarDAO(context).getCarByAccountID(-1);
                carDetailsDTO.setAccountId(accountDTO.getAccountId());
                new CarDAO(context).saveOrUpdateCar(carDetailsDTO);
               // new AsyncCreateCar(context).execute(carDetailsDTO);
            } else {
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
            }
        }
        else{
            Utils.showToast(context, context.getString(R.string.error_server));
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
        }
    }
}