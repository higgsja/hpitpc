package com.hpi.tpc.data.entities;

import com.hpi.tpc.app.security.*;
import lombok.*;
import lombok.Builder;

@EqualsAndHashCode
@Builder
@AllArgsConstructor
public class ClientSectorModel
    implements Comparable<ClientSectorModel> {
    @EqualsAndHashCode.Exclude @Getter @Setter private Integer joomlaId;
    @Getter @Setter private Integer clientSectorId; //unique
    @EqualsAndHashCode.Exclude @Getter @Setter private String clientSector;
    @EqualsAndHashCode.Exclude @Getter @Setter private String cSecShort;
    @EqualsAndHashCode.Exclude @Getter private String active;
    @EqualsAndHashCode.Exclude @Getter private Boolean bActive;
    @EqualsAndHashCode.Exclude @Getter @Setter private Double tgtPct;
    @EqualsAndHashCode.Exclude @Getter @Setter private Double adjPct;
    @EqualsAndHashCode.Exclude @Getter @Setter private String comment;
    @EqualsAndHashCode.Exclude @Getter private String tgtLocked;
    @EqualsAndHashCode.Exclude @Getter private Boolean bTgtLocked;
    @EqualsAndHashCode.Exclude @Getter @Setter private Double actPct;
    @EqualsAndHashCode.Exclude @Getter @Setter private Double mktVal;
    @EqualsAndHashCode.Exclude @Getter @Setter private Double lMktVal;
    @EqualsAndHashCode.Exclude @Getter @Setter private Integer customSector;
    @EqualsAndHashCode.Exclude@Getter @Setter private Integer changed;
    public static final String SQL_SELECT_ALL_SECTORS_BY_TGTPCT_D;
    public static final String SQL_SELECT_SECTORS_BY_CSECSHORT_A;
    public static final String SQL_SELECT_ALL_SECTORS_BY_NAME;
    public static final String SQL_SELECT_ALL_ACTIVE_SECTORS_BY_NAME;
    public static final String SQL_SECTORID_FROM_TKR;

    public static final String SQLINSERT;
    public static final String SQL_UPDATE_ALLOCATION;
    public static final String SQL_COUNT_SECTORS_BY_JOOMLAID;
    public static final String SQL_UPDATE_STD_SECTOR;
    public static final String SQL_UPDATE_CSTM_SECTOR;

    public static final int STD_SECTOR = 0;
    public static final int CSTM_SECTOR = 1;

    public static final int CHANGE_NONE = 0;
    public static final int CHANGE_EDIT = 1;
    public static final int CHANGE_NEW = 2;

    static {
        SQL_SELECT_ALL_SECTORS_BY_TGTPCT_D =
            "select JoomlaId, ClientSectorId, ClientSector, CSecShort, Active, round(TgtPct, 1) as TgtPct, Comment, TgtLocked, round(ActPct, 1) as ActPct, MktVal, LMktVal, CustomSector from hlhtxc5_dmOfx.ClientSectorList where JoomlaId = '%s' order by TgtPct desc;";

        SQL_SELECT_ALL_SECTORS_BY_NAME =
                        "select JoomlaId, ClientSectorId, ClientSector, CSecShort, Active, round(TgtPct, 1) as TgtPct, Comment, TgtLocked, round(ActPct, 1) as ActPct, MktVal, LMktVal, CustomSector from hlhtxc5_dmOfx.ClientSectorList where JoomlaId = '%s' order by ClientSector;";
        
        SQL_SELECT_ALL_ACTIVE_SECTORS_BY_NAME = 
            "select JoomlaId, ClientSectorId, ClientSector, CSecShort, Active, round(TgtPct, 1) as TgtPct, Comment, TgtLocked, round(ActPct, 1) as ActPct, MktVal, LMktVal, CustomSector from hlhtxc5_dmOfx.ClientSectorList where JoomlaId = '%s' and Active = 'Yes' order by ClientSector;";

        SQL_UPDATE_ALLOCATION =
            "update hlhtxc5_dmOfx.ClientSectorList set Active = '%s', TgtPct = '%s', Comment = '%s', TgtLocked = '%s' where JoomlaId = '%s' and ClientSectorId = '%s'";

        SQL_SELECT_SECTORS_BY_CSECSHORT_A =
            "select JoomlaId, ClientSectorId, ClientSector, CSecShort, Active, round(TgtPct, 1) as TgtPct, Comment, TgtLocked, round(ActPct, 1) as ActPct, MktVal, LMktVal, CustomSector from hlhtxc5_dmOfx.ClientSectorList where JoomlaId = '%s' and Active = 'Yes' order by CSecShort;";

        SQL_COUNT_SECTORS_BY_JOOMLAID = "select count(*) from `ClientSectorList` where JoomlaId = '%s'";

        SQLINSERT =
            "insert into hlhtxc5_dmOfx.ClientSectorList (JoomlaId, ClientSectorId, ClientSector, CSecShort, Active, TgtPct, Comment, TgtLocked, ActPct, MktVal, LMktVal, CustomSector) values (";

        SQL_UPDATE_STD_SECTOR =
            "update hlhtxc5_dmOfx.ClientSectorList set ClientSector = '%s', Comment = '%s' where JoomlaId = '%s' and ClientSectorId = '%s'";

        SQL_UPDATE_CSTM_SECTOR =
            "update hlhtxc5_dmOfx.ClientSectorList set ClientSector = '%s', CSecShort = '%s', Comment = '%s' where JoomlaId = '%s' and ClientSectorId = '%s'";

        SQL_SECTORID_FROM_TKR =
            "select ClientSectorId from ClientSectorList, (select Sector from hlhtxc5_dmOfx.EquityInfo where `Date` = (select MAX(`Date`) as `Date` from hlhtxc5_dmOfx.EquityInfo) and Ticker = '%s') as A where JoomlaId = '%s' and ClientSector = A.Sector";
    }

    public ClientSectorModel() {
        //default model
        this(SecurityUtils.getUserId(), null, "", "", "Yes", true, 0.0, 0.0, "", "No", 
            false, 0.0, 0.0, 0.0, 1, ClientSectorModel.CHANGE_NEW);
    }

    public ClientSectorModel(ClientSectorModel csm) {
        this.joomlaId = csm.joomlaId;
        this.clientSectorId = csm.getClientSectorId();
        this.clientSector = csm.getClientSector();
        this.cSecShort = csm.getCSecShort();
        this.active = csm.getActive();
        this.bActive = csm.getBActive();
        this.tgtPct = csm.getTgtPct();
        this.adjPct = csm.getAdjPct();
        this.comment = csm.getComment();
        this.tgtLocked = csm.getTgtLocked();
        this.bTgtLocked = csm.getBTgtLocked();
        this.actPct = csm.getActPct();
        this.mktVal = csm.getMktVal();
        this.lMktVal = csm.getLMktVal();
        this.customSector = csm.getCustomSector();
        this.changed = csm.changed;
    }

//    @Override
//    public int hashCode() {
//        //clientSectorId is unique
//        //cannot use as new model additions do not have the ID
//        return this.clientSectorId;
//    }
//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj) {
//            return true;
//        }
//        if (obj == null) {
//            return false;
//        }
//        if (getClass() != obj.getClass()) {
//            return false;
//        }
//        return Objects.equals(this.getClientSectorId(), ((ClientSectorModel) obj).getClientSectorId());
//    }
    @Override
    public int compareTo(ClientSectorModel csm) {
        return this.getClientSector().compareTo(csm.getClientSector());
    }

    @Override
    public String toString() {
        return this.cSecShort;
    }

    public void setBActive(Boolean bActive) {
        this.bActive = bActive;

        this.active = this.bActive ? "Yes" : "No";
    }

    public void setActive(String active) {
        this.active = active;

        this.bActive = this.active.equalsIgnoreCase("yes");
    }

    public void setBTgtLocked(Boolean bTgtLocked) {

        this.bTgtLocked = bTgtLocked;

        this.tgtLocked = this.bTgtLocked ? "Yes" : "No";
    }

    public void setTgtLocked(String tgtLocked) {
        this.tgtLocked = tgtLocked;

        this.bTgtLocked = this.tgtLocked.equalsIgnoreCase("yes");
    }
}
