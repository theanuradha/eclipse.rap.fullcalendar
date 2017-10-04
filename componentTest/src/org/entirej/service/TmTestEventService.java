package org.entirej.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.entirej.framework.core.EJForm;
import org.entirej.framework.core.service.EJBlockService;
import org.entirej.framework.core.service.EJQueryCriteria;

public class TmTestEventService implements EJBlockService<TMTestEvent>
{

    @Override
    public List<TMTestEvent> executeQuery(EJForm form, EJQueryCriteria queryCriteria)
    {
        ArrayList<TMTestEvent> arrayList = new java.util.ArrayList<TMTestEvent>(0);
        
        {
            TMTestEvent event = new TMTestEvent();
            event.setId(UUID.randomUUID().toString());
            event.setTitle("Meeting-(dev");
            event.setStart(new Date());
            event.setAllDay(true);
            event.setEditable(true);
            arrayList.add(event);
            
            
            
        }
        
        return arrayList
                ;
    }

    @Override
    public void executeInsert(EJForm form, List<TMTestEvent> newRecords)
    {

    }

    @Override
    public void executeUpdate(EJForm form, List<TMTestEvent> updateRecords)
    {
    }

    @Override
    public void executeDelete(EJForm form, List<TMTestEvent> recordsToDelete)
    {
    }

    @Override
    public boolean canQueryInPages()
    {
        return false;
    }
}
