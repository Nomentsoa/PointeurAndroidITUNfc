package mg.itu.lazanomentsoa.pointagenfcitu.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tache {
    @SerializedName("_id")
    @Expose
    private String id;

    @SerializedName("nom")
    @Expose
    private String nom;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}