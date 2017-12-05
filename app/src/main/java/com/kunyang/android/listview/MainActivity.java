package com.kunyang.android.listview;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Vector;

public class MainActivity extends AppCompatActivity implements AbsListView.OnScrollListener{

    private ListView mListView;
    private Vector<News> news=new Vector<>();
    private MyAdapter mAdapter;
    private static final int DATA_UPDATE=0x1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView=(ListView)this.findViewById(R.id.listView);
        mListView.setOnScrollListener(this);
        View footerView=getLayoutInflater().inflate(R.layout.loading,null);
        mListView.addFooterView(footerView);
        initData();
        mAdapter=new MyAdapter();
        mListView.setAdapter(mAdapter);
    }

    private int index=0;

    private void initData(){
        for (int i=0;i<10;i++){
            News n= new News();
            n.title="title--"+index;
            n.content="content--"+index;
            index++;
            news.add(n);
        }
    }

    private int visibleLastIndex;
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (mAdapter.getCount()==visibleLastIndex &&scrollState== AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
            new LoadDataThread().start();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        visibleLastIndex=firstVisibleItem+visibleItemCount-1;
    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case DATA_UPDATE:
                    mAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    class LoadDataThread extends Thread{
        @Override
        public void run() {
            initData();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //mAdapter.notifyDataSetChanged();
            handler.sendEmptyMessage(DATA_UPDATE);
        }
    }

    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return news.size();
        }

        @Override
        public Object getItem(int i) {
            return news.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder vh;
            if (view==null){
                view=getLayoutInflater().inflate(R.layout.list_item,null);
                vh=new ViewHolder();
                vh.tv_title=(TextView)view.findViewById(R.id.textView_title);
                vh.tv_content=(TextView)view.findViewById(R.id.textView_content);
                view.setTag(vh);
            }else {
                vh= (ViewHolder) view.getTag();
            }
            News n=news.get(i);
            vh.tv_title.setText(n.title);
            vh.tv_content.setText(n.content);
            return view;
        }

        class ViewHolder{
            TextView tv_title;
            TextView tv_content;
        }
    }
}
