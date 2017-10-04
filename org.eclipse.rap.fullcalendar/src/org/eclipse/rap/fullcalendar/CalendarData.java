package org.eclipse.rap.fullcalendar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.rap.json.JsonArray;
import org.eclipse.rap.json.JsonObject;

public class CalendarData
{
    final DateFormat     time_formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    final DateFormat     date_formatter = new SimpleDateFormat("yyyy-MM-dd");

    private final Header header         = new Header();

    private Date         defaultDate    = new Date();
    private String       defaultView    = "month";
    private boolean      editable       = true;
    private boolean      nowIndicator       = true;
    private List<Event>  events         = new ArrayList<CalendarData.Event>();

    public JsonObject toJson()
    {

        JsonObject object = new JsonObject();

        // header
        {

            object.add("header", header.toJson());
        }

        object.add("defaultView", defaultView);
        object.add("editable", editable);
        object.add("nowIndicator", nowIndicator);
        if (defaultDate != null)
            object.add("", date_formatter.format(defaultDate));

        JsonArray eventsObj = new JsonArray();

        for (Event event : events)
        {
            eventsObj.add(event.toJson(time_formatter));
        }
        object.add("events", eventsObj);

        return object;
    }

    public Header getHeader()
    {
        return header;
    }

    public List<Event> getEvents()
    {
        return events;
    }

    public Date getDefaultDate()
    {
        return defaultDate;
    }

    public void setDefaultDate(Date defaultDate)
    {
        this.defaultDate = defaultDate;
    }

    public String getDefaultView()
    {
        return defaultView;
    }

    public void setDefaultView(String defaultView)
    {
        this.defaultView = defaultView;
    }

    public boolean isEditable()
    {
        return editable;
    }

    public void setEditable(boolean editable)
    {
        this.editable = editable;
    }
    
    public void setNowIndicator(boolean nowIndicator)
    {
        this.nowIndicator = nowIndicator;
    }

    public static class Header
    {
        String left   = "prev,next, today";
        String center = "title";
        String right  = "month,agendaWeek,agendaDay";

        public String getLeft()
        {
            return left;
        }

        public void setLeft(String left)
        {
            this.left = left;
        }

        public String getCenter()
        {
            return center;
        }

        public void setCenter(String center)
        {
            this.center = center;
        }

        public String getRight()
        {
            return right;
        }

        public void setRight(String right)
        {
            this.right = right;
        }

        JsonObject toJson()
        {
            JsonObject headerObj = new JsonObject();
            headerObj.add("left", left);
            headerObj.add("right", right);
            headerObj.add("center", center);

            return headerObj;
        }

    }

    public static class Event
    {

        final String id;
        String       title;
        String       textColor;
        String       borderColor;
        String       backgroundColor;
        boolean      allDay;
        boolean      editable = true;

        private Date start = new Date();
        private Date end   = new Date();

        public Event(String id, String title)
        {
            this.id = id;
            this.title = title;
        }

        public String getTitle()
        {
            return title;
        }

        public void setTitle(String title)
        {
            this.title = title;
        }

        public boolean isAllDay()
        {
            return allDay;
        }

        public void setAllDay(boolean allDay)
        {
            this.allDay = allDay;
        }

        public Date getStart()
        {
            return start;
        }

        public void setStart(Date start)
        {
            this.start = start;
        }

        public Date getEnd()
        {
            return end;
        }

        public void setEnd(Date end)
        {
            this.end = end;
        }

        public String getId()
        {
            return id;
        }

        public String getTextColor()
        {
            return textColor;
        }

        public void setTextColor(String textColor)
        {
            this.textColor = textColor;
        }

        public String getBorderColor()
        {
            return borderColor;
        }

        public void setBorderColor(String borderColor)
        {
            this.borderColor = borderColor;
        }

        public String getBackgroundColor()
        {
            return backgroundColor;
        }

        public void setBackgroundColor(String backgroundColor)
        {
            this.backgroundColor = backgroundColor;
        }

        public void setEditable(boolean editable)
        {
            this.editable = editable;
        }

        public boolean isEditable()
        {
            return editable;
        }

        public void fromJson(JsonObject evt, DateFormat time_formatter)
        {
            title = evt.get("title").asString();
            allDay = evt.get("allDay").asBoolean();

            if (evt.get("start") != null && !evt.get("start").isNull())
                try
                {
                    start = time_formatter.parse(evt.get("start").asString() );
                }
                catch (java.text.ParseException e)
                {

                }
            else
                start = null;
            if (evt.get("end") != null && !evt.get("end").isNull())
                try
                {
                    end = time_formatter.parse(evt.get("end").asString());
                }
                catch (java.text.ParseException e)
                {

                }
            else
                end = null;

        }

        JsonObject toJson(DateFormat time_formatter)
        {
            JsonObject object = new JsonObject();
            object.add("id", id);
            object.add("title", title);
            object.add("allDay", allDay);
            object.add("editable", editable);
            object.add("backgroundColor", backgroundColor);
            object.add("borderColor", borderColor);
            object.add("textColor", textColor);
            if (start != null)
                object.add("start", time_formatter.format(start));
            if (end != null)
                object.add("end", time_formatter.format(end));

            return object;
        }

    }

}
