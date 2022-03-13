package kr.co.pgbdev.android.cyclealarm.BottomSheetFragment.Bluetooth;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import kr.co.pgbdev.android.cyclealarm.R;


public class ScanBluetoothViewHolder extends RecyclerView.ViewHolder {


    RelativeLayout rl_bluetooth ;
    TextView tv_bluetooth_name;
    TextView tv_bluetooth_mac;




    public ScanBluetoothViewHolder(@NonNull View itemView) {
        super(itemView);
        rl_bluetooth = itemView.findViewById(R.id.rl_bluetooth);
        tv_bluetooth_name = itemView.findViewById(R.id.tv_bluetooth_name);
        tv_bluetooth_mac = itemView.findViewById(R.id.tv_bluetooth_mac);

    }
}
