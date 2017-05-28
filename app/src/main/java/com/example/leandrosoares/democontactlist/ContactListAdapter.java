package com.example.leandrosoares.democontactlist;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.leandrosoares.democontactlist.R;
import com.example.leandrosoares.democontactlist.RowItem;

import java.util.ArrayList;
import java.util.List;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;

public class ContactListAdapter extends ArrayAdapter<RowItem> {


    Context context;

    public ContactListAdapter(Context context, int resourceId, //resourceId=your layout
                              ArrayList<RowItem> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    /*private view holder class*/
    private class ViewHolder {
        ImageView avatar;
        TextView contactName;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        String name = getItem(position).getContactName();

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.contact, null);
            holder = new ViewHolder();
            holder.contactName = (TextView) convertView.findViewById(R.id.contactName);
            holder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.contactName.setText(name);




            String firstLetter= name.substring(0,1);

            ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT

            // generate color based on a key (same key returns the same color), useful for list/grid views
            int color = generator.getColor(name);

            TextDrawable textDrawable = TextDrawable.builder()
                    .buildRound(firstLetter, color);

            holder.avatar.setImageDrawable(textDrawable);




        return convertView;
    }

}