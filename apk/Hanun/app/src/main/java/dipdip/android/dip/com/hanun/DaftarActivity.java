package dipdip.android.dip.com.hanun;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DaftarActivity extends Activity {
    private DatabaseReference mDatabase;
    EditText et1, et2, et3, et4;
    RadioGroup rad1;
    public boolean setted = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);
        et1 = (EditText)findViewById(R.id.editText2);
        et2 = (EditText)findViewById(R.id.editText4);
        et3 = (EditText)findViewById(R.id.editText5);
        et4 = (EditText)findViewById(R.id.editText6);
        rad1 = (RadioGroup)findViewById(R.id.radgroup);
    }

    public void onDaftar(View v)
    {
        final String nama = et1.getText().toString();
        final String usia = et2.getText().toString();
        final String username = et3.getText().toString();
        final String password = et4.getText().toString();
        mDatabase = FirebaseDatabase.getInstance().getReference("user_list").child(username);
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                if(!dataSnapshot.exists()) {
                    mDatabase.child("nama").setValue(nama);
                    mDatabase.child("usia").setValue(usia);
                    mDatabase.child("password").setValue(password);
                    mDatabase.child("jenis_kelamin").setValue("---");
                    mDatabase.child("notify").setValue("0");
                    mDatabase.child("notif_suhu").setValue("0");
                    mDatabase.child("notif_hearth_rate").setValue("0");
                    mDatabase.child("notif_diagnosis").setValue("0");
                    mDatabase.child("notif_dari").setValue("0");
                    setted = true;
                    finish();
                } else if(!setted){
                    Toast.makeText(DaftarActivity.this, "Username Sudah Terpakai, Pilih Username Lain", Toast.LENGTH_SHORT).show();
                    et3.setText("");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Toast.makeText(DaftarActivity.this, databaseError.toException().toString(), Toast.LENGTH_SHORT).show();
                // ...
            }
        };
        mDatabase.addValueEventListener(postListener);
    }

    public void onKembali(View v)
    {
        finish();
    }
}
