package mg.itu.lazanomentsoa.pointagenfcitu.ui;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import mg.itu.lazanomentsoa.pointagenfcitu.R;
import mg.itu.lazanomentsoa.pointagenfcitu.commun.AbsctractBaseActivity;
import mg.itu.lazanomentsoa.pointagenfcitu.commun.Constante;
import mg.itu.lazanomentsoa.pointagenfcitu.models.Employe;
import mg.itu.lazanomentsoa.pointagenfcitu.models.Journee;
import mg.itu.lazanomentsoa.pointagenfcitu.models.Tache;
import mg.itu.lazanomentsoa.pointagenfcitu.viewmodels.PointageViewModel;

public class MainActivity extends AbsctractBaseActivity {

    private String TAG = "MainActivity";
    public static String ADDRESS_MAC_NFC = "addressMacNFC";
    public static String JOURNEEE_IN_CARTE = "journeeInCarte";
    TextView tvNFCContent;
    TextView tvSerialNumber;
    EditText message;
    Button btnWrite;

    LinearLayout llImageAccueil, llInformations;


    Spinner spinnerTache;
    private List<Tache> listTaches;

    String idJournee = null;

    public static String  employeScanne = "employeScanne";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //spinner
        spinnerTache = (Spinner)findViewById(R.id.spiner_tache);

        context = this;

        tvNFCContent = (TextView) findViewById(R.id.nfc_contents);
        tvSerialNumber = (TextView) findViewById(R.id.tv_serial_number);

        llImageAccueil = (LinearLayout)findViewById(R.id.ll_image_acceuil);
        llInformations = (LinearLayout)findViewById(R.id.ll_informations);

        message = (EditText) findViewById(R.id.edit_message);
        btnWrite = (Button) findViewById(R.id.button);
        //myTag = getIntent().getParcelableExtra(NfcAdapter.EXTRA_TAG);
        btnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        readFromIntent(getIntent());

    }

    /**
     * lecrture du nfc tag
     * @param intent
     */
    private void readFromIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs = null;
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            }
            buildTagViews(msgs);
        }
    }

    private void buildTagViews(NdefMessage[] msgs) {
        if (msgs == null || msgs.length == 0) return;

        String text = "";
//        String tagId = new String(msgs[0].getRecords()[0].getType());
        byte[] payload = msgs[0].getRecords()[0].getPayload();
        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16"; // Get the Text Encoding
        int languageCodeLength = payload[0] & 0063; // Get the Language Code, e.g. "en"
        // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");

        try {
            // Get the Text
            text = new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
            idJournee = text.substring(2,text.length());
        } catch (UnsupportedEncodingException e) {
            Log.i(TAG," valeur dans la tag null");
        }

        tvNFCContent.setText("NFC Content: " + text);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        readFromIntent(intent);
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            myTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String macNfc = ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID));
            showLoading(false);
            Log.i(TAG," idJourn??e => "+idJournee);
            if(idJournee != null){
                pointageViewModel.getJourneeById(idJournee).observe(this, new Observer<Journee>() {
                    @Override
                    public void onChanged(Journee journee) {
                        Log.i(TAG, "valeur => "+journee.getEtat() + " id => "+journee.getId());
                        if(journee != null){
                            Log.i(TAG, "journ??e => " + journee.getEtat() + " journ??e mac => " + journee.getMacNfc() + " id " + journee.getId());
                            if(journee.getEtat() == 0){
                                Intent intent = new Intent(MainActivity.this, ValiderJourneeActivity.class);
                                Log.i(TAG, " journ??e mac => " + journee.getMacNfc() + " id " + journee.getId());
                                intent.putExtra(JOURNEEE_IN_CARTE,journee);
                                startActivity(intent);
                                dismissLoading();
                            }else{
                                isEmployeHasMacNfc(macNfc);
                            }
                        }
                    }
                });
            }else{
                isEmployeHasMacNfc(macNfc);
            }
            idJournee = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        WriteModeOn();
    }

    @Override
    public void onPause() {
        super.onPause();
        WriteModeOff();
    }

    private void isEmployeHasMacNfc(String macNfc){
        pointageViewModel.getEmployeByMacNfc(macNfc).observe(this, new Observer<Employe>() {
            @Override
            public void onChanged(Employe employe) {
                if(employe != null){
                    if(!employe.getEtatRequet().equals(Constante.ETAT_ERREUR_REQUET)){
                        Log.i(TAG, "employe => "+employe.getNom());
                        Intent intent = new Intent(MainActivity.this, BienvenuActivity.class);
                        intent.putExtra(employeScanne,employe);
                        finish();
                        startActivity(intent);
                    }else{
                        Toast.makeText(getApplicationContext(), "Veuillez refaire le scanne de votre tag", Toast.LENGTH_LONG).show();
                    }

                }else{
                    Intent intent = new Intent(MainActivity.this, AffectationNfcToEmployeActivity.class);
                    intent.putExtra(ADDRESS_MAC_NFC, macNfc);
                    finish();
                    startActivity(intent);
                }
                dismissLoading();
            }
        });
    }


}