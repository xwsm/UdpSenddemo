package com.open.door;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    EditText ipEdt;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ipEdt = findViewById(R.id.ipEdt);
        button = findViewById(R.id.button);
        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ipEdt.setVisibility(View.VISIBLE);
                return false;
            }
        });

    }

    public void openDoor(View view) {
        sendUdpMessage();
    }

    private void sendUdpMessage() {
        //48 3a 01 70 01 01 00 02 45 44
        String message2 = "H:\u0001p\u0001\u0001\u0000\u0002ED";
        String message=hexStringToString("48 3a 01 70 01 01 00 02 45 44");
        new Thread(() -> {
            try {
                DatagramSocket dgSocket = new DatagramSocket();
                System.out.println("发送消息：" + message);
                byte[] data = message.getBytes();
                DatagramPacket dgPacket = new DatagramPacket(data, data.length, InetAddress.getByName(ipEdt.getText().toString()), 1030);
                dgSocket.send(dgPacket);
                dgSocket.close();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "开门成功", Toast.LENGTH_SHORT).show();
                    }
                });
                System.out.println("send message is ok.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
    /**
     * 16进制转换成为string类型字符串
     * @param s
     * @return
     */
    public String hexStringToString(String s) {
        if (s == null || s.equals("")) {
            return null;
        }
        s = s.replace(" ", "");
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "UTF-8");
            new String();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }
}
