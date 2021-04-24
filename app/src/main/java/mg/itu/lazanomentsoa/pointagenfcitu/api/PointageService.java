package mg.itu.lazanomentsoa.pointagenfcitu.api;

import java.util.List;

import mg.itu.lazanomentsoa.pointagenfcitu.models.Employe;
import mg.itu.lazanomentsoa.pointagenfcitu.models.ReturSucces;
import mg.itu.lazanomentsoa.pointagenfcitu.models.Tache;
import mg.itu.lazanomentsoa.pointagenfcitu.models.UpdateMacEmploye;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PointageService {
    @GET("taches")
    Call<List<Tache>> getTaches();

    //personne
    @GET("employe/{macNfc}")
    Call<Employe> getEmployeByMacNfc(@Path("macNfc") String macNfc);

    @GET("employeMacNFC-Null")
    Call<List<Employe>> getEmployesNullMacNfc();

    @PATCH("employe/{id}")
    Call<ReturSucces> setMadEmployeById(@Path("id") String id, @Body UpdateMacEmploye updateMacEmploye);

    @POST("journees/{idTache}")
    Call<ReturSucces> addJourneeByMacAndTache(@Path("idTache") String idTache, @Body UpdateMacEmploye updateMacEmploye);

}
