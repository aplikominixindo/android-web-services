package id.inixindosurabaya.androidwebservices;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class RequestHandler {
    // class ini digunakan untuk mengirim HttpPostRequest
    // mengambil 2 parameter
    // parameter 1: URL
    // parameter 2: HashMap ~> pembungkus dan data
    public String sendPostRequest
    (String requestURL,
     HashMap<String, String> postDataParams){
        // membuat URL
        URL url;

        // object StringBuilder untuk simpan pesan diambil
        // dari server
        StringBuilder sb = new StringBuilder();
        try {
            // inisialisasi URL baru
            url = new URL(requestURL);
            // membuat koneksi
            HttpURLConnection conn = (HttpURLConnection)
                    url.openConnection();
            // konfigurasi terhadap koneksi
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            // buat keluaran stream
            OutputStream os = conn.getOutputStream();

            // parameter untuk request
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));
            writer.flush();
            writer.close();
            os.close();

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK){
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getInputStream())
                );
                sb = new StringBuilder();
                String response;
                // baca response dari server
                while ((response = br.readLine()) != null) {
                    sb.append(response);
                }
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return sb.toString();
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }

    // method untuk handle send get request
    public String sendGetRequest(String requestURL) {
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(requestURL);
            HttpURLConnection conn = (HttpURLConnection)
                    url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream())
            );

            String s;
            while ((s = bufferedReader.readLine()) != null) {
                sb.append(s + "\n");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return sb.toString();
    }

    // method untuk send get request dengan parameter ID
    public String sendGetRequest(String requestURL, String id){
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(requestURL + id);
            HttpURLConnection conn = (HttpURLConnection)
                    url.openConnection();
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream())
            );

            String s;
            while ((s = br.readLine()) != null){
                sb.append(s + "\n");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return sb.toString();
    }
}
