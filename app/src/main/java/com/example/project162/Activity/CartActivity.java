package com.example.project162.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.project162.Adapter.CartAdapter;
import com.example.project162.Helper.ChangeNumberItemsListener;
import com.example.project162.Helper.ManagmentCart;
import com.example.project162.R;
import com.example.project162.databinding.ActivityCartBinding;

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
        double percentTax = 0.02; // 2% tax
        double delivery = 10; // 10 Dollar

        // Tính thuế
        tax = Math.round(managmentCart.getTotalFee() * percentTax * 100.0) / 100.0;

        // Tính tổng tiền
        double total = Math.round((managmentCart.getTotalFee() + tax + delivery) * 100) / 100.0;
        double itemTotal = Math.round(managmentCart.getTotalFee() * 100) / 100.0;

        // Hiển thị các giá trị với dấu chấm phân cách
        binding.totalFeeTxt.setText(formatPriceWithCommas(itemTotal) + " vnd");
        binding.taxTxt.setText(formatPriceWithCommas(tax) + " vnd");
        binding.deliveryTxt.setText(formatPriceWithCommas(delivery) + " vnd");
        binding.totalTxt.setText(formatPriceWithCommas(total) + " vnd");
    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(v -> finish());
    }

    // Phương thức định dạng giá tiền với dấu chấm
    private String formatPriceWithCommas(double price) {
        return String.format("%,d", (int) price).replace(',', '.');
    }
}
