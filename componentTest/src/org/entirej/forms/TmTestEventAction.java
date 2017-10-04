package org.entirej.forms;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.entirej.constants.EJ_PROPERTIES;
import org.entirej.forms.constants.F_TEST_FULL_CALENDAR_EVENT;
import org.entirej.framework.core.EJActionProcessorException;
import org.entirej.framework.core.EJBlock;
import org.entirej.framework.core.EJForm;
import org.entirej.framework.core.EJRecord;
import org.entirej.framework.core.actionprocessor.EJDefaultFormActionProcessor;
import org.entirej.framework.core.actionprocessor.interfaces.EJFormActionProcessor;
import org.entirej.framework.core.enumerations.EJPopupButton;
import org.entirej.framework.core.enumerations.EJScreenType;

import ch.cresoft.temos.custom.renderer.cal.TMFullCalendarActions;
import ch.cresoft.temos.custom.renderer.cal.TMFullCalendarEvent;

public class TmTestEventAction extends EJDefaultFormActionProcessor implements EJFormActionProcessor
{

    @Override
    public void newFormInstance(EJForm form) throws EJActionProcessorException
    {
        // TODO Auto-generated method stub
        super.newFormInstance(form);
        form.getBlock(F_TEST_FULL_CALENDAR_EVENT.B_TEST.ID).executeQuery();
    }

    @Override
    public void executeActionCommand(EJForm form, String blockName, String command, EJScreenType screenType) throws EJActionProcessorException
    {

        TMFullCalendarEvent event = null;
        EJBlock eventBlock = form.getBlock(blockName);
        EJRecord focusedRecord = eventBlock.getFocusedRecord();
        if (focusedRecord != null && focusedRecord.getBlockServicePojo() instanceof TMFullCalendarEvent)
        {
            event = (TMFullCalendarEvent) focusedRecord.getBlockServicePojo();
        }
        switch (command)
        {
            
            
            case TMFullCalendarActions.AC_EVENT_CHANGED:
            {
                System.out.println("Event updated :"+event);

                break;
            }
            case TMFullCalendarActions.AC_EVENT_EDIT:
           
            case TMFullCalendarActions.AC_EVENT_ADD:
            {
                final DateFormat date_formatter = new SimpleDateFormat("yyyy-MM-dd");

                if (event != null && event.getStart() != null && "2017-10-10".equals(date_formatter.format(event.getStart())))
                {
                    eventBlock.deleteRecord(eventBlock.getFocusedRecord());
                    EJActionProcessorException ejActionProcessorException = new EJActionProcessorException("Event Create not allow on '2017-10-10'");
                    throw ejActionProcessorException;
                }
                
                event.setVa(EJ_PROPERTIES.VA_SUB_EVENT);
                EJBlock editBlock = form.getBlock(F_TEST_FULL_CALENDAR_EVENT.B_EDIT_EVENT_REQ.ID);
                editBlock.getScreenItem(EJScreenType.MAIN, F_TEST_FULL_CALENDAR_EVENT.B_EDIT_EVENT_REQ.I_TITLE).setValue(event.getTitle());
                editBlock.getScreenItem(EJScreenType.MAIN, F_TEST_FULL_CALENDAR_EVENT.B_EDIT_EVENT_REQ.I_END).setValue(event.getEnd());
                editBlock.getScreenItem(EJScreenType.MAIN, F_TEST_FULL_CALENDAR_EVENT.B_EDIT_EVENT_REQ.I_START).setValue(event.getStart());
                
                form.getPopupCanvas(F_TEST_FULL_CALENDAR_EVENT.C_EDIT_EVENT).open();
                //editBlock.getScreenItem(EJScreenType.MAIN, F_TEST_FULL_CALENDAR_EVENT.B_EDIT_EVENT_REQ.I_NOTES).setValue(focusedRecord.getValue(F_TEST_FULL_CALENDAR_EVENT.B_TEST.));
                break;
            }

            default:
                break;
        }

    }
    
    @Override
    public void popupCanvasClosed(EJForm form, String popupCanvasName, EJPopupButton button) throws EJActionProcessorException
    {
        if(button==EJPopupButton.ONE)
        {
            EJBlock eventBlock = form.getBlock(F_TEST_FULL_CALENDAR_EVENT.B_TEST.ID);
            EJBlock editBlock = form.getBlock(F_TEST_FULL_CALENDAR_EVENT.B_EDIT_EVENT_REQ.ID);
            
            EJRecord focusedRecord = eventBlock.getFocusedRecord();
            focusedRecord.setValue(F_TEST_FULL_CALENDAR_EVENT.B_TEST.I_TITLE,
                    editBlock.getScreenItem(EJScreenType.MAIN, F_TEST_FULL_CALENDAR_EVENT.B_EDIT_EVENT_REQ.I_TITLE).getValue(), true);
            focusedRecord.setValue(F_TEST_FULL_CALENDAR_EVENT.B_TEST.I_START,
                    editBlock.getScreenItem(EJScreenType.MAIN, F_TEST_FULL_CALENDAR_EVENT.B_EDIT_EVENT_REQ.I_START).getValue(), true);
            focusedRecord.setValue(F_TEST_FULL_CALENDAR_EVENT.B_TEST.I_END,
                    editBlock.getScreenItem(EJScreenType.MAIN, F_TEST_FULL_CALENDAR_EVENT.B_EDIT_EVENT_REQ.I_END).getValue(), true);
            
            focusedRecord.synchronize();
        }
        if(button==EJPopupButton.THREE)
        {
            EJBlock eventBlock = form.getBlock(F_TEST_FULL_CALENDAR_EVENT.B_TEST.ID);
            eventBlock.deleteRecord(eventBlock.getFocusedRecord());
        }
        
        
    }

}
