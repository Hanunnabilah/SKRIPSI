package dipdip.android.dip.com.hanun;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends Activity {
    private DatabaseReference mDatabase;
    EditText etUsername, etPassword;
    public static String uname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etUsername = (EditText)findViewById(R.id.editText);
        etPassword = (EditText)findViewById(R.id.editText3);
    }
    public void onLogin(View v)
    {
        final String username = etUsername.getText().toString();
        final String password = etPassword.getText().toString();
        mDatabase = FirebaseDatabase.getInstance().getReference("user_list").child(username);
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                if (!dataSnapshot.exists()) {
                    Toast.makeText(LoginActivity.this, "Username Atau Password Tidak Cocok", Toast.LENGTH_SHORT).show();
                } else {
                    String realPassword = dataSnapshot.child("password").getValue(String.class);
                    if (realPassword.equals(password)) {
                        uname = username;
                        AkunActivity.nama = dataSnapshot.child("nama").getValue(String.class);
                        AkunActivity.usia = dataSnapshot.child("usia").getValue(String.class);
                        AkunActivity.jenisKelamin = dataSnapshot.child("jenis_kelamin").getValue(String.class);
                        Intent i = new Intent(getBaseContext(), HomeActivity.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(LoginActivity.this, "Username Atau Password Tidak Cocok", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Toast.makeText(LoginActivity.this, databaseError.toException().toString(), Toast.LENGTH_SHORT).show();
                // ...
            }
        };
        mDatabase.addValueEventListener(postListener);
    }
    public void onDaftar(View v)
    {
        Intent i = new Intent(getBaseContext(), DaftarActivity.class);
        startActivity(i);
    }
}
