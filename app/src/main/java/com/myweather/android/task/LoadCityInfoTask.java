package com.myweather.android.task;

import android.content.res.AssetManager;
import android.os.AsyncTask;

import com.myweather.android.db.CityInfo;
import com.myweather.android.util.MApplication;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;

/**
 * 加载城市信息.xls异步任务
 * Created by kaler-chen on 2018/1/21.
 * com.myweather.android.task
 */

public class LoadCityInfoTask extends AsyncTask<String, Integer, Boolean> {
    @Override
    protected void onPreExecute() {

    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        try {
            InputStream is = MApplication.getContext().getAssets().open("thinkpage_cities.xls");
            HSSFWorkbook workbook = new HSSFWorkbook(is);
            HSSFSheet sheet = workbook.getSheet("china_cities");
            int firstRowNum = 2;
            int lastRowNum = sheet.getLastRowNum();
            HSSFRow row;
            CityInfo cityInfo;
            for (int i  = firstRowNum; i <= lastRowNum; i++){
                row = sheet.getRow(i);
                cityInfo = new CityInfo();
                cityInfo.setCityId(row.getCell(1).getStringCellValue());
                cityInfo.setCityName(row.getCell(2).getStringCellValue());
                cityInfo.setCityEnName(row.getCell(3).getStringCellValue());
                cityInfo.setCountryName(row.getCell(4).getStringCellValue());
                cityInfo.setCountryCode(row.getCell(5).getStringCellValue());
                cityInfo.setFirstLevelCityName(row.getCell(6).getStringCellValue());
                cityInfo.setFirstLevelCityEnName(row.getCell(7).getStringCellValue());
                cityInfo.setSecondLevelCityName(row.getCell(8).getStringCellValue());
                cityInfo.setSecondLevelCityEnName(row.getCell(9).getStringCellValue());
                cityInfo.setTimeZone(row.getCell(10).getStringCellValue());
                cityInfo.setCityLevel(row.getCell(11).getStringCellValue());
                cityInfo.save();
            }
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
