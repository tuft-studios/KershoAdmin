package com.tuftstudios.kershoadmin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DishesAdapter extends RecyclerView.Adapter<DishesAdapter.DishesViewHolder> {

    private Context mContext;
    private List<Dish> dishesList;
    Order order;

    public DishesAdapter(Context mContext, List<Dish> mDishesList, Order order) {
        this.mContext = mContext;
        this.dishesList = mDishesList;
        this.order = order;
    }


    @NonNull
    @Override
    public DishesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dishes_item, parent, false);
        return new DishesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DishesViewHolder dishesViewHolder, int position) {

        Dish dish = dishesList.get(position);

        dishesViewHolder.textViewDishName.setText(dish.getDishName());
        dishesViewHolder.textViewSOption.setText("Option: " + dish.getSelectedOption());
        dishesViewHolder.textViewSSide1.setText("Side 1: " + dish.getSelectedSide1());
        dishesViewHolder.textViewSSide2.setText("Side 2: " + dish.getSelectedSide2());
        dishesViewHolder.textViewSSize.setText("Size: " + dish.getSelectedSize());
        int count = order.getDishesList().get(position).getQuantity();

        dishesViewHolder.count.setText("x" + count);
        //dishesViewHolder.textViewKitchen.setText(String.valueOf(dish.getKitchen()));

    }

    @Override
    public int getItemCount() {

        return dishesList.size();
    }

    public class DishesViewHolder extends RecyclerView.ViewHolder{

        public TextView textViewDishName, textViewSOption, textViewSSide1, textViewSSide2, textViewSSize;
        public TextView count;
        public TextView textViewKitchen;
        public CardView cardView;

        public DishesViewHolder(@NonNull View itemView){
            super(itemView);

            textViewDishName = itemView.findViewById(R.id.textViewDishName);
            textViewSOption = itemView.findViewById(R.id.textViewSOption);
            textViewSSide1 = itemView.findViewById(R.id.textViewSSide1);
            textViewSSide2 = itemView.findViewById(R.id.textViewSSide2);
            textViewSSize = itemView.findViewById(R.id.textViewSSize);
            count = itemView.findViewById(R.id.count_sam);
            textViewKitchen = itemView.findViewById(R.id.textViewKitchen);

            cardView = itemView.findViewById(R.id.cardViewDishes);
        }
    }
}
