package dipdip.android.dip.com.hanun;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class HomeActivity extends Activity {
    String CHANNEL_ID="10001";
    private DatabaseReference mDatabase, mDatabase2;
    private DatabaseReference mDatabaseHist;
    private TextView tv,tv2,tv3,tv4;
    private Button bt1;
    public boolean isMulai = false;
    public static int timer = 0;
    MediaPlayer notif;
    long millis;
    public static String lastMenit = "0";
    public static boolean notif2 = false;
    Button btnHsil;
    Spinner sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        notif = MediaPlayer.create(this, R.raw.notifikasi);

        tv = (TextView)findViewById(R.id.textView8);
        tv2 = (TextView)findViewById(R.id.textView10);
        tv3 = (TextView)findViewById(R.id.textView12);
        tv4 = (TextView)findViewById(R.id.textView14);
        bt1 = (Button)findViewById(R.id.button6);
        sp = (Spinner)findViewById(R.id.spinner2);
        btnHsil  =(Button)findViewById(R.id.button8);
        btnHsil.setVisibility(View.INVISIBLE);
        millis = System.currentTimeMillis();

        mDatabase = FirebaseDatabase.getInstance().getReference("monitoring");
        mDatabaseHist = FirebaseDatabase.getInstance().getReference("riwayat");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot ds) {
                // Get Post object and use the values to update the UI

                    double score = ds.child("suhu").getValue(Double.class);
                    double score2 = ds.child("putaran").getValue(Double.class);
                    double score3 = ds.child("rpm").getValue(Double.class);
                    double score4 = ds.child("hearth_rate").getValue(Double.class);
                    if(isMulai && score2>0 && notif2 == false) {
                        if(score>38 || score4>(220-Integer.valueOf(AkunActivity.usia))){
                            notif.start();
                            notifikasi("STOP! DATA ANDA MELEBIHI BATAS NORMAL", "PERINGATAN !!!");
                            notif2 = true;
                        } else {
                            notif2 = false;
                        }
                        tv.setText(String.valueOf(score)+"C");
                        tv2.setText(String.valueOf(score2));
                        tv3.setText(String.valueOf(score3)+"RPM");
                        tv4.setText(String.valueOf(score4)+"Bpm");
                        if(System.currentTimeMillis()-millis>1000){
                            //RiwayatActivity.listSuhu.add(score);
                            //RiwayatActivity.listPutaran.add(score2);
                            //RiwayatActivity.listKecepatan.add(score3);
                            millis = System.currentTimeMillis();

                            String tanggal = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                            String waktu = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                            String menit = new SimpleDateFormat("mm", Locale.getDefault()).format(new Date());
                            if(!lastMenit.equals(menit)) {
                                mDatabaseHist.child("putaran").child(LoginActivity.uname +"_"+ tanggal + "_" + waktu).setValue(score2);
                                mDatabaseHist.child("rpm").child(LoginActivity.uname +"_"+ tanggal + "_" + waktu).setValue(score3);
                                mDatabaseHist.child("suhu").child(LoginActivity.uname +"_"+ tanggal + "_" + waktu).setValue(score);
                                mDatabaseHist.child("hearth_rate").child(LoginActivity.uname +"_"+ tanggal + "_" + waktu).setValue(score4);
                                HasilActivity.detak_jantung = String.valueOf(score4);
                                HasilActivity.suhu = String.valueOf(score);
                                HasilActivity.putaran = String.valueOf(score2);
                                HasilActivity.kecepatan = String.valueOf(score3);
                                lastMenit=menit;
                            }

                        }
                    } else {
                        tv.setText("0 C");
                        tv2.setText("0");
                        tv3.setText("0 RPM");
                        tv4.setText("0 Bpm");
                    }

                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Toast.makeText(HomeActivity.this, databaseError.toException().toString(), Toast.LENGTH_SHORT).show();
                // ...
            }
        };
        mDatabase.addValueEventListener(postListener);




        variable = (LoginActivity.uname).child(variabel.toString())
        mDatabase2 = FirebaseDatabase.getInstance().getReference("user_list").child(LoginActivity.uname);
        ValueEventListener postListener2 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot ds) {
                // Get Post object and use the values to update the UI
                String notify = ds.child("notify").getValue(String.class);
                String pengirimPesan = ds.child("notif_dari").getValue(String.class);
                if(notify.equals("1")){
                    mDatabase2.child("notify").setValue("0");
                    notifikasi("Halo, ini hasil olahragaku", "Hasil Olahraga "+pengirimPesan);
                }

                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Toast.makeText(HomeActivity.this, databaseError.toException().toString(), Toast.LENGTH_SHORT).show();
                // ...
            }
        };
        mDatabase2.addValueEventListener(postListener2);


        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(isMulai){
                    if(timer>0)timer--;
                    if(timer<=0){
                        isMulai=!isMulai;
                        bt1.setText("Mulai");
                        notif.start();

                        HomeActivity.this.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                btnHsil.setVisibility(View.VISIBLE);
                            }

                        });
                        //Toast.makeText(HomeActivity.this, "Waktu Habis", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }, 0, 1000);
    }
    public void onMulai(View v)
    {
        if(isMulai){
           isMulai=!isMulai;
           bt1.setText("Mulai");
        } else {
            timer = Integer.valueOf(sp.getSelectedItem().toString())*60;
            HasilActivity.waktu = sp.getSelectedItem().toString();
            RiwayatActivity.listKecepatan.clear();
            RiwayatActivity.listPutaran.clear();
            RiwayatActivity.listSuhu.clear();
            mDatabase.child("hearth_rate").setValue(0);
            mDatabase.child("putaran").setValue(0);
            mDatabase.child("rpm").setValue(0);
            mDatabase.child("suhu").setValue(0);
            if(timer>0) {
                isMulai = !isMulai;
                bt1.setText("Stop");
            } else {
                Toast.makeText(this, "Waktu Belum Di isi", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void notifikasi(String pesan, String pengirim)
    {
        String notification_title = pengirim;
        String notification_message = pesan;

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(notification_title)
                        .setContentText(notification_message);
        Intent intent = null;
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);
        int mNotificationId = (int) System.currentTimeMillis();
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);

            mBuilder.setChannelId(CHANNEL_ID);
            mNotifyMgr.createNotificationChannel(notificationChannel);
        }
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }
    public void onHome(View v)
    {
        Intent i = new Intent(getBaseContext(), HomeActivity.class);
        startActivity(i);
        finish();
    }
    public void onRiwayat(View v)
    {
        Intent i = new Intent(getBaseContext(), RiwayatActivity.class);
        startActivity(i);
        finish();
    }
    public void onAkun(View v)
    {
        Intent i = new Intent(getBaseContext(), AkunActivity.class);
        startActivity(i);
        finish();
    }
    public void onHasil(View v)
    {

        Intent i = new Intent(getBaseContext(), HasilActivity.class);
        startActivity(i);
        finish();
    }

}
