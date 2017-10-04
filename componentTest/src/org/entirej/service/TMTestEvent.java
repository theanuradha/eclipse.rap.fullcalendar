package org.entirej.service;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import org.entirej.framework.core.EJFieldName;
import org.entirej.service.TMTestEvent.FieldNames;

import ch.cresoft.temos.custom.renderer.cal.TMFullCalendarEvent;

public class TMTestEvent implements TMFullCalendarEvent
{
    private HashMap<FieldNames<?>, Object> _initialValues = new HashMap<FieldNames<?>, Object>();

    private String                         _id            = UUID.randomUUID().toString();
    private String                         _note;
    private Boolean                        _editable;
    private String                         _title;
    private Date                           _start;
    private Date                           _end;
    private Boolean                        _allDay;

    private String va;

    @EJFieldName("id")
    public String getId()
    {
        return _id;
    }

    @EJFieldName("id")
    public void setId(String id)
    {
        _id = id;
        if (!_initialValues.containsKey(FieldNames.id))
        {
            _initialValues.put(FieldNames.id, id);
        }
    }

    @EJFieldName("note")
    public String getNote()
    {
        return _note;
    }

    @EJFieldName("note")
    public void setNote(String note)
    {
        _note = note;
        if (!_initialValues.containsKey(FieldNames.note))
        {
            _initialValues.put(FieldNames.note, note);
        }
    }

    @EJFieldName("editable")
    public Boolean getEditable()
    {
        return _editable;
    }

    @EJFieldName("editable")
    public void setEditable(Boolean editable)
    {
        _editable = editable;
        if (!_initialValues.containsKey(FieldNames.editable))
        {
            _initialValues.put(FieldNames.editable, editable);
        }
    }

    @EJFieldName("title")
    public String getTitle()
    {
        return _title;
    }

    @EJFieldName("title")
    public void setTitle(String title)
    {
        _title = title;
        if (!_initialValues.containsKey(FieldNames.title))
        {
            _initialValues.put(FieldNames.title, title);
        }
    }

    @EJFieldName("start")
    public Date getStart()
    {
        return _start;
    }

    @EJFieldName("start")
    public void setStart(Date start)
    {
        _start = start;
        if (!_initialValues.containsKey(FieldNames.start))
        {
            _initialValues.put(FieldNames.start, start);
        }
    }

    @EJFieldName("end")
    public Date getEnd()
    {
        return _end;
    }

    @EJFieldName("end")
    public void setEnd(Date end)
    {
        _end = end;
        if (!_initialValues.containsKey(FieldNames.end))
        {
            _initialValues.put(FieldNames.end, end);
        }
    }

    @EJFieldName("allDay")
    public Boolean getAllDay()
    {
        return _allDay;
    }

    @EJFieldName("allDay")
    public void setAllDay(Boolean allDay)
    {
        _allDay = allDay;
        if (!_initialValues.containsKey(FieldNames.allDay))
        {
            _initialValues.put(FieldNames.allDay, allDay);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getInitialValue(FieldNames<T> fieldName)
    {
        if (_initialValues.containsKey(fieldName))
        {
            return (T) _initialValues.get(fieldName);
        }
        else
        {

            if (fieldName.equals(FieldNames.id))
            {
                return (T) getId();
            }
            if (fieldName.equals(FieldNames.note))
            {
                return (T) getNote();
            }
            if (fieldName.equals(FieldNames.editable))
            {
                return (T) getEditable();
            }
            if (fieldName.equals(FieldNames.title))
            {
                return (T) getTitle();
            }
            if (fieldName.equals(FieldNames.start))
            {
                return (T) getStart();
            }
            if (fieldName.equals(FieldNames.end))
            {
                return (T) getEnd();
            }
            if (fieldName.equals(FieldNames.allDay))
            {
                return (T) getAllDay();
            }

            return null;
        }
    }

    public void clearInitialValues()
    {
        _initialValues.clear();

        _initialValues.put(FieldNames.id, _id);

        _initialValues.put(FieldNames.note, _note);

        _initialValues.put(FieldNames.editable, _editable);

        _initialValues.put(FieldNames.title, _title);

        _initialValues.put(FieldNames.start, _start);

        _initialValues.put(FieldNames.end, _end);

        _initialValues.put(FieldNames.allDay, _allDay);
    }

    public static class FieldNames<T>
    {

        public static final FieldNames<java.lang.String>  id       = new FieldNames<>();
        public static final FieldNames<java.lang.String>  note     = new FieldNames<>();
        public static final FieldNames<java.lang.Boolean> editable = new FieldNames<>();
        public static final FieldNames<java.lang.String>  title    = new FieldNames<>();
        public static final FieldNames<java.util.Date>    start    = new FieldNames<>();
        public static final FieldNames<java.util.Date>    end      = new FieldNames<>();
        public static final FieldNames<java.lang.Boolean> allDay   = new FieldNames<>();
        T                                                 type;
    }

    @Override
    public String getVA()
    {
        return va;
    }
    
    @Override
    public void setVa(String va)
    {
       this.va = va;
        
    }

    @Override
    public String toString()
    {
        return "TMTestEvent [_id=" + _id + ", _note=" + _note + ", _editable=" + _editable + ", _title=" + _title + ", _start=" + _start + ", _end=" + _end + ", _allDay=" + _allDay + "]";
    }

}
