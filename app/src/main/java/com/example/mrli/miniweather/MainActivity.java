package com.example.mrli.miniweather;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.StringReader;
import java.io.IOException;
import android.widget.TextView;
import android.os.Handler;
import android.os.Message;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import zhaizhaizhai.bean.TodayWeather;
import zhaizhaizhai.util.NetUtil;

/**
 * Created by mrli on 2018/10/8.
 */



public class MainActivity extends Activity implements View.OnClickListener{
    private static final int UPDATE_TODAY_WEATHER =1;
    private ImageView mUpdateBtn;
    private ImageView mCitySelect;
    private ImageView mTitleShare;
    private ProgressBar mUpdateProgress;
    private LocationManager locationManager;
    private ImageView mTitleLocation;
    public LocationClient mLocationClient=null;
    private MyLocationListerner myListerner = new MyLocationListerner();
    private TextView cityTv, timeTv, humidityTv, weekTv, pmDataTv, pmQualityTv,
            temperatureTv, climateTv, windTv, city_name_Tv;
    private ImageView weatherImg, pmImg;
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case UPDATE_TODAY_WEATHER:
                    updateTodayWeather((TodayWeather) msg.obj);
                    break;
                default:
                    break;
            }
        }
    };
        @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_info);
        mUpdateBtn = (ImageView) findViewById(R.id.title_update_btn);
        mUpdateBtn.setOnClickListener(this);

        if (NetUtil.getNetworkState(this)!=NetUtil.NETWORN_NONE){
            Log.d("myWeather","网络ok");
            Toast.makeText(MainActivity.this,"网络ok！",Toast.LENGTH_LONG).show();
        }
        else{
            Log.d("myWeather","网络挂了！");
            Toast.makeText(MainActivity.this,"网络挂了！",Toast.LENGTH_LONG).show();
        }
        mCitySelect = (ImageView) findViewById(R.id.title_city_manager);
        mCitySelect.setOnClickListener(this);
        initView();
        mLocationClient=new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(myListerner);
        initLocation();

    }
    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        int span = 100;
        option.setScanSpan(0);
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setLocationNotify(true);
        option.setIsNeedLocationDescribe(true);
        option.setIsNeedLocationPoiList(true);
        option.setIgnoreKillProcess(false);
        option.setEnableSimulateGps(false);
        mLocationClient.setLocOption(option);
    }
    void initView() {
        city_name_Tv = findViewById(R.id.title_city_name);
        cityTv = findViewById(R.id.city);
        timeTv = findViewById(R.id.time);
        humidityTv = findViewById(R.id.humidity);
        weekTv = findViewById(R.id.week_today);
        pmDataTv = findViewById(R.id.pm_data);
        pmQualityTv = findViewById(R.id.pm2_5_quality);
        pmImg = findViewById(R.id.pm2_5_img);
        temperatureTv = findViewById(R.id.temperature);
        climateTv = findViewById(R.id.climate);
        windTv = findViewById(R.id.wind);
        weatherImg = findViewById(R.id.weather_img);

        city_name_Tv.setText("N/A");
        cityTv.setText("N/A");
        timeTv.setText("N/A");
        humidityTv.setText("N/A");
        pmDataTv.setText("N/A");
        pmQualityTv.setText("N/A");
        weekTv.setText("N/A");
        temperatureTv.setText("N/A");
        climateTv.setText("N/A");
        windTv.setText("N/A");
    }
    void updateTodayWeather(TodayWeather todayWeather){
            if(mUpdateBtn.getVisibility()==View.GONE&&mUpdateProgress.getVisibility()==View.VISIBLE){
                setUpdateBtn();
            }
            city_name_Tv.setText(todayWeather.getCity()+"天气");
            cityTv.setText(todayWeather.getCity());
            timeTv.setText(todayWeather.getUpdatetime()+ "发布");
            humidityTv.setText("湿度:"+todayWeather.getShidu());
            pmDataTv.setText(todayWeather.getPm25());
            pmQualityTv.setText(todayWeather.getQuality());
            weekTv.setText(todayWeather.getDate());
            temperatureTv.setText(todayWeather.getHigh()+"~"+todayWeather.getLow());
            climateTv.setText(todayWeather.getType());
            windTv.setText("风力:"+todayWeather.getFengli());
            Toast.makeText(MainActivity.this,"更新成功!",Toast.LENGTH_SHORT).show();

            //更新pm2.5的图案
            if(todayWeather.getPm25()!=null){

                 int pm2_5=Integer.parseInt(todayWeather.getPm25());

                 if(pm2_5<=50) pmImg.setImageResource(R.drawable.biz_plugin_weather_0_50);

                 if(pm2_5>50&&pm2_5<=100) pmImg.setImageResource(R.drawable.biz_plugin_weather_51_100);

                 if(pm2_5>100&&pm2_5<=150) pmImg.setImageResource(R.drawable.biz_plugin_weather_101_150);

                 if(pm2_5>150&&pm2_5<=200) pmImg.setImageResource(R.drawable.biz_plugin_weather_151_200);

                 if(pm2_5>200&&pm2_5<=300) pmImg.setImageResource(R.drawable.biz_plugin_weather_201_300);

                 if(pm2_5>300) pmImg.setImageResource(R.drawable.biz_plugin_weather_greater_300);

            }

            //更新天气图案

            String climate=todayWeather.getType();

            if(climate.equals("暴雪"))

                weatherImg.setImageResource(R.drawable.biz_plugin_weather_baoxue);

            if(climate.equals("暴雨"))

                weatherImg.setImageResource(R.drawable.biz_plugin_weather_baoyu);

            if(climate.equals("大暴雨"))

                weatherImg.setImageResource(R.drawable.biz_plugin_weather_dabaoyu);

            if(climate.equals("大雪"))

                weatherImg.setImageResource(R.drawable.biz_plugin_weather_daxue);

            if(climate.equals("大雨"))

                weatherImg.setImageResource(R.drawable.biz_plugin_weather_dayu);

            if(climate.equals("多云"))

                weatherImg.setImageResource(R.drawable.biz_plugin_weather_duoyun);

            if(climate.equals("雷阵雨"))
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_leizhenyu);

            if(climate.equals("雷阵雨冰雹"))

                weatherImg.setImageResource(R.drawable.biz_plugin_weather_leizhenyubingbao);

            if(climate.equals("晴"))

                weatherImg.setImageResource(R.drawable.biz_plugin_weather_qing);

            if(climate.equals("沙尘暴"))

                weatherImg.setImageResource(R.drawable.biz_plugin_weather_shachenbao);

            if(climate.equals("特大暴雨"))

                weatherImg.setImageResource(R.drawable.biz_plugin_weather_tedabaoyu);

            if(climate.equals("雾"))

                weatherImg.setImageResource(R.drawable.biz_plugin_weather_wu);

            if(climate.equals("小雪"))

                weatherImg.setImageResource(R.drawable.biz_plugin_weather_xiaoxue);

            if(climate.equals("小雨"))

                weatherImg.setImageResource(R.drawable.biz_plugin_weather_xiaoyu);

            if(climate.equals("阴"))

                weatherImg.setImageResource(R.drawable.biz_plugin_weather_yin);

            if(climate.equals("雨夹雪"))

                weatherImg.setImageResource(R.drawable.biz_plugin_weather_yujiaxue);

            if(climate.equals("阵雨"))

                weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhenyu);

            if(climate.equals("阵雪"))

                weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhenxue);

            if(climate.equals("中雪"))

                weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhongxue);

            if(climate.equals("中雨"))

                weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhongyu);

            Toast.makeText(MainActivity.this,"更新成功!",Toast.LENGTH_LONG).show();




    }
    private TodayWeather parseXML(String xmldata){
        TodayWeather todayWeather = null;
        int fengxiangCount=0;
        int fengliCount =0;
        int dateCount=0;
        int highCount =0;
        int lowCount=0;
        int typeCount =0;
        try {
            XmlPullParserFactory fac = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = fac.newPullParser();
            xmlPullParser.setInput(new StringReader(xmldata));
            int eventType = xmlPullParser.getEventType();
            Log.d("myWeather", "parseXML");
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                      // 判断当前事件是否为文档开始事件
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    // 判断当前事件是否为标签元素开始事件
                    case XmlPullParser.START_TAG:
                    if(xmlPullParser.getName().equals("resp")){
                        todayWeather = new TodayWeather();
                    }
                    if (todayWeather != null) {
                        if (xmlPullParser.getName().equals("city")){
                            eventType = xmlPullParser.next();
                            todayWeather.setCity(xmlPullParser.getText());
                        }
                        else if (xmlPullParser.getName().equals("updatetime")){
                            eventType = xmlPullParser.next();
                            todayWeather.setUpdatetime(xmlPullParser.getText());
                        }
                        else if (xmlPullParser.getName().equals("shidu")){
                            eventType = xmlPullParser.next();
                            todayWeather.setShidu(xmlPullParser.getText());
                        }
                        else if (xmlPullParser.getName().equals("wendu")){
                            eventType = xmlPullParser.next();
                            todayWeather.setWendu(xmlPullParser.getText());
                        }
                        else if (xmlPullParser.getName().equals("pm25")){
                            eventType = xmlPullParser.next();
                            todayWeather.setPm25(xmlPullParser.getText());
                        }
                        else if (xmlPullParser.getName().equals("quality")){
                            eventType = xmlPullParser.next();
                            todayWeather.setQuality(xmlPullParser.getText());
                        }
                        else if (xmlPullParser.getName().equals("fengxiang")&&fengxiangCount==0){
                            eventType = xmlPullParser.next();
                            todayWeather.setFengxiang(xmlPullParser.getText());
                            fengxiangCount++;
                        }
                        else if (xmlPullParser.getName().equals("fengli")&&fengliCount==0){
                            eventType = xmlPullParser.next();
                            todayWeather.setFengli(xmlPullParser.getText());
                            fengliCount++;
                        }
                        else if (xmlPullParser.getName().equals("date")&&dateCount==0){
                            eventType = xmlPullParser.next();
                            todayWeather.setDate(xmlPullParser.getText());
                            dateCount++;
                        }
                        else if (xmlPullParser.getName().equals("high")&&highCount==0){
                            eventType = xmlPullParser.next();
                            todayWeather.setHigh(xmlPullParser.getText().substring(2).trim());
                            highCount++;
                        }
                        else if (xmlPullParser.getName().equals("low")&&lowCount==0){
                            eventType = xmlPullParser.next();
                            todayWeather.setLow(xmlPullParser.getText().substring(2).trim());
                            lowCount++;
                        }
                        else if (xmlPullParser.getName().equals("type")&&typeCount==0){
                            eventType = xmlPullParser.next();
                            todayWeather.setType(xmlPullParser.getText());
                            typeCount++;
                        }

                    }

                    break;
                    //判断当前事件是否为标签元素结束事件
                    case XmlPullParser.END_TAG:
                        break;
                }
                // 进入下一个元素并触发相应事件
                eventType = xmlPullParser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return todayWeather;
    }

    /**
     * 隐藏更新按钮
     * 显示更新转动动画
     */
    void setUpdateProgress(){
        mUpdateBtn=(ImageView) findViewById(R.id.title_update_btn);
        mUpdateProgress=(ProgressBar)findViewById(R.id.title_update_progress);
        mTitleShare=(ImageView)findViewById(R.id.title_share);
        mUpdateBtn.setVisibility(View.GONE);
        RelativeLayout.LayoutParams params=(RelativeLayout.LayoutParams)mTitleShare.getLayoutParams();
        params.addRule(RelativeLayout.LEFT_OF, R.id.title_update_progress);
        mTitleShare.setLayoutParams(params);
        mUpdateProgress.setVisibility(View.VISIBLE);
    }

    /**
     * 显示更新按钮
     * 隐藏转动动画
     */
    void setUpdateBtn(){
        mUpdateBtn=(ImageView) findViewById(R.id.title_update_btn);
        mUpdateProgress=(ProgressBar)findViewById(R.id.title_update_progress);
        mUpdateBtn.setVisibility(View.VISIBLE);
        mTitleShare=(ImageView)findViewById(R.id.title_share);

        RelativeLayout.LayoutParams params=(RelativeLayout.LayoutParams)mTitleShare.getLayoutParams() ;
        params.addRule(RelativeLayout.LEFT_OF,R.id.title_update_btn);
        mTitleShare.setLayoutParams(params);

        mUpdateProgress.setVisibility(View.GONE);
    }


    /**
     * @param cityCode
     */
    private void queryWeatherCode(String cityCode)  {
        //http://wthrcdn.etouch.cn/WeatherApi?citykey=
        final String address = "http://wthrcdn.etouch.cn/WeatherApi?citykey=" + cityCode;
        Log.d("myWeather", address);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection con=null;
                TodayWeather todayWeather = null;
                try{
                    URL url = new URL(address);
                    con = (HttpURLConnection)url.openConnection();
                    con.setRequestMethod("GET");
                    con.setConnectTimeout(8000);
                    con.setReadTimeout(8000);
                    InputStream in = con.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String str;
                    while((str=reader.readLine()) != null){
                        response.append(str);
                        Log.d("myWeather", str);
                    }
                    String responseStr=response.toString();
                    Log.d("myWeather", responseStr);
                    todayWeather = parseXML(responseStr);
                    if (todayWeather != null){
                        Log.d("myWeather",todayWeather.toString());
                        Message msg =new Message();
                        msg.what = UPDATE_TODAY_WEATHER;
                        msg.obj=todayWeather;
                        mHandler.sendMessage(msg);
                    }
                    parseXML(responseStr);
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if(con != null){
                        con.disconnect();
                    }
                }
            }
        }).start();
    }
    @Override
    public void onClick(View view){
        if (view.getId() == R.id.title_city_manager){
            Intent i = new Intent(this,SelectCity.class);
            //startActivity(i);
            startActivityForResult(i,1);
        }
        if (view.getId() == R.id.title_update_btn){
            setUpdateProgress();
            SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
            String cityCode = sharedPreferences.getString("main_ city_code","101010100");
            Log.d("myWeather",cityCode);
            if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE){
                Log.d("myWeather", "网络OK");
                queryWeatherCode(cityCode);
            }
            else{
                Log.d("myWeather", "网络挂了");
                Toast.makeText(MainActivity.this,"网络挂了!",Toast.LENGTH_LONG).show();
            }

        }
        if (view.getId()==R.id.title_location){
            setUpdateProgress();
            if (mLocationClient.isStarted()){
                mLocationClient.stop();
            }
            mLocationClient.start();

            final Handler BDHandler=new Handler(){
                public void handleMessage(Message msg){
                    switch (msg.what){
                        case 5:
                            if (msg.obj !=null){
                                if (NetUtil.getNetworkState(MainActivity.this)!=NetUtil.NETWORN_NONE){
                                    Log.d("myWeather","网络ok");
                                    queryWeatherCode(myListerner.cityCode);
                                } else {
                                    Log.d("myWeather","网络挂了");
                                    Toast.makeText(MainActivity.this,"网络挂了！",Toast.LENGTH_LONG).show();
                                }
                            }
                            myListerner.cityCode=null;
                            break;
                        default:
                                break;
                    }
                }
            };

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        while (myListerner.cityCode==null){
                            Thread.sleep(2000);
                        }
                        Message msg=new Message();
                        msg.what=5;
                        msg.obj=myListerner.cityCode;
                        BDHandler.sendMessage(msg);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == 1 && resultCode== RESULT_OK){
            String newCityCode = data.getStringExtra("cityCode");
            Log.d("myWeather", "选择的城市代码为"+newCityCode);

            if (NetUtil.getNetworkState(this) !=NetUtil.NETWORN_NONE){
                Log.d("myWeather", "网络OK");
                queryWeatherCode(newCityCode);
            }
            else {
                Log.d("myWeather", "网络挂了");
                Toast.makeText(MainActivity.this, "网络挂了", Toast.LENGTH_LONG).show();
            }
        }
    }


}