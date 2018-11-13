package com.ProjectViolet.emotionDiary.ui;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ProjectViolet.emotionDiary.R;
import com.ProjectViolet.emotionDiary.bean.DiaryBean;
import com.ProjectViolet.emotionDiary.event.StartUpdateDiaryEvent;
import com.ProjectViolet.emotionDiary.utils.GetDate;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Text;

import android.os.Bundle;
import android.util.Log;

import static java.lang.Thread.sleep;


/**
 * Diary Adapter
 */
public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.DiaryViewHolder> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<DiaryBean> mDiaryBeanList;
    private int mEditPosition = -1;
    public static final int show = 0;

    private String baseURL = "http://47.100.0.222:2000/emotion/get?text=";
    private String result = "未进行心情判断";

    public DiaryAdapter(Context context, List<DiaryBean> mDiaryBeanList){
        mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mDiaryBeanList = mDiaryBeanList;
    }
    @Override
    public DiaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DiaryViewHolder(mLayoutInflater.inflate(R.layout.item_rv_diary, parent, false));
    }

    @Override
    public void onBindViewHolder(final DiaryViewHolder holder, final int position) {

        String dateSystem = GetDate.getDate().toString();
        if(mDiaryBeanList.get(position).getDate().equals(dateSystem)){
            holder.mIvCircle.setImageResource(R.drawable.circle_orange);
        }
        holder.mTvDate.setText(mDiaryBeanList.get(position).getDate());
        holder.mTvTitle.setText(mDiaryBeanList.get(position).getTitle());
        holder.mTvContent.setText("       " + mDiaryBeanList.get(position).getContent());
        holder.mIvEdit.setVisibility(View.INVISIBLE);
        if(mEditPosition == position){
            holder.mIvEdit.setVisibility(View.VISIBLE);
        }else {
            holder.mIvEdit.setVisibility(View.GONE);
        }

        // 自动刷新 又不是不能用 START
        // holder.manalyse.setVisibility(View.INVISIBLE);
        Log.d("manalyse","AutoFlash");

        String url = baseURL + mDiaryBeanList.get(position).getContent();
        url = url.replaceAll(" ", "%20");
        final HttpGet httpGet = new HttpGet(url);
        final HttpClient httpClient = new DefaultHttpClient();

        new Thread(new Runnable(){
            @Override
            public void run() {
                // 发送请求
                try
                {
                    HttpResponse response = httpClient.execute(httpGet);
                    result = showResponseResult(response);   // 显示返回结果
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).start();

        // 睡眠同步 又不是不能用
        try {
            sleep(100);
            holder.analyResDisp.setText(result);
            if(result != "未进行心情判断")
                holder.manalyse.setVisibility(View.INVISIBLE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



        // 自动刷新 又不是不能用 END


        holder.mLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.mIvEdit.getVisibility() == View.VISIBLE){
                    holder.mIvEdit.setVisibility(View.GONE);
                }else {
                    holder.mIvEdit.setVisibility(View.VISIBLE);
                }
                if(mEditPosition != position){
                    notifyItemChanged(mEditPosition);
                }
                mEditPosition = position;
            }
        });

        holder.mIvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new StartUpdateDiaryEvent(position));
            }
        });

        /***/
        holder.manalyse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.manalyse.setVisibility(View.INVISIBLE);
                Log.d("manalyse","setOnClickListener");

                String url = baseURL + mDiaryBeanList.get(position).getContent();
                url = url.replaceAll(" ", "%20");
                final HttpGet httpGet = new HttpGet(url);
                final HttpClient httpClient = new DefaultHttpClient();

                new Thread(new Runnable(){
                    @Override
                    public void run() {
                        // 发送请求
                        try
                        {
                            HttpResponse response = httpClient.execute(httpGet);
                            result = showResponseResult(response);   // 显示返回结果
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }).start();

                // 睡眠同步 又不是不能用
                try {
                    sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                holder.analyResDisp.setText(result);


                /**/
            }
        });
        /***/

    }

    private String showResponseResult(HttpResponse response)
    {
        if (null == response)
        {
            return "未进行心情判断";
        }

        HttpEntity httpEntity = response.getEntity();
        try
        {
            InputStream inputStream = httpEntity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inputStream));
            String result = "";
            String line = "";
            while (null != (line = reader.readLine()))
            {
                result += line;
            }
            System.out.println(result);
            Log.d("Analysis Result",result);
            return result;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int getItemCount() {
        return mDiaryBeanList.size();
    }

    public static class DiaryViewHolder extends RecyclerView.ViewHolder{

        TextView mTvDate;
        TextView mTvTitle;
        TextView mTvContent;
        TextView analyResDisp;
        ImageView mIvEdit;
        ImageView manalyse;

        LinearLayout mLlTitle;
        LinearLayout mLl;
        ImageView mIvCircle;
        LinearLayout mLlControl;
        RelativeLayout mRlEdit;

        DiaryViewHolder(View view){
            super(view);
            mIvCircle = (ImageView) view.findViewById(R.id.main_iv_circle);
            mTvDate = (TextView) view.findViewById(R.id.main_tv_date);
            mTvTitle = (TextView) view.findViewById(R.id.main_tv_title);
            mTvContent = (TextView) view.findViewById(R.id.main_tv_content);
            mIvEdit = (ImageView) view.findViewById(R.id.main_iv_edit);
            manalyse= (ImageView) view.findViewById(R.id.main_iv_analysis);
            analyResDisp = (TextView) view.findViewById(R.id.main_iv_analy_res);

            mLlTitle = (LinearLayout) view.findViewById(R.id.main_ll_title);
            mLl = (LinearLayout) view.findViewById(R.id.item_ll);
            mLlControl = (LinearLayout) view.findViewById(R.id.item_ll_control);
            mRlEdit = (RelativeLayout) view.findViewById(R.id.item_rl_edit);
        }
    }
}