package com.bestv.vrcinema;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bestv.vrcinema.adapter.SearchRecordsAdapter;
import com.bestv.vrcinema.adapter.SearchResultsAdapter;
import com.bestv.vrcinema.constant.ConstantInterface;
import com.bestv.vrcinema.httpReport.HttpReporterBuilder;
import com.bestv.vrcinema.httpReport.HttpReporterImpl;
import com.bestv.vrcinema.httpUtil.HttpGetTask;
import com.bestv.vrcinema.httpUtil.OnAPITaskDoneListener;
import com.bestv.vrcinema.httpUtil.OnSearchTaskDoneListener;
import com.bestv.vrcinema.model.MovieInfo;
import com.bestv.vrcinema.model.RecommendInfoSingleton;
import com.bestv.vrcinema.model.SearchResultInfo;
import com.bestv.vrcinema.util.GoogleUnityActivityHelper;
import com.bestv.vrcinema.util.NetWorkUtils;
import com.bestv.vrcinema.util.PermissionUtil;
import com.umeng.analytics.MobclickAgent;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by xujunyang on 17/1/4.
 */

public class SearchContentActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText searchContentEt;
    private ImageButton searchBack;
    private TextView searchDone;
    private ImageButton clearResult;
    private SearchRecordsAdapter recordsAdapter;
    private View recordsHistoryView;
    private ListView recordsList;
    private TextView clearAllRecords;

    private View searchResultView;
    private RecyclerView recyclerView;
    private SearchResultsAdapter resultsAdapter;

    private ProgressBar progressBar;


    private LinearLayout searchRecordsLl;
    private LinearLayout searchResultLl;
    private List<String> searchRecordsList;
    private List<String> tempList;
    private SearchRecordsManager recordsManager;

    private SearchResultInfo searchResultInfo;

    private String searchKeyword = "";

    public static void actionStart(Context context, String subTitle){
        Intent intent = new Intent(context, SearchContentActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().setAttributes(params);

        setContentView(R.layout.activity_search);

        initView();
        initData();
        bindAdapter();
        initListener();


        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
           public void run()
           {
               InputMethodManager inputManager =
                       (InputMethodManager)searchContentEt.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
               inputManager.showSoftInput(searchContentEt, 0);
           }}, 1000);
    }

    // 基于友盟的session的统计
    @Override
    protected void onResume(){
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause(){
        super.onPause();
        MobclickAgent.onPause(this);
    }

    private void initView(){
        initRecordsView();
        initResultView();
        searchRecordsLl = (LinearLayout)findViewById(R.id.search_content_show_ll);
        searchResultLl = (LinearLayout)findViewById(R.id.search_result_show_ll);
        searchContentEt = (EditText)findViewById(R.id.input_search_content_et);
        searchBack = (ImageButton)findViewById(R.id.search_back);
        searchDone = (TextView)findViewById(R.id.search_done);
        clearResult = (ImageButton) findViewById(R.id.clear_result);

        progressBar = (ProgressBar)findViewById(R.id.search_progressbar);
        progressBar.setVisibility(View.GONE);

        //添加搜索view
        searchRecordsLl.addView(recordsHistoryView);
        searchResultLl.addView(searchResultView);
        searchResultLl.setVisibility(View.GONE);
    }

    private  void initRecordsView(){
        recordsHistoryView = LayoutInflater.from(this).inflate(R.layout.search_records_list, null);
        //显示历史记录lv
        recordsList = (ListView)recordsHistoryView.findViewById(R.id.search_records_lv);
        clearAllRecords = (TextView)recordsHistoryView.findViewById(R.id.clear_all_records_tv);
    }

    private void initResultView(){
        searchResultView = LayoutInflater.from(this).inflate(R.layout.search_result_list, null);
        //显示历史记录lv
        recyclerView = (RecyclerView) searchResultView.findViewById(R.id.search_result_rv);
    }


    private void initData(){
        String searchHint = RecommendInfoSingleton.getInstance().getSearchMessage();
        if (!!TextUtils.isEmpty(searchHint)){
            searchContentEt.setHint(searchHint);
        }

        searchResultInfo = new SearchResultInfo();
        recordsManager = new SearchRecordsManager(this);
        searchRecordsList = new ArrayList<>();
        tempList = new ArrayList<>();
        tempList.addAll(recordsManager.getRecordsList());
        reversedList();
        //第一次进入时,判断数据库中是否有历史记录,没有则不显示
        checkRecordsSize();
    }

    private void reversedList(){
        searchRecordsList.clear();
        for(int i = tempList.size() - 1; i >= 0; i--){
            searchRecordsList.add(tempList.get(i));
        }
    }

    private void checkRecordsSize(){
        if(searchRecordsList.size() == 0){
            searchRecordsLl.setVisibility(View.GONE);
        }else{
            searchRecordsLl.setVisibility(View.VISIBLE);
        }
    }

    private void bindAdapter(){
        recordsAdapter = new SearchRecordsAdapter(this, searchRecordsList);
        recordsList.setAdapter(recordsAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        resultsAdapter = new SearchResultsAdapter(SearchContentActivity.this, searchResultInfo.getMovieInfos());
        recyclerView.setAdapter(resultsAdapter);
    }

    private void initListener(){
        clearAllRecords.setOnClickListener(this);
        searchBack.setOnClickListener(this);
        searchDone.setOnClickListener(this);
        clearResult.setOnClickListener(this);

        searchContentEt.setOnEditorActionListener(new TextView.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    if (searchContentEt.getText().toString().length() > 0) {
                        addSearchRecord(searchContentEt.getText().toString());

                        // 执行搜索
                        searchByKeyword();
                    }
                }
                return false;
            }
        });

        //根据输入的信息去模糊搜索
        searchContentEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                clearAndHideSearchResultLl();
            }
        });

        // 历史记录点击事件
        recordsList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                //将获取到的字符串传到搜索输入框
                TextView textView = (TextView) view.findViewById(R.id.search_content_tv);
                String name = textView.getText().toString();
                searchContentEt.setText(name);
                searchContentEt.setSelection(name.length());
            }
        });
    }

    private void addSearchRecord(String record){
        // 判断数据库中是否存在该记录
        if(!recordsManager.isHasRecord(record)){
            tempList.add(record);
            recordsManager.addRecord(record);
            reversedList();
            checkRecordsSize();
            recordsAdapter.notifyDataSetChanged();
        }
    }

    public void initSearchResult(SearchResultInfo info){
        ArrayList<MovieInfo> temp = info.getMovieInfos();
        searchResultLl.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        TextView searchKeyword = (TextView)findViewById(R.id.search_keyword);
        searchKeyword.setText(searchContentEt.getText().toString());
        if(temp.size() > 0){
            TextView searchDesc = (TextView)findViewById(R.id.search_desc);
            searchDesc.setText("的精彩内容");
        }else{
            TextView searchDesc = (TextView)findViewById(R.id.search_desc);
            searchDesc.setText("未搜索到相关内容");
        }

        for(MovieInfo movieInfo : temp){
            searchResultInfo.addMovieInfo(movieInfo);
        }
        if(temp.size()>0){
            resultsAdapter.notifyDataSetChanged();
        }
        Log.d("SearchContentActivity", "search ret: " + temp.size());

    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.search_back:
                finish();
                break;
            case R.id.search_done:
                if (searchContentEt.getText().toString().length() <= 0){
                    Toast.makeText(SearchContentActivity.this, "搜索内容不能为空", Toast.LENGTH_SHORT).show();
                    break;
                }

                addSearchRecord(searchContentEt.getText().toString());

                if(!NetWorkUtils.isNetworkConnected(SearchContentActivity.this)){
                    Toast.makeText(SearchContentActivity.this, "请检查网络连接后重试", Toast.LENGTH_SHORT).show();
                    break;
                }

                searchByKeyword();

                break;
            case R.id.clear_result:
                searchContentEt.setText("");
                clearAndHideSearchResultLl();
                break;
            case R.id.clear_all_records_tv:
                tempList.clear();
                reversedList();
                recordsManager.deleteAllRecords();
                recordsAdapter.notifyDataSetChanged();
                searchRecordsLl.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    private void searchByKeyword(){
        String tempKeyword = searchContentEt.getText().toString();
        if(!searchKeyword.equals(tempKeyword)){
            searchRecordsLl.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

            searchKeyword = tempKeyword;
            new HttpGetTask(new OnSearchTaskDoneListener(SearchContentActivity.this)).execute(ConstantInterface.SEARCHURL
                    +"&keyword="+searchKeyword+"&version="+ConstantInterface.VERSION);

            try{
                String report = HttpReporterBuilder.buildTemporaryReport(HttpReporterBuilder.search, URLEncoder.encode(searchKeyword, "utf-8"));
                HttpReporterImpl.getInstance().reportSth(report);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    private void clearAndHideSearchResultLl(){
        if(searchResultLl.getVisibility() == View.VISIBLE){
            while(searchResultInfo.getItemsCount()>0){
                searchResultInfo.removeAllItem();
                resultsAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(0);
            }
        }
        searchKeyword = "";
        searchRecordsLl.setVisibility(View.VISIBLE);
        searchResultLl.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ConstantInterface.REQUEST_CODE_WRITE_SETTINGS) {
            if (PermissionUtil.checkWriteSettingsPermission(SearchContentActivity.this)) {
                //检查返回结果
                Toast.makeText(this, "WRITE_SETTINGS permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "WRITE_SETTINGS permission not granted", Toast.LENGTH_SHORT).show();
            }

            GoogleUnityActivityHelper.startCardboard(SearchContentActivity.this);
        }
    }
}
