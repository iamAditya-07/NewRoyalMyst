package com.example.shoppingapp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CartAdapter extends RecyclerView.Adapter {

    private List<CartItemModel> cartItemModelList;
    private int lastPosition = -1;
    private TextView cartTotalAmount;
    private boolean showDeleteBtn;

    public CartAdapter(List<CartItemModel> cartItemModelList, TextView cartTotalAmount, boolean showDeleteBtn) {
        this.cartItemModelList = cartItemModelList;
        this.cartTotalAmount = cartTotalAmount;
        this.showDeleteBtn = showDeleteBtn;
    }

    @Override
    public int getItemViewType(int position) {
        switch (cartItemModelList.get(position).getType()) {
            case 0:
                return CartItemModel.CART_ITEM;
            case 1:
                return CartItemModel.TOTAL_AMOUNT;
            default:
                return -1;
        }
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case CartItemModel.CART_ITEM:
                View cartItemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_item_layout, viewGroup, false);
                return new CartItemViewholder(cartItemView);

            case CartItemModel.TOTAL_AMOUNT:
                View cartTotalView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_total_amount_layout, viewGroup, false);
                return new CartTotalAmountViewholder(cartTotalView);
            default:
                return null;
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int position) {
        switch (cartItemModelList.get(position).getType()) {
            case CartItemModel.CART_ITEM:
                String productID = cartItemModelList.get(position).getProductID();
                String resource = cartItemModelList.get(position).getProductImage();
                String title = cartItemModelList.get(position).getProductTitle();
                Long freeCoupons = cartItemModelList.get(position).getFreeCoupons();
                String productPrice = cartItemModelList.get(position).getProductPrice();
                String cuttedPrice = cartItemModelList.get(position).getCuttedPrice();
                Long offersApplied = cartItemModelList.get(position).getOffersApplied();
                boolean inStock = cartItemModelList.get(position).isInStock();
                Long productQuantity = cartItemModelList.get(position).getProductQuantity();
                Long maxQuantity = cartItemModelList.get(position).getMaxQuantity();
                boolean qtyError = cartItemModelList.get(position).isQtyError();
                List<String> qtyIds = cartItemModelList.get(position).getQtyIDs();
                long stockQty = cartItemModelList.get(position).getStockQuantity();
                boolean COD = cartItemModelList.get(position).isCOD();

                ((CartItemViewholder)viewHolder).setItemDetails(productID,resource,title,freeCoupons,productPrice,cuttedPrice,offersApplied,position,inStock,String.valueOf(productQuantity),maxQuantity,qtyError,qtyIds,stockQty,COD);
                break;
            case CartItemModel.TOTAL_AMOUNT:
                int totalItems = 0;
                int totalItemPrice = 0;
                String deliveryPrice;
                int totalAmount;
                int savedAmount = 0;


                for (int x = 0;x < cartItemModelList.size();x++){

                    if (cartItemModelList.get(x).getType() == CartItemModel.CART_ITEM && cartItemModelList.get(x).isInStock()){
                        int quantity =  Integer.parseInt(String.valueOf(cartItemModelList.get(x).getProductQuantity()));
                        totalItems = totalItems + quantity;
                        if (TextUtils.isEmpty(cartItemModelList.get(x).getSelectedCouponId())) {
                            totalItemPrice = totalItemPrice + Integer.parseInt(cartItemModelList.get(x).getProductPrice())*quantity;
                        }else
                        {
                            totalItemPrice = totalItemPrice + Integer.parseInt(cartItemModelList.get(x).getDiscountedPrice())*quantity;
                        }

                        if (!TextUtils.isEmpty(cartItemModelList.get(x).getCuttedPrice()))
                        {
                            savedAmount = savedAmount + (Integer.parseInt(cartItemModelList.get(x).getCuttedPrice()) - Integer.parseInt(cartItemModelList.get(x).getProductPrice())) * quantity;
                            if (!TextUtils.isEmpty(cartItemModelList.get(x).getSelectedCouponId())) {
                                savedAmount = savedAmount + (Integer.parseInt(cartItemModelList.get(x).getProductPrice()) - Integer.parseInt(cartItemModelList.get(x).getDiscountedPrice())) * quantity;
                            }
                        }else
                        {
                            if (!TextUtils.isEmpty(cartItemModelList.get(x).getSelectedCouponId())) {
                                savedAmount = savedAmount + (Integer.parseInt(cartItemModelList.get(x).getProductPrice()) - Integer.parseInt(cartItemModelList.get(x).getDiscountedPrice())) * quantity;
                            }
                        }

                    }

                }
                if (totalItemPrice > 500){
                    deliveryPrice = "FREE";
                    totalAmount = totalItemPrice;
                }else{
                    deliveryPrice = "60";
                    totalAmount = totalItemPrice + 60;
                }


                cartItemModelList.get(position).setTotalItems(totalItems);
                cartItemModelList.get(position).setTotalItemPrice(totalItemPrice);
                cartItemModelList.get(position).setDeliveryPrice(deliveryPrice);
                cartItemModelList.get(position).setTotalAmount(totalAmount);
                cartItemModelList.get(position).setSavedAmount(savedAmount);

                ((CartTotalAmountViewholder)viewHolder).setTotalAmount(totalItems,totalItemPrice,deliveryPrice,totalAmount,savedAmount);
                break;
            default:
                return;
        }

        if (lastPosition < position) {
            Animation animation = AnimationUtils.loadAnimation(viewHolder.itemView.getContext(), R.anim.fade_in);
            viewHolder.itemView.setAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return cartItemModelList.size();
    }

    class CartItemViewholder extends RecyclerView.ViewHolder {

        private ImageView productImage;
        private ImageView freeCouponIcon;
        private TextView productTitle;
        private TextView freeCoupons;
        private TextView productPrice;
        private TextView cuttedPrice;
        private TextView offersApplied;
        private TextView couponsApplied;
        private TextView productQuantity;
        private LinearLayout couponRedemptionLayout;
        private TextView couponRedemptionBody;

        private LinearLayout deleteBtn;
        private Button redeemBtn;
        private ImageView codIndicator;

        private TextView couponTitle;
        private TextView couponExpiryDate;
        private TextView couponBody;
        private RecyclerView couponsRecyclerview;
        private LinearLayout selectedCoupon;
        private TextView originalPrice;
        private TextView discountedPrice;
        private LinearLayout applyORremoveBtnContainer;
        private TextView footerText;
        private Button removeCouponBtn;
        private Button applyCouponBtn;
        private String productOriginalPrice;



        public CartItemViewholder(@NonNull @NotNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.product_image);
            productTitle = itemView.findViewById(R.id.product_Title);
            freeCoupons = itemView.findViewById(R.id.tv_free_coupon);
            freeCouponIcon = itemView.findViewById(R.id.free_coupon_icon);
            productPrice = itemView.findViewById(R.id.product_Price);
            cuttedPrice = itemView.findViewById(R.id.cutted_Price);
            offersApplied = itemView.findViewById(R.id.offers_applied);
            couponsApplied = itemView.findViewById(R.id.coupons_applied);
            productQuantity = itemView.findViewById(R.id.product_quantity);
            couponRedemptionLayout = itemView.findViewById(R.id.coupon_redemption_layout);
            couponRedemptionBody = itemView.findViewById(R.id.tv_coupon_redemption);
            codIndicator = itemView.findViewById(R.id.cod_indicator);

            redeemBtn = itemView.findViewById(R.id.coupon_redemption_btn);

            deleteBtn = itemView.findViewById(R.id.remove_item_btn);

        }

        @SuppressLint("NewApi")
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        private void setItemDetails(String productID, String resource, String title, Long freeCouponsNo, String productPriceText, String cuttedPriceText, Long offersAppliedNo, int position, boolean inStock, String quantity, Long maxQuantity, boolean qtyError, List<String> qtyIds, long stockQty, boolean COD) {
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.placeholder)).into(productImage);
            productTitle.setText(title);


            final Dialog checkCouponPriceDialog = new Dialog(itemView.getContext());
            checkCouponPriceDialog.setContentView(R.layout.coupon_redeem_dialog);
            checkCouponPriceDialog.setCancelable(false);
            checkCouponPriceDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            if (COD){
                codIndicator.setVisibility(View.VISIBLE);
            }else
            {
                codIndicator.setVisibility(View.INVISIBLE);
            }

            if (inStock) {

                if (freeCouponsNo > 0) {
                    freeCouponIcon.setVisibility(View.VISIBLE);
                    freeCoupons.setVisibility(View.VISIBLE);
                    if (freeCouponsNo == 1) {
                        freeCoupons.setText("free " + freeCouponsNo + " Coupon");
                    } else {
                        freeCoupons.setText("free " + freeCouponsNo + " Coupons");
                    }

                } else {
                    freeCouponIcon.setVisibility(View.INVISIBLE);
                    freeCoupons.setVisibility(View.INVISIBLE);
                }

                productPrice.setText("Rs."+productPriceText+"/-");
                productPrice.setTextColor(Color.parseColor("#FF000000"));
                cuttedPrice.setText("Rs."+cuttedPriceText+"/-");
                couponRedemptionLayout.setVisibility(View.VISIBLE);

                    ImageView toggleRecyclerView = checkCouponPriceDialog.findViewById(R.id.toggle_recyclerview);
                    couponsRecyclerview = checkCouponPriceDialog.findViewById(R.id.coupons_recyclerview);
                    selectedCoupon = checkCouponPriceDialog.findViewById(R.id.selected_coupon);

                    couponTitle = checkCouponPriceDialog.findViewById(R.id.Coupon_title);
                    couponExpiryDate = checkCouponPriceDialog.findViewById(R.id.Coupon_validity);
                    couponBody = checkCouponPriceDialog.findViewById(R.id.Coupon_body);
                    footerText = checkCouponPriceDialog.findViewById(R.id.footer_text);
                    applyORremoveBtnContainer = checkCouponPriceDialog.findViewById(R.id.apply_or_remove_btns_container);
                    removeCouponBtn = checkCouponPriceDialog.findViewById(R.id.remove_btn);
                    applyCouponBtn = checkCouponPriceDialog.findViewById(R.id.apply_btn);


                    footerText.setVisibility(View.GONE);
                    applyORremoveBtnContainer.setVisibility(View.VISIBLE);
                    originalPrice = checkCouponPriceDialog.findViewById(R.id.original_price);
                    discountedPrice = checkCouponPriceDialog.findViewById(R.id.discounted_price);


                    LinearLayoutManager layoutManager = new LinearLayoutManager(itemView.getContext());
                    layoutManager.setOrientation(RecyclerView.VERTICAL);
                    couponsRecyclerview.setLayoutManager(layoutManager);

                    originalPrice.setText(productPrice.getText());
                    productOriginalPrice = productPriceText;
                    MyRewardsAdapter myRewardsAdapter = new MyRewardsAdapter(position,DBqueries.rewardModelList, true,couponsRecyclerview,selectedCoupon,productOriginalPrice,couponTitle,couponExpiryDate,couponBody,discountedPrice,cartItemModelList);
                    couponsRecyclerview.setAdapter(myRewardsAdapter);
                    myRewardsAdapter.notifyDataSetChanged();

                    applyCouponBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (!TextUtils.isEmpty(cartItemModelList.get(position).getSelectedCouponId())) {

                                for (RewardModel rewardModel : DBqueries.rewardModelList) {
                                    if (rewardModel.getCouponId().equals(cartItemModelList.get(position).getSelectedCouponId())) {
                                        rewardModel.setAlreadyUsed(true);
                                        couponRedemptionLayout.setBackground(itemView.getContext().getResources().getDrawable(R.drawable.reward_gradient_background));
                                        couponRedemptionBody.setText(rewardModel.getCouponBody());
                                        redeemBtn.setText("Coupon");
                                    }
                                }
                                couponsApplied.setVisibility(View.VISIBLE);
                                cartItemModelList.get(position).setDiscountedPrice(discountedPrice.getText().toString().substring(3,discountedPrice.getText().length() - 2));
                                productPrice.setText(discountedPrice.getText());
                                String offerDiscountedAmt = String.valueOf(Long.valueOf(productPriceText) - Long.valueOf(discountedPrice.getText().toString().substring(3,discountedPrice.getText().length() - 2)));
                                couponsApplied.setText("Coupon applied - Rs." + offerDiscountedAmt+"/-");
                                notifyItemChanged(cartItemModelList.size() - 1);
                                checkCouponPriceDialog.dismiss();
                            }
                        }
                    });

                    removeCouponBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            for (RewardModel rewardModel: DBqueries.rewardModelList) {
                                if (rewardModel.getCouponId().equals(cartItemModelList.get(position).getSelectedCouponId())){
                                    rewardModel.setAlreadyUsed(false);
                                }
                            }
                            couponTitle.setText("Coupon");
                            couponExpiryDate.setText("validity");
                            couponBody.setText("Tap the icon on the top right corner to select your coupon.");
                            couponsApplied.setVisibility(View.INVISIBLE);
                            couponRedemptionLayout.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.couponRed));
                            couponRedemptionBody.setText("Apply your coupon here.");
                            redeemBtn.setText("Redeem");
                            cartItemModelList.get(position).setSelectedCouponId(null);
                            productPrice.setText("Rs."+productPriceText+"/-");
                            notifyItemChanged(cartItemModelList.size() - 1);
                            checkCouponPriceDialog.dismiss();
                        }
                    });

                    toggleRecyclerView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showDialogRecyclerView();
                        }
                    });

                  if (!TextUtils.isEmpty(cartItemModelList.get(position).getSelectedCouponId())) {

                                for (RewardModel rewardModel : DBqueries.rewardModelList) {
                                    if (rewardModel.getCouponId().equals(cartItemModelList.get(position).getSelectedCouponId())) {
                                        couponRedemptionLayout.setBackground(itemView.getContext().getResources().getDrawable(R.drawable.reward_gradient_background));
                                        couponRedemptionBody.setText(rewardModel.getCouponBody());
                                        redeemBtn.setText("Coupon");

                                       couponBody.setText(rewardModel.getCouponBody());
                                        if (rewardModel.getType().equals("Discount")) {
                                            couponTitle.setText(rewardModel.getType());
                                        } else {
                                            couponTitle.setText("FLAT Rs." + rewardModel.getdiscORamt() + " OFF");
                                        }
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy");
                                        couponExpiryDate.setText("till " + simpleDateFormat.format(rewardModel.getTimestamp()));
                                    }
                                }
                      discountedPrice.setText("Rs."+cartItemModelList.get(position).getDiscountedPrice()+"/-");
                      couponsApplied.setVisibility(View.VISIBLE);
                                productPrice.setText("Rs."+cartItemModelList.get(position).getDiscountedPrice()+"/-");
                                String offerDiscountedAmt = String.valueOf(Long.valueOf(productPriceText) - Long.valueOf(cartItemModelList.get(position).getDiscountedPrice()));
                                couponsApplied.setText("Coupon applied - Rs." + offerDiscountedAmt+"/-");
                            }else
                  {
                      couponsApplied.setVisibility(View.INVISIBLE);
                      couponRedemptionLayout.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.couponRed));
                      couponRedemptionBody.setText("Apply your coupon here.");
                      redeemBtn.setText("Redeem");
                  }


                productQuantity.setText("Qty: " + quantity);
                if (!showDeleteBtn) {
                    if (qtyError) {
                        productQuantity.setTextColor(itemView.getContext().getResources().getColor(R.color.red));
                        productQuantity.setBackgroundTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.red)));
                    } else {
                        productQuantity.setTextColor(itemView.getContext().getResources().getColor(R.color.black));
                        productQuantity.setBackgroundTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.black)));

                    }
                }

                productQuantity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Dialog quantityDialog = new Dialog(itemView.getContext());
                        quantityDialog.setContentView(R.layout.quantity_dialog);

                        quantityDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                        quantityDialog.setCancelable(false);

                        EditText quantityNo = quantityDialog.findViewById(R.id.quantity_no);
                        Button cancelBtn = quantityDialog.findViewById(R.id.cancel_btn);
                        Button okBtn = quantityDialog.findViewById(R.id.ok_btn);
                        quantityNo.setHint("Max "+String.valueOf(maxQuantity));

                        cancelBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                quantityDialog.dismiss();
                            }
                        });

                        okBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!TextUtils.isEmpty(quantityNo.getText())) {
                                    if (Long.valueOf(quantityNo.getText().toString()) <= maxQuantity && Long.valueOf(quantityNo.getText().toString()) != 0) {

                                       if (itemView.getContext() instanceof MainActivity2){
                                           cartItemModelList.get(position).setProductQuantity(Long.valueOf(quantityNo.getText().toString()));
                                       }else {
                                           if (DeliveryActivity.fromCart) {
                                               cartItemModelList.get(position).setProductQuantity(Long.valueOf(quantityNo.getText().toString()));
                                           } else {
                                               DeliveryActivity.cartItemModelList.get(position).setProductQuantity(Long.valueOf(quantityNo.getText().toString()));
                                           }
                                       }
                                        productQuantity.setText("Qty: " + quantityNo.getText());
                                       notifyItemChanged(cartItemModelList.size() - 1);

                                        if (!showDeleteBtn){
                                            DeliveryActivity.loadingDialog.show();
                                            DeliveryActivity.cartItemModelList.get(position).setQtyError(false);
                                            int initialQty = Integer.parseInt(quantity);
                                            int finalQty = Integer.parseInt(quantityNo.getText().toString());

                                            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

                                            if (finalQty > initialQty) {

                                                for (int y = 0; y < finalQty - initialQty; y++) {
                                                    String quantityDocumentName = UUID.randomUUID().toString().substring(0, 20);

                                                    Map<String, Object> timestamp = new HashMap<>();
                                                    timestamp.put("time", FieldValue.serverTimestamp());
                                                    int finalY = y;
                                                    firebaseFirestore.collection("PRODUCTS").document(productID).collection("QUANTITY").document(quantityDocumentName).set(timestamp)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    qtyIds.add(quantityDocumentName);

                                                                    if (finalY + 1 == finalQty - initialQty) {

                                                                        firebaseFirestore.collection("PRODUCTS").document(productID).collection("QUANTITY").orderBy("time", Query.Direction.ASCENDING).limit(stockQty).get()
                                                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                                                                                        if (task.isSuccessful()) {
                                                                                            List<String> serverQuantity = new ArrayList<>();

                                                                                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {

                                                                                                serverQuantity.add(queryDocumentSnapshot.getId());

                                                                                            }

                                                                                            long availableQty = 0;
                                                                                            for (String qtyId : qtyIds) {

                                                                                                if (!serverQuantity.contains(qtyId)) {


                                                                                                        DeliveryActivity.cartItemModelList.get(position).setQtyError(true);
                                                                                                        DeliveryActivity.cartItemModelList.get(position).setMaxQuantity(availableQty);
                                                                                                        Toast.makeText(itemView.getContext(), "Sorry !, All products may not be available in required quantity...", Toast.LENGTH_SHORT).show();


                                                                                                } else
                                                                                                {
                                                                                                    availableQty++;
                                                                                                }

                                                                                            }
                                                                                            DeliveryActivity.cartAdapter.notifyDataSetChanged();
                                                                                        } else {
                                                                                            String error = task.getException().getMessage();
                                                                                            Toast.makeText(itemView.getContext(), error, Toast.LENGTH_SHORT).show();
                                                                                        }
                                                                                        DeliveryActivity.loadingDialog.dismiss();
                                                                                    }
                                                                                });

                                                                    }

                                                                }
                                                            });
                                                }
                                            }else if (initialQty > finalQty){
                                                for (int x = 0;x < initialQty - finalQty;x++) {
                                                    String qtyId = qtyIds.get(qtyIds.size() - 1 - x);

                                                    int finalX = x;
                                                    firebaseFirestore.collection("PRODUCTS").document(productID).collection("QUANTITY").document(qtyId).delete()
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {

                                                                   qtyIds.remove(qtyId);
                                                                    DeliveryActivity.cartAdapter.notifyDataSetChanged();
                                                                    if (finalX + 1 ==  initialQty - finalQty){
                                                                        DeliveryActivity.loadingDialog.dismiss();
                                                                    }

                                                                }
                                                            });

                                                }
                                            }
                                        }

                                    }else{
                                        Toast.makeText(itemView.getContext(), "Max quantity : "+maxQuantity.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                                    quantityDialog.dismiss();

                            }
                        });

                        quantityDialog.show();
                    }
                });

                if (offersAppliedNo > 0) {
                    offersApplied.setVisibility(View.VISIBLE);
                    String offerDiscountedAmt = String.valueOf(Long.valueOf(cuttedPriceText) - Long.valueOf(productPriceText));
                    offersApplied.setText("Offer applied - Rs."+offerDiscountedAmt+"/-");
                } else {
                    offersApplied.setVisibility(View.INVISIBLE);
                }

            }else
            {
                productPrice.setText("Out of Stock");
                productPrice.setTextColor(itemView.getContext().getResources().getColor(R.color.red));
                cuttedPrice.setText("");
                couponRedemptionLayout.setVisibility(View.GONE);
                freeCoupons.setVisibility(View.INVISIBLE);
                productQuantity.setVisibility(View.INVISIBLE);
                couponsApplied.setVisibility(View.GONE);
                offersApplied.setVisibility(View.GONE);
                freeCouponIcon.setVisibility(View.INVISIBLE);
            }






            if (showDeleteBtn)
            {
                deleteBtn.setVisibility(View.VISIBLE);
            }
            else
            {
                deleteBtn.setVisibility(View.GONE);
            }




            redeemBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    for (RewardModel rewardModel: DBqueries.rewardModelList) {
                        if (rewardModel.getCouponId().equals(cartItemModelList.get(position).getSelectedCouponId())){
                            rewardModel.setAlreadyUsed(false);
                        }
                    }

                    checkCouponPriceDialog.show();
                }
            });

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!TextUtils.isEmpty(cartItemModelList.get(position).getSelectedCouponId())) {
                        for (RewardModel rewardModel: DBqueries.rewardModelList) {
                            if (rewardModel.getCouponId().equals(cartItemModelList.get(position).getSelectedCouponId())){
                                rewardModel.setAlreadyUsed(false);
                            }
                        }

                    }

                        if (!ProductDetailsActivity.running_cart_query){
                        ProductDetailsActivity.running_cart_query = true;

                        DBqueries.removeFromCart(position,itemView.getContext(),cartTotalAmount);
                    }
                }
            });
        }
        private void showDialogRecyclerView() {
            if (couponsRecyclerview.getVisibility() == View.GONE) {
                couponsRecyclerview.setVisibility(View.VISIBLE);
                selectedCoupon.setVisibility(View.GONE);
            } else {
                couponsRecyclerview.setVisibility(View.GONE);
                selectedCoupon.setVisibility(View.VISIBLE);
            }
        }
    }

    class CartTotalAmountViewholder extends RecyclerView.ViewHolder {

        private TextView totalItems;
        private TextView totalItemPrice;
        private TextView deliveryPrice;
        private TextView totalAmount;
        private TextView savedAmount;

        public CartTotalAmountViewholder(@NonNull @NotNull View itemView) {
            super(itemView);

            totalItems = itemView.findViewById(R.id.total_items);
            totalItemPrice = itemView.findViewById(R.id.total_items_price);
            deliveryPrice = itemView.findViewById(R.id.delivery_price);
            totalAmount = itemView.findViewById(R.id.total_price);
            savedAmount = itemView.findViewById(R.id.saved_amount);
        }

        private void setTotalAmount(int totalItemText, int totalItemPriceText, String deliveryPriceText, int totalAmountText, int savedAmountText) {
            totalItems.setText("Price("+totalItemText+" items)");
            totalItemPrice.setText("Rs."+totalItemPriceText+"/-");
            if (deliveryPriceText.equals("FREE")) {
                deliveryPrice.setText(deliveryPriceText);
            }else
            {
                deliveryPrice.setText("Rs."+deliveryPriceText+"/-");
            }
            totalAmount.setText("Rs."+totalAmountText+"/-");
            cartTotalAmount.setText("Rs."+totalAmountText+"/-");
            savedAmount.setText("You saved Rs."+savedAmountText+"/- on this order.");

            LinearLayout parent = (LinearLayout) cartTotalAmount.getParent().getParent();
            if (totalItemPriceText == 0){
                if (DeliveryActivity.fromCart) {
                    cartItemModelList.remove(cartItemModelList.size() - 1);
                    DeliveryActivity.cartItemModelList.remove(DeliveryActivity.cartItemModelList.size() - 1);

                }
                if (showDeleteBtn)
                {
                    cartItemModelList.remove(cartItemModelList.size() - 1);
                }
                parent.setVisibility(View.GONE);
            }else
            {
                parent.setVisibility(View.VISIBLE);
            }
        }
    }
}
