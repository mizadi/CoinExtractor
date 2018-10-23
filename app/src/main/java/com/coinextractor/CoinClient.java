package com.coinextractor;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import cz.msebera.android.httpclient.Header;

public class CoinClient {
    private static final String API_BASE_URL = "https://api.coinmarketcap.com/v2/ticker/";
    private final String NUMBER_OF_ITEMS = "10";
    private AsyncHttpClient client;
    private Context context;



    public CoinClient(Context context) {
        this.client = new AsyncHttpClient();
        this.context = context;
    }

    public void getCoins(String startIndex,JsonHttpResponseHandler handler) {
            client.get(API_BASE_URL+"?start="+startIndex+"&limit="+NUMBER_OF_ITEMS, handler);
    }

    public void fetchCoins(int startIndex) {
        getCoins(Integer.toString(startIndex),new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray coinsJsonArray = null;
                    JSONObject coinsJsonObject = null;
                    if (response != null) {
                        coinsJsonObject = response.getJSONObject("data");
                        Iterator iteratorObj = coinsJsonObject.keys();
                        coinsJsonArray = new JSONArray();
                        while (iteratorObj.hasNext()) {
                            String key = (String) iteratorObj.next();
                            coinsJsonArray.put(coinsJsonObject.getJSONObject(key));

                        }
                        ArrayList<Coin> coins = Coin.getCoins(coinsJsonArray);
                        ((MainActivity)context).updateView(coins);

                    }
                }catch (JSONException e) {
                    Log.e(MainActivity.TAG,e.toString());
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
                Log.d(MainActivity.TAG,s);
            }
        });
    }
}
