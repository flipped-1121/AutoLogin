package com.kang.autologin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity<ISP> extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private MyDBOpenHelper mhelper;
    private SQLiteDatabase db;
    private static String res;
    private static String account;
    private static String password;
    private static String ISP;

    public class initGet {
        final OkHttpClient client = new OkHttpClient();

        String run(String url) throws IOException {
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            try (Response response = client.newCall(request).execute()) {
                res = Objects.requireNonNull(response.body()).string();
            } catch (IOException e) {
                Looper.prepare();
                Toast.makeText(MainActivity.this, "请检查网络配置，确保连接到指定网络", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
            return res;
        }
    }

    public void loginPost(String account, String password, String ISP) {
        initPost example = new initPost();
        String json = example.bowlingJson(account, password, ISP);
        Log.d(TAG, "login: " + json);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String response = null;
                try {
                    response = example.post("http://10.255.0.19/drcom/login", json);
                    Log.d(TAG, "run: " + response);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        // Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
    }

    public void loginGet(String account, String password, String ISP) {
        new Thread(new Runnable() {
            private Toast FancyToast;

            @Override
            public void run() {
                initGet example = new initGet();
                // String response = null;
                try {
                    String url = "http://10.255.0.19/drcom/login?callback=dr1003&" + "DDDDD=" + account + ISP
                            + "&upass=" + password + "&0MKKey=123456";
                    // Log.d(TAG, "runUrl: " + url);
                    String resFirst = example.run(url);
                    String resSecond = example.run(url);
                    Log.d(TAG, "run: " + resSecond);
                    resSecond = resSecond.substring(12, resSecond.length() - 4);
                    JSONObject jsonObject = JSONObject.parseObject(resSecond);
                    // Log.d(TAG, "run: " + jsonObject.getString("result"));
                    if (jsonObject.getString("result").equals("0")) {
                        Looper.prepare();
                        Toast.makeText(MainActivity.this, "啊哦：" + jsonObject.getString("msga") + "\n请检查所填写信息是否正确",
                                Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    } else {
                        Looper.prepare();
                        Toast.makeText(MainActivity.this, "哦耶，连接成功", Toast.LENGTH_SHORT).show();
                        // 数据库增加操作
                        ContentValues values = new ContentValues();
                        values.put("account", account);
                        values.put("password", password);
                        db.insert("userInfo", null, values);
                        Log.d(TAG, "run: 数据库操作");
                        Looper.loop();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mhelper = new MyDBOpenHelper(MainActivity.this);// 实例化数据库帮助类
        db = mhelper.getWritableDatabase();// 创建数据库，获取数据库的读写权限
        // verifyStoragePermissions(this);

        EditText editTextAccount = (EditText) findViewById(R.id.editTextAccount);
        EditText editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        RadioGroup rgISP = (RadioGroup) findViewById(R.id.networkType);
        Button buttonSubmit = (Button) findViewById(R.id.buttonSubmit);

        // 开始查询 参数：（实现查询的 sql 语句，条件参数）
        @SuppressLint("Recycle")
        Cursor cursor = db.rawQuery("select * from userInfo where id=1", null);
        if (cursor.getCount() != 0) {
            Toast.makeText(MainActivity.this, "发现本地数据，自动填充", Toast.LENGTH_SHORT).show();
            // 循环遍历结果集，取出数据，显示出来
            while (cursor.moveToNext()) {
                @SuppressLint("Range")
                String account = cursor.getString(cursor.getColumnIndex("account"));
                @SuppressLint("Range")
                String password = cursor.getString(cursor.getColumnIndex("password"));
                editTextAccount.setText(account);
                editTextPassword.setText(password);
            }
        } else {
            Toast.makeText(MainActivity.this, "请填写登录信息", Toast.LENGTH_SHORT).show();
        }

        rgISP.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.teacher:
                        ISP = "@jzg";
                        break;
                    case R.id.aust:
                        ISP = "@aust";
                        break;
                    case R.id.unicom:
                        ISP = "@unicom";
                        break;
                    case R.id.cmcc:
                        ISP = "@cmcc";
                        break;
                    default:
                        break;
                }
            }
        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                account = editTextAccount.getText().toString();
                password = editTextPassword.getText().toString();
                Log.d(TAG, "account:" + account + " password:" + password + " ISP:" + ISP);
                if (account == null || password == null || ISP == null) {
                    Toast.makeText(MainActivity.this, "请将信息填写完整！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "登陆中……", Toast.LENGTH_SHORT).show();
                    loginGet(account, password, ISP);
                }
            }
        });
    }
}