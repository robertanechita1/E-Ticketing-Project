package classes;

import java.time.LocalDate;

public class OneDayPass extends Bilet {
    private LocalDate ziAcces;
    private boolean includeCamping;

    public OneDayPass(String eventName, String cumparator, LocalDate ziAcces, boolean includeCamping, String tip, Plata plata) {
        super(eventName, cumparator, tip, plata);
        this.ziAcces = ziAcces;
        this.includeCamping = includeCamping;
    }

    public LocalDate getZiAcces() {
        return ziAcces;
    }

    public void setZiAcces(LocalDate ziAcces) {
        this.ziAcces = ziAcces;
    }

    public boolean isIncludeCamping() {
        return includeCamping;
    }

    public void setIncludeCamping(boolean includeCamping) {
        this.includeCamping = includeCamping;
    }

    @Override
    public String toString() {
        return super.toString() +
                " (OneDayPass pentru " + ziAcces + ", camping inclus: " + includeCamping + ")\n\n";
    }
}
