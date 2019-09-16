package com.laioffer.botlogistics;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.laioffer.entity.Order;
import com.laioffer.entity.ShippingMethod;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ShippingMethodAdapter extends BaseAdapter {
    Context context;
    List<ShippingMethod> methods = new ArrayList<>();
    public ShippingMethodAdapter(Context context) {
        this.context = context;
    }

    public List<ShippingMethod> getMethods() {
        return methods;
    }

    public void updateStations(List<ShippingMethod> methods) {
        if (methods == null) {
            return;
        }
        this.methods.clear();
        this.methods.addAll(methods);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return methods.size();
    }

    @Override
    public Object getItem(int position) {
        return methods.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * @param position
     * @param convertView the old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using.
     *                    If it is not possible to convert this view to display the correct data, this method can create a new view.
     * @param parent the parent that this view will orderually be attached to
     * @return Gets a View that displays in the listView with the data at the specified position in the data set.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.shipping_item,
                    parent, false);
        }

        ImageView methodImage = (ImageView)  convertView.findViewById(R.id.method_image);
        TextView methodStation = (TextView) convertView.findViewById(
                R.id.method_station);
        TextView methodQuantity = (TextView) convertView.findViewById(R.id.method_quantity);
        TextView methodPrice = (TextView) convertView.findViewById(
                R.id.method_price);
        TextView methodDuration = (TextView) convertView.findViewById(
                R.id.method_duration);

        ShippingMethod method = methods.get(position);

        // set Image for different types of machine
        String machineType = method.getType();
        if(machineType.equals("robot")){
            methodImage.setImageResource(R.drawable.robot);
        }else if(machineType.equals("drone")){
            methodImage.setImageResource(R.drawable.drone);
        }

        // set basic information for an order
        methodStation.setText(method.getStation());
        methodQuantity.setText(Integer.toString(method.getQuantity()));
        methodPrice.setText(Double.toString(method.getPrice()));
        methodDuration.setText(Double.toString(method.getDutation()));

        return convertView;
    }
}
