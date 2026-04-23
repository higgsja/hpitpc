package com.hpi.tpc.ui.views.notes;

import com.hpi.tpc.data.entities.*;
import com.vaadin.flow.component.dependency.*;
import com.vaadin.flow.component.grid.*;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.spring.annotation.*;
import lombok.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Component;

@UIScope
@VaadinSessionScope
@Component
@CssImport(value = "./styles/tpc-grid-theme.css", themeFor = "vaadin-grid")
public abstract class NotesAbstractVL
    extends VerticalLayout
{

    @Getter @Autowired public NotesModel notesModel;

    @Getter private final Grid<NoteModel> notesGrid;

    public NotesAbstractVL()
    {
        this.setClassName("notesAbstractVL");
        this.notesGrid = new Grid<>();

        this.doTableSetup();
    }

    /*
     * Complete the table layout
     */
    private void doTableSetup()
    {
        this.notesGrid.setClassName("notesGrid");
        this.notesGrid.setThemeName("dense");

        this.notesGrid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);
        this.notesGrid.addThemeVariants(GridVariant.LUMO_COMPACT);
        this.notesGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        this.notesGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        this.notesGrid.setAllRowsVisible(false);

        this.notesGrid.addColumn(NoteModel::getTicker)
            .setClassNameGenerator(noteModel -> "grid-cell")
            .setHeader("Item")
            //.setWidth("100px")
            .setAutoWidth(true)
            .setFrozen(true)
            .setSortProperty("item");

        this.notesGrid.addColumn(NoteModel::getDescription)
            .setHeader("Description")
            .setSortProperty("description")
            .setWidth("200px");

        this.notesGrid.addColumn(NoteModel::getNotes)
            .setHeader("Notes")
            .setWidth("400px")
            .setResizable(true)
            .setSortProperty("notes")
            .setFlexGrow(1);

        this.notesGrid.addColumn(NoteModel::getAction)
            .setHeader("Action")
            //.setWidth("100px")
            .setAutoWidth(true)
            .setSortProperty("action")
            .setTextAlign(ColumnTextAlign.CENTER);

        this.notesGrid.addColumn(NoteModel::getGainPct)
            .setHeader("Gain%")
            //.setWidth("105px")
            .setAutoWidth(true)
            .setSortProperty("gainpct")
            .setClassNameGenerator(noteModel -> Double.parseDouble(
            noteModel.getGainPct()) < -10.0 ? "grid-cell-neg"
            : Double.parseDouble(noteModel.getGainPct()) > 10.0 ? "grid-cell-grn" : "")
            .setKey("gainpct")
            .setSortProperty("gainpctst")
            .setTextAlign(ColumnTextAlign.END);

        this.notesGrid.addColumn(NoteModel::getGain)
            .setHeader("Gain")
            //.setWidth("100px")
            .setAutoWidth(true)
            .setSortProperty("gain")
            .setTextAlign(ColumnTextAlign.END);

        this.notesGrid.addColumn(NoteModel::getUnits)
            .setHeader("Units")
            //.setWidth("100px")
            .setAutoWidth(true)
            .setSortProperty("units")
            .setTextAlign(ColumnTextAlign.END);

        this.notesGrid.addColumn(NoteModel::getAtr)
            .setHeader("ATR")
            //.setWidth("80px")
            .setAutoWidth(true)
            .setSortProperty("atr")
            .setTextAlign(ColumnTextAlign.END);

        this.notesGrid.addColumn(NoteModel::getIPrice)
            .setHeader("IPrice")
            //.setWidth("100px")
            .setAutoWidth(true)
            .setSortProperty("price")
            .setTextAlign(ColumnTextAlign.END);

        this.notesGrid.addColumn(NoteModel::getClose)
            .setHeader("Close")
            //.setWidth("100px")
            .setAutoWidth(true)
            .setSortProperty("close")
            .setTextAlign(ColumnTextAlign.END);

        this.notesGrid.addColumn(NoteModel::getPriceChange)
            .setHeader("Chng")
            //.setWidth("100px")
            .setAutoWidth(true)
            .setSortProperty("pricechange")
            .setTextAlign(ColumnTextAlign.END);

        this.notesGrid.addColumn(NoteModel::getPriceChangePct)
            .setHeader("Chng%")
            //.setWidth("90px")
            .setAutoWidth(true)
            .setSortProperty("pricechangepct")
            .setTextAlign(ColumnTextAlign.END);

        this.notesGrid.addColumn(NoteModel::getEarnDate)
            .setHeader("Earnings")
            //.setWidth("190px")
            .setAutoWidth(true)
            .setSortProperty("earndate")
            .setTextAlign(ColumnTextAlign.CENTER);

        this.notesGrid.addColumn(NoteModel::getTrigger)
            .setHeader("Target")
            //.setWidth("85px")
            .setAutoWidth(true)
            .setSortProperty("target")
            .setTextAlign(ColumnTextAlign.START);

        this.notesGrid.addColumn(NoteModel::getTriggerType)
            .setHeader("Type")
            //.setWidth("85px")
            .setAutoWidth(true)
            .setSortProperty("triggertype")
            .setTextAlign(ColumnTextAlign.CENTER);

        this.notesGrid.addColumn(NoteModel::getActive)
            .setHeader("Active")
            //.setWidth("120px")
            .setAutoWidth(true)
            .setKey("isActive");
        this.notesGrid.getColumnByKey("isActive").setVisible(false);

        this.notesGrid.addColumn(NoteModel::getDateEntered)
            .setHeader("Entered")
            //.setWidth("120px")
            .setAutoWidth(true)
            .setSortProperty("dateitem")
            .setTextAlign(ColumnTextAlign.CENTER);

        this.notesGrid.setColumnReorderingAllowed(true);
        this.notesGrid.recalculateColumnWidths();

        this.add(this.notesGrid);
    }
}
