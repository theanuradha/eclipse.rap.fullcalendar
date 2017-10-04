package ch.cresoft.temos.custom.renderer.cal;

import java.util.Date;

public interface TMFullCalendarEvent
{

    String getId();

    String getTitle();


    Boolean getAllDay();

    void setAllDay(Boolean allDay);

    Date getStart();

    void setStart(Date start);

    Date getEnd();

    void setEnd(Date end);

    String getVA();

    Boolean getEditable();
    
    void setEditable(Boolean editable);

    void setTitle(String string);
    
    void setVa(String va);

}
