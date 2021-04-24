package mg.itu.lazanomentsoa.pointagenfcitu.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import mg.itu.lazanomentsoa.pointagenfcitu.R;
import mg.itu.lazanomentsoa.pointagenfcitu.commun.AbsctractBaseActivity;
import mg.itu.lazanomentsoa.pointagenfcitu.models.Employe;
import mg.itu.lazanomentsoa.pointagenfcitu.models.Journee;
import mg.itu.lazanomentsoa.pointagenfcitu.models.ReturSucces;

public class ValiderJourneeActivity extends AbsctractBaseActivity {
    private TextView tvInformationEmploye, tvPosteEmploye, tvTacheEmploye, tvErreur;
    private MaterialButton btnValideJournee;
    private Journee journeeSelected;
    private LifecycleOwner lifecycleOwner;
    private LinearLayout llInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valider_journee);
        setToolBar();
        this.lifecycleOwner = this;

        tvInformationEmploye = (TextView)findViewById(R.id.tv_information_employee_scanned);
        tvPosteEmploye = (TextView)findViewById(R.id.tv_poste_employee);
        tvTacheEmploye = (TextView)findViewById(R.id.tv_tache);
        btnValideJournee = (MaterialButton)findViewById(R.id.btn_valider_journee);
        llInformation = (LinearLayout)findViewById(R.id.ll_informations);
        tvErreur = (TextView)findViewById(R.id.tv_erreur);

        journeeSelected =(Journee) getIntent().getSerializableExtra(MainActivity.JOURNEEE_IN_CARTE);
        showLoading(false);
        pointageViewModel.getEmployeByMacNfc(journeeSelected.getMacNfc()).observe(this, new Observer<Employe>() {
            @Override
            public void onChanged(Employe employe) {
                if(employe != null){
                    if(employe.getSexe().equals("Homme")){
                        tvInformationEmploye.setText("Mr " + employe.getNom());
                    }else{
                        tvInformationEmploye.setText("Mme " + employe.getNom());
                    }
                    tvPosteEmploye.setText(employe.getPoste());
                    tvTacheEmploye.setText(journeeSelected.getTache().getNom());
                    llInformation.setVisibility(View.VISIBLE);
                    tvErreur.setVisibility(View.GONE);
                }else{
                    llInformation.setVisibility(View.GONE);
                    tvErreur.setVisibility(View.VISIBLE);
                }
                dismissLoading();
            }
        });

         btnValideJournee.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 showLoading(false);
                 pointageViewModel.validerJourneeById(journeeSelected.getId()).observe(lifecycleOwner, new Observer<ReturSucces>() {
                     @Override
                     public void onChanged(ReturSucces returSucces) {
                         if(returSucces != null){
                             Toast.makeText(getApplicationContext(), "La journée est validée.", Toast.LENGTH_LONG).show();
                             dismissLoading();
                             startActivity(new Intent(ValiderJourneeActivity.this, MainActivity.class));
                             finish();
                         }
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
                this.startActivity(new Intent(ValiderJourneeActivity.this, MainActivity.class));
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.startActivity(new Intent(ValiderJourneeActivity.this, MainActivity.class));
        this.finish();
    }
}