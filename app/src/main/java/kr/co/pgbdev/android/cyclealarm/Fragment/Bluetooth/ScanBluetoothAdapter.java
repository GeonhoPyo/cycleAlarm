package kr.co.pgbdev.android.cyclealarm.Fragment.Bluetooth;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.co.pgbdev.android.cyclealarm.Bluetooth.BluetoothInfo;
import kr.co.pgbdev.android.cyclealarm.R;


public class ScanBluetoothAdapter extends RecyclerView.Adapter<ScanBluetoothViewHolder> {
    Context context;
    public static ArrayList<BluetoothInfo> bluetoothInfoArrayList;

    public ScanBluetoothAdapter(Context context, ArrayList<BluetoothInfo> bluetoothInfoArrayList){
        this.context = context;
        ScanBluetoothAdapter.bluetoothInfoArrayList = bluetoothInfoArrayList;
    }

    public void setConnectBluetoothArray(ArrayList<BluetoothInfo> bluetoothInfoArrayList, BluetoothInfo setBluetoothInfo){
        ArrayList<BluetoothInfo> noneBluetoothArrayList = new ArrayList<>();
        for(BluetoothInfo bluetoothInfo :  bluetoothInfoArrayList){
            if(!bluetoothInfo.bluetoothMacAddress.equals(setBluetoothInfo.bluetoothMacAddress)){
                bluetoothInfo.connectState = "NONE";
                noneBluetoothArrayList.add(bluetoothInfo);
            }
        }
        ArrayList<BluetoothInfo> result = new ArrayList<>();
        result.add(setBluetoothInfo);
        result.addAll(noneBluetoothArrayList);
        this.bluetoothInfoArrayList = result;
    }


    public void setScanBluetoothInfoArrayList (ArrayList<BluetoothInfo> bluetoothInfoArrayList){
        this.bluetoothInfoArrayList = bluetoothInfoArrayList;
    }

    public static ArrayList<BluetoothInfo> getScanBluetoothInfoArrayList(){
        return ScanBluetoothAdapter.bluetoothInfoArrayList;
    }

    @NonNull
    @Override
    public ScanBluetoothViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_beacon, parent, false);

        return new ScanBluetoothViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScanBluetoothViewHolder holder, int position) {
        try{
            if(bluetoothInfoArrayList != null && !bluetoothInfoArrayList.isEmpty()){
                BluetoothInfo bluetoothInfo = bluetoothInfoArrayList.get(position);
                holder.tv_beacon_name.setText(bluetoothInfo.bluetoothName);
                holder.tv_beacon_major.setText(bluetoothInfo.bluetoothMacAddress);

                holder.rl_beacon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //ContackShared.setMajor(context,beaconArrayList.get(position).getId2().toInt());
                        /*if(MainActivity.connectionBottomSheetFragment != null){
                            MainActivity.connectionBottomSheetFragment.dismiss();
                            MainActivity.connectionBottomSheetFragment = null;
                        }
                        if(MainActivity.viewHandler != null){
                            MainActivity.viewHandler.obtainMessage(6).sendToTarget();
                        }*/
                    }
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return bluetoothInfoArrayList.size();
    }
}
