package kr.co.pgbdev.android.cyclealarm.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.co.pgbdev.android.cyclealarm.Bluetooth.BluetoothInfo;
import kr.co.pgbdev.android.cyclealarm.R;
import kr.co.pgbdev.android.cyclealarm.Tool.TimeTool;


public class ScanBluetoothAdapter extends RecyclerView.Adapter<ScanBluetoothViewHolder> {
    Context context;
    static ArrayList<BluetoothInfo> bluetoothInfoArrayList;
    String autoConnectMacAddress;

    public ScanBluetoothAdapter(Context context, ArrayList<BluetoothInfo> bluetoothInfoArrayList, String macAddress) {
        this.context = context;
        this.bluetoothInfoArrayList = bluetoothInfoArrayList;
        this.autoConnectMacAddress = macAddress;
    }

    public void setConnectBluetoothArray(ArrayList<BluetoothInfo> bluetoothInfoArrayList, BluetoothInfo setBluetoothInfo) {
        ArrayList<BluetoothInfo> noneBluetoothArrayList = new ArrayList<>();
        for (BluetoothInfo bluetoothInfo : bluetoothInfoArrayList) {
            if (!bluetoothInfo.bluetoothMacAddress.equals(setBluetoothInfo.bluetoothMacAddress)) {
                bluetoothInfo.connectState = "NONE";
                noneBluetoothArrayList.add(bluetoothInfo);
            }
        }
        ArrayList<BluetoothInfo> result = new ArrayList<>();
        result.add(setBluetoothInfo);
        result.addAll(noneBluetoothArrayList);
        this.bluetoothInfoArrayList = result;
    }


    public void setScanBluetoothInfoArrayList(ArrayList<BluetoothInfo> bluetoothInfoArrayList) {
        this.bluetoothInfoArrayList = bluetoothInfoArrayList;
    }

    public static ArrayList<BluetoothInfo> getScanBluetoothInfoArrayList() {
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
        try {
            if (bluetoothInfoArrayList != null && !bluetoothInfoArrayList.isEmpty()) {
                BluetoothInfo bluetoothInfo = bluetoothInfoArrayList.get(position);
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

                    if (position == bluetoothInfoArrayList.size() - 1) {
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
    }

    private void connectBluetooth(BluetoothInfo bluetoothInfo) {
        //new BluetoothLEController().connect(bluetoothInfo);
    }

    @Override
    public int getItemCount() {
        return bluetoothInfoArrayList.size();
    }
}
