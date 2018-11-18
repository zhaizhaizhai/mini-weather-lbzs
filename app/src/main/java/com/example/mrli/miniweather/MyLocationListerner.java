package com.example.mrli.miniweather;

import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;

import java.util.List;

import zhaizhaizhai.app.MyApplication;
import zhaizhaizhai.bean.City;

/**
 * Created by mrli on 2018/11/18.
 */
public class MyLocationListerner extends BDAbstractLocationListener {
    public String recity;
    public String cityCode;
    @Override
    public void onReceiveLocation(BDLocation location){

        String addr = location.getAddrStr();
        String country = location.getCountry();
        String province = location.getProvince();
        String city = location.getCity();
        String district = location.getDistrict();
        String street = location.getStreet();
        Log.d("location_city", city);
        recity = city.replace("市","");

        List<City> mCityList;
        MyApplication myApplication;
        myApplication = MyApplication.getInstance();

        mCityList=myApplication.getCityList();

        for (City cityl:mCityList){
            if (cityl.getCity().equals(recity)){
                cityCode=cityl.getNumber();
                Log.d("location_code",cityCode);
            }
        }
    }
}
