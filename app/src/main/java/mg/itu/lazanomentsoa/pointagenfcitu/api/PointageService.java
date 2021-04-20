package mg.itu.lazanomentsoa.pointagenfcitu.api;

import java.util.List;

import mg.itu.lazanomentsoa.pointagenfcitu.models.Tache;
import retrofit2.Call;
import retrofit2.http.GET;

public interface PointageService {
    @GET("taches")
    Call<List<Tache>> getTaches();
}
