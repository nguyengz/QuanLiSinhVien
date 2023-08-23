package vn.edu.stu.duongngocnguyen_dh51900713;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    final String SERVER = "http://document.fitstu.net/2022/";
    EditText txtEmail, txtPass;
    Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControls();
        addEvents();
    }

    private void addControls() {
        txtEmail = findViewById(R.id.txtEmail);
        txtPass = findViewById(R.id.txtPassword);
        btnLogin = findViewById(R.id.btnLogin);

    }

    private void addEvents() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xuLyLogin();
            }
        });
    }

    private void xuLyLogin() {
        RequestQueue requestQueue = Volley.newRequestQueue(
                MainActivity.this
        );
        Response.Listener<String> responseListener =
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(
                                MainActivity.this,
                                response,
                                Toast.LENGTH_LONG
                        ).show();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean result = jsonObject.getBoolean("KETQUA");
                            if (result) {
                                Toast.makeText(
                                        MainActivity.this,
                                        "Đăng nhập thành công",
                                        Toast.LENGTH_LONG
                                ).show();
                                Intent intent = new Intent(MainActivity.this, SinhVienActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(
                                        MainActivity.this,
                                        "Đăng nhập thất bại",
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(
                        MainActivity.this,
                        error.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        };
        Uri.Builder builder = Uri.parse(SERVER + "ws2.php").buildUpon();
        builder.appendQueryParameter("action","login");
        builder.appendQueryParameter("email",txtEmail.getText().toString());
        builder.appendQueryParameter("pass",txtPass.getText().toString());
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