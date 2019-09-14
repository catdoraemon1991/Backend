package com.laioffer.botlogistics;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

public class OrderDetailDialog extends Dialog {
    public OrderDetailDialog(@NonNull Context context) {
        this(context, R.style.MyAlertDialogStyle);
    }

    public OrderDetailDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public static OrderDetailDialog newInstance(Context context, String json) {
        OrderDetailDialog dialog = new OrderDetailDialog(context, R.style.MyAlertDialogStyle);
        Gson gson = new Gson();
        Order order = gson.fromJson(json, Order.class);

//        TextView orderId = (TextView) context.findViewById(
//                R.id.order_id);
//        TextView orderShippingAddress = (TextView) convertView.findViewById(
//                R.id.order_shipping_address);
//        TextView orderDeliveryTime = (TextView) convertView.findViewById(
//                R.id.order_delivery_time);
//        TextView orderStatus = (TextView) convertView.findViewById(R.id.order_status);

        return dialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View dialogView = View.inflate(getContext(), R.layout.order_detail_dialog, null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(dialogView);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }
}
