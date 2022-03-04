package kr.co.pgbdev.android.cyclealarm.Bluetooth;

import kr.co.pgbdev.android.cyclealarm.Tool.Dlog;

public class ResponseProtocol {
    private static StringBuilder response_text = new StringBuilder();
    public void bleTestDataRead(byte[] responseData){
        try{
            String stringHex = new ProtocolTool().byteArrayToHex(responseData);
            if(response_text == null){
                response_text = new StringBuilder();
            }
            response_text.append(stringHex);
            String strResponse = response_text.toString();
            if(strResponse.contains("0D0A")){ //0D0A까지 잘라야함.
                Dlog.e("-----pre Result : "+  strResponse);
                String[] splitResponse = strResponse.split("0D0A");
                strResponse = splitResponse[0];

                response_text = new StringBuilder();
                if(splitResponse.length>=2){
                    response_text.append(splitResponse[1]);
                }

            }


        }catch (Exception e){
            e.printStackTrace();
            response_text = new StringBuilder();
        }
    }

}
