package mg.itu.lazanomentsoa.pointagenfcitu.api;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import mg.itu.lazanomentsoa.pointagenfcitu.models.Employe;
import mg.itu.lazanomentsoa.pointagenfcitu.models.Tache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PointageRepository {
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


    // employe
    public LiveData<Employe> getEmployeByMacNfc(String macNfc){
        MutableLiveData<Employe> employeMutableLiveData = new MutableLiveData<>();
        pointageService.getEmployeByMacNfc(macNfc)
                .enqueue(new Callback<Employe>() {
                    @Override
                    public void onResponse(Call<Employe> call, Response<Employe> response) {
                        employeMutableLiveData.setValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<Employe> call, Throwable t) {
                        employeMutableLiveData.setValue(null);
                    }
                });

        return employeMutableLiveData;
    }
}
