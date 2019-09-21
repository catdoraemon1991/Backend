package com.laioffer.botlogistics;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.laioffer.entity.Order;

import java.util.ArrayList;
import java.util.List;

public class OrderAdapter extends BaseAdapter{
    Context context;
    List<Order> orderData = new ArrayList<>();

    public OrderAdapter(Context context) {
        this.context = context;
    }

    public void updateOrder(List<Order> orders) {
        if (orders == null) {
            return;
        }
        orderData.clear();
        orderData.addAll(orders);
        notifyDataSetChanged();
    }

    public void searchOrder(String string) {

    }

    public List<Order> getOrders() {
        return orderData;
    }

    /**
     * list view lenght
     */
    @Override
    public int getCount() {
        return orderData.size();
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
        return orderData.get(position);
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
            convertView = inflater.inflate(R.layout.order_item,
                    parent, false);
        }

        ImageView orderImage = (ImageView)  convertView.findViewById(R.id.order_image);
        TextView orderId = (TextView) convertView.findViewById(
                R.id.order_id);
        TextView orderShippingAddress = (TextView) convertView.findViewById(
                R.id.order_shipping_address);
        TextView orderShippingTime = (TextView) convertView.findViewById(
                R.id.order_shipping_time);
        TextView orderStatus = (TextView) convertView.findViewById(R.id.order_status);

        Order r = orderData.get(position);

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
        orderShippingTime.setText(Utils.convertTime(r.getShippingTime()));


        // set order status
        Long now = System.currentTimeMillis();
        if(now >= r.getDeliveryTime()){
            orderStatus.setText("Delivered");

            orderStatus.setTextColor(ContextCompat.getColor(context, R.color.colorCoral));
        }else{
            orderStatus.setText("Shipping");
            orderStatus.setTextColor(ContextCompat.getColor(context, R.color.colorYellow));

        }

        return convertView;
    }
}
