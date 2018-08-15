package id.inixindosurabaya.androidwebservices;

public class Konfigurasi {
    // lokasi link dimana web services berada
    public static final String URL_ADD = "http://ionicschool.000webhostapp.com/json_pegawai/tambahPgw.php";
    public static final String URL_GET_ALL = "http://ionicschool.000webhostapp.com/json_pegawai/index.php";
    public static final String URL_GET_PGW = "http://ionicschool.000webhostapp.com/json_pegawai/tampilPgw.php?id=";
    public static final String URL_UPDATE_PGW = "http://ionicschool.000webhostapp.com/json_pegawai/updatePgw.php";
    public static final String URL_DELETE_PGW = "http://ionicschool.000webhostapp.com/json_pegawai/hapusPgw.php?id=";

    // key untuk kirim request ke PHP
    public static final String KEY_PGW_ID = "id";
    public static final String KEY_PGW_NAMA = "name";
    public static final String KEY_PGW_JABATAN = "desg";
    public static final String KEY_PGW_GAJI = "salary";

    // JSON Tag
    public static final String TAG_JSON_ARRAY = "result";
    public static final String TAG_ID = "id";
    public static final String TAG_NAMA = "name";
    public static final String TAG_JABATAN = "desg";
    public static final String TAG_GAJI = "salary";

    // ID pegawai
    public static final String PGW_ID = "emp_id";
}
