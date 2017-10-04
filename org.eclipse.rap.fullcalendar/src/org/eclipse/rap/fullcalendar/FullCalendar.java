/*******************************************************************************
 * Copyright (c) 2014 EclipseSource and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: EclipseSource - initial API and implementation
 ******************************************************************************/

package org.eclipse.rap.fullcalendar;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.eclipse.rap.fullcalendar.CalendarData.Event;
import org.eclipse.rap.fullcalendar.internal.ResourceLoaderUtil;
import org.eclipse.rap.json.JsonArray;
import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.internal.lifecycle.WidgetUtil;
import org.eclipse.rap.rwt.remote.AbstractOperationHandler;
import org.eclipse.rap.rwt.remote.Connection;
import org.eclipse.rap.rwt.remote.OperationHandler;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.rap.rwt.service.ResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class FullCalendar extends Canvas
{

    private static final String    HANDLER_JS         = "fullcalendar.handler.js";
    private static final String    FULLCALENDAR_LIB_1 = "jquery.min.js";
    private static final String    FULLCALENDAR_LIB_2 = "jquery-ui.min.js";
    private static final String    FULLCALENDAR_LIB_3 = "moment.min.js";
    private static final String    FULLCALENDAR_JS    = "fullcalendar.js";
    private static final String    FULLCALENDAR_CSS   = "fullcalendar.css";

    private static final String    REMOTE_TYPE        = "fullcalendar.io";

    private RemoteObject           remoteObject;
    private final OperationHandler operationHandler   = new AbstractOperationHandler()
                                                      {
                                                          private static final long serialVersionUID = 1L;

                                                          public void handleSet(JsonObject properties)
                                                          {

                                                          }

                                                          @Override
                                                          public void handleCall(final String method, final JsonObject parameters)
                                                          {
                                                              Display.getCurrent().asyncExec(new Runnable()
                                                                                                                {

                                                                                                                    @Override
                                                                                                                    public void run()
                                                                                                                    {

                                                                                                                        action(method, parameters);

                                                                                                                    }

                                                                                                                });
                                                          }
                                                      };
    private CalendarData           data;

    public FullCalendar(Composite parent, int style)
    {
        super(parent, style);
        registerJS();
        requireJS();

        Connection connection = RWT.getUISession().getConnection();
        remoteObject = connection.createRemoteObject(REMOTE_TYPE);

        remoteObject.set("parent", WidgetUtil.getId(this));
        remoteObject.setHandler(operationHandler);
    }

    public void clear()
    {

        redraw();
    }

    public void drawEvents(CalendarData data)
    {

        this.data = data;

        remoteObject.set("context", data.toJson());
    }

    protected void action(String method, JsonObject parameters)
    {
        switch (method)
        {
            case "event_select":
            {

                String id = parameters.get("id").asString();
                List<Event> events = data.getEvents();
                for (Event event : events)
                {
                    if (id != null && id.equals(event.getId()))
                    {
                        eventSelected(event);
                        break;
                    }
                }
            }
                break;
            case "day_select":
            {

                String dateStr = parameters.get("select_date").asString();
                Date date;
                try
                {
                    date = data.date_formatter.parse(dateStr);
                }
                catch (java.text.ParseException e)
                {
                    date = new Date();
                }
                newEventRequest(date);
            }
                break;
            case "event_changed":
            {

                JsonObject evt = parameters;

                String id = evt.get("id").asString();
                List<Event> events = data.getEvents();
                for (Event event : events)
                {
                    if (id != null && id.equals(event.getId()))
                    {
                        event.fromJson(evt, evt.get("allDay").asBoolean() ?data.date_formatter: data.time_formatter);
                        eventChanged(event);
                        break;
                    }
                }

            }
                break;

            default:
                break;
        }
        System.out.println(method + "-" + parameters.toString());

    }

    protected void eventSelected(Event event)
    {
        System.out.println(event.id);
    }

    protected void eventChanged(Event event)
    {
        System.out.println(event.id);
    }

    protected void newEventRequest(Date date)
    {
        System.out.println(date);
    }

    public void updateEvent(Event event)
    {
        checkWidget();
        if (event == null)
        {
            SWT.error(SWT.ERROR_NULL_ARGUMENT);
        }
        remoteObject.set("update", event.toJson(data.time_formatter));
    }

    public void addEvent(Event event)
    {
        data.getEvents().add(event);
        JsonArray array = new JsonArray();
        array.add(event.toJson(data.time_formatter));
        remoteObject.set("add", array);
    }

    public void removeEvent(Event event)
    {
        data.getEvents().remove(event);
        remoteObject.set("remove", event.id);
    }

    public static void requireJS()
    {
        org.eclipse.rap.rwt.client.service.ClientFileLoader service = RWT.getClient().getService(org.eclipse.rap.rwt.client.service.ClientFileLoader.class);
        service.requireJs(RWT.getResourceManager().getLocation(FULLCALENDAR_LIB_1));
        service.requireJs(RWT.getResourceManager().getLocation(FULLCALENDAR_LIB_2));
        service.requireJs(RWT.getResourceManager().getLocation(FULLCALENDAR_LIB_3));
        service.requireJs(RWT.getResourceManager().getLocation(FULLCALENDAR_JS));
        service.requireCss(RWT.getResourceManager().getLocation(FULLCALENDAR_CSS));
        service.requireJs(RWT.getResourceManager().getLocation(HANDLER_JS));
    }

    public static void registerJS()
    {
        ResourceManager manager = RWT.getResourceManager();
        if (!manager.isRegistered(FULLCALENDAR_JS))
        {

            InputStream inputStream = ResourceLoaderUtil.class.getResourceAsStream(FULLCALENDAR_LIB_1);
            manager.register(FULLCALENDAR_LIB_1, inputStream);
            try
            {
                inputStream.close();
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
            inputStream = ResourceLoaderUtil.class.getResourceAsStream(FULLCALENDAR_LIB_2);
            manager.register(FULLCALENDAR_LIB_2, inputStream);
            try
            {
                inputStream.close();
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
            inputStream = ResourceLoaderUtil.class.getResourceAsStream(FULLCALENDAR_LIB_3);
            manager.register(FULLCALENDAR_LIB_3, inputStream);
            try
            {
                inputStream.close();
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
            inputStream = ResourceLoaderUtil.class.getResourceAsStream(FULLCALENDAR_JS);

            manager.register(FULLCALENDAR_JS, inputStream);
            try
            {
                inputStream.close();
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
            inputStream = ResourceLoaderUtil.class.getResourceAsStream(FULLCALENDAR_CSS);
            manager.register(FULLCALENDAR_CSS, inputStream);
            try
            {
                inputStream.close();
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
            inputStream = ResourceLoaderUtil.class.getResourceAsStream(HANDLER_JS);
            manager.register(HANDLER_JS, inputStream);
            try
            {
                inputStream.close();
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }

        }
    }

    public void showDate(Date start)
    {
        // TODO Auto-generated method stub
        
    }

}
