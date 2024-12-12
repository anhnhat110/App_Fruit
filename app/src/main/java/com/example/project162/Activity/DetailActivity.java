package com.example.project162.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.project162.Domain.Foods;
import com.example.project162.Helper.ManagmentCart;
import com.example.project162.R;
import com.example.project162.databinding.ActivityDetailBinding;

public class DetailActivity extends BaseActivity {
    ActivityDetailBinding binding;
    private Foods object;
    private int num = 1;
    private ManagmentCart managmentCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(getResources().getColor(R.color.black));

        getIntentExtra();
        initCartManager();
        setProductDetails();
        setupListeners();
    }

    /**
     * Khởi tạo đối tượng quản lý giỏ hàng.
     */
    private void initCartManager() {
        managmentCart = new ManagmentCart(this);
    }

    /**
     * Hiển thị thông tin sản phẩm chi tiết.
     */
    private void setProductDetails() {
        if (object != null) {
            Glide.with(this)
                    .load(object.getImagePath())
                    .into(binding.pic);

            // Định dạng và hiển thị giá sản phẩm
            binding.priceTxt.setText(formatPriceWithCommas(object.getPrice()) + " VND");

            binding.titleTxt.setText(object.getTitle());
            binding.descriptionTxt.setText(object.getDescription());
            binding.rateTxt.setText(object.getStar() + " Rating");
            binding.ratingBar.setRating((float) object.getStar());

            // Hiển thị tổng giá trị ban đầu
            binding.totalTxt.setText(formatPriceWithCommas(num * object.getPrice()) + " VND");
            binding.numTxt.setText(String.valueOf(num));
        } else {
            // Xử lý trường hợp object = null
            binding.titleTxt.setText("Product not found");
            binding.descriptionTxt.setText("");
            binding.priceTxt.setText("0 VND");
            binding.totalTxt.setText("0 VND");
        }
    }

    /**
     * Cài đặt các sự kiện nút nhấn.
     */
    private void setupListeners() {
        binding.backBtn.setOnClickListener(v -> finish());

        binding.plusBtn.setOnClickListener(v -> {
            num++;
            updateQuantityAndTotal();
        });

        binding.minusBtn.setOnClickListener(v -> {
            if (num > 1) {
                num--;
                updateQuantityAndTotal();
            }
        });

        binding.addBtn.setOnClickListener(v -> {
            if (object != null) {
                object.setNumberInCart(num);
                managmentCart.insertFood(object);
                showToast("Added to cart successfully!");
            }
        });
    }

    /**
     * Cập nhật số lượng và tổng giá trị hiển thị.
     */
    private void updateQuantityAndTotal() {
        binding.numTxt.setText(String.valueOf(num));
        if (object != null) {
            binding.totalTxt.setText(formatPriceWithCommas(num * object.getPrice()) + " VND");
        }
    }

    /**
     * Định dạng giá tiền với dấu chấm.
     */
    private String formatPriceWithCommas(double price) {
        return String.format("%,d", (int) price).replace(',', '.');
    }

    /**
     * Lấy dữ liệu sản phẩm từ Intent.
     */
    private void getIntentExtra() {
        object = (Foods) getIntent().getSerializableExtra("object");
    }

    /**
     * Hiển thị thông báo ngắn gọn.
     */
    private void showToast(String message) {
        // Có thể dùng Toast hoặc Snackbar tùy theo thiết kế
        // Ví dụ: Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
