package classes;

import java.time.LocalDate;

public interface IArtist {
    public LocalDate getData();
    public String getDescriere();
    public Double getViews();
    public void setViews(Double views);
    public void setData(LocalDate data);
    public void setDescriere(String descriere);
}
