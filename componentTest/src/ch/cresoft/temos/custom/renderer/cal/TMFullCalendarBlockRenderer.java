/**
 *
 */
package ch.cresoft.temos.custom.renderer.cal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.rap.fullcalendar.CalendarData;
import org.eclipse.rap.fullcalendar.CalendarData.Event;
import org.eclipse.rap.fullcalendar.FullCalendar;
import org.eclipse.rwt.EJ_RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.entirej.applicationframework.rwt.layout.EJRWTEntireJGridPane;
import org.entirej.applicationframework.rwt.renderer.interfaces.EJRWTAppBlockRenderer;
import org.entirej.applicationframework.rwt.utils.EJRWTKeysUtil;
import org.entirej.applicationframework.rwt.utils.EJRWTKeysUtil.KeyInfo;
import org.entirej.framework.core.EJApplicationException;
import org.entirej.framework.core.EJForm;
import org.entirej.framework.core.EJMessage;
import org.entirej.framework.core.data.EJDataRecord;
import org.entirej.framework.core.data.controllers.EJEditableBlockController;
import org.entirej.framework.core.data.controllers.EJQuestion;
import org.entirej.framework.core.enumerations.EJManagedBlockProperty;
import org.entirej.framework.core.enumerations.EJManagedScreenProperty;
import org.entirej.framework.core.enumerations.EJQuestionButton;
import org.entirej.framework.core.enumerations.EJScreenType;
import org.entirej.framework.core.interfaces.EJScreenItemController;
import org.entirej.framework.core.properties.EJCoreVisualAttributeProperties;
import org.entirej.framework.core.properties.definitions.interfaces.EJFrameworkExtensionProperties;
import org.entirej.framework.core.properties.interfaces.EJBlockProperties;
import org.entirej.framework.core.properties.interfaces.EJMainScreenProperties;
import org.entirej.framework.core.renderers.interfaces.EJInsertScreenRenderer;
import org.entirej.framework.core.renderers.interfaces.EJQueryScreenRenderer;
import org.entirej.framework.core.renderers.interfaces.EJUpdateScreenRenderer;

public class TMFullCalendarBlockRenderer implements EJRWTAppBlockRenderer, KeyListener
{

    private EJEditableBlockController       _block;
    private boolean                         _isFocused        = false;
    private FullCalendar                    _calendar;

    private EJDataRecord                    currentRec;

    final CalendarData                      calendarData      = new CalendarData();

    private List<String>                    _actionkeys       = new ArrayList<String>();
    private Map<KeyInfo, String>            _actionInfoMap    = new HashMap<EJRWTKeysUtil.KeyInfo, String>();
    private List<EJDataRecord>              _tableBaseRecords = new ArrayList<EJDataRecord>();
    private Composite                       scrollComposite;

    public static final String              DEFAULT_VIEW      = "defaultView";

    public static final String              NOW_INDICATOR     = "nowIndicator";
    public static final String              EVENT_VA          = "eventVa";
    private EJCoreVisualAttributeProperties eventVa;

    @Override
    public void askToDeleteRecord(final EJDataRecord recordToDelete, String msg)
    {
        if (msg == null)
        {
            msg = "Are you sure you want to delete the current record?";
        }
        EJMessage message = new EJMessage(msg);
        EJQuestion question = new EJQuestion(new EJForm(_block.getForm()), "DELETE_RECORD", "Delete", message, "Yes", "No")
        {

            @Override
            public void setAnswer(EJQuestionButton answerButton)
            {

                super.setAnswer(answerButton);

                if (EJQuestionButton.ONE == answerButton)
                {
                    _block.getBlock().deleteRecord(recordToDelete);
                }
                if (_calendar != null && !_calendar.isDisposed())
                {
                    if (recordToDelete.getServicePojo() instanceof TMFullCalendarEvent)
                    {
                        TMFullCalendarEvent event = (TMFullCalendarEvent) recordToDelete.getServicePojo();
                        CalendarData.Event evt = null;
                        for (CalendarData.Event cevt : calendarData.getEvents())
                        {
                            if (cevt.getId().equals(event.getId()))
                            {
                                evt = cevt;
                                break;
                            }
                        }
                        if (evt != null)
                            _calendar.removeEvent(evt);

                    }
                }
                _block.setRendererFocus(true);
            }

        };
        _block.getForm().getMessenger().askQuestion(question);

    }

    @Override
    public void blockCleared()
    {
        currentRec = null;

        calendarData.getEvents().clear();
        _tableBaseRecords.clear();

        if (_calendar != null && !_calendar.isDisposed())
            _calendar.drawEvents(calendarData);

    }

    public void setFilter(String filter)
    {
        throw new IllegalStateException("not supported yet");
        // this.filterText = filter;
        // if(filterTree!=null)
        // {
        // filterTree.setFilterText(filter);
        // filterTree.filter(filter);
        // }

    }

    public String getFilter()
    {
        throw new IllegalStateException("not supported yet");
        // return filterText;

    }

    public void executingQuery()
    {
    }

    @Override
    public void detailBlocksCleared()
    {
        // no impl

    }

    @Override
    public void enterInsert(EJDataRecord arg0)
    {
        // no impl

    }

    @Override
    public void enterUpdate(EJDataRecord arg0)
    {
        // no impl

    }

    @Override
    public void gainFocus()
    {
        setHasFocus(true);

    }

    @Override
    public EJInsertScreenRenderer getInsertScreenRenderer()
    {

        return null;
    }

    @Override
    public EJQueryScreenRenderer getQueryScreenRenderer()
    {

        return null;
    }

    @Override
    public EJUpdateScreenRenderer getUpdateScreenRenderer()
    {
        return null;
    }

    @Override
    public boolean hasFocus()
    {
        return _isFocused;

    }

    @Override
    public void initialiseRenderer(EJEditableBlockController block)
    {
        _block = block;

    }

    @Override
    public boolean isCurrentRecordDirty()
    {

        return false;
    }

    @Override
    public void queryExecuted()
    {
        currentRec = null;
        refreshData();
        currentRec = getFirstRecord();

    }

    @Override
    public void recordDeleted(int arg0)
    {
        // clearFilter();

        EJDataRecord recordAt = getRecordAt(arg0);
        if (recordAt != null && recordAt.getServicePojo() instanceof TMFullCalendarEvent)
        {
            TMFullCalendarEvent event = (TMFullCalendarEvent) recordAt.getServicePojo();
            CalendarData.Event evt = null;
            for (CalendarData.Event cevt : calendarData.getEvents())
            {
                if (cevt.getId().equals(event.getId()))
                {
                    evt = cevt;
                    break;
                }
            }
            if (evt != null)
            {
                _tableBaseRecords.remove(arg0);
                _calendar.removeEvent(evt);
            }

        }

    }

    @Override
    public void recordInserted(EJDataRecord arg0)
    {
        // clearFilter();
        _tableBaseRecords.add(arg0);

        if (arg0.getServicePojo() instanceof TMFullCalendarEvent)
        {
            TMFullCalendarEvent evt = (TMFullCalendarEvent) arg0.getServicePojo();

            CalendarData.Event event = new CalendarData.Event(evt.getId(), evt.getTitle());
            toCalEvent(evt, event);
            _calendar.addEvent(event);

        }
    }

    @Override
    public void refreshBlockProperty(EJManagedBlockProperty arg0)
    {
        // no impl
    }

    @Override
    public void refreshBlockRendererProperty(String arg0)
    {
        Display.getCurrent().asyncExec(new Runnable()
        {

            @Override
            public void run()
            {
                refreshAfterChange(currentRec);

            }
        });
    }

    @Override
    public void setFocusToItem(EJScreenItemController arg0)
    {
        setHasFocus(true);
    }

    @Override
    public void setHasFocus(boolean focus)
    {
        _isFocused = focus;

        if (_isFocused)
        {
            _block.focusGained();
            // showFocusedBorder(true);
        }
        else
        {
            _block.focusLost();
            // showFocusedBorder(false);
        }

    }

    @Override
    public void enterQuery(EJDataRecord arg0)
    {
        // no impl
    }

    @Override
    public EJDataRecord getRecordAfter(EJDataRecord record)
    {
        return _block.getDataBlock().getRecordAfter(record);
    }

    @Override
    public EJDataRecord getRecordBefore(EJDataRecord record)
    {
        return _block.getDataBlock().getRecordBefore(record);
    }

    @Override
    public EJDataRecord getFirstRecord()
    {
        return _block.getDataBlock().getRecord(0);
    }

    @Override
    public EJDataRecord getLastRecord()
    {
        return _block.getDataBlock().getRecord(_block.getBlockRecordCount() - 1);
    }

    @Override
    public void recordSelected(EJDataRecord arg0)
    {
        currentRec = arg0;
        if (arg0 != null && arg0.getServicePojo() instanceof TMFullCalendarEvent)
        {
            TMFullCalendarEvent event = (TMFullCalendarEvent) arg0.getServicePojo();
            if (event.getStart() != null)
            {
                _calendar.showDate(event.getStart());
            }
        }
    }

    @Override
    public EJDataRecord getFocusedRecord()
    {
        return currentRec != null ? currentRec : getFirstRecord();
    }

    @Override
    public void refreshAfterChange(EJDataRecord arg0)
    {
        if (arg0.getServicePojo() instanceof TMFullCalendarEvent)
        {
            TMFullCalendarEvent event = (TMFullCalendarEvent) arg0.getServicePojo();
            CalendarData.Event evt = null;
            for (CalendarData.Event cevt : calendarData.getEvents())
            {
                if (cevt.getId().equals(event.getId()))
                {
                    evt = cevt;
                    break;
                }
            }
            if (evt != null)
            {
                toCalEvent(event, evt);
                _calendar.updateEvent(evt);
            }

        }

    }

    @Override
    public void refreshItemProperty(String itemName, EJManagedScreenProperty managedItemPropertyType, EJDataRecord record)
    {
        if (EJManagedScreenProperty.ITEM_INSTANCE_VISUAL_ATTRIBUTE.equals(managedItemPropertyType))
        {
            EJScreenItemController item = _block.getScreenItem(EJScreenType.MAIN, itemName);
            if (item != null)
            {
                Display.getCurrent().asyncExec(new Runnable()
                {

                    @Override
                    public void run()
                    {
                        refreshAfterChange(currentRec);

                    }
                });
            }
        }
        else if (EJManagedScreenProperty.SCREEN_ITEM_VISUAL_ATTRIBUTE.equals(managedItemPropertyType))
        {
            EJScreenItemController item = _block.getScreenItem(EJScreenType.MAIN, itemName);
            if (item != null)
            {
                item.getManagedItemRenderer().setVisualAttribute(item.getProperties().getVisualAttributeProperties());

                Display.getCurrent().asyncExec(new Runnable()
                {

                    @Override
                    public void run()
                    {
                        refreshAfterChange(currentRec);

                    }
                });
            }
        }

    }

    @Override
    public void refreshItemRendererProperty(String arg0, String arg1)
    {
        // no impl

    }

    @Override
    public void synchronize()
    {
        // no impl

    }

    @Override
    public Object getGuiComponent()
    {
        return scrollComposite;
    }

    private FullCalendar createCal(Composite parent)
    {
        return new FullCalendar(parent, SWT.NONE)
        {
            private static final long serialVersionUID = 1L;

            @Override
            protected void eventChanged(Event event)
            {
                if (event != null)
                {
                    for (final EJDataRecord rec : _tableBaseRecords)
                    {
                        if (rec != null && rec.getServicePojo() instanceof TMFullCalendarEvent)
                        {
                            TMFullCalendarEvent evt = (TMFullCalendarEvent) rec.getServicePojo();
                            if (evt.getId().equals(event.getId()))
                            {
                                currentRec = rec;
                                fromCalEvent(evt, event);
                                Display.getDefault().asyncExec(new Runnable()
                                {

                                    @Override
                                    public void run()
                                    {
                                        _block.newRecordInstance(rec);
                                        _block.executeActionCommand(TMFullCalendarActions.AC_EVENT_CHANGED, EJScreenType.MAIN);

                                    }
                                });

                                break;
                            }
                        }
                    }
                }
            }

            @Override
            protected void eventSelected(Event event)
            {
                if (event != null)
                {
                    for (final EJDataRecord rec : _tableBaseRecords)
                    {
                        if (rec != null && rec.getServicePojo() instanceof TMFullCalendarEvent)
                        {
                            TMFullCalendarEvent evt = (TMFullCalendarEvent) rec.getServicePojo();
                            if (evt.getId().equals(event.getId()))
                            {
                                currentRec = rec;
                                Display.getDefault().asyncExec(new Runnable()
                                {

                                    @Override
                                    public void run()
                                    {
                                        _block.newRecordInstance(rec);
                                        _block.executeActionCommand(TMFullCalendarActions.AC_EVENT_EDIT, EJScreenType.MAIN);

                                    }
                                });

                                break;
                            }
                        }
                    }
                }
            }

            @Override
            protected void newEventRequest(Date date)
            {

                EJDataRecord rec = _block.createRecordNoAction();
                if (rec != null && rec.getServicePojo() instanceof TMFullCalendarEvent)
                {
                    TMFullCalendarEvent evt = (TMFullCalendarEvent) rec.getServicePojo();
                    evt.setStart(date);

                    evt.setEnd(new Date(date.getTime() + (1000 * 60 * 60)));
                    evt.setTitle("new Event");
                    evt.setEditable(true);

                    _block.insertRecord(rec);

                    currentRec = rec;
                    Display.getCurrent().asyncExec(new Runnable()
                    {

                        @Override
                        public void run()
                        {
                            try
                            {
                                _block.executeActionCommand(TMFullCalendarActions.AC_EVENT_ADD, EJScreenType.MAIN);
                            }
                            catch (EJApplicationException e)
                            {
                                System.out.println(e.getMessage());
                            }

                        }
                    });

                }

                super.newEventRequest(date);
            }

        };
    }

    @Override
    public void buildGuiComponent(EJRWTEntireJGridPane blockCanvas)
    {
        if (_calendar != null && !_calendar.isDisposed())
        {
            _calendar.dispose();
        }

        EJBlockProperties blockProperties = _block.getProperties();
        EJMainScreenProperties mainScreenProperties = blockProperties.getMainScreenProperties();

        EJFrameworkExtensionProperties blockRendererProperties = blockProperties.getBlockRendererProperties();

        if (blockRendererProperties != null)
        {
            // EJCoreFrameworkExtensionPropertyList propertyList =
            // blockRendererProperties.getPropertyList(ACTIONS);
            //
            // if (propertyList != null)
            // {
            // List<EJFrameworkExtensionPropertyListEntry> allListEntries =
            // propertyList.getAllListEntries();
            // for (EJFrameworkExtensionPropertyListEntry entry :
            // allListEntries)
            // {
            // String actionID = entry.getProperty(ACTION_ID);
            // String actionkey = entry.getProperty(ACTION_KEY);
            // if (actionID != null && actionkey != null &&
            // actionID.trim().length() > 0 && actionkey.trim().length() > 0)
            // {
            // addActionKeyinfo(actionkey, actionID);
            // }
            // }
            // }

        }

        String defaultView = blockRendererProperties.getStringProperty(DEFAULT_VIEW);
        if (defaultView != null)
        {
            calendarData.setDefaultView(defaultView);
        }
        String eventVaName = blockRendererProperties.getStringProperty(EVENT_VA);
        if (eventVaName != null)
            eventVa = _block.getForm().getVisualAttribute(eventVaName);
        calendarData.setDefaultDate(new Date());
        calendarData.setNowIndicator(blockRendererProperties.getBooleanProperty(NOW_INDICATOR, true));

        GridData gridData = new GridData(GridData.FILL_BOTH);
        gridData.widthHint = mainScreenProperties.getWidth();
        gridData.heightHint = mainScreenProperties.getHeight();

        gridData.horizontalSpan = mainScreenProperties.getHorizontalSpan();
        gridData.verticalSpan = mainScreenProperties.getVerticalSpan();
        gridData.grabExcessHorizontalSpace = mainScreenProperties.canExpandHorizontally();
        gridData.grabExcessVerticalSpace = mainScreenProperties.canExpandVertically();

        // if (gridData.grabExcessHorizontalSpace)
        // gridData.minimumHeight = mainScreenProperties.getHeight();
        // if (gridData.grabExcessVerticalSpace)
        // gridData.minimumWidth = mainScreenProperties.getHeight();
        blockCanvas.setLayoutData(gridData);

        scrollComposite = new Composite(blockCanvas, SWT.NONE);
        scrollComposite.setLayout(new FillLayout());

        if (mainScreenProperties.getDisplayFrame())
        {
            String frameTitle = mainScreenProperties.getFrameTitle();

            {
                Group group = new Group(scrollComposite, SWT.NONE);
                hookKeyListener(group);
                group.setLayout(new FillLayout());
                scrollComposite.setLayoutData(gridData);

                if (frameTitle != null && frameTitle.length() > 0)
                {
                    group.setText(frameTitle);
                }

                {

                    _calendar = createCal(group);
                }
            }

        }
        else
        {

            {
                _calendar = createCal(scrollComposite);
            }

            scrollComposite.setLayoutData(gridData);
            hookKeyListener(scrollComposite);
        }

        _calendar.addFocusListener(new FocusListener()
        {

            private static final long serialVersionUID = 1L;

            @Override
            public void focusLost(FocusEvent arg0)
            {
                setHasFocus(false);

            }

            @Override
            public void focusGained(FocusEvent arg0)
            {
                setHasFocus(true);

            }
        });
        _calendar.addMouseListener(new MouseAdapter()
        {

            private static final long serialVersionUID = 1L;

            @Override
            public void mouseDown(MouseEvent arg0)
            {
                setHasFocus(true);

            }

        });

        hookKeyListener(_calendar);

        refreshData();

    }

    void refreshData()
    {
        calendarData.getEvents().clear();
        _tableBaseRecords.clear();
        Collection<EJDataRecord> records = _block.getRecords();
        for (EJDataRecord ejDataRecord : records)
        {
            _tableBaseRecords.add(ejDataRecord);
        }

        for (EJDataRecord ejDataRecord : _tableBaseRecords)
        {
            if (ejDataRecord.getServicePojo() instanceof TMFullCalendarEvent)
            {
                TMFullCalendarEvent evt = (TMFullCalendarEvent) ejDataRecord.getServicePojo();

                CalendarData.Event event = new CalendarData.Event(evt.getId(), evt.getTitle());
                toCalEvent(evt, event);

                calendarData.getEvents().add(event);
            }
        }

        if (_calendar != null && !_calendar.isDisposed())
            _calendar.drawEvents(calendarData);
    }

    private void toCalEvent(TMFullCalendarEvent evt, CalendarData.Event event)
    {
        event.setAllDay(evt.getAllDay() != null ? evt.getAllDay() : false);
        event.setStart(evt.getStart());
        event.setEnd(evt.getEnd());
        event.setEditable(evt.getEditable() != null ? evt.getEditable() : false);
        event.setTitle(evt.getTitle());

        EJCoreVisualAttributeProperties eva = eventVa;
        if (evt.getVA() != null)
        {
            EJCoreVisualAttributeProperties visualAttribute = _block.getForm().getVisualAttribute(evt.getVA());
            if (visualAttribute != null)
                eva = visualAttribute;
        }

        if (eva != null)
        {
            if (eva.getForegroundColor() != null)
            {
                event.setTextColor(toHex(eva.getForegroundColor().getRed(), eva.getForegroundColor().getGreen(), eva.getForegroundColor().getBlue()));
                event.setBorderColor(toHex(eva.getForegroundColor().getRed(), eva.getForegroundColor().getGreen(), eva.getForegroundColor().getBlue()));
            }
            if (eva.getBackgroundColor() != null)
            {
                event.setBackgroundColor(toHex(eva.getBackgroundColor().getRed(), eva.getBackgroundColor().getGreen(), eva.getBackgroundColor().getBlue()));

            }
        }

    }

    private void fromCalEvent(TMFullCalendarEvent evt, CalendarData.Event event)
    {
        evt.setAllDay(event.isAllDay());
        evt.setStart(event.getStart());
        evt.setEnd(event.getEnd());

    }

    public static String toHex(int r, int g, int b)
    {
        return "#" + toBrowserHexValue(r) + toBrowserHexValue(g) + toBrowserHexValue(b);
    }

    private static String toBrowserHexValue(int number)
    {
        StringBuilder builder = new StringBuilder(Integer.toHexString(number & 0xff));
        while (builder.length() < 2)
        {
            builder.append("0");
        }
        return builder.toString().toUpperCase();
    }

    @Override
    public void keyPressed(KeyEvent arg0)
    {
        // ignore
    }

    @Override
    public void keyReleased(KeyEvent arg0)
    {
        int keyCode = arg0.keyCode;
        KeyInfo keyInfo = EJRWTKeysUtil.toKeyInfo(keyCode, (arg0.stateMask & SWT.SHIFT) != 0, (arg0.stateMask & SWT.CTRL) != 0, (arg0.stateMask & SWT.ALT) != 0);

        String actionID = _actionInfoMap.get(keyInfo);
        if (actionID != null)
        {
            _block.executeActionCommand(actionID, EJScreenType.MAIN);
        }
    }

    private void addActionKeyinfo(String actionKey, String actionId)
    {
        if (actionKey != null && actionKey.trim().length() > 0)
        {
            try
            {
                KeyInfo keyInfo = EJRWTKeysUtil.toKeyInfo(actionKey);
                _actionInfoMap.put(keyInfo, actionId);
                _actionkeys.add(actionKey);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private void hookKeyListener(Control control)
    {
        List<String> subActions = new ArrayList<String>(_actionkeys);
        Object data = control.getData(EJ_RWT.ACTIVE_KEYS);

        if (data != null)
        {
            String[] current = (String[]) data;
            for (String action : current)
            {
                if (subActions.contains(action))
                {
                    continue;
                }
                subActions.add(action);
            }
        }
        control.setData(EJ_RWT.ACTIVE_KEYS, subActions.toArray(new String[0]));
        control.addKeyListener(this);
    }

    @Override
    public int getDisplayedRecordCount()
    {
        // Indicates the number of records that are available within the View.
        // the number depends on the filters set on the table!
        return _tableBaseRecords.size();
    }

    @Override
    public int getDisplayedRecordNumber(EJDataRecord record)
    {
        if (record == null)
        {
            return -1;
        }

        return _tableBaseRecords.indexOf(record);
    }

    @Override
    public EJDataRecord getRecordAt(int displayedRecordNumber)
    {
        if (displayedRecordNumber > -1 && displayedRecordNumber < getDisplayedRecordCount())
        {
            return _tableBaseRecords.get(displayedRecordNumber);
        }
        return null;
    }

}
