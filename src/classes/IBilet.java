package classes;

public interface IBilet {
    String getCodUnic();
    String getEventName();
    String getCumparator();
    String getTip();

    boolean esteValid();

    void setTip(String tip);
    void anuleaza();
    void setValid(boolean b);
}

