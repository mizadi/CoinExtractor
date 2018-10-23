package com.coinextractor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CoinsListViewAdapter extends ArrayAdapter<Coin> {

    ArrayList<Coin> coins;

    public CoinsListViewAdapter(Context context, ArrayList<Coin> coinsList) {
        super(context,0 , coinsList);
        this.coins = coinsList;
    }

    @Override
    public int getCount() {
        return coins.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.coin_list_item, parent, false);
        }
        Coin coin = coins.get(position);
        TextView name = convertView.findViewById(R.id.coin_name);
        name.setText(coin.getName());
        TextView price = convertView.findViewById(R.id.coin_price);
        price.setText(coin.getPrice());
        TextView volume = convertView.findViewById(R.id.coin_volume);
        volume.setText(coin.getVolume24h());
        checkTrend(coin,price);
        return convertView;
    }

    public void checkTrend(Coin coin, TextView price) {
        int trend = coin.getTrend();
        if (trend > 0) {
            price.setTextColor(getContext().getResources().getColor(R.color.green));
        } else if( trend < 0) {
            price.setTextColor(getContext().getResources().getColor(R.color.red));
        } else if( trend == 0) {
            price.setTextColor(getContext().getResources().getColor(R.color.black));
        }
    }
}