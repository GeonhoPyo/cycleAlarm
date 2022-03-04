package kr.co.pgbdev.android.cyclealarm.Bluetooth;

public class ProtocolTool {

    public String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for(byte b: a){
            sb.append(String.format("%02x", b).toUpperCase());
        }
        return sb.toString();
    }

    public String logByteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for(byte b: a){
            sb.append(String.format("%02x", b).toUpperCase());
            sb.append(" ");
        }
        return sb.toString();
    }

    public byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len/2];

        for(int i = 0; i < len; i+=2){
            data[i/2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i+1), 16));
        }

        return data;
    }

    public String byteToHex(byte a){
        return String.format("%02x", a).toUpperCase();
    }
}
