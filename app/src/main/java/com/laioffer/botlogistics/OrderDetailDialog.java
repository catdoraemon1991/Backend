package com.laioffer.botlogistics;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.laioffer.entity.Order;


public class OrderDetailDialog extends Dialog {
    private Order order;
    public OrderDetailDialog(@NonNull Context context) {
        this(context, R.style.MyAlertDialogStyle);
    }

    public OrderDetailDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public static OrderDetailDialog newInstance(Context context, Order orderInput) {
        OrderDetailDialog dialog = new OrderDetailDialog(context, R.style.MyAlertDialogStyle);
        dialog.order = orderInput;
        return dialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View dialogView = View.inflate(getContext(), R.layout.order_detail_dialog, null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(dialogView);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        // show order detail data
        TextView orderId = (TextView) findViewById(R.id.order_detail_order_id);
        orderId.setText(order.getOrderId());
        TextView orderDeliveryTime = (TextView) findViewById(R.id.order_detail_order_delivery_time);
        orderDeliveryTime.setText(Utils.convertTime(order.getDeliveryTime()));
        TextView orderDepartTime = (TextView) findViewById(R.id.order_detail_order_depart_time);
        orderDepartTime.setText(Utils.convertTime(order.getDepartTime()));
        TextView orderDestination = (TextView) findViewById(R.id.order_detail_order_destination);
        orderDestination.setText(order.getDestination());
        TextView orderMachineId = (TextView) findViewById(R.id.order_detail_order_machine_id);
        orderMachineId.setText(order.getMachineId());
        TextView orderPickupTime = (TextView) findViewById(R.id.order_detail_order_pick_up_time);
        orderPickupTime.setText(Utils.convertTime(order.getPickupTime()));
        TextView orderShippingAddress = (TextView) findViewById(R.id.order_detail_order_shipping_address);
        orderShippingAddress.setText(order.getShippingAddress());
        TextView orderShippingMethod = (TextView) findViewById(R.id.order_detail_shipping_method);
        orderShippingMethod.setText(order.getShippingMethod());
        TextView orderShippingTime = (TextView) findViewById(R.id.order_detail_shipping_time);
        orderShippingTime.setText(Utils.convertTime(order.getShippingTime()));
        TextView orderUserId = (TextView) findViewById(R.id.order_detail_user_id);
        orderUserId.setText(order.getUserId());
    }
}
