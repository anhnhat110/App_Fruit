package com.example.project162.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.project162.Adapter.CartAdapter;
import com.example.project162.Helper.ManagmentCart;
import com.example.project162.R;
import com.example.project162.databinding.ActivityCartBinding;

import java.text.DecimalFormat;

public class CartActivity extends BaseActivity {
    private ActivityCartBinding binding;
    private RecyclerView.Adapter adapter;
    private ManagmentCart managmentCart;
    private double tax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        managmentCart = new ManagmentCart(this);

        setVariable();
        calculateCart();
        initList();
    }

    private void initList() {
        if (managmentCart.getListCart().isEmpty()) {
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.scrollviewCart.setVisibility(View.GONE);
        } else {
            binding.emptyTxt.setVisibility(View.GONE);
            binding.scrollviewCart.setVisibility(View.VISIBLE);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.cardView.setLayoutManager(linearLayoutManager);
        adapter = new CartAdapter(managmentCart.getListCart(), this, () -> calculateCart());
        binding.cardView.setAdapter(adapter);
    }

    private void calculateCart() {
        double percentTax = 0.02; // percent 2% tax
        double delivery = 10; // 10 Dollar

        tax = Math.round(managmentCart.getTotalFee() * percentTax * 100.0) / 100.0;

        double total = Math.round((managmentCart.getTotalFee() + tax + delivery) * 100.0) / 100.0;
        double itemTotal = Math.round(managmentCart.getTotalFee() * 100.0) / 100.0;

        // Sử dụng DecimalFormat để định dạng giá trị và thay dấu ',' bằng '.'
        DecimalFormat formatter = new DecimalFormat("#,###");
        String formattedItemTotal = formatter.format(itemTotal).replace(',', '.');
        String formattedTax = formatter.format(tax).replace(',', '.');
        String formattedDelivery = formatter.format(delivery).replace(',', '.');
        String formattedTotal = formatter.format(total).replace(',', '.');

        binding.totalFeeTxt.setText(formattedItemTotal + " VND");
        binding.taxTxt.setText(formattedTax + " VND");
        binding.deliveryTxt.setText(formattedDelivery + " VND");
        binding.totalTxt.setText(formattedTotal + " VND");
    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(v -> finish());
    }
}
