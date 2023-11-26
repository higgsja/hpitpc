package com.hpi.tpc.ui.views.notes;

import com.hpi.tpc.app.security.*;
import com.hpi.tpc.data.entities.*;
import com.hpi.tpc.ui.views.baseClass.*;
//import com.studerw.tda.client.*;
//import com.studerw.tda.model.quote.*;
import com.vaadin.flow.data.binder.*;
import com.vaadin.flow.spring.annotation.*;
import java.util.*;
import lombok.*;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * handles state of application
 * contains objects representing the data
 * provides ways to query and change the data
 * responds to requests from View and instructions from Controller
 */
@UIScope
@VaadinSessionScope
@Component
public class NotesModel
    extends MVCModelBase
{

    @Setter @Getter private NoteModel noteModel;
    @Getter private List<NoteModel> dataProvider;
    @Getter private final BeanValidationBinder<NoteModel> binder;

    private final Properties props;
//    private final HttpTdaClient httpTdaClient;
//    @Getter private Quote quote;
    @Getter @Setter private Boolean isSave;
    @Getter @Setter private Boolean isAdd;

    private String noteQuoteSql = "select ei.Ticker, ei.Company, ulds.`Close`  from hlhtxc5_dmOfx.EquityInfo ei, hlhtxc5_dmOfx.Util_LastDailyStock ulds where ei.Ticker = ulds.EquityId and ei.Ticker  = '%s' limit 1";

    public NotesModel()
    {
        this.binder = new BeanValidationBinder<>(NoteModel.class);
        this.props = new Properties();
        props.setProperty("tda.client_id", "VYEUORVKFCJSAZG9TBJOGSQZX8PKXWZB");
        props
            .setProperty("tda.token.refresh", "2iMXpwg1leo+00/wo8yNTb+fazO/sVbXfR8Vlo0AaRcfq+amIEaKhzFiP8NmJeamEphh8yXKwGaikZqQ+K9J6GfIzwRI0MMzGqSzULBhyfr328+MpI4yJEQdAVPScs9snrjpJ4zBufq6Ce5zNRcoFKiJXn+nEVYgJcuWQ2uUAN22OkbMGS36IelZzFZcngtvdPvECPx+wzEKv0Tvw4RBzgf9sC3RtvD5ooB+vGB8iHBe30UkqxBy6bhggn91vfk/lkJAsKqLDrz0KJiWDMKCse5q5UOmyF5k3iAD1yYbULiemBoPemRCbxkW4gpVPqmlWkedHXMI0ucocAqQFrveWH76lrp4lqfikqq3nyOUPvyOCZBh0rISgeZuuimjwi0ugxbq+4YlGij6F1/cfS1lJE6+kbeabjePnqrjw3MERaWKCqui3i+33K2+vLI100MQuG4LYrgoVi/JHHvlV8GeGFVOL5va0jhjRbRt2Eu6zw3/wZV2vTl+krK2LMBu40XPZG38PoDDJ6pWtSnoqAw11YkkxXA1DUcCojG4Et0bQK+2D4l1A4fRn3hmoItrfZnb5ThylT9bmh1nDktdnODwIQCDeT2/xUsi2UZ8n23AtUc2gL73NlgLa6AyLEuH8H1+M2IJxtGuwDmdYwToPpt8/hDeeDflTNGqMEI0w8QvUBZRaP/kjUatjInBGD9WSLW1+8ZsAB1DVsIeS/d8fN171PyKxmGBl+OGJLX0D5mP+KRJzUDv4WaTxgmDEvuprBlbu3uZjI3Qz6Y48pIfs5e5kqB83m2FTKEf94GnRq6sAzv50X4Jo+uYDT5hYlU9H3QnqayvWGde6wRcIDWM8tWzxBckW5jvbrsNlBjOuDgZ0OtfuUq7aCM+zUnrJOUApKIQ8tC5gqUgWLM=212FD3x19z9sWBHDJACbC00B75E");
        props.setProperty("tda.account.id", "865-837053");
//        props.setProperty("tda.url", CMOfxDirectModel.getFIMODELS().
//            get(0).getHttpHost());
        props.setProperty("tda.debug.bytes.length", "-1");

//        this.httpTdaClient = new HttpTdaClient(props);
//
//        this.quote = null;
        this.isSave = false;
        this.isAdd = false;
    }

    public List<NoteQuoteModel> getTickerInfo(String ticker)
    {
        String sql = String.format(this.noteQuoteSql, ticker);
        List<NoteQuoteModel> quote;

        if (ticker.isEmpty())
        {
            return null;
        }

        quote = jdbcTemplate.query(sql, new NoteQuoteMapper());

        if (quote.isEmpty())
        {
            return null;
        }

        return quote;
    }

    public void getPrefs(String prefPrefix)
    {
//        this.prefsController.setDefaults("PortfolioTrack");
//        this.prefsController.readPrefsByPrefix("PortfolioTrack");
//        this.selectedTrackActive = this.prefsController
//            .getPref("PortfolioTrackActive").equals("Yes");
//        this.selectedTrackOpen = this.prefsController
//            .getPref("PortfolioTrackOpen").equals("Yes");
    }

    public void writePrefs()
    {
//        //preferences, update the hashmap, then write to database
//        this.prefsController.setPref("PortfolioTrackActive",
//            selectedTrackActive ? "Yes" : "No");
//        this.prefsController.setPref("PortfolioTrackOpen",
//            selectedTrackOpen ? "Yes" : "No");
//        
//        this.prefsController.writePrefsByPrefix("PortfolioTrack");
    }

//    public void setSelectNoteModel(NoteModel noteModel)
//    {
//        //without the session variable, this could be simple setter
//        this.setSelectedNoteModel(noteModel);
//    }
    /**
     * Save the note to the database
     * need to come in with the NoteModel from the form.
     *
     * @param isArchive: true if archiving a note
     */
    public void saveUpdate(Boolean isArchive)
    {
        //not hit
        NoteModel tempNoteModel;

        if (isArchive)
        {
            this.serviceTPC.AppTracking("TPC:Notes:Edit:Archive");
        } else
        {
            this.serviceTPC.AppTracking("TPC:Notes:Edit:Save");
        }

        //get data from the form
        tempNoteModel = new NoteModel(
            //these are key fields
            this.noteModel.getJoomlaId(),
            //null for add
            this.noteModel.getTStamp(),
            //uppercase if a ticker
            this.noteModel.getTicker(),
            this.noteModel.getIPrice(),
            this.noteModel.getDescription(),
            this.noteModel.getNotes(),
            this.noteModel.getUnits(),
            NoteModel.setActionInt(this.noteModel.getAction()).toString(),
            NoteModel.setTriggerInt(this.noteModel.getTriggerType()).toString(),
            this.noteModel.getTrigger(),
            this.noteModel.getActive(),
            this.noteModel.getDateEntered()
        );

        this.serviceTPC.updateNote(tempNoteModel);
    }

    /**
     * Save the note to the database
     * need to come in with the NoteModel from the form.
     */
    public void saveAdd()
    {
        NoteModel tempNoteModel;

        this.serviceTPC.AppTracking("TPC:Notes:Add:Save");

        tempNoteModel = new NoteModel(
            //these are key fields
            this.noteModel.getJoomlaId(),
            //null for add
            this.noteModel.getTStamp(),
            //uppercase if a ticker
            this.noteModel.getTicker(),
            this.noteModel.getIPrice(),
            this.noteModel.getDescription(),
            this.noteModel.getNotes(),
            this.noteModel.getUnits(),
            NoteModel.setActionInt(this.noteModel.getAction()).toString(),
            NoteModel.setTriggerInt(this.noteModel.getTriggerType()).toString(),
            this.noteModel.getTrigger(),
            this.noteModel.getActive(),
            this.noteModel.getDateEntered()
        );

        this.serviceTPC.saveNote(tempNoteModel);
    }

    public void getData(String subset)
    {
        List<NoteModel> aList;

        aList = this.serviceTPC.getByJId(subset);

        this.dataProvider = aList;
    }

    @Override
    public void writePrefs(String prefix)
    {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
