package cn.zcgames.lottery.country.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.zcgames.lottery.R;
import cn.zcgames.lottery.app.AppConstants;
import cn.zcgames.lottery.app.MainActivity;
import cn.zcgames.lottery.base.BaseActivity;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.country.model.CountrySortModel;
import cn.zcgames.lottery.country.util.CharacterParserUtil;


/**
 * 国家区号选择界面
 *
 * @author NorthStar
 * @date 2018/2/27 14:46
 */
public class CountryActivity extends BaseActivity {

    String TAG = "CountryActivity";

    private List<CountrySortModel> mAllCountryList;

    private EditText country_edt_search;

    private ListView country_lv_countryList;

    private ImageView country_iv_clearText;

    private CountrySortAdapter adapter;

    private SideBar sideBar;

    private TextView dialog;

    private CountryComparator pinyinComparator;

    private GetCountryNameSort countryChangeUtil;

    private CharacterParserUtil characterParserUtil;

    /**
     * 跳转回调
     *
     * @param context
     */
    public static void launch(Activity context) {
        Intent intent = new Intent(context, CountryActivity.class);
        context.startActivityForResult(intent, AppConstants.REQUEST_COUNTRY_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UIHelper.hideSystemTitleBar(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country);
        initView();
    }

    public void initView() {
        setTitleBar();
        setListener();
        getCountryList();
    }

    private void setTitleBar() {
        TextView titleTV = findViewById(R.id.title_tv);
        titleTV.setText(getString(R.string.select_country_zone));
        UIHelper.showWidget(findViewById(R.id.title_back), true);
        findViewById(R.id.title_back).setOnClickListener(view -> goBack(CountryActivity.this));
        initDV();
    }

    private void initDV() {
        country_edt_search = findViewById(R.id.country_et_search);
        country_lv_countryList = findViewById(R.id.country_lv_list);
        country_iv_clearText = findViewById(R.id.country_iv_cleartext);

        dialog = findViewById(R.id.country_dialog);
        sideBar = findViewById(R.id.country_sidebar);
        sideBar.setTextView(dialog);

        mAllCountryList = new ArrayList<>();
        pinyinComparator = new CountryComparator();
        countryChangeUtil = new GetCountryNameSort();
        characterParserUtil = new CharacterParserUtil();

        //将国家区号进行排序，按照A~Z的顺序
        Collections.sort(mAllCountryList, pinyinComparator);
        adapter = new CountrySortAdapter(this, mAllCountryList);
        country_lv_countryList.setAdapter(adapter);
    }

    private void setListener() {
        country_iv_clearText.setOnClickListener(v -> {
            country_edt_search.setText("");
            Collections.sort(mAllCountryList, pinyinComparator);
            adapter.updateListView(mAllCountryList);
        });

        country_edt_search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchContent = country_edt_search.getText().toString();
                boolean isEmpty = TextUtils.isEmpty(searchContent);
                country_iv_clearText.setVisibility(isEmpty ? View.INVISIBLE : View.VISIBLE);
                if (searchContent.length() > 0) {
                    // 按照输入内容进行匹配
                    ArrayList<CountrySortModel> filterList = (ArrayList<CountrySortModel>) countryChangeUtil
                            .search(searchContent, mAllCountryList);

                    adapter.updateListView(filterList);
                } else {
                    adapter.updateListView(mAllCountryList);
                }
                country_lv_countryList.setSelection(0);
            }
        });

        //右侧sideBar监听
        sideBar.setOnTouchingLetterChangedListener(s -> {
            //该字母首次出现的位置
            int position = adapter.getPositionForSection(s.charAt(0));
            if (position != -1) {
                country_lv_countryList.setSelection(position);
            }
        });

        country_lv_countryList.setOnItemClickListener((adapterView, view, position, arg3) -> {
            String countryName;
            String countryNumber;
            String searchContent = country_edt_search.getText().toString();

            if (searchContent.length() > 0) {
                //按照输入内容进行匹配
                ArrayList<CountrySortModel> filterList = (ArrayList<CountrySortModel>) countryChangeUtil
                        .search(searchContent, mAllCountryList);
                countryName = filterList.get(position).countryName;
                countryNumber = filterList.get(position).countryNumber;
            } else {
                //点击后返回
                countryName = mAllCountryList.get(position).countryName;
                countryNumber = mAllCountryList.get(position).countryNumber;
            }
            setSelectResult(countryName, countryNumber);
        });
    }

    //回传选择结果
    private void setSelectResult(String countryName, String countryNumber) {
        Intent intent = new Intent();
        intent.setClass(CountryActivity.this, MainActivity.class);
        intent.putExtra("countryName", countryName);
        intent.putExtra("countryNumber", countryNumber);
        setResult(RESULT_OK, intent);
        Log.e(TAG, "countryName: + " + countryName + "countryNumber: " + countryNumber);
        finish();
    }

    /**
     * 获取国家列表
     */
    private void getCountryList() {
        String[] countryList = getResources().getStringArray(R.array.country_code_list_ch);

        for (String aCountryList : countryList) {
            String[] country = aCountryList.split("\\*");

            String countryName = country[0];
            String countryNumber = country[1];
            String countrySortKey = characterParserUtil.getSelling(countryName);
            CountrySortModel countrySortModel = new CountrySortModel(countryName, countryNumber,
                    countrySortKey);
            String sortLetter = countryChangeUtil.getSortLetterBySortKey(countrySortKey);
            if (sortLetter == null) {
                sortLetter = countryChangeUtil.getSortLetterBySortKey(countryName);
            }

            countrySortModel.sortLetters = sortLetter;
            mAllCountryList.add(countrySortModel);
        }

        Collections.sort(mAllCountryList, pinyinComparator);
        adapter.updateListView(mAllCountryList);
        Log.e(TAG, "mAllCountryList.size()" + mAllCountryList.size());
    }
}
