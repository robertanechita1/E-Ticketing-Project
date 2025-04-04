package classes;

import java.util.UUID;

public class Bilet implements IBilet {
    private final String codUnic;
    private final String eventName;
    private final String cumparator;
    private double pret;
    private boolean valid;
    private String tip;

    public Bilet(String eventName, String cumparator, double pret, String tip) {
        this.codUnic = UUID.randomUUID().toString(); // genereaza un cod unic
        this.eventName = eventName;
        this.cumparator = cumparator;
        this.pret = pret;
        this.valid = true;
        this.tip = tip;
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
    public double getPret() {
        return pret;
    }

    @Override
    public void setPret(double pret) {
        this.pret = pret;
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
    public String toString() {
        return "Bilet\n" +
                "    cod = '" + codUnic + "'\n" +
                "    event = '" + eventName + "'\n" +
                "    cumparator = '" + cumparator + "'\n" +
                "    pret = " + pret + "\n" +
                "    valid = " + valid;
    }
}

