package com.example.sayac;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private int sayac = 0, kalan = 0;
    private boolean sallandi = false, ilkDegilse = false;
    private SensorManager sensorManager;
    private Sensor sensor;
    private Vibrator vibrator;
    private float x, y, z, sonX, sonY, sonZ;
    private float xFarki, yFarki, zFarki;
    private float sallama = 5f;

    private Button artiButton, eksiButton, ayarlarButton;
    private TextView sayacTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        artiButton = findViewById(R.id.artiButton);
        eksiButton = findViewById(R.id.eksiButton);
        ayarlarButton = findViewById(R.id.ayarlarButton);
        sayacTextView = findViewById(R.id.sayacTextView);

        //ses oynatıcısını oluşturduk
        final MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.uyari);
        //SharedPreferences sınıfımızı oluşturduk
        SharedPref sharedPref = SharedPref.getInstance(getApplicationContext());

        artiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // eğer sayaç değişkeni +10 sayısına eşit değilse sayacı bir arttır
                // ve sayaç değişkenini yazdır
                if (sayac != 10) {
                    sayac++;
                    sayacTextView.setText("Sayaç: " + sayac);
                }
                else {
                    //eğer ses/titreşim ayarı açıksa uyarı sesini başlat ve titreşim yap
                    if (sharedPref.objeyiÇek()) {
                        mediaPlayer.start();
                        vibrator.vibrate(500);
                    }
                }
            }
        });

        eksiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // eğer sayaç değişkeni -10 sayısına eşit değilse sayacı bir eksilt
                // ve sayaç değişkenini yazdır
                if (sayac != -10) {
                    sayac--;
                    sayacTextView.setText("Sayaç: " + sayac);
                }
                else {
                    //eğer ses/titreşim ayarı açıksa uyarı sesini başlat ve titreşim yap
                    if (sharedPref.objeyiÇek()) {
                        mediaPlayer.start();
                        vibrator.vibrate(500);
                    }
                }
            }
        });

        ayarlarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
                finish();
            }
        });

        //titreşim servisine erişiyoruz ve vibrator değişkenine bu servisi tanımlıyoruz
        //bununla beraber sonZ değeri sensörden gelen önceki değeri saklamamızı sağlayacak
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        //sensör hareketlerini dinlemeye başlamadan önce sistem servislerinden sensör servisi çağırarak
        //yukardaki classta SensorEventListener sınıfını implemente ettik
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sallandi = true;
        }
        else {
            sallandi = false;
        }
    }

    //bir tuşa basıldığında fonksiyonu
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //ses oynatıcı oluşturduk
        final MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.uyari);
        //SharedPreferences sınıfını oluşturduk
        SharedPref sharedPref = SharedPref.getInstance(getApplicationContext());
        //ses arttırma tuşuna basıldığında
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            //kalan değişkeni sayacı 5 arttırıldığında limiti aşıp aşmadığını kontrol etmek için koydum
            kalan = sayac + 5;
            //eğer sayaç +10 sayısına eşit değilse ve kalan değişkeni +10dan küçük eşitse sayacı 5 arttır
            if (sayac != 10 && kalan <= 10) {
                sayac += 5;
                sayacTextView.setText("Sayaç: " + sayac);
            }
            else {
                //eğer ses/titreşim ayarı açıksa uyarı sesini başlat ve titreşim yap
                if (sharedPref.objeyiÇek()) {
                    mediaPlayer.start();
                    vibrator.vibrate(500);
                }
            }
        }
        //ses azaltma tuşuna basıldığında
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            //kalan değişkeni sayacı 5 azalttığında limiti aşıp aşmadığını kontrol etmek için koydum
            kalan = sayac - 5;
            //eğer sayaç -10 sayısına eşit değilse ve kalan değişkeni -10dan büyük eşitse sayacı 5 azalt
            if (sayac != -10 && kalan >= -10) {
                sayac -= 5;
                sayacTextView.setText("Sayaç: " + sayac);
            }
            else {
                //eğer ses/titreşim ayarı açıksa uyarı sesini başlat ve titreşim yap
                if (sharedPref.objeyiÇek()) {
                    mediaPlayer.start();
                    vibrator.vibrate(500);
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    //onSensorChanged fonksiyonu sensörden gelen bilgileri anlık olarak dinlememizi sağlıyor
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        //Sensörden gelen veri onSensorChanged fonksiyonu içerisinde event değişkeni üzerinden gönderilir
        //Accelerometer adlı sensör bize telefonun x, y ve z ekseni üzerinde yaptığı hareketleri aşağıdaki gibi verir
        x = sensorEvent.values[0];
        y = sensorEvent.values[1];
        z = sensorEvent.values[2];
        //Sensörden gelen değerler values dizisi içine gönderilir. Biz burada her eksende yapılan hareketi
        //x, y, ve z değişkenlerimize attık

        if (ilkDegilse) {
            //x, y ve z farklarını hesapladık
            xFarki = Math.abs(sonX - x);
            yFarki = Math.abs(sonY - y);
            zFarki = Math.abs(sonZ - z);

            //x, y ve z farkları sallama eşiğinden büyükse titreşim yap
            //sallama eşiği 5
            if ((xFarki > sallama && yFarki > sallama) ||
                    (xFarki > sallama && zFarki > sallama) ||
                    (yFarki > sallama && zFarki > sallama)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                }
                else {
                    vibrator.vibrate(500);
                }

                Toast.makeText(this, "Telefon sallandı ve sayaç sıfırlandı", Toast.LENGTH_SHORT).show();
                sayac = 0;
                sayacTextView.setText("Sayaç: " + sayac);
            }
        }

        sonX = x; sonY = y; sonZ = y;
        ilkDegilse = true;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sallandi)
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sallandi)
            sensorManager.unregisterListener(this);
    }
}