package com.xaqb.hotel.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xaqb.hotel.R;
import com.xaqb.hotel.Utils.Globals;
import com.xaqb.hotel.Utils.SPUtils;


/**
 * Created by fule on 2016/11/25.
 */
public class LeftMenuListAdapter extends BaseAdapter {
    private Context context;
    private String status = "";
    private String[] leftMenuTitles = {
            "修改密码","版本更新", "线索信息", "联合检查"
    };
    private int[] icons = {
            R.mipmap.more_icon_password,
            R.mipmap.more_icon_update,
            R.mipmap.more_icon_message,
            R.mipmap.more_icon_journal,
    };
    private ViewHolder holder;

    public LeftMenuListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return icons.length;
    }

    @Override
    public Object getItem(int i) {
        return icons[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.left_listview_item, null);
            holder.ivStatus = (ImageView) convertView.findViewById(R.id.iv_card_status);
            holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title_left);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.ivIcon.setImageResource(icons[i]);
        holder.tvTitle.setText(leftMenuTitles[i]);
        if (i == 4) {
            status = SPUtils.get(context, "staff_is_real", "").toString();
            if (status.equals(Globals.staffIsRealNo) || status.equals(Globals.staffIsRealFaild)) {
                holder.ivStatus.setImageResource(R.mipmap.error);
            } else if (status.equals(Globals.staffIsRealSuc)) {
                holder.ivStatus.setImageResource(R.mipmap.ok);
            } else if (status.equals(Globals.staffIsRealIng)) {
                holder.ivStatus.setImageResource(R.mipmap.waring);
            }
            holder.ivStatus.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    private class ViewHolder {
        private ImageView ivIcon, ivStatus;
        private TextView tvTitle;
    }
}