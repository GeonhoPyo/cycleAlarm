package kr.co.pgbdev.android.cyclealarm.BottomSheetFragment.Bluetooth;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.co.pgbdev.android.cyclealarm.Connection.Bluetooth.BluetoothInfo;
import kr.co.pgbdev.android.cyclealarm.Connection.Bluetooth.BluetoothLEConnection;
import kr.co.pgbdev.android.cyclealarm.Connection.Bluetooth.ConnectState;
import kr.co.pgbdev.android.cyclealarm.Activity.MainActivity;
import kr.co.pgbdev.android.cyclealarm.R;


public class ScanBluetoothAdapter extends RecyclerView.Adapter<ScanBluetoothViewHolder>{
    Context context;
    static ArrayList<BluetoothInfo> bluetoothInfoArrayList;

    public ScanBluetoothAdapter(Context context, ArrayList<BluetoothInfo> bluetoothInfoArrayList){
        this.context = context;
        this.bluetoothInfoArrayList = bluetoothInfoArrayList;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_bluetooth, parent, false);

        return new ScanBluetoothViewHolder(view);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull ScanBluetoothViewHolder holder, int position) {
        try{
            if(bluetoothInfoArrayList != null && !bluetoothInfoArrayList.isEmpty()){
                BluetoothInfo bluetoothInfo = bluetoothInfoArrayList.get(position);
                if(bluetoothInfo != null){
                    holder.tv_bluetooth_name.setText(bluetoothInfo.bluetoothName);
                    holder.tv_bluetooth_mac.setText(bluetoothInfo.bluetoothMacAddress);
                    switch (bluetoothInfo.connectState){
                        case "NONE" :
                        case "FAIL" :
                            holder.tv_bluetooth_name.setTextColor(context.getResources().getColor(R.color.black,null));
                            holder.tv_bluetooth_mac.setTextColor(context.getResources().getColor(R.color.black,null));
                            break;
                        case "PAIRED" :
                            holder.tv_bluetooth_name.setTextColor(context.getResources().getColor(R.color.color_00c1ff,null));
                            holder.tv_bluetooth_mac.setTextColor(context.getResources().getColor(R.color.color_00c1ff,null));
                            break;
                        case "CONNECTING" :
                            holder.tv_bluetooth_name.setTextColor(context.getResources().getColor(R.color.color_ABBADB,null));
                            holder.tv_bluetooth_mac.setTextColor(context.getResources().getColor(R.color.color_ABBADB,null));
                            break;
                    }

                    if(position == bluetoothInfoArrayList.size()-1){
                        holder.rl_bluetooth.setBackground(null);
                    }else {
                        holder.rl_bluetooth.setBackground(context.getResources().getDrawable(R.drawable.bg_bluetooth_under_line,null));
                    }
                    holder.rl_bluetooth.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try{
                                if(ConnectState.isConnectSuccess()){
                                    new BluetoothLEConnection().connectFinish();
                                }

                                if(bluetoothInfo != null){
                                    bluetoothInfo.connectState = "CONNECTING";
                                    connectBluetooth(bluetoothInfo);
                                    notifyDataSetChanged();
                                }

                                if(MainActivity.connectionBottomSheetBluetoothFragment != null){
                                    MainActivity.connectionBottomSheetBluetoothFragment.dismiss();
                                    MainActivity.connectionBottomSheetBluetoothFragment = null;
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    });
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void connectBluetooth(BluetoothInfo bluetoothInfo){
        new BluetoothLEConnection().connect(bluetoothInfo);
    }

    @Override
    public int getItemCount() {
        return bluetoothInfoArrayList.size();
    }
}
