package classes;

import java.time.LocalDateTime;

public interface INotificare {
    String getText();
    User getEmitator();
    User getReceptor();
    LocalDateTime getData();
}