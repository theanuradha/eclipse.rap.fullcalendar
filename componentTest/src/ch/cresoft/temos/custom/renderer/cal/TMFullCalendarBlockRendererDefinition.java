package ch.cresoft.temos.custom.renderer.cal;

import java.util.Collections;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.entirej.applicationframework.rwt.renderers.block.definition.interfaces.EJRWTSingleRecordBlockDefinitionProperties;
import org.entirej.applicationframework.rwt.renderers.block.definition.interfaces.EJRWTTreeBlockDefinitionProperties;
import org.entirej.framework.core.properties.definitions.EJPropertyDefinitionType;
import org.entirej.framework.core.properties.definitions.interfaces.EJFrameworkExtensionProperties;
import org.entirej.framework.core.properties.definitions.interfaces.EJPropertyDefinition;
import org.entirej.framework.core.properties.definitions.interfaces.EJPropertyDefinitionGroup;
import org.entirej.framework.core.properties.definitions.interfaces.EJPropertyDefinitionListener;
import org.entirej.framework.core.properties.interfaces.EJMainScreenProperties;
import org.entirej.framework.dev.properties.EJDevPropertyDefinition;
import org.entirej.framework.dev.properties.EJDevPropertyDefinitionGroup;
import org.entirej.framework.dev.properties.EJDevPropertyDefinitionList;
import org.entirej.framework.dev.properties.interfaces.EJDevBlockDisplayProperties;
import org.entirej.framework.dev.properties.interfaces.EJDevScreenItemDisplayProperties;
import org.entirej.framework.dev.renderer.definition.EJDevBlockRendererDefinitionControl;
import org.entirej.framework.dev.renderer.definition.EJDevItemRendererDefinitionControl;
import org.entirej.framework.dev.renderer.definition.interfaces.EJDevBlockRendererDefinition;
import org.entirej.framework.dev.renderer.definition.interfaces.EJDevInsertScreenRendererDefinition;
import org.entirej.framework.dev.renderer.definition.interfaces.EJDevQueryScreenRendererDefinition;
import org.entirej.framework.dev.renderer.definition.interfaces.EJDevUpdateScreenRendererDefinition;

public class TMFullCalendarBlockRendererDefinition implements EJDevBlockRendererDefinition
{

    public static final String DEFAULT_VIEW = "defaultView";
    public static final String EVENT_VA     = "eventVa";

    public TMFullCalendarBlockRendererDefinition()
    {

    }

    @Override
    public boolean allowMultipleItemGroupsOnMainScreen()
    {

        return false;
    }

    @Override
    public boolean allowSpacerItems()
    {
        return false;
    }

    @Override
    public EJPropertyDefinitionGroup getBlockPropertyDefinitionGroup()
    {
        EJDevPropertyDefinitionGroup mainGroup = new EJDevPropertyDefinitionGroup("Calendar Block");

        EJDevPropertyDefinition defaultValue = new EJDevPropertyDefinition(DEFAULT_VIEW, EJPropertyDefinitionType.STRING);
        defaultValue.setLabel("Default View");
        defaultValue.setDefaultValue("month");
        defaultValue.addValidValue("month", "Month");
        defaultValue.addValidValue("agendaWeek", "Agenda Week");
        defaultValue.addValidValue("agendaDay", "Agenda Day");

        EJDevPropertyDefinition eventVa = new EJDevPropertyDefinition(EVENT_VA, EJPropertyDefinitionType.VISUAL_ATTRIBUTE);
        eventVa.setLabel("Event Default VA");

        mainGroup.addPropertyDefinition(eventVa);
        mainGroup.addPropertyDefinition(defaultValue);

        return mainGroup;
    }

    @Override
    public EJPropertyDefinitionGroup getItemPropertiesDefinitionGroup()
    {
        EJDevPropertyDefinitionGroup mainGroup = new EJDevPropertyDefinitionGroup("Calendar: Required Item Properties");

        return mainGroup;
    }

    @Override
    public EJPropertyDefinitionGroup getSpacerItemPropertiesDefinitionGroup()
    {

        return null;
    }

    @Override
    public boolean useInsertScreen()
    {

        return false;
    }

    @Override
    public boolean useQueryScreen()
    {

        return false;
    }

    @Override
    public boolean useUpdateScreen()
    {

        return false;
    }

    @Override
    public String getRendererClassName()
    {
        return "ch.cresoft.temos.custom.renderer.cal.TMFullCalendarBlockRenderer";
    }

    @Override
    public void loadValidValuesForProperty(EJFrameworkExtensionProperties arg0, EJPropertyDefinition arg1)
    {
        // no impl

    }

    @Override
    public void propertyChanged(EJPropertyDefinitionListener arg0, EJFrameworkExtensionProperties arg1, String arg2)
    {
        // no impl

    }

    @Override
    public EJDevBlockRendererDefinitionControl addBlockControlToCanvas(EJMainScreenProperties mainScreenProperties, EJDevBlockDisplayProperties blockDisplayProperties, Composite parent, FormToolkit toolkit)
    {
        Composite layoutBody;

        if (mainScreenProperties.getDisplayFrame())
        {
            if (mainScreenProperties.getFrameTitle() != null && mainScreenProperties.getFrameTitle().length() > 0)
            {
                layoutBody = new Group(parent, SWT.NONE)
                {

                    @Override
                    public void addMouseListener(MouseListener listener)
                    {
                        // TODO Auto-generated method stub
                        super.addMouseListener(listener);
                        Control[] controls = getChildren();
                        for (Control control : controls)
                        {
                            control.addMouseListener(listener);
                        }
                    }

                };
                ((Group) layoutBody).setText(mainScreenProperties.getFrameTitle());
            }
            else
            {
                layoutBody = new Composite(parent, SWT.BORDER)
                {

                    public void addMouseListener(MouseListener listener)
                    {
                        // TODO Auto-generated method stub
                        super.addMouseListener(listener);
                        Control[] controls = getChildren();
                        for (Control control : controls)
                        {
                            control.addMouseListener(listener);
                        }
                    }

                };
            }

        }
        else
        {
            layoutBody = new Composite(parent, SWT.NONE)
            {

                @Override
                public void addMouseListener(MouseListener listener)
                {
                    // TODO Auto-generated method stub
                    super.addMouseListener(listener);
                    Control[] controls = getChildren();
                    for (Control control : controls)
                    {
                        control.addMouseListener(listener);
                    }
                }

            };
        }

        layoutBody.setLayout(new FillLayout());

        Label browser = new Label(layoutBody, SWT.NONE);
        browser.setText("[" + blockDisplayProperties.getName() + "] : " + "FULLCALENDAR RENDERER");
        return new EJDevBlockRendererDefinitionControl(blockDisplayProperties, Collections.<EJDevItemRendererDefinitionControl> emptyList());
    }

    @Override
    public EJDevInsertScreenRendererDefinition getInsertScreenRendererDefinition()
    {

        return null;
    }

    @Override
    public EJDevQueryScreenRendererDefinition getQueryScreenRendererDefinition()
    {

        return null;
    }

    @Override
    public EJDevItemRendererDefinitionControl getSpacerItemControl(EJDevScreenItemDisplayProperties arg0, Composite arg1, FormToolkit arg2)
    {

        return null;
    }

    @Override
    public EJDevUpdateScreenRendererDefinition getUpdateScreenRendererDefinition()
    {

        return null;
    }

    @Override
    public EJPropertyDefinitionGroup getItemGroupPropertiesDefinitionGroup()
    {
        EJDevPropertyDefinitionGroup mainGroup = new EJDevPropertyDefinitionGroup(" Block");

        return mainGroup;
    }

}
