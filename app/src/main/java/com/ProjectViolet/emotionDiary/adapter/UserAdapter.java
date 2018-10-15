package com.ProjectViolet.emotionDiary.adapter;




import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ProjectViolet.emotionDiary.R;
import com.ProjectViolet.emotionDiary.bean.User;

import java.util.List;

public class UserAdapter extends BaseAdapter {
    private Context mContext;
    private List<User> list;
    private int position;
    private View convertView;
    private ViewGroup parent;

    public UserAdapter(Context mContext, List<User> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        this.position = position;
        this.convertView = convertView;
        this.parent = parent;
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_user, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_username.setText(list.get(position).getUsername());
        holder.tv_name.setText(list.get(position).getName());
        holder.tv_sex.setText(list.get(position).getSex() == 1 ? "男" : "女");
        holder.tv_phone.setText(list.get(position).getPhone());
        return convertView;
    }

    private static class ViewHolder {
        public TextView tv_name;
        public TextView tv_username;
        public TextView tv_sex;
        public TextView tv_phone;

        public ViewHolder(View view) {
            this.tv_name = (TextView)view.findViewById(R.id.tv_name);
            this.tv_username = (TextView)view.findViewById(R.id.tv_username);
            this.tv_sex = (TextView)view.findViewById(R.id.tv_sex);
            this.tv_phone = (TextView)view.findViewById(R.id.tv_phone);
        }
    }
}
