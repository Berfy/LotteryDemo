package cn.zcgames.lottery.personal.view.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

import cn.zcgames.lottery.R;
import cn.zcgames.lottery.app.AppConstants;

/**
 * 密码键盘适配器
 */
public class KeyAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Map<String, String>> numList;
    private Handler handler;

    public KeyAdapter(Context context, ArrayList<Map<String, String>> numList, Handler handler) {
        this.context = context;
        this.numList = numList;
        this.handler = handler;
    }

    @Override
    public int getCount() {
        return numList.size();
    }

    @Override
    public Object getItem(int position) {
        return numList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final KeyViewHolder holder;

        if (convertView == null) {
            convertView = View.inflate(context, R.layout.griditem_keyboard, null);
            holder = new KeyViewHolder();
            holder.btnKey = convertView.findViewById(R.id.tv_number);
            convertView.setTag(holder);
        } else {
            holder = (KeyViewHolder) convertView.getTag();
        }

        holder.btnKey.setText(numList.get(position).get("num"));
        if (position == 9) {
            holder.btnKey.setBackgroundColor(context.getResources().getColor(R.color.colorGray));
        }
        if (position == 11) {
            holder.btnKey.setBackgroundResource(R.drawable.icon_delete_num_bmp);
        }

        holder.btnKey.setOnClickListener(v -> {
            Message msg = new Message();
            msg.what = AppConstants.KEYBOARD_INPUT;
            msg.obj = position;
            handler.sendMessage(msg);
        });
        return convertView;
    }

    class KeyViewHolder {
        public TextView btnKey;
    }
}
