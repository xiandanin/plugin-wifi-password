package com.dyhdyh.plugin.wifipwd;

import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * @author dengyuhan
 *         created 2018/5/21 17:43
 */
public class WiFiInfoAdapter extends BaseAdapter {
    private List<WiFiInfo> mData;

    public WiFiInfoAdapter(List<WiFiInfo> data) {
        this.mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public WiFiInfo getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holer;
        if (convertView == null) {
            holer = new ViewHolder(parent);
            convertView = holer.itemView;
            convertView.setTag(holer);
        } else {
            holer = (ViewHolder) convertView.getTag();
        }
        final WiFiInfo item = mData.get(position);/*
        holer.tv_ssid.setText(String.format("名称   %s",item.getSsid()));
        holer.tv_pwd.setText(String.format("密码   %s",item.getPassword()));*/
        holer.tv_ssid.setText(item.getSsid());
        holer.tv_pwd.setText(item.getPassword());
        holer.tv_cp_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cm = (ClipboardManager) v.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(item.getSsid() + "\n" + item.getPassword());
                Toast.makeText(v.getContext(), item.getSsid()+" 复制成功", Toast.LENGTH_SHORT).show();
            }
        });
        holer.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cm = (ClipboardManager) v.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(item.getPassword());
                Toast.makeText(v.getContext(), "密码复制成功", Toast.LENGTH_SHORT).show();
            }
        });
        return holer.itemView;
    }


    public static class ViewHolder {
        View itemView;
        TextView tv_ssid;
        TextView tv_pwd;
        TextView tv_cp_pwd;

        public ViewHolder(ViewGroup parent) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wifi_info, parent, false);
            tv_ssid = itemView.findViewById(R.id.tv_ssid);
            tv_pwd = itemView.findViewById(R.id.tv_pwd);
            tv_cp_pwd = itemView.findViewById(R.id.tv_cp_pwd);

        }
    }


}
