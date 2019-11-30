package com.tuftstudios.kershoadmin;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//initial commit from ts

public class PreOrdersFragment extends Fragment {

    private RecyclerView recyclerView;
    private OrdersAdapter ordersAdapter;
    private List<Order> ordersList;

    Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_orders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        fetchPreData();

        final SwipeRefreshLayout mySwipeRefreshLayout = view.findViewById(R.id.orders_refresh);

        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        fetchPreData();
                        mySwipeRefreshLayout.setRefreshing(false);
                    }
                }
        );

    }

    private void fetchPreData(){

        Call<KitchenOrders> call;

        /*if(SharedPrefManager.getInstance(context).getSavedUser().getType() == 0){

            call = RetrofitClient.getInstance().getApi().getAllOldOrders();

        }else if (SharedPrefManager.getInstance(context).getSavedUser().getType() == 1){

            call = RetrofitClient.getInstance().getApi()
                    .getKetOldOrders(SharedPrefManager.getInstance(context).getSavedUser().getKitchen());
        }*/

        call = RetrofitClient.getInstance().getApi()
                .getOldOrders(SharedPrefManager.getInstance(context).getSavedUser().getId());


//        Call<KitchenOrders> call = RetrofitClient.getInstance().getApi().getAllOldOrders();
        call.enqueue(new Callback<KitchenOrders>() {

            @Override
            public void onResponse(Call<KitchenOrders> call, Response<KitchenOrders> response) {

                ordersList = response.body().getOrders();
                ordersAdapter = new OrdersAdapter(getActivity(), ordersList);
                recyclerView.setAdapter(ordersAdapter);

            }

            @Override
            public void onFailure(Call<KitchenOrders> call, Throwable t) {

                Toast.makeText(getContext(),t.getMessage(), Toast.LENGTH_LONG).show();

                Log.i("error: ", t.getMessage());

            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
}
