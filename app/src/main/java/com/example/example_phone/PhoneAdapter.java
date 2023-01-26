package com.example.example_phone;

import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhoneAdapter extends RecyclerView.Adapter<PhoneAdapter.MyViewHolder> {
    private List<Phone> phoneList;

    public PhoneAdapter(List<Phone> phoneList){
        this.phoneList = phoneList;
    }

    @NonNull
    @Override
    public PhoneAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.phone_list,parent,false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PhoneAdapter.MyViewHolder holder, int position) {

        Phone phone = phoneList.get(position);
        holder.tvname.setText(phone.getName());
        holder.tvtel.setText(phone.getTel());
        //리사이클뷰에 있는 리스트 중에 하나를 클릭 시 팝업창에 출력
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialogView = v.inflate(v.getContext(),R.layout.layout_add_concat,null);
                final EditText etName = dialogView.findViewById(R.id.etname);
                final EditText etTel = dialogView.findViewById(R.id.ettel);

                etName.setText(phone.getName());
                etTel.setText(phone.getTel());

                AlertDialog.Builder dlg = new AlertDialog.Builder(v.getContext());
                dlg.setTitle("연락처 수정");
                dlg.setView(dialogView);
                dlg.setPositiveButton("update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Phone phoneDto = new Phone();
                        //phoneDto.setId(phone.getId());
                        phoneDto.setName(etName.getText().toString());
                        phoneDto.setTel(etTel.getText().toString());

                        Log.d("update","onClick:등록 클릭시 값 확인" +phoneDto);

                        PhoneService phoneService = Retrofit2Client.getInstance().getPhoneService();
                        Call<Phone> call = phoneService.update(phone.getId(), phoneDto);
                        call.enqueue(new Callback<Phone>() {
                            @Override
                            public void onResponse(Call<Phone> call, Response<Phone> response) {
                                updateItem(response.body(),position);
                            }

                            @Override
                            public void onFailure(Call<Phone> call, Throwable t) {

                            }
                        });

                    }
                });
                dlg.setNegativeButton("delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PhoneService phoneService = Retrofit2Client.getInstance().getPhoneService();
                        Call<Void> call = phoneService.deleteById(phone.getId());
                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                removeItem(position);
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {

                            }
                        });
                    }
                });
                dlg.show();
            }
        });



    }

    @Override
    public int getItemCount() {
        return (null != phoneList ? phoneList.size():0);
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tvname;
        private TextView tvtel;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvname = itemView.findViewById(R.id.txName);
            tvtel = itemView.findViewById(R.id.txTel);
        }
    }

    //insert
    public void addItem(Phone phone){
        phoneList.add(phone);
        notifyDataSetChanged(); //새로고침 해야 변경된 사항 확인가ㅡㅇ

    }
    //List

    //update

    public void updateItem(Phone phone, int position ){
        Phone p = phoneList.get(position);
        p.setName(phone.getName());
        p.setTel(phone.getTel());
        notifyDataSetChanged();

    }

    public void removeItem(int position){
      phoneList.remove(position);
      notifyDataSetChanged();



    }
}
