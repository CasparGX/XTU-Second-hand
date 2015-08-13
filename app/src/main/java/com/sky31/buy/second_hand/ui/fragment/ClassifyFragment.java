package com.sky31.buy.second_hand.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sky31.buy.second_hand.R;
import com.sky31.buy.second_hand.context.values.Constants;
import com.sky31.buy.second_hand.model.ClassifyInfo;
import com.sky31.buy.second_hand.model.GoodsData;
import com.sky31.buy.second_hand.ui.adapter.ClassifyFragmentGridViewAdapter;
import com.sky31.buy.second_hand.ui.adapter.HomeFragmentListViewAdapter;
import com.sky31.buy.second_hand.util.HttpUtil;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.MaterialHeader;
import in.srain.cube.views.ptr.util.PtrLocalDisplay;

public class ClassifyFragment extends Fragment{

    private String TAG = ClassifyFragment.class.getName();


    /*������Ϣ*/
    private ArrayList<ClassifyInfo> mClassifyInfo = new ArrayList<>();

    /*������Ϣ����*/
    private GridView mGvClassify;
    private ClassifyFragmentGridViewAdapter adapter;

    /*����ˢ�²���*/
    private PtrFrameLayout ptrFrame;

    /*��Ʒ�б�*/
    private ListView mListView;
    private HomeFragmentListViewAdapter mListViewAdapter;

    /*����*/
    private EditText mEtSearch;
    private Button mBtnSearch;

    /*��ȡ��Ʒ��Ϣ*/
    private ArrayList<GoodsData> mGoodsData = new ArrayList<>();
    private JSONArray mGoodsArray = new JSONArray();
    private RequestParams params = new RequestParams();
    private int limitID;
    private String queryUrl;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_classify, container, false);
        /*��Ʒ�б���*/
        mListView = (ListView) view.findViewById(R.id.lv_classify);
        View lvHeader = inflater.inflate(R.layout.include_lv_classify_header, null);
        mListView.addHeaderView(lvHeader);
        mListViewAdapter = new HomeFragmentListViewAdapter(inflater);
        mListViewAdapter.setmGoodsData(mGoodsData);
        mListView.setAdapter(mListViewAdapter);

        /*������*/
        mEtSearch = (EditText) lvHeader.findViewById(R.id.et_search);
        mBtnSearch = (Button) lvHeader.findViewById(R.id.btn_search);

        /*���಼��*/
        mGvClassify = (GridView) lvHeader.findViewById(R.id.gv_classify);
        adapter = new ClassifyFragmentGridViewAdapter(inflater, mClassifyInfo);

        /*����GridView����¼�*/
        mGvClassify.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //��ȡ���id�����ݲ���
                //ÿ�ε����limitID��0
                if (params.has("limitID")) {
                    params.remove("limitID");
                }
                limitID = 0;
                //params.add("limitID", String.valueOf(limitID));
                getGoodsData(null, i);
            }
        });
        mGvClassify.setAdapter(adapter);



        //����ˢ��
        final MaterialHeader header = new MaterialHeader(getActivity());
        ptrFrame = (PtrFrameLayout) view.findViewById(R.id.ptr_classify_grid);

        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
        header.setPadding(0, PtrLocalDisplay.dp2px(15), 0, PtrLocalDisplay.dp2px(10));
        header.setPtrFrameLayout(ptrFrame);
        ptrFrame.setPinContent(true);
        ptrFrame.setLoadingMinTime(1000);
        ptrFrame.setDurationToCloseHeader(1500);
        ptrFrame.setHeaderView(header);
        ptrFrame.addPtrUIHandler(header);
        ptrFrame.postDelayed(new Runnable() {
            @Override
            public void run() {
                ptrFrame.autoRefresh(true);
            }
        }, 100);
        ptrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mListView, header);
            }
            @Override
            public void onRefreshBegin(final PtrFrameLayout frame) {
                getClassify();
                Log.i(TAG, "-------- onRefreshBegin : ˢ�� - ������������ ---------");
            }
        });


        return view;
    }

    /*���ݲ����ϳɲ�ѯURL*/
    public void getGoodsData(String title, int id) {
        if (title != null){
        params.add("title", title);
        }
        if (id != -1) {
            if (params.has("type")) {
                params.remove("type");
            }
            params.add("type", mClassifyInfo.get(id).getId());
        }
        params.add("limitID", String.valueOf(limitID));
        System.out.println(params.toString() + " id = " + id);
        HttpUtil.get(Constants.Apis.API_GOODS_LIST_GET, params, mListJsonHttpResponseHandler);
        Log.i(TAG, "------------getGoodsData URL:" + Constants.Apis.API_GOODS_LIST_GET + "------------------");
    }

    /*��ȡ��Ʒ�б��Handler*/
    JsonHttpResponseHandler mListJsonHttpResponseHandler = new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
            super.onSuccess(statusCode, headers, response);
            Log.i(TAG, "------------------------------ getGoodsData Handler onSuccess ----------------------");
            Log.i(TAG, response + "");
            //��������,ˢ����ʾ
            mGoodsData.clear();
            mGoodsArray = response;
            mGoodsData.addAll(GoodsData.JSONArrayToGoodsData(mGoodsArray));
            mListViewAdapter.notifyDataSetChanged();

            limitID++;
            Log.i(TAG, mGoodsData.get(0).imgUrl + "");
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            //if return JSONObject, it's have no data
            Toast.makeText(getActivity(), "�Ѽ���ȫ��",
                    Toast.LENGTH_SHORT).show();
            Log.i(TAG, "---------------------- return JSONObject, it's have no data ------------------");
            Log.i(TAG, response + "");
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
            Log.e(TAG, " onFailure" + responseString.toString());
        }

        public void onFinish() {
            Log.i(TAG, "onFinish");
        }
    };



    /*��ȡ������Ϣ*/
    private void getClassify() {
        Log.i(TAG, "------------getClassify URL:" + Constants.Apis.API_GOODS_LIST_GET + "------------------");
        HttpUtil.get(Constants.Apis.API_GOODS_CLASSIFY_GET, null, mClassifyJsonHttpResponseHandler);
    }

    /*��ȡ������Ϣ��Handler*/
    JsonHttpResponseHandler mClassifyJsonHttpResponseHandler = new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
            super.onSuccess(statusCode, headers, response);
            Toast.makeText(getActivity(), "���ط�����Ϣ����",
                    Toast.LENGTH_SHORT).show();
            Log.i(TAG, "---------------getClassify Error: response JSONArray, need JSONObject----------------");
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            Log.i(TAG, "------------------------------ getClassify Handler onSuccess ----------------------");
            mClassifyInfo.clear();
            System.out.println(response);
            JsonToClassifyInfo(response);
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
            Log.e(TAG, " onFailure" + responseString.toString());
        }

        public void onFinish() {
            ptrFrame.refreshComplete();
            Log.i(TAG, "onFinish");
        }
    };

    /**
     * ����ȡ��JSONObjectתΪ�Զ����ClassifyInfo����
     * @param mJsonObject
     */
    public void JsonToClassifyInfo(JSONObject mJsonObject) {
        String key;
        int i = 0;
        for (Iterator iter = mJsonObject.keys(); iter.hasNext();) { //�ȱ������� people ����
            key = (String)iter.next();
            try {
                ClassifyInfo mObjClassifyInfo = new ClassifyInfo();
                System.out.println(key + "#" + mJsonObject.getString(key));
                mObjClassifyInfo.setTitle(mJsonObject.getString(key));
                mObjClassifyInfo.setId(key);
                mObjClassifyInfo.setIcon(null);
                this.mClassifyInfo.add(mObjClassifyInfo);

                System.out.println(this.mClassifyInfo.get(i).getId() + " # " + this.mClassifyInfo.get(i).getTitle() + " # " + i + " # " + this.mClassifyInfo.size());
                i++;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}