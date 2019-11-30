package com.tuftstudios.kershoadmin;

import android.content.Context;
import android.os.Bundle;
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

public class CurOrdersFragment extends Fragment {

    private RecyclerView recyclerView;
    private OrdersAdapter ordersAdapter;
    private List<Order> ordersList;


    Context context;
//    private List<Dish> dishesList;


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


        fetchCurData();

        final SwipeRefreshLayout mySwipeRefreshLayout = view.findViewById(R.id.orders_refresh);

        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        fetchCurData();
                        Toast.makeText(context, "Kitchen: " +
                                SharedPrefManager.getInstance(context).getSavedUser().getKitchen(),
                                Toast.LENGTH_SHORT).show();

                        mySwipeRefreshLayout.setRefreshing(false);
                    }
                }
        );

    }

    private void fetchCurData(){

        Call<KitchenOrders> call;

        /*if(SharedPrefManager.getInstance(context).getSavedUser().getType() == 0){

            call = RetrofitClient.getInstance().getApi().getAllCurOrders();

        }else if (SharedPrefManager.getInstance(context).getSavedUser().getType() == 1){

            call = RetrofitClient.getInstance().getApi()
                    .getKetCurOrders(SharedPrefManager.getInstance(context).getSavedUser().getKitchen());
        }
*/
        call = RetrofitClient.getInstance().getApi()
                .getCurOrders(SharedPrefManager.getInstance(context).getSavedUser().getId());

//        Call<OrdersItem> call = RetrofitClient.getInstance().getApi().getOrders("Main Dish", "Maadi");

       call.enqueue(new Callback<KitchenOrders>() {

            @Override
            public void onResponse(Call<KitchenOrders> call, Response<KitchenOrders> response) {


//                dishesList = response.body().getUsers();
//                ordersAdapter = new OrdersAdapter(getActivity(), dishesList);
                ordersList = response.body().getOrders();
                ordersAdapter = new OrdersAdapter(getActivity(), ordersList);
                recyclerView.setAdapter(ordersAdapter);

            }

            @Override
            public void onFailure(Call<KitchenOrders> call, Throwable t) {

                Toast.makeText(context,t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

//        @Override
//    public boolean onQueryTextSubmit(String query) {
//        return false;
//    }
//
//    @Override
//    public boolean onQueryTextChange(String newText) {
//
//        String userInput = newText.toLowerCase();
//        List<Order> orderListFiltered = new ArrayList<>();
//
//        for (Order order : orderListFiltered){
//            if (order.toString().toLowerCase().contains(userInput)){
//                orderListFiltered.add(order);
//            }
//        }
//
//
//        ordersAdapter.updateList(orderListFiltered);
//
////        OrdersAdapter.updateList(orderListFiltered);
//        return false;
//    }
}
