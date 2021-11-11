package com.example.shoppingapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class ProductSpecificationFragment extends Fragment {






    public ProductSpecificationFragment() {

    }

    private RecyclerView productSpecificationRecyclerView;
    public List<ProductSpecificationModel> productSpecificationModelList;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view =  inflater.inflate(R.layout.fragment_product_specification, container, false);

        productSpecificationRecyclerView = view.findViewById(R.id.product_specification_recyclerview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

        productSpecificationRecyclerView.setLayoutManager(linearLayoutManager);

       // productSpecificationModelList.add(new ProductSpecificationModel(0,"General"));
       // productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","4GB"));
        //productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","4GB"));
        //productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","4GB"));
       // productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","4GB"));
      //  productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","4GB"));
       // productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","4GB"));
       // productSpecificationModelList.add(new ProductSpecificationModel(0,"Display"));
       // productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","4GB"));
       // productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","4GB"));
       // productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","4GB"));
       // productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","4GB"));
       // productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","4GB"));
       // productSpecificationModelList.add(new ProductSpecificationModel(0,"General"));
       // productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","4GB"));
       // productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","4GB"));
       // productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","4GB"));
      //  productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","4GB"));
       // productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","4GB"));
       // productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","4GB"));
       // productSpecificationModelList.add(new ProductSpecificationModel(0,"Display"));
       // productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","4GB"));
       // productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","4GB"));
       // productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","4GB"));
       // productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","4GB"));
        //productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","4GB"));


        ProductSpecificationAdapter productSpecificationAdapter = new ProductSpecificationAdapter(productSpecificationModelList);
        productSpecificationRecyclerView.setAdapter(productSpecificationAdapter);
        productSpecificationAdapter.notifyDataSetChanged();

        return view;
    }
}