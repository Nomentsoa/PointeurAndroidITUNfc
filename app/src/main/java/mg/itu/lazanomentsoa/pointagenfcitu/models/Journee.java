package mg.itu.lazanomentsoa.pointagenfcitu.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Journee implements Serializable {

    @SerializedName("_id")
    @Expose
    private String id;

    @SerializedName("macNFC")
    @Expose
    private String macNfc;

    @SerializedName("tache")
    @Expose
    private Tache tache;

    @SerializedName("etat")
    @Expose
    private int etat;

    @SerializedName("heureDebut")
    @Expose
    private String heureDebut;

    @SerializedName("heureSortie")
    @Expose
    private String heureSortie;

    @SerializedName("__v")
    @Expose
    private int veresion;

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

    public Tache getTache() {
        return tache;
    }

    public void setTache(Tache tache) {
        this.tache = tache;
    }

    public int getEtat() {
        return etat;
    }

    public void setEtat(int etat) {
        this.etat = etat;
    }

    public String getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(String heureDebut) {
        this.heureDebut = heureDebut;
    }

    public String getHeureSortie() {
        return heureSortie;
    }

    public void setHeureSortie(String heureSortie) {
        this.heureSortie = heureSortie;
    }

    public int getVeresion() {
        return veresion;
    }

    public void setVeresion(int veresion) {
        this.veresion = veresion;
    }
}
