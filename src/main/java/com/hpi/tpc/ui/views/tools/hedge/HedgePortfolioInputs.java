package com.hpi.tpc.ui.views.tools.hedge;

import com.hpi.tpc.services.TPCDAOImpl;
import com.hpi.tpc.prefs.PrefsDAOImpl;
import com.hpi.tpc.prefs.*;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.spring.annotation.*;
import java.util.*;
import jakarta.annotation.*;
import lombok.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.jdbc.core.*;
import org.springframework.stereotype.*;

@UIScope
@VaadinSessionScope
@Component
@NoArgsConstructor
public class HedgePortfolioInputs
{

    @Autowired private JdbcTemplate jdbcTemplate;
    @Autowired TPCDAOImpl serviceTPC;
    @Autowired PrefsController prefsController;
    @Autowired PrefsDAOImpl servicePrefs;

    @Getter private VerticalLayout inputsLayout;
    private ComboBox<String> instrumentCB;
    private Div amtDiv;
    private NativeLabel amtLabel;
    @Getter private Input amtInput;
    private Div expirationDiv;
    private NativeLabel expirationLabel;
    @Getter private ComboBox<String> expirationCB;
    private Div typeDiv;
    private NativeLabel typeLabel;
    @Getter private ComboBox<String> typeCB;
    private Div realizationDiv;
    private Span realizationSpan;
    private Span minSpan;
    private NativeLabel realizationLabel;
    @Getter private Input realizationInput;
    private Div minStrikeDiv;
    private NativeLabel minStrikeLabel;
    @Getter private Input minStrikeInput;
    private Div ticketCostDiv;
    private Span contractSpan;
    private Div maxStrikeDiv;
    private Span maxSpan;
    private NativeLabel ticketCostLabel;
    @Getter private Input ticketCostInput;
    private NativeLabel maxStrikeLabel;
    @Getter private Input maxStrikeInput;
    private Div contractCostDiv;
    private NativeLabel contractCostLabel;
    @Getter private Input contractCostInput;

    @PostConstruct
    public void init()
    {
        this.prefsController.readPrefsByPrefix("Hedge");

        this.doHedgeInputsLayout();
    }

    public void writeHedgeInputsPrefs()
    {
        this.servicePrefs.updateModelSet("HedgeAmount", this.amtInput.getValue());
        this.servicePrefs.updateModelSet("HedgeContractCost", this.contractCostInput.getValue());
        this.servicePrefs.updateModelSet("HedgeInstrument", this.instrumentCB.getValue());
        this.servicePrefs.updateModelSet("HedgeMaxStrike", this.maxStrikeInput.getValue());
        this.servicePrefs.updateModelSet("HedgeMinStrike", this.minStrikeInput.getValue());
        this.servicePrefs.updateModelSet("HedgeMonth", this.expirationCB.getValue());
        this.servicePrefs.updateModelSet("HedgeRealizationPct", this.realizationInput.getValue());
        this.servicePrefs.updateModelSet("HedgeTicketCost", this.ticketCostInput.getValue());
        this.servicePrefs.updateModelSet("HedgeType", this.typeCB.getValue());

        this.prefsController.writePrefsByPrefix("Hedge");
    }

    private void doHedgeInputsLayout()
    {
        StringTokenizer tokenizer;
        List<String> instrumentList;
        String sql;
        String instrumentList2;

        this.inputsLayout = new VerticalLayout();
        this.inputsLayout.addClassName("inputs2VL");
        inputsLayout.setSizeFull();

        instrumentList = new ArrayList<>();
        this.instrumentCB = new ComboBox<>();
        this.instrumentCB.setWidth("6em");
        sql = "select keyDefault from hlhtxc5_dmOfx.TPCPreferences where JoomlaId = 0 and KeyId = ?";
        instrumentList2 = jdbcTemplate.queryForObject(sql, new Object[]
        {
            "HedgeInstrument"
        }, String.class);

        tokenizer = new StringTokenizer(instrumentList2, ",");
        while (tokenizer.hasMoreTokens())
        {
            instrumentList.add(tokenizer.nextToken().trim());
        }
        this.instrumentCB.setItems(instrumentList);
        this.instrumentCB.setValue(instrumentList.get(0));

        this.expirationCB = new ComboBox<>();
        this.expirationCB.setWidth("10em");

        this.typeCB = new ComboBox<>();
        this.typeCB.setWidth("6em");
        this.typeCB.setItems("Long");
        this.typeCB.setValue("Long");

        this.amtDiv = new Div();
        this.amtLabel = new NativeLabel("Amount: ");
        this.amtInput = new Input();
        this.amtInput.setPlaceholder("Amount");
        this.amtInput.setValue(servicePrefs.getPrefsModelsSet().get("HedgeAmount"));
        this.amtInput.getElement().setProperty("title", "Amount to hedge portfolio, $");
        this.amtInput.setWidth("7em");
        this.amtDiv.add(amtLabel, amtInput);

        this.expirationDiv = new Div();
        this.expirationLabel = new NativeLabel("Expiry: ");
        this.expirationCB.getElement().setProperty("title", "Contract Expiration");
        this.expirationDiv.add(expirationLabel, expirationCB);

        this.typeDiv = new Div();

        this.typeLabel = new NativeLabel("Type: ");
        this.typeCB.getElement().setProperty("title", "Contract Type, Long or Short");
        this.typeCB.setWidth("10em");
        this.typeDiv.add(typeLabel, typeCB);

        this.realizationDiv = new Div();

        this.realizationLabel = new NativeLabel("Realization %: ");
        this.realizationInput = new Input();
        this.realizationInput.setPlaceholder("Realization");
        this.realizationInput.setValue(servicePrefs.getPrefsModelsSet().get("HedgeRealizationPct"));
        this.realizationInput.getElement().setProperty("title", "Realization, %");
        this.realizationInput.setWidth("5em");
        this.realizationDiv.add(this.realizationLabel, this.realizationInput);

        this.minStrikeDiv = new Div();
        this.minStrikeLabel = new NativeLabel("Min Strike: ");
        this.minStrikeInput = new Input();
        this.minStrikeInput.setPlaceholder("Min Strike");
        this.minStrikeInput.setValue(servicePrefs.getPrefsModelsSet().get("HedgeMinStrike"));
        this.minStrikeInput.getElement().setProperty("title", "Minimum Strike in results, $");
        this.minStrikeInput.setWidth("5em");
        this.minStrikeDiv.add(this.minStrikeLabel, minStrikeInput);

        this.maxStrikeDiv = new Div();
        this.maxStrikeLabel = new NativeLabel("Max Strike: ");
        this.maxStrikeInput = new Input();
        this.maxStrikeInput.setPlaceholder("MaxStrike");
        this.maxStrikeInput.setValue(servicePrefs.getPrefsModelsSet().get("HedgeMaxStrike"));
        this.maxStrikeInput.getElement().setProperty("title", "Maximum Strike in results, $");
        this.maxStrikeInput.setWidth("5em");
        this.maxStrikeDiv.add(this.maxStrikeLabel, this.maxStrikeInput);

        this.contractCostDiv = new Div();
        this.contractCostLabel = new NativeLabel("Contract Cost: ");
        this.contractCostInput = new Input();
        this.contractCostInput.setPlaceholder("Ctrct Cost");
        this.contractCostInput.setValue(servicePrefs.getPrefsModelsSet().get("HedgeContractCost"));
        this.contractCostInput.getElement().setProperty("title", "Fees for execution of position, $");
        this.contractCostInput.setWidth("5em");
        this.contractCostDiv.add(this.contractCostLabel, this.contractCostInput);

        this.ticketCostDiv = new Div();
        this.ticketCostLabel = new NativeLabel("Ticket Cost: ");
        this.ticketCostInput = new Input();
        this.ticketCostInput.setPlaceholder("Tkt Cost");
        this.ticketCostInput.setRequiredIndicatorVisible(true);
        this.ticketCostInput.setValue(servicePrefs.getPrefsModelsSet().get("HedgeTicketCost"));
        this.ticketCostInput.getElement().setProperty("title", "Base cost of transaction without fees, $");
        this.ticketCostInput.setWidth("5em");
        this.ticketCostDiv.add(ticketCostLabel, ticketCostInput);

        //todo: currently only interested in VXX for hedging
        this.doExpiry("VXX");

        this.inputsLayout.add(this.instrumentCB, this.amtDiv, this.expirationDiv, this.typeDiv,
            this.realizationDiv, this.minStrikeDiv, this.maxStrikeDiv, this.contractCostDiv, this.ticketCostDiv);
    }

    private void doExpiry(String underlying)
    {
        List<String> expiry;

        expiry = serviceTPC.getOptionExpiry(underlying,
            this.minStrikeInput.getValue(),
            this.maxStrikeInput.getValue());

        this.expirationCB.setItems(expiry);
        this.expirationCB.setValue(this.servicePrefs.getPrefsModelsSet().get(
            "HedgeMonth"));
    }
}
