package mg.itu.lazanomentsoa.pointagenfcitu.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;


import android.app.PendingIntent;
import android.content.Context;
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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.button.MaterialButton;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mg.itu.lazanomentsoa.pointagenfcitu.R;
import mg.itu.lazanomentsoa.pointagenfcitu.adapter.CustomAdapterSpinnerTache;
import mg.itu.lazanomentsoa.pointagenfcitu.commun.AbsctractBaseActivity;
import mg.itu.lazanomentsoa.pointagenfcitu.commun.Constante;
import mg.itu.lazanomentsoa.pointagenfcitu.models.Employe;
import mg.itu.lazanomentsoa.pointagenfcitu.models.ReturSucces;
import mg.itu.lazanomentsoa.pointagenfcitu.models.Tache;
import mg.itu.lazanomentsoa.pointagenfcitu.models.UpdateMacEmploye;

public class BienvenuActivity extends AbsctractBaseActivity {
    private String TAG = "BienvenuActivity";
    private Employe employeScanned;
    private TextView tvInformationEmployeScanned, tvPostEmployeScanned, tvHeurePointage;
    private Spinner spinnerTache;
    private Context context;
    private MaterialButton btnValiderTache, btnMettreJourneeInCarte;
    private List<Tache> tacheList;
    private String idJournee;
    private LifecycleOwner lifecycleOwner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenu);
        this.context = this;
        this.lifecycleOwner = this;
        setToolBar();
        employeScanned = (Employe)getIntent().getSerializableExtra(MainActivity.employeScanne);


        tvInformationEmployeScanned = (TextView)findViewById(R.id.tv_information_employee_scanned);
        tvPostEmployeScanned = (TextView)findViewById(R.id.tv_poste_employee);
        tvHeurePointage = (TextView)findViewById(R.id.tv_heure_pointage);

        btnMettreJourneeInCarte = (MaterialButton)findViewById(R.id.btn_mettre_tache_dans_carte);

        spinnerTache = (Spinner)findViewById(R.id.spiner_tache);
        Date currentTime = Calendar.getInstance().getTime();

        if(employeScanned != null){
           if(employeScanned.getSexe().equals("Homme")){
               tvInformationEmployeScanned.setText("Mr "+employeScanned.getNom());
           }else{
               if(employeScanned.getAge() < 18)
                   tvInformationEmployeScanned.setText("Mlle "+employeScanned.getNom());
               else
                   tvInformationEmployeScanned.setText("Mme "+employeScanned.getNom());
           }
           tvPostEmployeScanned.setText(employeScanned.getPoste());
           tvHeurePointage.setText(DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(currentTime));
        }else{

        }


        showLoading(false);
        pointageViewModel.getTaches().observe(this, new Observer<List<Tache>>() {
                @Override
                public void onChanged(List<Tache> taches) {
                    if(taches !=  null){
                        tacheList = taches;
                        CustomAdapterSpinnerTache customAdapterSpinnerTache = new CustomAdapterSpinnerTache(taches);
                        spinnerTache.setAdapter(customAdapterSpinnerTache);
                    }else{
                        Toast.makeText(context, "Erreur sur la récupération des tâches.", Toast.LENGTH_LONG).show();
                    }
                    dismissLoading();
                }
            });


        btnValiderTache = (MaterialButton)findViewById(R.id.btn_valider_tache);
        btnValiderTache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnMettreJourneeInCarte.setVisibility(View.GONE);
                showLoading(false);
                pointageViewModel.addJourneeByMacAndTache(tacheList.get(spinnerTache.getSelectedItemPosition()).getId(),new UpdateMacEmploye(employeScanned.getMacNfc()) ).observe(lifecycleOwner, new Observer<ReturSucces>() {
                    @Override
                    public void onChanged(ReturSucces returSucces) {
                        if(returSucces != null){
                            btnMettreJourneeInCarte.setVisibility(View.VISIBLE);
                            idJournee = returSucces.getMessage();
                            Log.i(TAG, "idJournée => " + idJournee);
                        }
                        dismissLoading();
                    }
                });
            }
        });

        btnMettreJourneeInCarte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (myTag == null) {
                        Toast.makeText(context, ERROR_DETECTED, Toast.LENGTH_LONG).show();
                    } else {
                        write(idJournee, myTag);
                        Toast.makeText(context, WRITE_SUCCESS, Toast.LENGTH_LONG).show();
                        startActivity(new Intent(BienvenuActivity.this, MainActivity.class));
                    }
                } catch (IOException e) {
                    Toast.makeText(context, WRITE_ERROR, Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                } catch (FormatException e) {
                    Toast.makeText(context, WRITE_ERROR, Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);

        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            myTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        }
    }

    private void setToolBar(){
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
               this.startActivity(new Intent(BienvenuActivity.this, MainActivity.class));
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.startActivity(new Intent(BienvenuActivity.this, MainActivity.class));
        this.finish();
    }

    /**
     * Ecriture dans le NFC TAG
     * @param text
     * @param tag
     * @throws IOException
     * @throws FormatException
     */
    private void write(String text, Tag tag) throws IOException, FormatException {
        NdefRecord[] records = {createRecord(text)};
        NdefMessage message = new NdefMessage(records);
        // Get an instance of Ndef for the tag.
        Ndef ndef = Ndef.get(tag);
        Log.i("tag", " tag => " + tag + "ndef => " + ndef);


        if (ndef != null) {
            NdefMessage ndefMesg = ndef.getCachedNdefMessage();
            if (ndefMesg != null) {
                // Enable I/O
                ndef.connect();
                // Write the message
                ndef.writeNdefMessage(message);
                // Close the connection
                ndef.close();
            }
        } else {
            NdefFormatable ndefFormatable = NdefFormatable.get(tag);
            if (ndefFormatable != null) {
                // initialize tag with new NDEF message
                try {
                    ndefFormatable.connect();
                    ndefFormatable.format(message);
                } finally {
                    try {
                        ndefFormatable.close();
                    } catch (Exception e) {
                    }
                }
            }
        }
    }

    private NdefRecord createRecord(String text) throws UnsupportedEncodingException {
        String lang = "en";
        byte[] textBytes = text.getBytes();
        byte[] langBytes = lang.getBytes("US-ASCII");
        int langLength = langBytes.length;
        int textLength = textBytes.length;
        byte[] payload = new byte[1 + langLength + textLength];

        // set status byte (see NDEF spec for actual bits)
        payload[1] = (byte) langLength;

        // copy langbytes and textbytes into payload
        System.arraycopy(langBytes, 0, payload, 1, langLength);
        System.arraycopy(textBytes, 0, payload, 1 + langLength, textLength);

        NdefRecord recordNFC = new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], payload);

        return recordNFC;
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
}