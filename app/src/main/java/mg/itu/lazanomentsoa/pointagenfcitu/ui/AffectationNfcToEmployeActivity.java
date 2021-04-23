package mg.itu.lazanomentsoa.pointagenfcitu.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.util.List;

import mg.itu.lazanomentsoa.pointagenfcitu.R;
import mg.itu.lazanomentsoa.pointagenfcitu.adapter.CustomAdapterSpinnerEmploye;
import mg.itu.lazanomentsoa.pointagenfcitu.commun.AbsctractBaseActivity;
import mg.itu.lazanomentsoa.pointagenfcitu.models.Employe;
import mg.itu.lazanomentsoa.pointagenfcitu.models.ReturSucces;
import mg.itu.lazanomentsoa.pointagenfcitu.models.UpdateMacEmploye;

public class AffectationNfcToEmployeActivity extends AbsctractBaseActivity {
    private Context context;
    private String TAG = "AffectationNfcToEmployeActivity";
    private Spinner spinnerEmploye;
    private MaterialButton btnValideEmploye;
    private List<Employe> employeList;
    private LifecycleOwner lifecycleOwner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affectation_nfc_to_employe);
        setToolBar();

        this.context = this;
        this.lifecycleOwner = this;
        spinnerEmploye = (Spinner)findViewById(R.id.spiner_employe);
        btnValideEmploye = (MaterialButton)findViewById(R.id.btn_valide_employe);

        showLoading(false);
        pointageViewModel.getEmployesNullMacNfc().observe(this, new Observer<List<Employe>>() {
            @Override
            public void onChanged(List<Employe> employes) {
                if(employes == null){
                    Toast.makeText(context, "Erreur sur la récupération des employes.", Toast.LENGTH_LONG).show();
                }else{
                    employeList = employes;
                    Log.i(TAG," liste employes => "+employes);
                    CustomAdapterSpinnerEmploye customAdapterSpinnerEmploye = new CustomAdapterSpinnerEmploye(employes);
                    spinnerEmploye.setAdapter(customAdapterSpinnerEmploye);
                }
                dismissLoading();
            }
        });

        btnValideEmploye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoading(false);
                Employe employeSelected = employeList.get(spinnerEmploye.getSelectedItemPosition());
                Log.i(TAG, "selected employe => " + employeSelected.getNom());
                String adressMac = (String)getIntent().getStringExtra(MainActivity.ADDRESS_MAC_NFC);
                Log.i(TAG, "adresse mack => "+adressMac);
                pointageViewModel.updateMacEmployeById(employeSelected.getId(), new UpdateMacEmploye(adressMac)).observe(lifecycleOwner, new Observer<ReturSucces>() {
                    @Override
                    public void onChanged(ReturSucces returSucces) {
                        if(returSucces != null){
                            startActivity(new Intent(AffectationNfcToEmployeActivity.this, MainActivity.class));
                            finish();
                        }else{
                            Toast.makeText(context, "Erreur sur l'affectation de carte sur l'employe", Toast.LENGTH_LONG).show();
                        }
                        dismissLoading();
                    }
                });

            }
        });
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
                this.startActivity(new Intent(AffectationNfcToEmployeActivity.this, MainActivity.class));
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.startActivity(new Intent(AffectationNfcToEmployeActivity.this, MainActivity.class));
        this.finish();
    }
}