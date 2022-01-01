package com.example.projectcuoikhoa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProductDetailActivity extends AppCompatActivity {
    TextView tvName, tvPrice, tvDescription, tvBrand, tvOrigin, tvTangSL, tvGiamSL, tvXemThem;
    static TextView tvSoLuongGioHang;
    TextView tvSoLuong, tvMuaNgay;
    ImageView img, imgShoppingcart_detail, imgBack;
    int SoLuong =1;
    int id = 0;
    //String id = "";
    int price =0;
    String name="";
    String GiaTriImg="";
    LinearLayout llThemGiohang;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    int width = LinearLayout.LayoutParams.MATCH_PARENT;
    int height = LinearLayout.LayoutParams.WRAP_CONTENT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        tvName = findViewById(R.id.tvNameProductDetail);
        tvPrice = findViewById(R.id.tvPriceDetail);
        tvDescription = findViewById(R.id.tvProductDetail);
        img = findViewById(R.id.img);
        tvBrand = findViewById(R.id.tvBrand);
        tvOrigin = findViewById(R.id.tvOrigin);
        tvTangSL = findViewById(R.id.tvTangSL);
        tvGiamSL = findViewById(R.id.tvGiamSL);
        tvSoLuong = findViewById(R.id.tvSoLuong);
        imgShoppingcart_detail = findViewById(R.id.imgShoppingcart_detail);
        llThemGiohang = findViewById(R.id.llThemGioHang);
        imgBack = findViewById(R.id.imgBackDetail);
        tvSoLuongGioHang = findViewById(R.id.tvSoLuongGioHang);
        tvMuaNgay =findViewById(R.id.tvMuaNgay);
        tvXemThem = findViewById(R.id.tvXemThem);
        //GetInformation();
        //Bundle bundle = getIntent().getExtras();
        GetInformation("ProductDetail");
        TangSL();
        GiamSL();
        Shopping_cart();
        EventButton();
        SoLuongGioHang();
        OnClickMuaNgay();

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, height);
        tvXemThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvDescription.setLayoutParams(lp);
            }
        });
    }

    private void GetInformation(String t ) {
        Bundle bundle = getIntent().getExtras();
        Product product=(Product)bundle.get("ProductDetail");
        Glide.with(getApplication())
                .load(product.getImg())
                .into(img);
        tvName.setText(product.getName());
        tvPrice.setText(product.getPrice()+"đ");
        tvDescription.setText(product.getDesciption());
        tvBrand.setText(product.getBrand());
        tvOrigin.setText(product.getOrigin()+"");
        id = product.getId();
        price = product.getPrice();
        name = product.getName();
        GiaTriImg = product.getImg();
    }
    private void TangSL(){
        tvTangSL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoLuong = SoLuong+1;
                tvSoLuong.setText(SoLuong+"");
                return;
            }
        });
    }
    private void GiamSL(){
        tvGiamSL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SoLuong >= 2){
                    SoLuong = SoLuong-1;
                    tvSoLuong.setText(SoLuong+"");
                    return;
                }
            }
        });
    }
    private void Shopping_cart(){
        imgShoppingcart_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference();
                if(user == null){
                    Toast.makeText(getApplication(),"Đăng nhập xem giỏ hàng",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(getApplication(), GioHangActivity.class);
                startActivity(intent);
            }
        });
    }
    public static void SoLuongGioHang(){
        int SoLuong_GH =0 ;
        for(int i=0;i<MainActivity.mangGioHang.size();i++){
            SoLuong_GH += MainActivity.mangGioHang.get(i).getSoLuong();
        }
        tvSoLuongGioHang.setText(SoLuong_GH+"");
    }
    private void EventButton() {
        llThemGiohang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference();
                if(user == null){
                    Toast.makeText(getApplication(),"Đăng nhập để thêm giỏ hàng",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(MainActivity.mangGioHang.size()>0){
                    boolean exists = false;
                    int sl = Integer.parseInt(tvSoLuong.getText().toString());
                    for(int i=0;i<MainActivity.mangGioHang.size();i++){
                        if(MainActivity.mangGioHang.get(i).getId() == (id)){
                            MainActivity.mangGioHang.get(i).setSoLuong(MainActivity.mangGioHang.get(i).getSoLuong()+ sl);
                            databaseReference.child("GioHang").child(user.getUid()).child(MainActivity.mangGioHang.get(i).getId()+"").setValue(MainActivity.mangGioHang.get(i));
                            exists = true;
                            }
                        }
                        if(exists == false){//k tìm đc sản phẩm trùng add sản phẩm mới
                            //long GiaMoi = SoLuong * price;
                            MainActivity.mangGioHang.add(new GioHang( id, price, name, GiaTriImg, sl));
                            GioHang gioHang = new GioHang(id, price, name, GiaTriImg, Integer.parseInt(tvSoLuong.getText().toString()));
                            //MainActivity.mangGioHang.add(gioHang);
                            databaseReference.child("GioHang").child(user.getUid()+"").child(gioHang.getId()+"").setValue(gioHang);
                        }
                    }
                else {
                    long GiaMoi = SoLuong * price;
                    MainActivity.mangGioHang.add(new GioHang( id, price, name, GiaTriImg, SoLuong));
                    GioHang gioHang = new GioHang(id, price, name, GiaTriImg, Integer.parseInt(tvSoLuong.getText().toString()));
                    //MainActivity.mangGioHang.add(gioHang);
                    databaseReference.child("GioHang").child(user.getUid()+"").child(gioHang.getId()+"").setValue(gioHang);
                }
                SoLuongGioHang();
                //GioHang gioHang = new GioHang( id, price, name, GiaTriImg, Integer.parseInt(tvSoLuong.getText().toString()));
                Toast.makeText(getApplication(),"Thêm thành công",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void OnClickMuaNgay() {
        tvMuaNgay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference();
                MainActivity.mangMuaNgay.clear();
                if(user == null){
                    Toast.makeText(getApplication(),"Đăng nhập để mua hàng",Toast.LENGTH_SHORT).show();
                    return;
                }
                    long GiaMoi = SoLuong * price;
                    MainActivity.mangMuaNgay.add(new GioHang( id, price, name, GiaTriImg, SoLuong));
                Intent intent = new Intent(getApplicationContext(),DatHangActivity.class);
                startActivity(intent);
            }
        });
    }
}