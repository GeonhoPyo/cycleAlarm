package kr.co.pgbdev.android.cyclealarm.Fragment.Beacon;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.altbeacon.beacon.Beacon;

import java.util.ArrayList;

import kr.co.pgbdev.android.cyclealarm.Connection.Bluetooth.BluetoothInfo;
import kr.co.pgbdev.android.cyclealarm.MainActivity;
import kr.co.pgbdev.android.cyclealarm.Tool.ContackShared;
import kr.co.pgbdev.android.cyclealarm.R;


public class ScanBeaconAdapter extends RecyclerView.Adapter<ScanBeaconViewHolder> {
    Context context;
    static ArrayList<Beacon> beaconArrayList;

    public ScanBeaconAdapter(Context context, ArrayList<Beacon> beaconArrayList) {
        this.context = context;
        ScanBeaconAdapter.beaconArrayList = beaconArrayList;
    }

    public void setBeaconArrayList(ArrayList<Beacon> beaconArrayList){
        ScanBeaconAdapter.beaconArrayList = beaconArrayList;
    }





    @NonNull
    @Override
    public ScanBeaconViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_beacon, parent, false);

        return new ScanBeaconViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScanBeaconViewHolder holder, @SuppressLint("RecyclerView") int position) {
        try{
            if(beaconArrayList != null && !beaconArrayList.isEmpty()){
                Beacon beacon = beaconArrayList.get(position);
                holder.tv_beacon_name.setText(beacon.getId1().toString());
                int major = beacon.getId2().toInt(); //beacon major
                int minor = beacon.getId3().toInt();// beacon minor
                holder.tv_beacon_major.setText(String.valueOf(major));
                holder.tv_beacon_minor.setText(String.valueOf(minor));
                holder.rl_beacon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ContackShared.setMajor(context,beaconArrayList.get(position).getId2().toInt());
                        if(MainActivity.connectionBottomSheetFragment != null){
                            MainActivity.connectionBottomSheetFragment.dismiss();
                            MainActivity.connectionBottomSheetFragment = null;
                        }
                    }
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /*@SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull ScanBluetoothViewHolder holder, int position) {
        try {


            if (beaconArrayList != null && !beaconArrayList.isEmpty()) {
                BluetoothInfo bluetoothInfo = beaconArrayList.get(position);
                if (bluetoothInfo != null) {
                    holder.tv_bluetooth_name.setText(bluetoothInfo.bluetoothName);
                    holder.ll_bluetooth_state_layout.setVisibility(View.VISIBLE);
                    holder.iv_bluetooth_state_icon.setVisibility(View.VISIBLE);
                    holder.tv_bluetooth_state.setTextColor(context.getResources().getColor(R.color.color_9C9C9C, null));
                    holder.iv_bluetooth_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_link_24, null));
                    switch (bluetoothInfo.connectState) {
                        case "NONE":
                            holder.iv_bluetooth_state_icon.setVisibility(View.INVISIBLE);
                            holder.tv_bluetooth_name.setTextColor(context.getResources().getColor(R.color.black, null));
                            holder.tv_bluetooth_state.setText(null);
                            break;
                        case "PAIRED":
                            holder.tv_bluetooth_state.setText("연결됨");
                            holder.iv_bluetooth_state_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_done_24, null));
                            holder.tv_bluetooth_name.setTextColor(context.getResources().getColor(R.color.color_5389FF, null));
                            holder.iv_bluetooth_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_link_24, null));
                            break;
                        case "FAIL":
                            holder.tv_bluetooth_state.setText("연결 실패");
                            holder.iv_bluetooth_state_icon.setImageDrawable(null);
                            holder.tv_bluetooth_name.setTextColor(context.getResources().getColor(R.color.black, null));
                            break;
                        case "CONNECTING":
                            holder.tv_bluetooth_state.setText("연결중");
                            holder.tv_bluetooth_name.setTextColor(context.getResources().getColor(R.color.color_ABBADB, null));
                            holder.iv_bluetooth_state_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_link_24, null));
                            break;
                    }

                    if (position == beaconArrayList.size() - 1) {
                        holder.rl_bluetooth_background.setBackground(null);
                    } else {
                        holder.rl_bluetooth_background.setBackground(context.getResources().getDrawable(R.drawable.bg_bluetooth_under_line, null));
                    }
                    holder.rl_bluetooth_background.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                //new BluetoothLEController().connectFinish();

                                if (bluetoothInfo != null) {
                                    bluetoothInfo.connectState = "CONNECTING";
                                    connectBluetooth(bluetoothInfo);
                                    notifyDataSetChanged();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    private void connectBluetooth(BluetoothInfo bluetoothInfo) {
        //new BluetoothLEController().connect(bluetoothInfo);
    }

    @Override
    public int getItemCount() {
        return beaconArrayList.size();
    }
}
