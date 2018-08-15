package id.inixindosurabaya.androidwebservices;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class TampilSemuaData extends AppCompatActivity implements AdapterView.OnItemClickListener {
    // komponen dalam layout
    private ListView listView;
    private String JSON_STRING;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampil_semua_data);

        // inisialisasi komponen
        listView = findViewById(R.id.listView);
        // event handling list view
        listView.setOnItemClickListener(this);
        // method untuk ambil data JSON
        getJSON();
    }

    private void getJSON() {
        class GetJSON extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            // sebelum proses
            protected void onPreExecute(){
                super.onPreExecute();
                loading = ProgressDialog.show(
                        TampilSemuaData.this,
                        "mengambil Data",
                        "Harap Tunggu...",
                        false, false
                );
            }

            // saat proses
            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(Konfigurasi.URL_GET_ALL);
                return s;
            }

            // setelah proses
            protected void onPostExecute(String s){
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                // method untuk menampilkan semua data pegawai
                tampilkanSemuaDataPegawai();
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    private void tampilkanSemuaDataPegawai() {
        // buat json object
        JSONObject jsonObject = null;
        // buat array list
        ArrayList<HashMap<String, String>> list =
                new ArrayList<HashMap<String, String>>();

        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject
                    .getJSONArray(Konfigurasi.TAG_JSON_ARRAY);
            // tampilkan data json
            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                String id = jo.getString(Konfigurasi.TAG_ID);
                String name = jo.getString(Konfigurasi.TAG_NAMA);

                HashMap<String, String> pegawai = new HashMap<>();
                pegawai.put(Konfigurasi.TAG_ID, id);
                pegawai.put(Konfigurasi.TAG_NAMA, name);
                list.add(pegawai);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // buat adapter untuk mendistribusikan data json array
        // kedalam list view
        ListAdapter adapter = new SimpleAdapter(
                getApplicationContext(), list,
                R.layout.list_item,
                new String[]{Konfigurasi.TAG_ID, Konfigurasi.TAG_NAMA},
                new int[]{R.id.id, R.id.name}
        );
        listView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent,
                            View view, int position, long id) {
        Intent myIntent = new Intent(TampilSemuaData.this,
                TampilDetailData.class);
        HashMap<String, String> map =
                (HashMap) parent.getItemAtPosition(position);
        String pgwId = map.get(Konfigurasi.TAG_ID).toString();
        myIntent.putExtra(Konfigurasi.PGW_ID, pgwId);
        startActivity(myIntent);
    }
}
