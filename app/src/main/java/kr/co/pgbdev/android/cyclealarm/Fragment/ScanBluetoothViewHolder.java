package kr.co.pgbdev.android.cyclealarm.Fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import kr.co.pgbdev.android.cyclealarm.R;


public class ScanBluetoothViewHolder extends RecyclerView.ViewHolder {

    RelativeLayout rl_bluetooth_background;
    ImageView iv_bluetooth_icon;
    TextView tv_bluetooth_name;
    LinearLayout ll_bluetooth_state_layout;
    TextView tv_bluetooth_state;
    ImageView iv_bluetooth_state_icon;



    public ScanBluetoothViewHolder(@NonNull View itemView) {
        super(itemView);

        rl_bluetooth_background = itemView.findViewById(R.id.rl_bluetooth_background);
        iv_bluetooth_icon = itemView.findViewById(R.id.iv_bluetooth_icon);
        tv_bluetooth_name = itemView.findViewById(R.id.tv_bluetooth_name);
        ll_bluetooth_state_layout = itemView.findViewById(R.id.ll_bluetooth_state_layout);
        tv_bluetooth_state = itemView.findViewById(R.id.tv_bluetooth_state);
        iv_bluetooth_state_icon = itemView.findViewById(R.id.iv_bluetooth_state_icon);
    }
}
