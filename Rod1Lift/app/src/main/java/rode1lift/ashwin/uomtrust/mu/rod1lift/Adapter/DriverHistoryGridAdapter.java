package rode1lift.ashwin.uomtrust.mu.rod1lift.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import rode1lift.ashwin.uomtrust.mu.rod1lift.DTO.AccountDTO;
import rode1lift.ashwin.uomtrust.mu.rod1lift.R;
import rode1lift.ashwin.uomtrust.mu.rod1lift.Utils.Utils;

/**
 * Created by Ashwin on 16-Jul-17.
 */

public class DriverHistoryGridAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private Context context;
    private List<AccountDTO> accountDTOList;

    public DriverHistoryGridAdapter(Context context, List<AccountDTO> accountDTOList) {

        this.context = context;
        this.accountDTOList = accountDTOList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        if (accountDTOList != null && accountDTOList.size() > 0)
            return accountDTOList.size();
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.activity_driver_view_history_content_sub_content, null);

        final AccountDTO a = accountDTOList.get(i);

        ImageView imageView = (ImageView) view.findViewById(R.id.imgProfile);
        if (a.getProfilePicture() != null)
            imageView.setImageBitmap(Utils.convertBlobToBitmap(a.getProfilePicture()));

        TextView txtFullName = (TextView) view.findViewById(R.id.txtFullName);
        txtFullName.setText(a.getFullName());

        return view;
    }
}
