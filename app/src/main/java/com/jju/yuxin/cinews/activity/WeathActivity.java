package com.jju.yuxin.cinews.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.jju.yuxin.cinews.R;
import com.jju.yuxin.cinews.utils.MyLogger;
import com.jju.yuxin.cinews.volleyutils.VolleyUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class WeathActivity extends Activity implements View.OnClickListener{
    private String path = "http://v.juhe.cn/weather/index";
    private VolleyUtils volleyUtils;
    private String info;
    private String city="北京";  //默认为北京


    private Button bt_top_left;
    private Button bt_top_right;

    private TextView tv_city, tv_temperature, tv_weather, tv_wind, tv_date_y, tv_week;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weath);
        initdata();   //初始化数据
        if (getIntent().getStringExtra("city")!=null) {
            city = getIntent().getStringExtra("city");
        }
        try {
            String cityurl = URLEncoder.encode(city, "UTF-8");
            String  url =path + "?cityname=" +
                    cityurl + "&dtype=json&format=1&key=68dda6ce091f734db537380671fd31a1";
            get_info(url);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }

    private void initdata() {
        bt_top_left = (Button) findViewById(R.id.bt_top_left);
        bt_top_right = (Button) findViewById(R.id.bt_top_right);
        bt_top_left.setVisibility(View.VISIBLE);
        bt_top_right.setVisibility(View.VISIBLE);
        bt_top_left.setBackgroundResource(R.drawable.bt_return_selector);
        bt_top_right.setBackgroundResource(R.drawable.weather_location);
        bt_top_left.setOnClickListener(this);
        bt_top_right.setOnClickListener(this);

        tv_city = (TextView) findViewById(R.id.city);
        tv_temperature = (TextView) findViewById(R.id.temperature);
        tv_weather = (TextView) findViewById(R.id.weather);
        tv_wind = (TextView) findViewById(R.id.wind);
        tv_date_y = (TextView) findViewById(R.id.date_y);
        tv_week = (TextView) findViewById(R.id.week);
    }

    //得到JSON数据
    private void get_info(String url) {
        volleyUtils = new VolleyUtils(this);
        volleyUtils.doGetRequest(url,null);
        volleyUtils.getReusltListener(new VolleyUtils.ReusltListener() {
            @Override
            public void Result(Object sucessinfo, VolleyError error) {
                info = (String) sucessinfo;
                MyLogger.lLog().e("info"+info);
                if (info!=null){
                    try {
                        GetTodayTemperatureByCity(info);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(WeathActivity.this, "天气接口已失效！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 将获取的JSON数据解析
     *
     * @param info
     */
    private void GetTodayTemperatureByCity(String info) throws JSONException {
        JSONObject object = new JSONObject(info);
        JSONObject result = object.getJSONObject("result");
        JSONObject today = result.getJSONObject("today");
        String temperature = today.getString("temperature");
        String weather = today.getString("weather");
        String wind = today.getString("wind");
        String week = today.getString("week");
        String city = today.getString("city");
        String date_y = today.getString("date_y");
//        TodayBean todayBean = new TodayBean(temperature, weather, wind, week, city, date_y);
        tv_city.setText(city);
        tv_temperature.setText(temperature);
        tv_date_y.setText(date_y);
        tv_weather.setText(weather);
        tv_week.setText(week);
        tv_wind.setText(wind);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_top_left:
                WeathActivity.this.finish();
                break;
            case R.id.bt_top_right:
                Toast.makeText(WeathActivity.this,"你点击了位置",Toast.LENGTH_SHORT).show();
                startActivityForResult(new Intent(WeathActivity.this,CityPickerActivity.class),200);
        break;
    }
}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==200&resultCode==200){

        }

    }
}
