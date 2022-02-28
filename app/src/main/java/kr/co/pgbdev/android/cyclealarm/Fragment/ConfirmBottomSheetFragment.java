package kr.co.pgbdev.android.cyclealarm.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import kr.co.pgbdev.android.cyclealarm.R;


public class ConfirmBottomSheetFragment extends BottomSheetDialogFragment {

    ImageView iv_connect_err;
    TextView tv_bottom_sheet_title;
    TextView tv_bottom_sheet_message;
    Button bt_bottom_sheet_confirm;

    String title;
    String message;
    boolean isConnectError;

    View.OnClickListener positiveClickListener;

    public ConfirmBottomSheetFragment(String title, String message, boolean isConnectError, View.OnClickListener positiveClickListener){
        this.title = title;
        this.message = message;
        this.isConnectError = isConnectError;
        this.positiveClickListener = positiveClickListener;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_bottom_sheets_confirm,container,false);
        initView(view);
        return view;
    }


    private void initView (View view){
        try{
            iv_connect_err = view.findViewById(R.id.iv_connect_err);
            tv_bottom_sheet_title = view.findViewById(R.id.tv_bottom_sheet_title);
            tv_bottom_sheet_message = view.findViewById(R.id.tv_bottom_sheet_message);
            bt_bottom_sheet_confirm = view.findViewById(R.id.bt_bottom_sheet_confirm);

            if(isConnectError){
                iv_connect_err.setVisibility(View.VISIBLE);
            }else{
                iv_connect_err.setVisibility(View.GONE);
            }
            tv_bottom_sheet_title.setText(title);
            tv_bottom_sheet_message.setText(message);
            bt_bottom_sheet_confirm.setOnClickListener(positiveClickListener);
        }catch (Exception e ){
            e.printStackTrace();
        }


    }

}
