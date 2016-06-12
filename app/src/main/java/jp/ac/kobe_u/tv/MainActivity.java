package jp.ac.kobe_u.tv;

import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private final static String URL = "http://192.168.3.102:8080/IRKitService/api/send/tv_pow";
    TextToSpeech tts;
    String toSpeech = "テレビを点けます";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set Text to speech
        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    tts.setLanguage(Locale.JAPAN);
                    tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                        @Override
                        public void onStart(String s) {
                            Log.i("utterance", s);
                        }

                        @Override
                        public void onDone(String s) {
                            Log.i("utterance", s);
                            finishApp();
                        }

                        @Override
                        public void onError(String s) {
                            Log.i("utterance", s);
                        }
                    });
                } else {
                    Log.d("utterance", "error");
                }
            }
        });

        httpGet();

    }

    public void httpGet() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(URL);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setReadTimeout(10000);
                    con.setRequestMethod("GET");
                    con.connect();
                    int code = con.getResponseCode();
                    Log.d("code", Integer.toString(code));
                    speech(toSpeech);
                } catch (Exception ex) {
                    Log.d("geterror", ex.toString());
                    finishApp();
                } finally {
                }
            }
        }).start();
    }

    /**
     * text to speech
     *
     * @param toSpeech
     */
    public void speech(String toSpeech) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(toSpeech, TextToSpeech.QUEUE_FLUSH, null, "tv");
        } else {
            tts.speak(toSpeech, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    /**
     * アプリを終了させる
     */
    public void finishApp() {
        MainActivity.this.finish();
        MainActivity.this.moveTaskToBack(true);
    }

    @Override
    protected void onDestroy() {
        tts.shutdown();
        super.onDestroy();
    }
}
