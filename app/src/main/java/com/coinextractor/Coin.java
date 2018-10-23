package com.coinextractor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Coin {

    private String id;
    private String name;
    private String price;
    private String volume24h;
    //Trend determines weather the price compared to previous price is higher or lower (In order to set background color)
    private int trend; //0,1,-1


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getVolume24h() {
        return volume24h;
    }

    public void setVolume24h(String volume24h) {
        this.volume24h = volume24h;
    }

    public static ArrayList<Coin> getCoins(JSONArray jsonArray) {
        ArrayList<Coin> coins = new ArrayList<Coin>(jsonArray.length());
        // Process each result in json array, decode and convert to business
        // object
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject coinJson = null;
            try {
                coinJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            Coin coin = Coin.getCoin(coinJson);
            if (coin != null) {
                coins.add(coin);
            }
        }
        return coins;
    }

    public static Coin getCoin(JSONObject jsonObject) {
        Coin coin = new Coin();
        try {
            if (jsonObject.has("id")) {
                coin.setId(jsonObject.getString("id"));
            }
            if (jsonObject.has("name")) {
                coin.setName(jsonObject.getString("name"));
            }
            if (jsonObject.has("quotes")) {
                JSONObject quotes = jsonObject.getJSONObject("quotes");
                if (quotes.has("USD")) {
                    JSONObject USD = quotes.getJSONObject("USD");
                    if (USD.has("volume_24h")) {
                        coin.setVolume24h(USD.getString("volume_24h"));
                    }
                    if (USD.has("price")) {
                        coin.setPrice(USD.getString("price"));
                    }
                }
            }
        }catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return coin;
    }

    public int getTrend() {
        return trend;
    }

    public void updateCoin(Coin coin) {
        if (this.getId().equals(coin.getId())) {
            if (Double.parseDouble(this.getPrice()) < Double.parseDouble(coin.getPrice())) {
                this.trend = 1;
            }
            if (Double.parseDouble(this.getPrice()) > Double.parseDouble(coin.getPrice())) {
                this.trend = -1;
            }
            if (this.getPrice().equals(coin.getPrice())) {
                this.trend = 0;
            }
            this.setPrice(coin.getPrice());
            this.setVolume24h(coin.getVolume24h());
        }
    }


}
