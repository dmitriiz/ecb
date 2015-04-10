package ru.dazzled.ecb.ui;

import com.vaadin.addon.jpacontainer.JPAContainer;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.cdi.CDIUI;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.FieldEvents;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.HeaderRow;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import javax.inject.Inject;
import ru.dazzled.ecb.jpa.CurrencyRate;

/**
 *
 */
@CDIUI("")
@Theme("ecb")
@Widgetset("ru.dazzled.ecb.ui.EcbWidgetset")
public class EcbUI extends UI {
    
    @Inject
    CurrencyRateEntityProvider entityProvider;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        setContent(layout);

        final JPAContainer<CurrencyRate> container = new JPAContainer<>(CurrencyRate.class);
        container.setEntityProvider(entityProvider);
        container.removeContainerProperty("id");

        Grid grid = new Grid(container);
        grid.setColumnOrder("currency", "rate", "date");
        grid.setSizeFull();
        //grid.setReadOnly(true);

        HeaderRow filterRow = grid.appendHeaderRow();
        for (final String pid : new String[] {"currency", "date"}) {
            Grid.HeaderCell cell = filterRow.getCell(pid);
            TextField filterField = new TextField();
            filterField.setHeight("24px");
            //filterField.setColumns(20);
            filterField.addTextChangeListener(new FieldEvents.TextChangeListener() {
               @Override
                public void textChange(FieldEvents.TextChangeEvent event) {
                    container.removeContainerFilters(pid);
                    if (!event.getText().isEmpty()) {
                        container.addContainerFilter(new SimpleStringFilter(pid, event.getText(), true, false));
                    }
                }
            });
            cell.setComponent(filterField);
        }

        layout.addComponent(grid);
    }

}
