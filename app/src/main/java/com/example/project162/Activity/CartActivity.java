package com.example.project162.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.project162.Adapter.CartAdapter;
import com.example.project162.Helper.ManagmentCart;
import com.example.project162.R;
import com.example.project162.databinding.ActivityCartBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class CartActivity extends BaseActivity {
    private ActivityCartBinding binding;
    private RecyclerView.Adapter adapter;
    private ManagmentCart managmentCart;
    private double tax;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference ordersRef;
    private EditText addressInput;
    private RadioGroup paymentMethodGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        managmentCart = new ManagmentCart(this);
        addressInput = findViewById(R.id.addressInput);
        paymentMethodGroup = findViewById(R.id.paymentMethodGroup);
        // Khởi tạo Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        ordersRef = firebaseDatabase.getReference("Orders");

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

        // Thêm sự kiện nút đặt hàng
        binding.checkoutBtn.setOnClickListener(v -> createOrderInFirebase());
    }

    private void calculateCart() {
        double percentTax = 0.02; // 2% tax
        double delivery = 15000; // 10 Dollar

        // Tính thuế
        tax = Math.round(managmentCart.getTotalFee() * percentTax * 100.0) / 100.0;

        // Tính tổng tiền
        double total = Math.round((managmentCart.getTotalFee() + tax + delivery) * 100) / 100.0;
        double itemTotal = Math.round(managmentCart.getTotalFee() * 100) / 100.0;

        // Hiển thị các giá trị với dấu chấm phân cách
        binding.totalFeeTxt.setText(formatPriceWithCommas(itemTotal) + " VNĐ");
        binding.taxTxt.setText(formatPriceWithCommas(tax) + " VNĐ");
        binding.deliveryTxt.setText(formatPriceWithCommas(delivery) + " VNĐ");
        binding.totalTxt.setText(formatPriceWithCommas(total) + " VNĐ");
    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(v -> finish());
    }

    // Phương thức tạo đơn hàng trong Firebase
    private void createOrderInFirebase() {
        if (managmentCart.getListCart().isEmpty()) {
            Toast.makeText(this, "Giỏ hàng rỗng, không thể tạo đơn hàng!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lấy địa chỉ từ EditText
        String address = addressInput.getText().toString().trim();
        if (address.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập địa chỉ giao hàng!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lấy phương thức thanh toán từ RadioGroup
        int selectedPaymentMethodId = paymentMethodGroup.getCheckedRadioButtonId();
        String paymentMethod = "";

        if (selectedPaymentMethodId == R.id.paymentMethodCash) {
            paymentMethod = "Thanh toán tiền mặt";
        } else if (selectedPaymentMethodId == R.id.paymentMethodCard) {
            paymentMethod = "Thanh toán thẻ";
        } else if (selectedPaymentMethodId == R.id.paymentMethodOnline) {
            paymentMethod = "Thanh toán trực tuyến";
        } else {
            Toast.makeText(this, "Vui lòng chọn phương thức thanh toán!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Chuẩn bị dữ liệu đơn hàng
        Map<String, Object> orderData = new HashMap<>();
        orderData.put("items", managmentCart.getListCart()); // Danh sách sản phẩm
        orderData.put("total", managmentCart.getTotalFee());
        orderData.put("tax", tax);
        orderData.put("delivery", 10); // Phí giao hàng
        orderData.put("orderTime", System.currentTimeMillis()); // Thời gian tạo đơn hàng
        orderData.put("address", address); // Địa chỉ giao hàng
        orderData.put("paymentMethod", paymentMethod); // Phương thức thanh toán

        // Đẩy dữ liệu lên Firebase
        ordersRef.push().setValue(orderData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Đặt hàng thành công!", Toast.LENGTH_SHORT).show();
                managmentCart.clearCart(); // Xóa giỏ hàng sau khi đặt hàng thành công
                initList(); // Cập nhật giao diện
                calculateCart(); // Cập nhật tổng tiền
            } else {
                Toast.makeText(this, "Đặt hàng thất bại. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    // Phương thức định dạng giá tiền với dấu chấm
    private String formatPriceWithCommas(double price) {
        return String.format("%,d", (int) price).replace(',', '.');
    }
}
