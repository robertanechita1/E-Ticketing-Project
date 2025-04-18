package classes;

import java.util.UUID;

public class Bilet implements IBilet {
    private final String codUnic;
    private final String eventName;
    private final String cumparator;
    private boolean valid;
    private String tip;
    Plata plata;

    public Bilet(String eventName, String cumparator,  String tip, Plata plata) {
        this.codUnic = UUID.randomUUID().toString(); // genereaza un cod unic
        this.eventName = eventName;
        this.cumparator = cumparator;
        this.valid = true;
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
    public void setValid(boolean b) {
        this.valid = b;
        plata.setStatus("Rambursat");
    }

    @Override
    public String toString() {
        return "Bilet\n" +
                "    cod = '" + codUnic + "'\n" +
                "    event = '" + eventName + "'\n" +
                "    cumparator = '" + cumparator + "'\n" +
                "    valid = " + valid+ "'\n" +
                "    status plata = " + plata.getStatus() + "\n\n";
    }
}

