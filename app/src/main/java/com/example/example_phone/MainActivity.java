package com.example.example_phone;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PhoneAdapter phoneAdapter;
    LinearLayoutManager manager;
    private FloatingActionButton floatinBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        floatinBtn = findViewById(R.id.floatingBtn);
        floatinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //팝업창을 띄워 contact.xml파일을 띄우면 된다.
                addContact();

            }
        });
        //객체가 만들어져있으면 만들어져 있던 애 반환, 아니면 객체 만들어서 반환
        PhoneService phoneService = Retrofit2Client.getInstance().getPhoneService();
        Call<List<Phone>> call = phoneService.findAll();

        call.enqueue(new Callback<List<Phone>>() {
            @Override
            public void onResponse(Call<List<Phone>> call, Response<List<Phone>> response) {
                List<Phone> phoneList = response.body();
                recyclerView = findViewById(R.id.recyclerView); //view에 뿌리기 위해 객체 생성
                manager = new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false);
                recyclerView.setLayoutManager(manager);

                //data가져오기
                phoneAdapter = new PhoneAdapter(phoneList);
                //가져온 data recyclerView에 연결
                recyclerView.setAdapter(phoneAdapter);
                Log.d("data", "OnResponse: 응답 받은 데이터: " + phoneList);


            }

            @Override
            public void onFailure(Call<List<Phone>> call, Throwable t) {

            }
        });
    }
        private void addContact(){
            View dialogView = LayoutInflater.from(getApplicationContext())
                    .inflate(R.layout.layout_add_concat,null);

            final EditText etName = dialogView.findViewById(R.id.etname);
            final EditText etTel = dialogView.findViewById(R.id.ettel);

            AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
            dlg.setTitle("연락처 등록");
            dlg.setView(dialogView);
            dlg.setPositiveButton("등록", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Phone phoneDto = new Phone();
                    phoneDto.setName(etName.getText().toString());
                    phoneDto.setTel(etTel.getText().toString());

                    Log.d("insert", "onClick: 등록 클릭시 값 확인" + phoneDto );

                    PhoneService phoneService = Retrofit2Client.getInstance().getPhoneService();
                    Call<Phone> call = phoneService.save(phoneDto);

                    call.enqueue(new Callback<Phone>() {
                        @Override
                        public void onResponse(Call<Phone> call, Response<Phone> response) {
                            Log.d("insert response", "onResponse: phoneDto" + phoneDto);
                            phoneAdapter.addItem(response.body());
                        }

                        @Override
                        public void onFailure(Call<Phone> call, Throwable t) {

                        }
                    });


                }
            });
            dlg.setNegativeButton("닫기", null);
            dlg.show();


        }
    }