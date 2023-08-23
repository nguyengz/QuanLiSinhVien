package vn.edu.stu.duongngocnguyen_dh51900713;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import vn.edu.stu.duongngocnguyen_dh51900713.Model.Sinhvien;

public class SinhVienActivity extends AppCompatActivity {
    final String SERVER = "http://document.fitstu.net/2022/";
    EditText txtGetdata;
    Button btnGetdata;
    ListView lvSinhvien;
    ArrayList<Sinhvien> dsSinhvien;
    ArrayAdapter<Sinhvien> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sinh_vien);
        addControls();
        addEvents();
    }

    private void addControls() {
        txtGetdata = findViewById(R.id.txtGetdata);
        btnGetdata = findViewById(R.id.btnGetdata);
        lvSinhvien = findViewById(R.id.lvSvien);
        dsSinhvien = new ArrayList<>();
        adapter = new ArrayAdapter<>(
                SinhVienActivity.this,
                android.R.layout.simple_list_item_1,
                dsSinhvien
        );
        lvSinhvien.setAdapter(adapter);
    }

    private void addEvents() {
        btnGetdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xuLyGetData();
            }
        });
    }

    private void xuLyGetData() {
        RequestQueue requestQueue = Volley.newRequestQueue(
                SinhVienActivity.this
        );
        Response.Listener<String> responseListener =
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(
                                SinhVienActivity.this,
                                response,
                                Toast.LENGTH_LONG
                        ).show();
                        try {
                            dsSinhvien.clear();
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("KETQUA");

                            //JSONObject jsonObject = new JSONObject();
                            int len = jsonArray.length();
                            for (int i=0; i<len;i++) {
                                //jsonArray = new JSONArray(response);
                                jsonObject = jsonArray.getJSONObject(i);
                                String ten = jsonObject.getString("TEN");
                                //String dtb = jsonObject.getString("DTB");
                                double dtb = Double.parseDouble(jsonObject.getString("DTB"));
                                dsSinhvien.add(new Sinhvien(ten,dtb));
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException ex) {
                            Toast.makeText(
                                    SinhVienActivity.this,
                                    ex.getMessage(),
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    }
                };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(
                        SinhVienActivity.this,
                        error.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        };
        Uri.Builder builder = Uri.parse(SERVER + "ws2.php").buildUpon();
        builder.appendQueryParameter("action","getsinhvien");
        builder.appendQueryParameter("lop",txtGetdata.getText().toString());
        String url = builder.build().toString();
        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                responseListener,
                errorListener
        );
        request.setRetryPolicy(
                new DefaultRetryPolicy(
                        DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
        requestQueue.add(request);
    }
}