package dipdip.android.dip.com.hanun;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class AkunActivity extends Activity {
    public static String nama = "";
    public static String usia = "";
    public static String jenisKelamin = "";
    EditText etNama, etUsia, etJenis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_akun);
        etNama = (EditText)findViewById(R.id.editText7);
        etUsia = (EditText)findViewById(R.id.editText8);
        etJenis = (EditText)findViewById(R.id.editText9);

        etNama.setText(nama);
        etUsia.setText(usia);
        etJenis.setText(jenisKelamin);
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
}
