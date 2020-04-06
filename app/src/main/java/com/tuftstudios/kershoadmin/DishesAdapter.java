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

        int userType = SharedPrefManager.getInstance(mContext).getSavedUser().getType();
        String userKitchen = SharedPrefManager.getInstance(mContext).getSavedUser().getUsername();

        if (userType == 0 || userType == 2) {
            dishesViewHolder.textViewDishName.setText(dish.getDishName());
            if (dish.getSide1().equals("none")) {
                dishesViewHolder.textViewSSide1.setVisibility(View.GONE);
            }else{
                dishesViewHolder.textViewSSide1.setText("Side 1: " + dish.getSide1());
            }
            if (dish.getSide2().equals("none")) {
                dishesViewHolder.textViewSSide2.setVisibility(View.GONE);
            }else {
                dishesViewHolder.textViewSSide2.setText("Side 1: " + dish.getSide2());
            }
            if (dish.getOption().equals("none")) {
                dishesViewHolder.textViewSOption.setVisibility(View.GONE);
            }else{
                dishesViewHolder.textViewSOption.setText("Option: " + dish.getOption());
            }
            dishesViewHolder.textViewSSize.setText("Size: " + dish.getSize());
            dishesViewHolder.textViewKitchen.setText("Kitchen: " + dish.getKitchen());
            dishesViewHolder.count.setText(toString().valueOf("Quantity: " + dish.getQuantity()));

        } else if (userType == 1 && dish.getKitchen().equals(userKitchen)){
            dishesViewHolder.textViewDishName.setText(dish.getDishName());
            if (dish.getSide1().equals("none")) {
                dishesViewHolder.textViewSSide1.setVisibility(View.GONE);
            }else{
                dishesViewHolder.textViewSSide1.setText("Side 1: " + dish.getSide1());
            }
            if (dish.getSide2().equals("none")) {
                dishesViewHolder.textViewSSide2.setVisibility(View.GONE);
            }else {
                dishesViewHolder.textViewSSide2.setText("Side 1: " + dish.getSide2());
            }
            if (dish.getOption().equals("none")) {
                dishesViewHolder.textViewSOption.setVisibility(View.GONE);
            }else{
                dishesViewHolder.textViewSOption.setText("Option: " + dish.getOption());
            }
            dishesViewHolder.textViewSSize.setText("Size: " + dish.getSize());
            dishesViewHolder.count.setText(toString().valueOf("Quantity: " + dish.getQuantity()));
        }
    }

/*
        dishesViewHolder.textViewDishName.setText(dish.getDishName());
        dishesViewHolder.textViewSOption.setText("Option: " + dish.getOption());
        dishesViewHolder.textViewSSide1.setText("Side 1: " + dish.getSide1());
        dishesViewHolder.textViewSSide2.setText("Side 2: " + dish.getSide2());
        dishesViewHolder.textViewSSize.setText("Size: " + dish.getSize());
        dishesViewHolder.textViewKitchen.setText("Kitchen: " + dish.getKitchen());
        dishesViewHolder.count.setText(toString().valueOf(dish.getQuantity()));
*/



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
