package com.tuftstudios.kershoadmin;

public class SpinnerClass {


//    public static void getKitchens(final Spinner kitchenSpinner, final Context context) {
//        Call<Users> call = RetrofitClient.getInstance().getApi().getKitchens();
//
//        call.enqueue(new Callback<Users>() {
//            @Override
//            public void onResponse(Call<Users> call, Response<Users> response) {
//                if (!response.isSuccessful()) {
//                    //textViewResult.setText("code: " + response.code());
//
//                    showMessage(String.valueOf(response.code()), context);
//                    return;
//                }else{
//
//                    assert response.body() != null;
//                    final ArrayList<User> users = response.body().getUsers();
//
//                    final List<String> usernames = new ArrayList<>();
//                    for(User user : users){
//
//                        usernames.add(user.toSting());
//                    }
//
//                    ArrayAdapter<String> spinnerAdapter;
//                    spinnerAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item, usernames);
//                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                    kitchenSpinner.setAdapter(spinnerAdapter);
//                    kitchenSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                        @Override
//                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//                            if (users.get(position).getType() == 1){
//
//
//
//                            }
//
//                            showMessage(String.valueOf(users.get(position).getArea()), context);
//                        }
//
//                        @Override
//                        public void onNothingSelected(AdapterView<?> parent) {
//
//                        }
//                    });
//                }
//
//
//
//            }
//
//            @Override
//            public void onFailure(Call<Users> call, Throwable t) {
//                showMessage(t.getMessage(), context);
//
//            }
//        });
//    }
//
//
//    private static  void showMessage(String message, Context context) {
//        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
//    }
}
