package com.sky31.buy.second_hand.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.sky31.buy.second_hand.context.BuyApp;
import com.sky31.buy.second_hand.context.values.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Caspar on 2015/7/17.
 */
public class GoodsData implements Parcelable{

    private BuyApp app;


    private static final String API_URL      = Constants.Apis.API_URL;
    private static final String KEY_PICNAME  = Constants.Keys.KEY_PICNAME;
    private static final String KEY_PICSRC   = Constants.Keys.KEY_PICSRC;
    //GoodsData
    //private ArrayList<GoodsData> data = app.getGoodsData();



    public int id,price,countImg;
    public String title,seller,dec,phone,qq,imgUrl,type,date;
    public List<String> imgUrlArray = new ArrayList<String>();

    public GoodsData() {

    }

    public GoodsData(int id
            , String title
            , String seller
            , String type
            , String dec
            , String phone
            , String qq
            , String imgUrl
            , List<String> imgUrlArray
            , int price
            , String date) {
        this.dec = dec;
        this.id = id;
        this.phone = phone;
        this.qq = qq;
        this.seller = seller;
        this.title = title;
        this.type = type;
        this.imgUrl = imgUrl;
        this.imgUrlArray = imgUrlArray;
        this.date = date;
        this.price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(seller);
        parcel.writeString(type);
        parcel.writeString(dec);
        parcel.writeString(phone);
        parcel.writeString(qq);
        parcel.writeString(imgUrl);
        parcel.writeStringList(imgUrlArray);
        parcel.writeInt(price);
        parcel.writeString(date);
    }

    public static final Creator<GoodsData> CREATOR = new Creator<GoodsData>() {
        @Override
        public GoodsData createFromParcel(Parcel parcel) {
            return new GoodsData(parcel);
        }

        @Override
        public GoodsData[] newArray(int i) {
            return new GoodsData[i];
        }
    };

    public GoodsData(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.seller = in.readString();
        this.type = in.readString();
        this.dec = in.readString();
        this.phone = in.readString();
        this.qq = in.readString();
        this.imgUrl = in.readString();

        in.readStringList(imgUrlArray);

        this.price = in.readInt();
        this.date = in.readString();
    }

    public static ArrayList<GoodsData> JSONArrayToGoodsData(JSONArray goodsData) {
        final ArrayList<GoodsData> mGoodsData = new ArrayList<>();
        String imgUrl;
        for (int i = 0; i < goodsData.length(); i++) {
            try {
                JSONObject good = goodsData.getJSONObject(i);
                JSONObject imgUrlObj = good.getJSONObject(KEY_PICNAME);
                Iterator x = imgUrlObj.keys();
                List<String> imgUrlArray = new ArrayList<String>();
                //int xi = 0;
                imgUrl = API_URL + good.getString(KEY_PICSRC);
                while (x.hasNext()) {
                    String key = (String) x.next();
                    imgUrlArray.add(imgUrlObj.getString(key));
                    //imgUrlArray.put(imgUrlObj.get(key));
                    System.out.println(key + ":" + imgUrlObj.get(key));
                    //xi++;
                }

                //imgUrl = "http://buy.sky31.com/fleaapi"+good.getString("picsrc")+"m_"+good.getJSONObject("picname").getString("1");
                mGoodsData.add(
                        new GoodsData(
                                good.getInt("id"),
                                good.getString("title"),
                                good.getString("seller"),
                                good.getString("type"),
                                good.getString("describe"),
                                good.getString("phone"),
                                good.getString("qq"),
                                imgUrl,
                                imgUrlArray,
                                good.getInt("price"),
                                good.getString("creat_time")
                        )
                );
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return mGoodsData;
    }


}
