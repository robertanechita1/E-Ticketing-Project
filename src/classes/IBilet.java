package classes;

public interface IBilet {
    String getCodUnic();
    String getEventName();
    String getCumparator();
    String getTip();

    double getPret();
    boolean esteValid();

    void setTip(String tip);
    void setPret(double pret);
    void anuleaza();
}

