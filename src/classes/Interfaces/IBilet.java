package classes.Interfaces;

public interface IBilet {
    String getCodUnic();
    String getEventName();
    String getCumparator();
    String getTip();
    String getPlata();

    boolean esteValid();

    void setTip(String tip);
    void anuleaza();
    void setValid(boolean valid);
    void setCodUnic(String codUnic);
}

