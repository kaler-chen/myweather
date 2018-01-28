package com.myweather.android;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.myweather.android.db.County;
import com.myweather.android.db.City;
import com.myweather.android.db.Province;
import com.myweather.android.util.ConstUtil;
import com.myweather.android.util.LogUtil;
import com.myweather.android.util.MApplication;
import com.myweather.android.util.PropertyUtil;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * 选择城市
 * Created by kaler-chen on 2018/1/14.
 * com.myweather.android
 */

public class ChooseAreaFragment extends Fragment {
    private static final String TAG = "ChooseAreaFragment";

    private static final int LEVEL_PROVINCE = 0;
    private static final int LEVEL_CITY = 1;
    private static final int LEVEL_COUNTY = 2;

    private ProgressDialog progressDialog;
    private TextView titleText;
    private Button backButton;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> dataList = new ArrayList<>();
    //省列表
    private List<Province> provinceList = new ArrayList<>();
    //市列表
    private List<City> cityList = new ArrayList<>();
    //县列表
    private List<County> countyList = new ArrayList<>();

    private Province selectedProvince;
    private City selectedCity;
    private County selectedCounty;

    private int currentLevel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area, container, false);
        titleText = view.findViewById(R.id.title_text);
        backButton = view.findViewById(R.id.back_button);
        listView = view.findViewById(R.id.list_view);
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String loadCityFlag = sharedPreferences.getString(ConstUtil.LOAD_AREAINFO_FLAG, null);
        if (loadCityFlag == null){
            //如果加载城市信息的标识为空，则加载一次城市信息
            new LoadAreaInfoTask().execute();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String areaLevel = PropertyUtil.getProp(ConstUtil.AREA_LEVEL);
                String locationName = "";
                if (TextUtils.isEmpty(areaLevel)){
                    areaLevel = "city"; //默认天气区域级别为市
                }
                if(currentLevel == LEVEL_PROVINCE){
                    if (ConstUtil.PROVINCE.equalsIgnoreCase(areaLevel)){
                        locationName = provinceList.get(position).getProvinceName();
                        startWeatherInfo(locationName);
                    }else{
                        selectedProvince = provinceList.get(position);
                        queryCities();
                    }
                } else if(currentLevel == LEVEL_CITY){
                    if (ConstUtil.CITY.equalsIgnoreCase(areaLevel)){
                        locationName = cityList.get(position).getCityName();
                        startWeatherInfo(locationName);
                    }else{
                        selectedCity = cityList.get(position);
                        queryCounties();
                    }
                } else if(currentLevel == LEVEL_COUNTY){
                    locationName = countyList.get(position).getCountyName();
                    startWeatherInfo(locationName);
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentLevel == LEVEL_COUNTY){
                    queryCities();
                }else if(currentLevel == LEVEL_CITY){
                    queryProvinces();
                }
            }
        });
        queryProvinces();
    }

    /**
     * 加载城市信息.xls异步任务
     * Created by kaler-chen on 2018/1/21.
     * com.myweather.android.task
     */
    public class LoadAreaInfoTask extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
            showProgressDialog();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            closeProgressDialog();
            SharedPreferences.Editor editor =
                    PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
            if (!aBoolean){
                editor.remove(ConstUtil.LOAD_AREAINFO_FLAG);
                Toast.makeText(getContext(), "加载城市信息失败", Toast.LENGTH_SHORT).show();
            }else{
                editor.putString(ConstUtil.LOAD_AREAINFO_FLAG, "true");
                queryProvinces();
                adapter.notifyDataSetChanged();
            }
            editor.apply();
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            try {
                InputStream is = MApplication.getContext().getAssets().open("thinkpage_cities.xls");
                HSSFWorkbook workbook = new HSSFWorkbook(is);
                loadProvinceInfo(workbook);     //加载省份信息
                loadCityInfo(workbook);         //加载地级市信息
//                loadCountyInfo(workbook);       //加载县信息
                is.close();
                return true;
            } catch (IOException e) {
                LogUtil.e(TAG, "从EXCEL加载城市信息失败，" + e.getMessage(), e);
            }
            return false;
        }

        /**
         * 加载省份信息
         * @param workbook
         */
        private void loadProvinceInfo(HSSFWorkbook workbook){
            HSSFSheet sheet = workbook.getSheet("province");
            int firstRowNum = sheet.getFirstRowNum() + 1;
            int lastRowNum = sheet.getLastRowNum();
            HSSFRow row;
            Province province;
            //插入信息前先清空表数据
            DataSupport.deleteAll(Province.class);
            //开始插入数据
            for (int i  = firstRowNum; i <= lastRowNum; i++){
                row = sheet.getRow(i);
                province = new Province();
                province.setProvinceName(row.getCell(0).getStringCellValue());
                province.setProvinceCode(row.getCell(1).getStringCellValue());
                province.save();
            }
        }

        /**
         * 加载地级市信息
         * @param workbook
         */
        private void loadCityInfo(HSSFWorkbook workbook){
            HSSFSheet sheet = workbook.getSheet("city");
            int firstRowNum = sheet.getFirstRowNum() + 1;
            int lastRowNum = sheet.getLastRowNum();
            HSSFRow row;
            City city;
            //插入信息前先清空表数据
            DataSupport.deleteAll(City.class);
            //开始插入数据
            for (int i  = firstRowNum; i <= lastRowNum; i++){
                row = sheet.getRow(i);
                city = new City();
                city.setCityCode(row.getCell(3).getStringCellValue());
                city.setCityName(row.getCell(2).getStringCellValue());
                city.setProvinceCode(row.getCell(0).getStringCellValue());
                city.save();
            }
        }

        /**
         * 加载县信息
         * @param workbook
         */
        private void loadCountyInfo(HSSFWorkbook workbook){
            HSSFSheet sheet = workbook.getSheet("county");
            int firstRowNum = sheet.getFirstRowNum() + 1;
            int lastRowNum = sheet.getLastRowNum();
            HSSFRow row;
            County county;
            //插入信息前先清空表数据
            DataSupport.deleteAll(County.class);
            //开始插入数据
            for (int i  = firstRowNum; i <= lastRowNum; i++){
                row = sheet.getRow(i);
                county = new County();
                county.setCountyCode(row.getCell(1).getStringCellValue());
                county.setCountyName(row.getCell(0).getStringCellValue());
                county.setCityCode(row.getCell(2).getStringCellValue());
                String level = row.getCell(4).getStringCellValue();
                level  = "中国地级市".equals(level) ? "1" : "2";
                county.setCountyLevel(level);
                county.save();
            }
        }
    }

    /**
     * 启动天气显示活动
     * @param locationName
     */
    private void startWeatherInfo(String locationName){
        if(getActivity() instanceof MainActivity){
            WeatherActivity.startAction(getContext(),locationName);
            getActivity().finish();
        }else if(getActivity() instanceof WeatherActivity){
            WeatherActivity activity = (WeatherActivity) getActivity();
            activity.drawerLayout.closeDrawers();
            activity.refreshWeatherInfo(locationName);
        }
    }

    /**
     * 查询所有的省，从本地数据库查询
     */
    private void queryProvinces() {
        titleText.setText("中国");
        backButton.setVisibility(View.GONE);
        provinceList = DataSupport.order("provinceName asc").find(Province.class);
        if (provinceList.size() > 0){
            dataList.clear();
            for (Province province : provinceList){
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_PROVINCE;
        }
    }

    /**
     * 查询所有的市，从本地数据库查询
     */
    private void queryCities() {
        titleText.setText(selectedProvince.getProvinceName());
        backButton.setVisibility(View.VISIBLE);
        cityList = DataSupport.where("provinceCode = ?",
                selectedProvince.getProvinceName()).order("cityName asc").find(City.class);
        if (cityList.size() > 0){
            dataList.clear();
            for (City city : cityList){
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_CITY;
        }
    }

    /**
     * 查询所有的县，从本地数据库查询
     */
    private void queryCounties() {
        titleText.setText(selectedCity.getCityName());
        backButton.setVisibility(View.VISIBLE);
        countyList = DataSupport.where("cityCode = ?", selectedCity.getCityName())
                .order("countyLevel asc,countyName asc").find(County.class);
        if (countyList.size() > 0){
            dataList.clear();
            for (County county : countyList){
                dataList.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_COUNTY;
        }
    }

    private void closeProgressDialog() {
        if (progressDialog != null){
            progressDialog.dismiss();
        }
    }

    private void showProgressDialog() {
        if (progressDialog == null){
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在初始化城市信息");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

}
