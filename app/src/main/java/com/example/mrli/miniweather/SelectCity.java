package com.example.mrli.miniweather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import zhaizhaizhai.app.MyApplication;
import zhaizhaizhai.bean.City;
import android.widget.ArrayAdapter;
import android.widget.Toast;

/**
 * Created by mrli on 2018/10/21.
 */
public class SelectCity extends Activity implements View.OnClickListener{
    private ImageView mBackBtn;
    private ListView listView = null;
    private TextView cityselected = null;

    private List<City> listcity = MyApplication.getInstance().getCityList();
    private int listSize = listcity.size();
    private String[] city = new String[listSize];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_city);
        mBackBtn = (ImageView) findViewById(R.id.title_back);
        cityselected = (TextView) findViewById(R.id.title_name);
        mBackBtn.setOnClickListener(this);
        Log.i("City", listcity.get(1).getCity());
        for (int i = 0; i < listSize; i++){
            city[i] = listcity.get(i).getCity();
            Log.d("City", city[i]);
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_single_choice,
                city);
        listView = findViewById(R.id.list_view);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(SelectCity.this, "你已选择:" + city[i],
                        Toast.LENGTH_SHORT).show();
                cityselected.setText("当前城市："+city[i]);

            }
        });
    }
    
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:
                int position = listView.getCheckedItemPosition();
                String select_cityCode = listcity.get(position).getNumber();
                Intent i = new Intent();
                i.putExtra("cityCode", select_cityCode);
                setResult(RESULT_OK, i);
                Log.d("citycode", select_cityCode);
                finish();
                break;
            default:
                break;

        }
    }
}
