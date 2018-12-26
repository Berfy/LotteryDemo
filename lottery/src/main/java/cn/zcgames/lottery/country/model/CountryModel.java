package cn.zcgames.lottery.country.model;

import android.graphics.drawable.Drawable;

/**
 * 国家区号数据
 * @author NorthStar
 * @date  2018/2/27 15:09
 */
public class CountryModel {
    // 国家名称
    public String countryName;

    // 国家区号
    public String countryNumber;

    public String simpleCountryNumber;

    // 国家名称缩写
    public String countrySortKey;

    // 国家图标
    public Drawable contactPhoto;

    public CountryModel(String countryName, String countryNumber, String countrySortKey) {
        super();
        this.countryName = countryName;
        this.countryNumber = countryNumber;
        this.countrySortKey = countrySortKey;
        if (countryNumber != null) {
            this.simpleCountryNumber = countryNumber.replaceAll("\\-|\\s", "");
        }
    }

}
