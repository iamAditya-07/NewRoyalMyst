package com.example.shoppingapp;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MyRewardsAdapter extends RecyclerView.Adapter<MyRewardsAdapter.Viewholder> {

    private List<RewardModel> rewardModelList;
    private Boolean useMiniLayout = false;
    private RecyclerView couponsRecyclerview;
    private LinearLayout selectedCoupon;
    private String productOriginalPrice;
    private TextView selectedCouponTitle;
    private TextView selectedCouponExpiryDate;
    private TextView selectedCouponBody;
    private TextView discountedPrice;
    private int cartItemPosition = -1;
    private List<CartItemModel> cartItemModelList;

    public MyRewardsAdapter(List<RewardModel> rewardModelList, Boolean useMiniLayout) {
        this.rewardModelList = rewardModelList;
        this.useMiniLayout = useMiniLayout;
    }

    public MyRewardsAdapter(List<RewardModel> rewardModelList, Boolean useMiniLayout, RecyclerView couponsRecyclerview, LinearLayout selectedCoupon, String productOriginalPrice, TextView couponTitle, TextView couponExpiryDate, TextView couponBody, TextView discountedPrice) {
        this.rewardModelList = rewardModelList;
        this.useMiniLayout = useMiniLayout;
        this.couponsRecyclerview = couponsRecyclerview;
        this.selectedCoupon = selectedCoupon;
        this.productOriginalPrice = productOriginalPrice;
        this.selectedCouponTitle = couponTitle;
        this.selectedCouponExpiryDate = couponExpiryDate;
        this.selectedCouponBody = couponBody;
        this.discountedPrice = discountedPrice;
    }

    public MyRewardsAdapter(int cartItemPosition,List<RewardModel> rewardModelList, Boolean useMiniLayout, RecyclerView couponsRecyclerview, LinearLayout selectedCoupon, String productOriginalPrice, TextView couponTitle, TextView couponExpiryDate, TextView couponBody, TextView discountedPrice, List<CartItemModel> cartItemModelList) {
        this.rewardModelList = rewardModelList;
        this.useMiniLayout = useMiniLayout;
        this.couponsRecyclerview = couponsRecyclerview;
        this.selectedCoupon = selectedCoupon;
        this.productOriginalPrice = productOriginalPrice;
        this.selectedCouponTitle = couponTitle;
        this.selectedCouponExpiryDate = couponExpiryDate;
        this.selectedCouponBody = couponBody;
        this.discountedPrice = discountedPrice;
        this.cartItemPosition = cartItemPosition;
        this.cartItemModelList = cartItemModelList;
    }

    @NonNull
    @NotNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull @NotNull ViewGroup viewGroup, int viewType) {
        View view;

        if (useMiniLayout) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mini_rewards_item_layout, viewGroup, false);

        } else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rewards_item_layout, viewGroup, false);
        }
        return new Viewholder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull @NotNull Viewholder viewholder, int position) {
        String couponId = rewardModelList.get(position).getCouponId();
        String type = rewardModelList.get(position).getType();
        Date validity = rewardModelList.get(position).getTimestamp();
        String body = rewardModelList.get(position).getCouponBody();
        String lowerLimit = rewardModelList.get(position).getLowerLimit();
        String upperLimit = rewardModelList.get(position).getUpperLimit();
        String discORamt = rewardModelList.get(position).getdiscORamt();
        Boolean alreadyUsed = rewardModelList.get(position).getAlreadyUsed();

        viewholder.setData(couponId,type, validity, body, lowerLimit, upperLimit, discORamt, alreadyUsed);
    }

    @Override
    public int getItemCount() {
        return rewardModelList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        private TextView couponTitle;
        private TextView couponExpiryDate;
        private TextView couponBody;

        public Viewholder(@NonNull @NotNull View itemView) {
            super(itemView);

            couponTitle = itemView.findViewById(R.id.Coupon_title);
            couponExpiryDate = itemView.findViewById(R.id.Coupon_validity);
            couponBody = itemView.findViewById(R.id.Coupon_body);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        private void setData(String couponId, String type, Date validity, String body, String lowerLimit, String upperLimit, String discORamt, boolean alreadyUsed) {
            if (type.equals("Discount")) {
                couponTitle.setText(type);
            } else {
                couponTitle.setText("FLAT Rs." + discORamt + " OFF");
            }

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy");
            if (alreadyUsed) {
                couponExpiryDate.setText("Already used");
                couponExpiryDate.setTextColor(itemView.getContext().getResources().getColor(R.color.red));
                couponBody.setTextColor(itemView.getContext().getResources().getColor(R.color.whites));
                couponTitle.setTextColor(itemView.getContext().getResources().getColor(R.color.whites));
            } else {
                couponBody.setTextColor(itemView.getContext().getResources().getColor(R.color.white));
                couponTitle.setTextColor(itemView.getContext().getResources().getColor(R.color.white));
                couponExpiryDate.setTextColor(itemView.getContext().getResources().getColor(R.color.coupon));
                couponExpiryDate.setText("till " + simpleDateFormat.format(validity));
            }

            couponBody.setText(body);


            if (useMiniLayout) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!alreadyUsed) {
                            selectedCouponTitle.setText(type);
                            selectedCouponExpiryDate.setText(simpleDateFormat.format(validity));
                            selectedCouponBody.setText(body);

                            if (Long.valueOf(productOriginalPrice) > Long.valueOf(lowerLimit) && Long.valueOf(productOriginalPrice) < Long.valueOf(upperLimit)) {
                                if (type.equals("Discount")) {
                                    Long discountAmount = Long.valueOf(productOriginalPrice) * Long.valueOf(discORamt) / 100;
                                    discountedPrice.setText("Rs." + String.valueOf(Long.valueOf(productOriginalPrice) - discountAmount) + "/-");
                                } else {
                                    discountedPrice.setText("Rs." + String.valueOf(Long.valueOf(productOriginalPrice) - Long.valueOf(discORamt)) + "/-");
                                }
                                if (cartItemPosition != -1) {
                                    cartItemModelList.get(cartItemPosition).setSelectedCouponId(couponId);
                                }
                            } else {
                                if (cartItemPosition != -1) {
                                    cartItemModelList.get(cartItemPosition).setSelectedCouponId(null);
                                }
                                discountedPrice.setText("Invalid");
                                Toast.makeText(itemView.getContext(), "Sorry ! Product doesn't matches the coupon terms.", Toast.LENGTH_SHORT).show();
                            }

                            if (couponsRecyclerview.getVisibility() == View.GONE) {
                                couponsRecyclerview.setVisibility(View.VISIBLE);
                                selectedCoupon.setVisibility(View.GONE);
                            } else {
                                couponsRecyclerview.setVisibility(View.GONE);
                                selectedCoupon.setVisibility(View.VISIBLE);
                            }
                        }

                    }
                });
            }

        }
    }
}
