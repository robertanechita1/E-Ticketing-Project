package classes;

import java.util.UUID;

import classes.Interfaces.IBilet;

public class Bilet implements IBilet {
    private String codUnic;
    private final String eventName;
    private final String cumparator;
    private boolean valid;
    private String tip;
    private String plata;

    public Bilet() {
        this.codUnic = null;
        this.eventName = null;
        this.cumparator = null;
        this.valid = false;
    }

    public Bilet(String eventName, String cumparator,  String tip, String plata) {
        this.codUnic = UUID.randomUUID().toString(); // genereaza un cod unic
        this.eventName = eventName;
        this.cumparator = cumparator;
        this.valid = true;
        this.tip = tip;
        this.plata = plata;
    }

    public Bilet(String codUnic, String eventName, String cumparator, boolean valid, String tip, String plata) {
        this.codUnic = codUnic;
        this.eventName = eventName;
        this.cumparator = cumparator;
        this.valid = valid;
        this.tip = tip;
        this.plata = plata;
    }

    @Override
    public String getCodUnic() {
        return codUnic;
    }

    @Override
    public String getEventName() {
        return eventName;
    }

    @Override
    public String getCumparator() {
        return cumparator;
    }

    @Override
    public String getTip() {
        return tip;
    }

    @Override
    public String getPlata() {
        return plata;
    }

    @Override
    public void setTip(String tip) {
        this.tip = tip;
    }

    @Override
    public boolean esteValid() {
        return valid;
    }

    @Override
    public void anuleaza() {
        this.valid = false;
    }

    @Override
    public void setValid(boolean valid){
        this.valid = valid;
    }

    @Override
    public void setCodUnic(String codUnic){
        this.codUnic = codUnic;
    }

    @Override
    public String toString() {


        return "Bilet\n" +
                "    cod = '" + codUnic + "'\n" +
                "    event = '" + eventName + "'\n" +
                "    cumparator = '" + cumparator + "'\n" +
                "    valid = " + valid+ "'\n" +
                "    cod plata = " + plata + "\n\n";
    }
}