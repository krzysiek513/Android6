package pl.studia.tipcalvulator;


import android.os.Bundle; // do zapisywania informacji o stanie
import android.support.v7.app.AppCompatActivity; // klasa bazowa
import android.text.Editable; // do obsługi zdarzeń EditText
import android.text.TextWatcher; // obiekt nasłuchujący EditText
import android.widget.EditText; // do obsługi danych wejściowych kwoty rachunku
import android.widget.SeekBar; // do zmiany procentu napiwku
import android.widget.SeekBar.OnSeekBarChangeListener; // obiekt nasłuchujący SeekBar
import android.widget.TextView; // do wyświetlania tekstu

import java.text.NumberFormat; // do formatowania waluty

// Klasa MainActivity aplikacji Tip Calculator
public class MainActivity extends AppCompatActivity {

    // obiekty formatujące obiekty walutowe i procentowe
    private static final NumberFormat currencyFormat =
            NumberFormat.getCurrencyInstance();
    private static final NumberFormat percentFormat =
            NumberFormat.getPercentInstance();

    private double billAmount = 0.0; // kwota rachunku wprowadzona przez użytkownika
    private double percent = 0.15; // początkowy procent napiwku
    private TextView amountTextView; // pokazuje sformatowaną kwotę rachunku
    private TextView percentTextView; // pokazuje procent napiwku
    private TextView tipTextView; // pokazuje obliczoną kwotę napiwku
    private TextView totalTextView; // pokazuje obliczoną kwotę całkowitej należności

    // wywoływany przy tworzeniu klasy Activity po raz pierwszy
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // wywołuje wersję klasy nadrzędnej
        setContentView(R.layout.activity_main); // tworzy graficzny interfejs użytkownika

        // uzyskuje odwołania do pół TextView, których zawartość jest określana za pomocą kodu
        amountTextView = (TextView) findViewById(R.id.amountTextView);
        percentTextView = (TextView) findViewById(R.id.percentTextView);
        tipTextView = (TextView) findViewById(R.id.tipTextView);
        totalTextView = (TextView) findViewById(R.id.totalTextView);
        tipTextView.setText(currencyFormat.format(0));
        totalTextView.setText(currencyFormat.format(0));

        // definiuje parametr amountEditText obiektu TextWatcher
        EditText amountEditText =
                (EditText) findViewById(R.id.amountEditText);
        amountEditText.addTextChangedListener(amountEditTextWatcher);

        // definiuje parametr OnSeekBarChangeListener obiektu percentSeekBar
        SeekBar percentSeekBar =
                (SeekBar) findViewById(R.id.percentSeekBar);
        percentSeekBar.setOnSeekBarChangeListener(seekBarListener);
    }

    // oblicz i wyświetl kwotę napiwku oraz całkowitą należność
    private void calculate() {
        // sformatuj wartość procentową i wyświetl ją w percentTextView
        percentTextView.setText(percentFormat.format(percent));

        // oblicz kwotę napiwku oraz całkowitą należność
        double tip = billAmount * percent;
        double total = billAmount + tip;

        // wyświetl kwotę napiwku oraz całkowitą należność sformatowane jako wartości walutowe
        tipTextView.setText(currencyFormat.format(tip));
        totalTextView.setText(currencyFormat.format(total));
    }

    // obiekty nasłuchujące zdarzeń zmian położenia suwaka SeekBar
    private final OnSeekBarChangeListener seekBarListener =
            new OnSeekBarChangeListener() {
                // zaktualizuj procent, a następnie wykonaj obliczenia
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress,
                                              boolean fromUser) {
                    percent = progress / 100.0; // określa procent w zależności od położenia suwaka
                    calculate(); // oblicz i wyświetl kwotę napiwku i całkowitą należność
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) { }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) { }
            };

    // obiekt nasłuchujący zdarzeń zmian tekstu pola EditText
    private final TextWatcher amountEditTextWatcher = new TextWatcher() {
        // kod wywoływany, gdy użytkownik zmodyfikuje kwotę rachunku
        @Override
        public void onTextChanged(CharSequence s, int start,
                                  int before, int count) {

            try { // odczytaj kwotę rachunku i wyświetl wartość sformatowaną jako waluta
                billAmount = Double.parseDouble(s.toString()) / 100.0;
                amountTextView.setText(currencyFormat.format(billAmount));
            }
            catch (NumberFormatException e) { // gdy łańcuch jest pusty lub nie jest ciągiem liczb
                amountTextView.setText("");
                billAmount = 0.0;
            }

            calculate(); // zaktualizuj pola TextView napiwku i całkowitej należności
        }

        @Override
        public void afterTextChanged(Editable s) { }

        @Override
        public void beforeTextChanged(
                CharSequence s, int start, int count, int after) { }
    };
}
