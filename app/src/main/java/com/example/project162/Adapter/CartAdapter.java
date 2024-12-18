package com.example.project162.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.project162.Domain.Foods;
import com.example.project162.Helper.ChangeNumberItemsListener;
import com.example.project162.Helper.ManagmentCart;
import com.example.project162.R;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.viewholder> {
    ArrayList<Foods> list;
    private ManagmentCart managmentCart;
    ChangeNumberItemsListener changeNumberItemsListener;

    public CartAdapter(ArrayList<Foods> list, Context context, ChangeNumberItemsListener changeNumberItemsListener) {
        this.list = list;
        managmentCart = new ManagmentCart(context);
        this.changeNumberItemsListener = changeNumberItemsListener;
    }

    @NonNull
    @Override
    public CartAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_cart, parent, false);
        return new viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.viewholder holder, int position) {
        Foods food = list.get(position);

        // Hiển thị tên món ăn
        holder.title.setText(food.getTitle());

        // Hiển thị giá tiền tổng cộng cho sản phẩm
        String formattedPriceEachItem = formatPriceWithCommas(food.getNumberInCart() * food.getPrice());
        holder.feeEachItem.setText(formattedPriceEachItem + " VND");
        holder.feeEachItem.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.green)); // Màu xanh

        // Xóa phần hiển thị "2 * VND 35.000"
        holder.totalEachItem.setVisibility(View.GONE);

        // Hiển thị số lượng của sản phẩm
        holder.num.setText(String.valueOf(food.getNumberInCart()));

        // Tải ảnh sản phẩm bằng Glide
        Glide.with(holder.itemView.getContext())
                .load(food.getImagePath())
                .transform(new CenterCrop(), new RoundedCorners(30))
                .into(holder.pic);

        // Xử lý sự kiện tăng số lượng
        holder.plusItem.setOnClickListener(v -> managmentCart.plusNumberItem(list, position, () -> {
            notifyDataSetChanged();
            changeNumberItemsListener.change();
        }));

        // Xử lý sự kiện giảm số lượng
        holder.minusItem.setOnClickListener(v -> managmentCart.minusNumberItem(list, position, () -> {
            notifyDataSetChanged();
            changeNumberItemsListener.change();
        }));
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView title, feeEachItem, plusItem, minusItem;
        ImageView pic;
        TextView totalEachItem, num;

        public viewholder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.titleTxt);
            pic = itemView.findViewById(R.id.pic);
            feeEachItem = itemView.findViewById(R.id.feeEachItem);
            plusItem = itemView.findViewById(R.id.plusCartBtn);
            minusItem = itemView.findViewById(R.id.minusCartBtn);
            totalEachItem = itemView.findViewById(R.id.totalEachItem);
            num = itemView.findViewById(R.id.numberItemTxt);
        }
    }

    // Phương thức định dạng giá tiền với dấu chấm
    private String formatPriceWithCommas(double price) {
        // Dùng String.format để định dạng số với dấu phẩy giữa mỗi 3 chữ số
        return String.format("%,d", (int) price).replace(',', '.');
    }
}
