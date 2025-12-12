package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private List<Pytanie> pytania;
    TextView textViewPytanie;
    RadioGroup radioGroup;
    int radioButtonid[] = new int[]{
            R.id.radioButton,
            R.id.radioButton2,
            R.id.radioButton3
    };
    RadioButton radioButton_a;
    RadioButton radioButton_b;
    RadioButton radioButton_c;
    Button buttonDalej;
    int aktualnePytanie = 0;
    int sumaPunktow = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewPytanie = findViewById(R.id.textViewTrescPytania);
        radioGroup = findViewById(R.id.radioGroup);
        radioButton_a = findViewById(R.id.radioButton);
        radioButton_b = findViewById(R.id.radioButton2);
        radioButton_c = findViewById(R.id.radioButton3);
        buttonDalej = findViewById(R.id.button);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://my-json-server.typicode.com/mechaniktgmobilne/retrofit_pytania/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi jsonPlaceHolderApi
                = retrofit.create(JsonPlaceHolderApi.class);
        Call<List<Pytanie>> call = jsonPlaceHolderApi.getPytania();
        call.enqueue(
                new Callback<List<Pytanie>>() {
                    @Override
                    public void onResponse(Call<List<Pytanie>> call, Response<List<Pytanie>> response) {
                        if(!response.isSuccessful()){
                            Toast.makeText(MainActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        pytania = response.body();
                        Toast.makeText(MainActivity.this,
                                pytania.get(0).getTrescPytania(), Toast.LENGTH_SHORT).show();
                    wyswietlPytanie(0);
                    }

                    @Override
                    public void onFailure(Call<List<Pytanie>> call, Throwable t) {
                        Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        buttonDalej.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (sprawdzOdpowiedz(aktualnePytanie)) {
                            sumaPunktow++;
                            Toast.makeText(MainActivity.this, "dobrze", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "źle", Toast.LENGTH_SHORT).show();
                        }
                        if (aktualnePytanie < pytania.size() - 1) {
                            aktualnePytanie++;
                            wyswietlPytanie(aktualnePytanie);
                        } else {

                            //TODO:koniec testu
                            //podliczenie punktow znika wszystko wysyłamy wynik sms
                            radioGroup.setVisibility(View.INVISIBLE);
                            textViewPytanie.setText("Koniec testu, punkty:"+sumaPunktow);
                            buttonDalej.setVisibility(View.INVISIBLE);
                        }
                    }
                }
        );
    }

    private boolean sprawdzOdpowiedz(int aktualnePytanie) {
        Pytanie pytanie = pytania.get(aktualnePytanie);
        if(radioGroup.getCheckedRadioButtonId() == radioButtonid[pytanie.getPoprawna()])
            return true;
        return false;
    }

    private void wyswietlPytanie(int ktore){
        Pytanie pytanie =pytania.get(ktore);
        textViewPytanie.setText(pytanie.getTrescPytania());
        radioButton_a.setText(pytanie.getOdpa());
        radioButton_b.setText(pytanie.getOdpb());
        radioButton_c.setText(pytanie.getOdpc());
        radioButton_a.setChecked(false);
        radioButton_b.setChecked(false);
        radioButton_c.setChecked(false);
    }
}
