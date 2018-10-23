package com.coinextractor;

import java.util.ArrayList;
import java.util.HashMap;

public class CoinList {
    HashMap<String,Coin> coins;
    ArrayList<Coin> coinsList;

    public CoinList() {
        this.coins = new HashMap<>();
        this.coinsList = new ArrayList<>();
    }

    public ArrayList<Coin> getCoins() {
        return coinsList;
    }

    public void updateCoinsList(ArrayList<Coin> coinsList) {
        for (Coin coin : coinsList) {
            if (this.coins.containsKey(coin.getId())) {
                // We found the coin in the hash map
                this.coins.get(coin.getId()).updateCoin(coin);
            } else {
                // Coin was not found and need to be added to the hashmap
                this.coins.put(coin.getId(),coin);
                this.coinsList.add(coin);
            }
        }
    }
}
