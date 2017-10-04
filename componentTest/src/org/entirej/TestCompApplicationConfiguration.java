package org.entirej;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.eclipse.rap.fullcalendar.CalendarData;
import org.eclipse.rap.fullcalendar.CalendarData.Event;
import org.eclipse.rap.fullcalendar.FullCalendar;
import org.eclipse.rap.rwt.application.Application;
import org.eclipse.rap.rwt.application.EntryPoint;
import org.eclipse.rap.rwt.application.EntryPointFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class TestCompApplicationConfiguration implements org.eclipse.rap.rwt.application.ApplicationConfiguration
{

    @Override
    public void configure(Application arg0)
    {

        Map<String, String> properties = new HashMap<String, String>();

        arg0.addEntryPoint("/ej", new EntryPointFactory()
        {

            @Override
            public EntryPoint create()
            {

                return new org.eclipse.rap.rwt.application.EntryPoint()
                {

                    @Override
                    public int createUI()
                    {
                        Display display = Display.getDefault();
                        if (display.isDisposed())
                            display = new Display();
                        Shell shell = new Shell(display, SWT.NO_TRIM);

                        shell.setLayout(new FillLayout());
                        FullCalendar calendar = new FullCalendar(shell, SWT.NONE) {
                            
                            @Override
                            protected void eventSelected(Event event)
                            {
                                if(event.getId().equals("evt_id"))
                                {
                                    event.setTitle(event.getTitle()+"-Selected");
                                    super.eventSelected(event);
                                    updateEvent(event);
                                }
                                else
                                {
                                    removeEvent(event);
                                }
                               
                            }
                            
                            @Override
                            protected void newEventRequest(Date date)
                            {
                                Event ev1 = new Event(UUID.randomUUID().toString(), "New Evt");
                                ev1.setStart(date);
                                ev1.setEnd(new Date(ev1.getStart().getTime() + (1000 * 60 * 60 * 24)));
                                addEvent(ev1);
                            }
                            
                            @Override
                            protected void eventChanged(Event event)
                            {
                                System.out.println(event.getStart());
                                System.out.println(event.getEnd());
                                
                                showDate(new Date());
                            }
                            
                        };
                      
                        
                        
                        
                        
                        CalendarData data = new CalendarData();
                        Event ev1 = new Event("evt_id", "Test from EJ");
                        ev1.setStart(new Date());
                        ev1.setEnd(new Date(ev1.getStart().getTime() + (1000 * 60 * 60 * 24)));
                        ev1.setBackgroundColor("#3D9970");
                        data.getEvents().add(ev1);
                        calendar.drawEvents(data);
                        
                        
                        
                        
                        
                        
                        
                        
                        
                        shell.layout();
                        shell.setMaximized(true);
                        shell.open();
                        return 0;
                    }
                };
            }
        }, properties);

    }

}
