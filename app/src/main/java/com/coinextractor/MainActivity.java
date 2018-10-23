package com.coinextractor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "CoinsExtractor";
    final int REFRESH_GAP = 2;

    private CoinsListViewAdapter adapter;
    private CoinList coinList = new CoinList();
    private CoinClient coinClient;

    //ListView Pagination
    private ListView coinsListView;
    private int previousTotal = 0;
    private int currentPage = 0;
    private int visibleThreshold = 11;
    private int firstVisibleIndex = 0;
    private boolean refreshThreadRunning = false;
    private boolean loading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setCoinClinet();
        setListView();
    }

    public void setCoinClinet() {
        coinClient = new CoinClient(this);
    }

    public void setListView() {
        coinsListView = findViewById(R.id.coinListView);
        adapter = new CoinsListViewAdapter(this, coinList.getCoins());
        coinsListView.setAdapter(adapter);
        coinsListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                firstVisibleIndex = firstVisibleItem;
                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                        currentPage++;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                    coinClient.fetchCoins(previousTotal);
                    loading = true;
                }
            }
        });
    }

    public void updateView(ArrayList<Coin> coins) {
        coinList.updateCoinsList(coins);
        adapter.notifyDataSetChanged();
        if (!refreshThreadRunning) {
            refreshThreadRunning = true;
            startRefreshThread();
        }
    }



    public void startRefreshThread() {
        ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);

        // This schedule a runnable task every 2 minutes
        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            public void run() {

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!loading) {
                            coinClient.fetchCoins(firstVisibleIndex);
                        }
                    }
                });
            }
        }, REFRESH_GAP, REFRESH_GAP, TimeUnit.MINUTES);
    }


}
