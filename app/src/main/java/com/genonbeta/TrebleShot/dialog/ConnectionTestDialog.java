/*
 * Copyright (C) 2019 Veli Tasalı
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package com.genonbeta.TrebleShot.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import androidx.annotation.ColorInt;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import com.genonbeta.TrebleShot.R;
import com.genonbeta.TrebleShot.object.NetworkDevice;
import com.genonbeta.TrebleShot.util.AppUtils;
import com.genonbeta.TrebleShot.util.TextUtils;

import java.util.List;

public class ConnectionTestDialog extends AlertDialog.Builder
{
    final private List<EstablishConnectionDialog.ConnectionResult> mConnections;

    @ColorInt
    private int mActiveColor;

    @ColorInt
    private int mPassiveColor;

    public ConnectionTestDialog(Context context, NetworkDevice device,
                                List<EstablishConnectionDialog.ConnectionResult> resultList)
    {
        super(context);

        mConnections = resultList;
        mActiveColor = ContextCompat.getColor(context, AppUtils.getReference(context, R.attr.colorAccent));
        mPassiveColor = ContextCompat.getColor(context, AppUtils.getReference(context, R.attr.colorControlNormal));

        setTitle(context.getString(R.string.text_connectionTest, device.nickname));
        setNegativeButton(R.string.butn_close, null);

        if (resultList.size() < 1)
            setMessage(R.string.text_empty);
        else
            setAdapter(new ConnectionListAdapter(), null);
    }

    private class ConnectionListAdapter extends BaseAdapter
    {
        @Override
        public int getCount()
        {
            return mConnections.size();
        }

        @Override
        public Object getItem(int position)
        {
            return mConnections.get(position);
        }

        @Override
        public long getItemId(int position)
        {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            if (convertView == null)
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_available_interface, parent,
                        false);

            EstablishConnectionDialog.ConnectionResult address = (EstablishConnectionDialog.ConnectionResult) getItem(
                    position);

            TextView textView1 = convertView.findViewById(R.id.pending_available_interface_text1);
            TextView textView2 = convertView.findViewById(R.id.pending_available_interface_text2);
            TextView textView3 = convertView.findViewById(R.id.pending_available_interface_text3);

            boolean accessible = address.pingTime >= 0;

            textView1.setTextColor(accessible ? mActiveColor : mPassiveColor);
            textView1.setText(TextUtils.getAdapterName(getContext(), address.connection));
            textView2.setText(address.connection.ipAddress);

            if (accessible)
                textView3.setText(getContext().getString(R.string.text_textMillisecond, address.pingTime));
            else
                textView3.setText(R.string.text_error);

            return convertView;
        }
    }
}
