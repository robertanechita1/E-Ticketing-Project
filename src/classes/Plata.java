package classes;

import java.util.UUID;

public class Plata implements IPlata {
    private String cod_unic;
    private double suma;
    private String ultimeleCifreCard;
    private String status;

    public Plata(){}

    public Plata(double suma, String ultimeleCifreCard, String status) {
        this.cod_unic = UUID.randomUUID().toString();
        this.suma = suma;
        this.ultimeleCifreCard = ultimeleCifreCard;
        this.status = status;
    }

    @Override
    public double getSuma() {
        return suma;
    }


    @Override
    public String getUltimeleCifreCard() {
        return ultimeleCifreCard;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Plata{" +
                "suma=" + suma +
                ", card='****" + ultimeleCifreCard + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    @Override
    public String getcod(){
        return cod_unic;
    }
}