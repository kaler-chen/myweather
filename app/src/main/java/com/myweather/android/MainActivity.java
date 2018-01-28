package com.myweather.android;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.widget.Toast;

import com.myweather.android.base.BaseActivity;
import com.myweather.android.db.City;
import com.myweather.android.db.County;
import com.myweather.android.db.Province;
import com.myweather.android.util.ConstUtil;
import com.myweather.android.util.LogUtil;
import com.myweather.android.util.MApplication;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String nowWeatherString = sharedPreferences.getString(ConstUtil.NOW_WEATHER, null);



        if (nowWeatherString != null){
            Intent intent = new Intent(this, WeatherActivity.class);
            intent.putExtra(ConstUtil.NOW_WEATHER,nowWeatherString);
            startActivity(intent);
            finish();
        }
    }

    /**
     * 加载城市信息.xls同步任务
     * Created by kaler-chen on 2018/1/24.
     * com.myweather.android.task
     */
    private void loadAreaInfo(){
        showProgressDialog("正在加载城市信息");
        Boolean result;
        InputStream is = null;
        try {
            is = MApplication.getContext().getAssets().open("thinkpage_cities.xls");
            HSSFWorkbook workbook = new HSSFWorkbook(is);
            loadProvinceInfo(workbook);     //加载省份信息
            loadCityInfo(workbook);         //加载地级市信息
//                loadCountyInfo(workbook);       //加载县信息
            result = true;
        } catch (Exception e) {
            LogUtil.e(TAG, "从EXCEL加载城市信息失败，" + e.getMessage(), e);
            result = false;
        } finally {
            if (is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        SharedPreferences.Editor editor =
                PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
        if (!result){
            editor.remove(ConstUtil.LOAD_AREAINFO_FLAG);
            Toast.makeText(MainActivity.this, "加载城市信息失败", Toast.LENGTH_SHORT).show();
        }else{
            editor.putString(ConstUtil.LOAD_AREAINFO_FLAG, "true");
        }
        editor.apply();
        closeProgressDialog();
    }

    protected void closeProgressDialog() {
        if (progressDialog != null){
            progressDialog.dismiss();
        }
    }

    protected void showProgressDialog(String msg) {
        if (msg == null){
            msg = "正在加载......";
        }
        if (progressDialog == null){
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(msg);
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
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


    /**
     * 加载城市信息.xls异步任务
     * Created by kaler-chen on 2018/1/21.
     * com.myweather.android.task
     */
    public class LoadAreaInfoTask extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
            showProgressDialog("正在加载城市信息");
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            closeProgressDialog();
            SharedPreferences.Editor editor =
                    PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
            if (!aBoolean){
                editor.remove(ConstUtil.LOAD_AREAINFO_FLAG);
                Toast.makeText(MainActivity.this, "加载城市信息失败", Toast.LENGTH_SHORT).show();
            }else{
                editor.putString(ConstUtil.LOAD_AREAINFO_FLAG, "true");
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
}
