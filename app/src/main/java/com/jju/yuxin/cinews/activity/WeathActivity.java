package com.jju.yuxin.cinews.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.jju.yuxin.cinews.R;
import com.jju.yuxin.cinews.utils.MyLogger;
import com.jju.yuxin.cinews.volleyutils.VolleyUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class WeathActivity extends Activity implements View.OnClickListener {
    private String path = "http://v.juhe.cn/weather/index";
    private VolleyUtils volleyUtils;
    private String info;
    private String city = "北京";  //默认为北京


    private Button bt_top_left;
    private Button bt_top_right;

    private TextView tv_city, tv_temperature, tv_weather, tv_wind, tv_date_y, tv_week;//今天的
    private TextView f_week1,f_weather1,f_temp1,f_week2,f_weather2,f_temp2,f_week3,f_weather3,f_temp3;//未来的
    private ImageView f_image1,f_image2,f_image3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weath);
        initdata();   //初始化数据
        if (getIntent().getStringExtra("city") != null) {
            city = getIntent().getStringExtra("city");
        }
        get_info(city);


    }

    /**
     * 控件初始化
     */
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

        f_week1 = (TextView) findViewById(R.id.f_week1);
        f_weather1 = (TextView) findViewById(R.id.f_weather1);
        f_temp1 = (TextView) findViewById(R.id.f_temp1);
        f_image1 = (ImageView) findViewById(R.id.f_image1);

        f_week2 = (TextView) findViewById(R.id.f_week2);
        f_weather2 = (TextView) findViewById(R.id.f_weather2);
        f_temp2 = (TextView) findViewById(R.id.f_temp2);
        f_image2 = (ImageView) findViewById(R.id.f_image2);

        f_week3 = (TextView) findViewById(R.id.f_week3);
        f_weather3 = (TextView) findViewById(R.id.f_weather3);
        f_temp3 = (TextView) findViewById(R.id.f_temp3);
        f_image3 = (ImageView) findViewById(R.id.f_image3);
    }

    //得到JSON数据
    private void get_info(String city) {
        try {
            String cityurl = URLEncoder.encode(city, "UTF-8");
            String url = path + "?cityname=" +
                    cityurl + "&dtype=json&format=1&key=68dda6ce091f734db537380671fd31a1";

            volleyUtils = new VolleyUtils(this);
            volleyUtils.doGetRequest(url, null);
            volleyUtils.getReusltListener(new VolleyUtils.ReusltListener() {
                @Override
                public void Result(Object sucessinfo, VolleyError error) {
                    info = (String) sucessinfo;
                    MyLogger.lLog().e("info" + info);
                    if (info != null) {
                        try {
                            GetTodayTemperatureByCity(info);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(WeathActivity.this, R.string.tianqixinxihuoqushibai, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将获取的JSON数据解析
     *
     * @param info
     */
    private void GetTodayTemperatureByCity(String info) throws JSONException {
        JSONObject object = new JSONObject(info);
        int resultcode = object.getInt("resultcode");
        if (resultcode==200) {  //200查询成功
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

            JSONObject future = result.getJSONObject("future");
            JSONObject day1=future.getJSONObject(get_fututreday(1));
            String week1=day1.getString("week");
            String temp1 = day1.getString("temperature");
            String weather1 = day1.getString("weather");
            String weatherid_1 = day1.getJSONObject("weather_id").getString("fa");

            JSONObject day2=future.getJSONObject(get_fututreday(2));
            String week2=day2.getString("week");
            String temp2 = day2.getString("temperature");
            String weather2 = day2.getString("weather");
            String weatherid_2 = day2.getJSONObject("weather_id").getString("fa");

            JSONObject day3=future.getJSONObject(get_fututreday(3));
            String week3=day3.getString("week");
            String temp3 = day3.getString("temperature");
            String weather3 = day3.getString("weather");
            String weatherid_3 = day3.getJSONObject("weather_id").getString("fa");

            f_week1.setText(week1);
            f_weather1.setText(weather1);
            f_temp1.setText(temp1);
            f_image1.setImageResource(getResources().getIdentifier("d" + weatherid_1, "drawable", "com.jju.yuxin.cinews"));

            f_week2.setText(week2);
            f_weather2.setText(weather2);
            f_temp2.setText(temp2);
            f_image2.setImageResource(getResources().getIdentifier("d" + weatherid_2, "drawable", "com.jju.yuxin.cinews"));

            f_week3.setText(week3);
            f_weather3.setText(weather3);
            f_temp3.setText(temp3);
            f_image3.setImageResource(getResources().getIdentifier("d" + weatherid_3, "drawable", "com.jju.yuxin.cinews"));
        }else {
            Toast.makeText(this, "查询不到该城市天气信息！", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //退出当前activity
            case R.id.bt_top_left:
                WeathActivity.this.finish();
                break;
            //重新选择城市
            case R.id.bt_top_right:
                startActivityForResult(new Intent(WeathActivity.this, CityPickerActivity.class), 1);
                break;
        }
    }

    /**
     * 城市选择 结果返回
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 & resultCode == 1) {
            city = data.getStringExtra("city");
            get_info(city);

        }

    }

    /**
     * 获取未来几天的日期
     * @param i   传入后几天
     * @return
     */
    private String get_fututreday(int i) {
        Calendar c=Calendar.getInstance();
        Date date=new Date();//取时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE,i);//把日期往后增加一天.整数往后推,负数往前移动
        date=calendar.getTime(); //这个时间就是日期往后推一天的结果
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String dateString = "day_"+formatter.format(date);
        return dateString;
    }
}
