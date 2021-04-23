package mg.itu.lazanomentsoa.pointagenfcitu.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateMacEmploye {
    @SerializedName("macNFC")
    @Expose
    private String macNfc;

    public UpdateMacEmploye(String macNfc) {
        this.macNfc = macNfc;
    }
}
