package com.example.currentlatlon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class InfoActivity extends AppCompatActivity {

    TextView tvInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        tvInfo = findViewById(R.id.tvInfo);

        String information = "1. Aby włączyć ochronę w aplikacji kliknij w przycisk włącz ochronę.\n" +
                "2. Musisz mieć włączony dostęp do internetu oraz właczyć lokalizację.\n" +
                " 3. Po kliknieciu w przycisk dodaj nowy punkt możesz dodać nowe przejscie o długości i szerokości takiej jak na ekranie głownym.\n" +
                " 4. Nie możesz podawać wartości ujemnych. \n" +
                " 5. Możesz przeglądać gdzie juz zostaly dodane punkty oznaczajace poczatek i koniec przejscia \n" +
                " 6. Użytkownik nie może usuwać punktów \n" +
                " 7. Gdy zblizasz sie do danego przejscia dla pieszych zostaniesz powiadomiony o tym sygnalem dzwiękowym oraz wibracją";

        tvInfo.setText(information);
    }
}