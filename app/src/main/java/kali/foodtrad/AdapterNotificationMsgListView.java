package kali.foodtrad;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by elliotching on 24-Nov-16.
 */
class AdapterNotificationMsgListView extends BaseAdapter {

    Context context;
    ArrayList<NotiMsg> list;

    public AdapterNotificationMsgListView(Context c, ArrayList<NotiMsg> list){
        context = c;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public NotiMsg getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;

        if(view == null){
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.list_view_noti_msg, null);

            holder.tv1 = (TextView) view.findViewById(R.id.list_item_column_1);
            holder.tv2 = (TextView) view.findViewById(R.id.list_item_column_2);
            view.setTag(holder);
        }
        else{
            holder = (ViewHolder)view.getTag();
        }

        holder.tv1.setText(list.get(i)._time);
        holder.tv2.setText(list.get(i)._msg);

        return view;
    }

    private class ViewHolder{
        TextView tv1;
        TextView tv2;
    }
}
