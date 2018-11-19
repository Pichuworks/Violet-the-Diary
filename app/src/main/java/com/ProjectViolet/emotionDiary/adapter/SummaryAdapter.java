package com.ProjectViolet.emotionDiary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ProjectViolet.emotionDiary.R;
import com.ProjectViolet.emotionDiary.utils.UserSummary;

import java.util.List;

public class SummaryAdapter extends ArrayAdapter {
    private final int resourceId;

    public SummaryAdapter(Context context, int textViewResourceId, List<UserSummary> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UserSummary userSummary = (UserSummary) getItem(position);                                  // 获取当前项的实例
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);               // 实例化一个对象
        TextView textSummaryName = (TextView) view.findViewById(R.id.textSummaryName);
        TextView textSummaryTop5 = (TextView) view.findViewById(R.id.textSummaryTop5);
        textSummaryName.setText(userSummary.getUsername());
        textSummaryTop5.setText(userSummary.getUserDiaryStatus());
        return view;
    }

}
