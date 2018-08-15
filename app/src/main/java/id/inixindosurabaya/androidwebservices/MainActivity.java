package id.inixindosurabaya.androidwebservices;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // komponen dalam layout
    private EditText editNama, editJabatan, editGaji;
    private Button btnSimpanData, btnLihatData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // inisialisasi komponen
        editNama = findViewById(R.id.editNama);
        editJabatan = findViewById(R.id.editJabatan);
        editGaji = findViewById(R.id.editGaji);
        btnSimpanData = findViewById(R.id.btnSimpan);
        btnLihatData = findViewById(R.id.btnLihatData);

        // event handling untuk Button
        btnSimpanData.setOnClickListener(this);
        btnLihatData.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // jika button simpan ditekan
        if (v == btnSimpanData) {
            // method untuk perintah menyimpan data
            simpanDataPegawai();
        }

        // jika button lihat ditekan
        if (v == btnLihatData) {
            startActivity(new Intent(this,
                    TampilSemuaData.class));
        }
    }

    private void simpanDataPegawai() {
        // variabel data yg akan disimpan
        final String nama = editNama.getText().toString().trim();
        final String jabatan = editJabatan.getText().toString().trim();
        final String gaji = editGaji.getText().toString().trim();

        class SimpanDataPegawai extends AsyncTask<Void, Void, String>{
            ProgressDialog loading;

            // sebelum perintah dijalankan
            protected void onPreExecute(){
                super.onPreExecute();
                loading =
                        ProgressDialog.show(
                                MainActivity.this,
                                "Menyimpan Data",
                                "Harap Tunggu...",
                                false, false
                        );
            }

            // saat perintah dijalankan
            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> params = new HashMap<>();
                params.put(Konfigurasi.KEY_PGW_NAMA, nama);
                params.put(Konfigurasi.KEY_PGW_JABATAN, jabatan);
                params.put(Konfigurasi.KEY_PGW_GAJI, gaji);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Konfigurasi.URL_ADD,
                        params);
                return res;
            }

            // setelah perintah dijalanlan
            protected void onPostExecute(String s){
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(MainActivity.this,
                        "message: " + s,
                        Toast.LENGTH_LONG).show();
            }
        }
        SimpanDataPegawai simpan = new SimpanDataPegawai();
        simpan.execute();
    }
}
