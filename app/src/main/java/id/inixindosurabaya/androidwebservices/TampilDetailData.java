package id.inixindosurabaya.androidwebservices;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class TampilDetailData extends AppCompatActivity implements View.OnClickListener {
    // komponen dalam layout
    private EditText editId, editNama, editJabatan, editGaji;
    private Button btnUbah, btnHapus;
    private String id;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampil_detail_pegawai);

        // inisialisasi
        editId = findViewById(R.id.editIdPegawai);
        editNama = findViewById(R.id.editNamaPegawai);
        editJabatan = findViewById(R.id.editJabatanPegawai);
        editGaji = findViewById(R.id.editGajiPegawai);
        btnUbah = findViewById(R.id.btnUbahData);
        btnHapus = findViewById(R.id.btnHapusData);

        // ambil dari Intent
        Intent myIntent = getIntent();
        id = myIntent.getStringExtra(Konfigurasi.PGW_ID);
        editId.setText(id);

        // event handling
        btnUbah.setOnClickListener(this);
        btnHapus.setOnClickListener(this);

        getJSON();
    }

    private void getJSON() {
        class GetJSON extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(TampilDetailData.this,
                        "Mengambil Data",
                        "Harap Tunggu...",
                        false, false);
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(Konfigurasi.URL_GET_PGW, id);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                tampilkanDataPegawai(s);
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    private void tampilkanDataPegawai(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject
                    .getJSONArray(Konfigurasi.TAG_JSON_ARRAY);
            JSONObject x = result.getJSONObject(0);
            String name = x.getString(Konfigurasi.TAG_NAMA);
            String desg = x.getString(Konfigurasi.TAG_JABATAN);
            String sal = x.getString(Konfigurasi.TAG_GAJI);

            editNama.setText(name);
            editJabatan.setText(desg);
            editGaji.setText(sal);
        } catch (JSONException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btnUbah) {
            ubahDataPegawai();
        }
        if (v == btnHapus) {
            hapusDataPegawai();
        }
    }

    private void hapusDataPegawai() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Menghapus Data");
        builder.setMessage("Apakah yakin menghapus data?");
        builder.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        konfirmasiHapusDataPegawai();
                        startActivity(new Intent(TampilDetailData.this,
                                TampilSemuaData.class));
                    }
                });
        builder.setNegativeButton("CANCEL", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void konfirmasiHapusDataPegawai() {
        class KonfirmasiHapusDataPegawai extends AsyncTask<Void, Void, String>{
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(TampilDetailData.this,
                        "Menghapus Data",
                        "Harap Tunggu..",
                        false, false);
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(Konfigurasi.URL_DELETE_PGW, id);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(TampilDetailData.this,
                        "message: " + s,
                        Toast.LENGTH_LONG).show();
            }
        }
        KonfirmasiHapusDataPegawai hapus = new KonfirmasiHapusDataPegawai();
        hapus.execute();
    }

    private void ubahDataPegawai() {
        // variabel mana yg mau diubah
        final String nama = editNama.getText().toString().trim();
        final String jabatan = editJabatan.getText().toString().trim();
        final String gaji = editGaji.getText().toString().trim();

        class UbahDataPegawai extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(TampilDetailData.this,
                        "Mengubah Data",
                        "Harap Tunggu...",
                        false, false);
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(Konfigurasi.KEY_PGW_ID, id);
                hashMap.put(Konfigurasi.KEY_PGW_NAMA, nama);
                hashMap.put(Konfigurasi.KEY_PGW_JABATAN, jabatan);
                hashMap.put(Konfigurasi.KEY_PGW_GAJI, gaji);

                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(
                        Konfigurasi.URL_UPDATE_PGW, hashMap);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(TampilDetailData.this,
                        "message: " + s,
                        Toast.LENGTH_LONG).show();
            }
        }
        UbahDataPegawai ubah = new UbahDataPegawai();
        ubah.execute();
    }
}
