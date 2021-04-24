package mg.itu.lazanomentsoa.pointagenfcitu.api;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.JsonObject;

import java.util.List;

import mg.itu.lazanomentsoa.pointagenfcitu.commun.Constante;
import mg.itu.lazanomentsoa.pointagenfcitu.models.Employe;
import mg.itu.lazanomentsoa.pointagenfcitu.models.Journee;
import mg.itu.lazanomentsoa.pointagenfcitu.models.ReturSucces;
import mg.itu.lazanomentsoa.pointagenfcitu.models.Tache;
import mg.itu.lazanomentsoa.pointagenfcitu.models.UpdateMacEmploye;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PointageRepository {
    private String TAG ="PointageRepository";
    private static final String NFC_POINTAGE_URL = "https://nfc-api-gp8.herokuapp.com/api/";

    private PointageService pointageService;
    private MutableLiveData<List<Tache>> listTaches;

    public PointageRepository(){
        listTaches = new MutableLiveData<>();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        pointageService = new Retrofit.Builder()
                .baseUrl(NFC_POINTAGE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(PointageService.class);
    }

    public LiveData<List<Tache>> getTaches(){
        pointageService.getTaches()
                .enqueue(new Callback<List<Tache>>() {
                    @Override
                    public void onResponse(Call<List<Tache>> call, Response<List<Tache>> response) {
                        listTaches.setValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<Tache>> call, Throwable t) {
                        listTaches.postValue(null);
                    }
                });

        return  listTaches;
    }

    // journée
    public LiveData<ReturSucces> addJourneeByMadAndTache(String idTache, UpdateMacEmploye updateMacEmploye){
        MutableLiveData<ReturSucces> returSuccesMutableLiveData = new MutableLiveData<>();
        pointageService.addJourneeByMacAndTache(idTache, updateMacEmploye)
                .enqueue(new Callback<ReturSucces>() {
                    @Override
                    public void onResponse(Call<ReturSucces> call, Response<ReturSucces> response) {
                        returSuccesMutableLiveData.setValue(response.body() );
                    }

                    @Override
                    public void onFailure(Call<ReturSucces> call, Throwable t) {
                        returSuccesMutableLiveData.setValue(null);
                    }
                });

        return returSuccesMutableLiveData;
    }

    public LiveData<Journee> getJourneeById(String idJournee){
        MutableLiveData<Journee> journeeMutableLiveData = new MutableLiveData<>();
        pointageService.getJourneeById(idJournee)
                .enqueue(new Callback<Journee>() {
                    @Override
                    public void onResponse(Call<Journee> call, Response<Journee> response) {
                        Log.i(TAG," journee => succes "+response.body().toString());
                        journeeMutableLiveData.setValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<Journee> call, Throwable t) {
                        Log.i(TAG," journee => erreur "+t.getMessage());
                        journeeMutableLiveData.setValue(null);
                    }
                });

        return journeeMutableLiveData;
    }

    public LiveData<ReturSucces> validerJourneeById(String id){
        MutableLiveData<ReturSucces> returSuccesMutableLiveData = new MutableLiveData<>();
        pointageService.valideJourneeById(id)
                .enqueue(new Callback<ReturSucces>() {
                    @Override
                    public void onResponse(Call<ReturSucces> call, Response<ReturSucces> response) {
                        returSuccesMutableLiveData.setValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<ReturSucces> call, Throwable t) {
                        returSuccesMutableLiveData.setValue(null);
                    }
                });
        return returSuccesMutableLiveData;
    }

    // employe
    public LiveData<Employe> getEmployeByMacNfc(String macNfc){
        MutableLiveData<Employe> employeMutableLiveData = new MutableLiveData<>();
        pointageService.getEmployeByMacNfc(macNfc)
                .enqueue(new Callback<Employe>() {
                    @Override
                    public void onResponse(Call<Employe> call, Response<Employe> response) {
                        if(response.body() != null){
                            Employe employe = response.body();
                            employe.setEtatRequet(Constante.ETAT_SUCCES_REQUET);
                            employeMutableLiveData.setValue(employe);
                        }else{
                            employeMutableLiveData.setValue(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<Employe> call, Throwable t) {
                        employeMutableLiveData.setValue(new Employe(Constante.ETAT_ERREUR_REQUET));
                    }
                });

        return employeMutableLiveData;
    }

    public LiveData<List<Employe>> getEmployesNulMacNfc(){
        MutableLiveData<List<Employe>> listMutableLiveData = new MutableLiveData<>();
        pointageService.getEmployesNullMacNfc().enqueue(new Callback<List<Employe>>() {
            @Override
            public void onResponse(Call<List<Employe>> call, Response<List<Employe>> response) {
                listMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Employe>> call, Throwable t) {
                listMutableLiveData.setValue(null);
            }
        });

        return  listMutableLiveData;
    }

    public LiveData<ReturSucces> updateMacEmployeById(String id, UpdateMacEmploye updateMacEmploye){
        MutableLiveData<ReturSucces> returSuccesMutableLiveData = new MutableLiveData<>();
        pointageService.setMadEmployeById(id, updateMacEmploye).enqueue(new Callback<ReturSucces>() {
            @Override
            public void onResponse(Call<ReturSucces> call, Response<ReturSucces> response) {
                returSuccesMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ReturSucces> call, Throwable t) {
                returSuccesMutableLiveData.setValue(null);
                Log.i(TAG, "erreur => "+ t.getMessage());
            }
        });

        return returSuccesMutableLiveData;
    }
}
