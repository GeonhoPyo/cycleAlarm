package kr.co.pgbdev.android.cyclealarm.Fragment.Bluetooth;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import kr.co.pgbdev.android.cyclealarm.R;


public class ScanBluetoothViewHolder extends RecyclerView.ViewHolder {


    RelativeLayout rl_beacon ;
    TextView tv_beacon_name;
    TextView tv_beacon_major;
    TextView tv_beacon_minor;




    public ScanBluetoothViewHolder(@NonNull View itemView) {
        super(itemView);
        rl_beacon = itemView.findViewById(R.id.rl_beacon);
        tv_beacon_name = itemView.findViewById(R.id.tv_beacon_name);
        tv_beacon_major = itemView.findViewById(R.id.tv_beacon_major);
        tv_beacon_minor = itemView.findViewById(R.id.tv_beacon_minor);

    }
}
