package classes;

public class Plata implements IPlata {
    private double suma;
    private String ultimeleCifreCard;
    private String status;

    public Plata(double suma, String ultimeleCifreCard, String status) {
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
}