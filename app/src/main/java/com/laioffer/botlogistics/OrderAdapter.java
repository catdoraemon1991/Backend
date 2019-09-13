package com.laioffer.botlogistics;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class OrderAdapter extends BaseAdapter{
    Context context;
    List<Order> eventData;

    public OrderAdapter(Context context, List<Order> eventData) {
        this.context = context;
        this.eventData = eventData;
    }

    /**
     * list view lenght
     */
    @Override
    public int getCount() {
        return eventData.size();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public Order getItem(int position) {
        return eventData.get(position);
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
     * @param parent the parent that this view will eventually be attached to
     * @return Gets a View that displays in the listView with the data at the specified position in the data set.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.order_item,
                    parent, false);
        }

        ImageView orderImage = (ImageView)  convertView.findViewById(R.id.order_image);
        TextView orderId = (TextView) convertView.findViewById(
                R.id.order_id);
        TextView orderShippingAddress = (TextView) convertView.findViewById(
                R.id.order_shipping_address);
        TextView orderDeliveryTime = (TextView) convertView.findViewById(
                R.id.order_delivery_time);
        TextView orderStatus = (TextView) convertView.findViewById(R.id.order_status);

        Order r = eventData.get(position);

        // set Image for different types of machine
        String machineId = r.getMachineId();
        String machineType = machineId.substring(0, machineId.length() - 2);
        if(machineType.equals("robot")){
            orderImage.setImageResource(R.drawable.robot);
        }else if(machineType.equals("drone")){
            orderImage.setImageResource(R.drawable.drone);
        }

        // set basic information for an order
        orderId.setText(r.getOrderId());
        orderShippingAddress.setText(r.getShippingAddress());
        orderDeliveryTime.setText(DataService.convertTime(r.getDeliveryTime()));

        // set order status
        Long now = System.currentTimeMillis();
        if(now >= r.getDeliveryTime()){
            orderStatus.setText("Delivered");
            orderStatus.setTextColor(Color.GREEN);
        }else{
            orderStatus.setText("Shipping");
            orderStatus.setTextColor(Color.BLUE);
        }

        return convertView;
    }
}
