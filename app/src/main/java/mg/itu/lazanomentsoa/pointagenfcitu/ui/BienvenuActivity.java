package mg.itu.lazanomentsoa.pointagenfcitu.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Spinner;
import android.widget.TextView;


import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mg.itu.lazanomentsoa.pointagenfcitu.R;
import mg.itu.lazanomentsoa.pointagenfcitu.adapter.CustomAdapterSpinnerTache;
import mg.itu.lazanomentsoa.pointagenfcitu.commun.AbsctractBaseActivity;
import mg.itu.lazanomentsoa.pointagenfcitu.models.Employe;
import mg.itu.lazanomentsoa.pointagenfcitu.models.Tache;

public class BienvenuActivity extends AbsctractBaseActivity {
    private Employe employeScanned;
    private TextView tvInformationEmployeScanned, tvPostEmployeScanned, tvHeurePointage;
    private Spinner spinnerTache;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenu);
        setToolBar();
        employeScanned = (Employe)getIntent().getSerializableExtra(MainActivity.employeScanne);

        tvInformationEmployeScanned = (TextView)findViewById(R.id.tv_information_employee_scanned);
        tvPostEmployeScanned = (TextView)findViewById(R.id.tv_poste_employee);
        tvHeurePointage = (TextView)findViewById(R.id.tv_heure_pointage);

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
                        dismissLoading();
                        CustomAdapterSpinnerTache customAdapterSpinnerTache = new CustomAdapterSpinnerTache(taches);
                        spinnerTache.setAdapter(customAdapterSpinnerTache);
                    }else{

                    }
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
               this.startActivity(new Intent(BienvenuActivity.this, MainActivity.class));
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}