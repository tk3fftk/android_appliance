package jp.ac.kobe_u.tv;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private final static String URL = "http://192.168.3.102:8080/IRKitService/api/send/tv_pow";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        httpGet(this);
    }

    public void httpGet(final MainActivity mainActivity){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(URL);
                    HttpURLConnection con = (HttpURLConnection)url.openConnection();
                    con.setReadTimeout(10000);
                    con.setRequestMethod("GET");
                    con.connect();
                    int code = con.getResponseCode();
                    Log.d("code", Integer.toString(code));
                } catch(Exception ex) {
                    System.out.println(ex);
                } finally {
                    // アプリ終了処理
                    mainActivity.finish();
                    mainActivity.moveTaskToBack(true);
                }
            }
        }).start();
    }
}
