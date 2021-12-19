package ddwucom.mobile.finalproject.ma02_20190980;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    private EditText etName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = findViewById(R.id.etName);
    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnStart:
                if(etName.getText().toString().equals("")) {
                    Toast.makeText(this, "이름을 입력해주세요!", Toast.LENGTH_SHORT).show();
                    break;
                }
                Intent intent = new Intent(this, PooDaysActivity.class);
                intent.putExtra("name", etName.getText().toString());
                startActivity(intent);
                break;
        }
    }


}