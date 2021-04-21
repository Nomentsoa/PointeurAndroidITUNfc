package mg.itu.lazanomentsoa.pointagenfcitu.api;

import java.util.List;

import mg.itu.lazanomentsoa.pointagenfcitu.models.Employe;
import mg.itu.lazanomentsoa.pointagenfcitu.models.Tache;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PointageService {
    @GET("taches")
    Call<List<Tache>> getTaches();

    //personne
    @GET("employe/{macNfc}")
    Call<Employe> getEmployeByMacNfc(@Path("macNfc") String macNfc);
}
