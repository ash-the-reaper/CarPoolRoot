package rode1lift.ashwin.uomtrust.mu.rod1lift.Adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;

import java.util.List;

import rode1lift.ashwin.uomtrust.mu.rod1lift.Activities.PickerActivitySendMessage;
import rode1lift.ashwin.uomtrust.mu.rod1lift.Constant.CONSTANT;
import rode1lift.ashwin.uomtrust.mu.rod1lift.DTO.AccountDTO;
import rode1lift.ashwin.uomtrust.mu.rod1lift.ENUM.ViewType;
import rode1lift.ashwin.uomtrust.mu.rod1lift.R;
import rode1lift.ashwin.uomtrust.mu.rod1lift.Utils.Utils;

/**
 * Created by Ashwin on 09-Jul-17.
 */

public class PassengerViewDriverProfileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context = null;
    private List<ProfileObject> profileObjectList;
    private ViewType viewType;

    public PassengerViewDriverProfileAdapter(Context context, List<ProfileObject> profileObjectList){
        this.context = context;
        this.profileObjectList = profileObjectList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_profile_content, parent, false);
        return new AllViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        viewType = profileObjectList.get(position).getViewType();

        AllViewHolder view = (AllViewHolder)holder;

        view.llCar.setVisibility(View.VISIBLE);
        view.llData.setVisibility(View.VISIBLE);
        view.llProfile.setVisibility(View.VISIBLE);

        if(viewType == ViewType.PROFILE_PICTURE){
            view.llData.setVisibility(View.GONE);
            view.llCar.setVisibility(View.GONE);

            String fullName = profileObjectList.get(position).getLabel();

            view.txtFullName.setText(fullName);
            view.imgViewProfile.setImageBitmap(Utils.convertBlobToBitmap(profileObjectList.get(position).getProfilePicture()));

            /*view.fabPhoneNum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //not tested
                    *//*Integer phoneNumber = profileObjectList.get(position).getOtherUserPhoneNumber();
                    if (phoneNumber != null) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:"+phoneNumber.toString()));
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(((Activity)context), new String[]{Manifest.permission.CALL_PHONE}, 0);
                            return;
                        }
                        context.startActivity(callIntent);
                    }*//*

                    Intent intent = new Intent(context, PickerActivitySendMessage.class);
                    intent.putExtra(CONSTANT.OTHER_USER_ID, profileObjectList.get(position).getOtherUserId());
                    context.startActivity(intent);
                }
            });*/

        }
        else if (viewType == ViewType.CARS_PICTURES){
            view.llProfile.setVisibility(View.GONE);
            view.llData.setVisibility(View.GONE);
            view.imgViewCar.setImageBitmap(Utils.convertBlobToBitmap(profileObjectList.get(position).getCarsPicture()));
        }
        else if(viewType == ViewType.DATA){
            view.llProfile.setVisibility(View.GONE);
            view.llCar.setVisibility(View.GONE);

            String label = profileObjectList.get(position).getLabel();
            String data = profileObjectList.get(position).getData();

            view.txtLabel.setText(label);
            view.txtData.setText(data);
        }
    }

    @Override
    public int getItemCount() {
        return profileObjectList.size();
    }

    public class AllViewHolder extends RecyclerView.ViewHolder {
        public TextView txtLabel, txtData, txtFullName;
        public ImageView imgViewProfile, imgViewCar ;

        public LinearLayout llProfile, llData, llCar;;

        //public FloatingActionButton fabPhoneNum;

        public AllViewHolder(View view) {
            super(view);

            txtLabel = (TextView) view.findViewById(R.id.txtLabel);
            txtData = (TextView) view.findViewById(R.id.txtData);

            txtFullName = (TextView) view.findViewById(R.id.txtFullName);

            imgViewProfile = (ImageView) view.findViewById(R.id.imgViewProfile);
            imgViewCar = (ImageView) view.findViewById(R.id.imgViewCar);

            llProfile = (LinearLayout) view.findViewById(R.id.llProfile);
            llData = (LinearLayout) view.findViewById(R.id.llData);
            llCar = (LinearLayout) view.findViewById(R.id.llCar);

            //fabPhoneNum = (FloatingActionButton) view.findViewById(R.id.fabPhoneNum);

        }
    }
}
