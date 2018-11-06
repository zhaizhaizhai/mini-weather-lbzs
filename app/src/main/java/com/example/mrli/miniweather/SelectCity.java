package com.example.mrli.miniweather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.*;

import zhaizhaizhai.app.MyApplication;
import zhaizhaizhai.bean.City;
import android.widget.ArrayAdapter;
import android.widget.Toast;

/**
 * Created by mrli on 2018/10/21.
 */
public class SelectCity extends Activity implements View.OnClickListener{
    private ImageView mBackBtn;
    private ListView listView = null; //声明ListView对象，用于绑定select_city布局中的ListView
    private TextView cityselected = null; // 绑定顶部栏的显示内容

    private List<City> listcity = MyApplication.getInstance().getCityList();
    private int listSize = listcity.size();    //存储在ListView中展示的内容
    private String[] city = new String[listSize];

    private ArrayList<String> mSearchResult = new ArrayList<>(); //搜索结果
    private Map<String,String> nameTocode = new HashMap<>(); //城市名到编码
    private Map<String,String> nameToPinyin = new HashMap<>();//城市名拼音

    private EditText mSearch; //搜索

    String returnCode;

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

        //建立映射
        String strName;

        String strNamePinyin;

        String strCode;

        for(City city1:listcity){

            strCode = city1.getNumber();
            strName = city1.getCity();
            strNamePinyin = city1.getFirstPY();
            nameTocode.put(strName, strCode);
            nameToPinyin.put(strName, strNamePinyin);
            mSearchResult.add(strName);

        }

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_single_choice,
                mSearchResult);
        listView = findViewById(R.id.list_view);
        listView.setAdapter(arrayAdapter);//设置适配器
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(SelectCity.this, "你已选择:" + city[i],
                        Toast.LENGTH_SHORT).show();
                cityselected.setText("当前城市："+city[i]);
                returnCode = nameTocode.get(returnCode);
                Log.d("returnCode", returnCode);
                cityselected.setText("当前城市：" + returnCode);


            }
        });

        mSearch = (EditText)findViewById(R.id.search_edit);
        mSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                arrayAdapter.getFilter().filter(s);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
    private List<String> getData(){
        List<String> data = new ArrayList<String>();
        data.add("测试数据1");
        data.add("测试数据2");
        data.add("测试数据3");

        return data;
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
