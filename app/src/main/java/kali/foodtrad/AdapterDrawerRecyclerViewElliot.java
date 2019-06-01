package kali.foodtrad;

/**
 * Created by elliotching on 04-Jul-16.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

abstract class AdapterDrawerRecyclerViewElliot extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<DrawerItem> drawerItemList;
    int layoutResID;
    DrawerItemHolder mHolder;

    public AdapterDrawerRecyclerViewElliot(Context context, int layoutResourceID,//R.layout.drawer_single_item_layout_foodie_main
                                           List<DrawerItem> listItems) {
        super();
        this.context = context;
        this.drawerItemList = listItems;
        this.layoutResID = layoutResourceID;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(layoutResID, parent, false);

        return new DrawerItemHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        mHolder = (DrawerItemHolder) holder;

        mHolder.mText = (TextView) mHolder.mView.findViewById(R.id.m_text_item);
        mHolder.mText.setText(drawerItemList.get(position).mItem);

        mHolder.mBackground = (LinearLayout) mHolder.mView.findViewById(R.id.m_drawer_item_background_layout);

//        int choosenColor = if theme dark = c-t-pale-cyan / if light = c-t-sdark-cyan

        int choosenColor = (getPrefTheme().equals("Dark"))? R.color.c_t_pale_cyan : R.color.c_t_sdark_cyan;
        int colorR = drawerItemList.get(position).choosen ? choosenColor : R.color.c_transparent;
        mHolder.mBackground.setBackgroundResource(colorR);


        mHolder.mView.setOnClickListener(new ElliotOnClick(position, getItemCount()));
    }

    @Override
    public int getItemCount() {
        return drawerItemList.size();
    }

    public abstract void onClickElliot(int position, int size);

    private String getPrefTheme(){
        SharedPreferences pref = context.getSharedPreferences("FoodieRoute", Context.MODE_PRIVATE);
        String themeSetting = pref.getString(ResFR.THEME,"Light");
        if(themeSetting.equals("Light")){
            pref.edit().putString(ResFR.THEME,"Light");
            pref.edit().commit();
        }
        return themeSetting;
    }

    private static class DrawerItemHolder extends RecyclerView.ViewHolder {
        TextView mText;
        View mView;
        LinearLayout mBackground;

        public DrawerItemHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
    }

    class ElliotOnClick implements View.OnClickListener {
        int pos;
        int size;

        ElliotOnClick(int p, int s) {
            pos = p;
            size = s;
        }

        @Override
        public void onClick(View v) {
            onClickElliot(pos, size);
        }
    }
}