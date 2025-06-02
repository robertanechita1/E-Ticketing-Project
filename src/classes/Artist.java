package classes;

import classes.Interfaces.IArtist;

import java.time.LocalDate;


public class Artist implements IArtist {
    private String nume;
    private String descriere;
    private Double views;

    public Artist(String nume, String descriere, Double views) {
        this.nume = nume;
        this.descriere = descriere;
        this.views = views;
    }

    public String getNume(){
        return this.nume;
    }


    public String getDescriere() {
        return descriere;
    }
    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }
    public Double getViews() {
        return views;
    }
    public void setViews(Double views) {
        this.views = views;
    }

}
