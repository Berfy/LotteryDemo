package cn.zcgames.lottery.country.view;

import java.util.Comparator;

import cn.zcgames.lottery.country.model.CountrySortModel;

/**
 * 国家排序器
 *
 * @author NorthStar
 * @date 2018/2/27 15:07
 */
public class CountryComparator implements Comparator<CountrySortModel> {

    @Override
    public int compare(CountrySortModel o1, CountrySortModel o2) {

        if (o1.sortLetters.equals("@") || o2.sortLetters.equals("#")) {
            return -1;
        } else if (o1.sortLetters.equals("#") || o2.sortLetters.equals("@")) {
            return 1;
        } else {
            return o1.sortLetters.compareTo(o2.sortLetters);
        }
    }

}
