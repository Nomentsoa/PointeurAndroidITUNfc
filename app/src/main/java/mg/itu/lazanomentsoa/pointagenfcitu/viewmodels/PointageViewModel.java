package mg.itu.lazanomentsoa.pointagenfcitu.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import mg.itu.lazanomentsoa.pointagenfcitu.api.PointageRepository;
import mg.itu.lazanomentsoa.pointagenfcitu.models.Employe;
import mg.itu.lazanomentsoa.pointagenfcitu.models.Tache;

public class PointageViewModel extends AndroidViewModel {
    private PointageRepository pointageRepository;
    private LiveData<List<Tache>> listeTaches;
    public PointageViewModel(@NonNull Application application) {
        super(application);
    }

    public void init(){
        pointageRepository = new PointageRepository();
        listeTaches = pointageRepository.getTaches();
    }

    public LiveData<List<Tache>> getTaches(){
        return listeTaches;
    }

    // employe
    public LiveData<Employe> getEmployeByMacNfc(String macNfc){
        return pointageRepository.getEmployeByMacNfc(macNfc);
    }
}
