package mg.itu.lazanomentsoa.pointagenfcitu.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Employe implements Serializable {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("macNFC")
    @Expose
    private String macNfc;

    @SerializedName("nom")
    @Expose
    private String nom;

    @SerializedName("sexe")
    @Expose
    private String sexe;

    @SerializedName("age")
    @Expose
    private int age;

    @SerializedName("poste")
    @Expose
    private String poste;

    @SerializedName("penalite")
    @Expose
    private int penalite;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMacNfc() {
        return macNfc;
    }

    public void setMacNfc(String macNfc) {
        this.macNfc = macNfc;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPoste() {
        return poste;
    }

    public void setPoste(String poste) {
        this.poste = poste;
    }

    public int getPenalite() {
        return penalite;
    }

    public void setPenalite(int penalite) {
        this.penalite = penalite;
    }
}
