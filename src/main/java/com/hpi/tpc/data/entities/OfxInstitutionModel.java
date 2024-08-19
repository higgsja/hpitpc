package com.hpi.tpc.data.entities;

/*
 * This is a global model for data shared by all on the OFX institutions
 */
import java.util.*;
import lombok.*;

@AllArgsConstructor
@Getter @Setter
@Builder
public class OfxInstitutionModel {
    
    public static final ArrayList<OfxInstitutionModel> OFXINSTITUTION_MODELS;
    
    public static final String SQL_INSERT;
    public static final String SQL_UPDATE;
    public static final String SQL_GETALL;
    public static final String SQL_ALL_INSTITUTIONS;
    public static final String SQL_CLIENT_INSTITUTIONS;
    public static final String SQL_UPSERT;

    private String name;
    //dbOfx.Brokers.FId; dmOfx.OfxInstitutions.FId fldProv_mProvBankId unique
    private String fId;
    //dmOfx.OfxInstitutions.Org fldProv_mld
    //dbOfx.Brokers.Org etrade.com
    private String org;
    //dmOfx.OfxInstitutions.BrokerId etrade.com not unique
    //dbOfx.Brokers.BrokerIdFi etrade.com unique
    private String brokerId;
    private String url;
    private String ofxFail;
    private String sslFail;
    private String lastOfxValidation;
    private String lastSslValidation;
    private String profile;

    static {
        SQL_ALL_INSTITUTIONS =
            "select Name, FId, Org, BrokerId, Url, OfxFail, SslFail, LastOfxValidation, LastSslValidation, Profile from hlhtxc5_dmOfx.OfxInstitutions order by Name;";
        
        SQL_CLIENT_INSTITUTIONS =
            "select distinct ofxInst.Name, Accts.dmOfxFId as FId, ofxInst.Org, ofxInst.BrokerId, ofxInst.Url, ofxInst.OfxFail, ofxInst.SslFail, ofxInst.LastOfxValidation, ofxInst.LastSslValidation, ofxInst.Profile from hlhtxc5_dmOfx.OfxInstitutions as ofxInst, hlhtxc5_dmOfx.Accounts2 as Accts where Accts.dmOfxFId = ofxInst.FId and Accts.JoomlaId = '%s' order by ofxInst.Name";
        
        SQL_INSERT = "insert into hlhtxc5_dmOfx.OfxInstitutions (Name, FId, Org, BrokerId, Url, OfxFail, SSlFail, LastOfxValidation, LastSslValidation) values (";
        
        SQL_UPSERT = " on duplicate key update Name = \"%s\", FId = '%s', Org = \"%s\", BrokerId = \"%s\", Url = '%s', OfxFail = '%s', SSLFail = '%s', LastOfxValidation = '%s', LastSslValidation = '%s';";
        
        SQL_UPDATE = "update hlhtxc5_dmOfx.OfxInstitutions set ";
        
        SQL_GETALL = "SELECT * FROM `OfxInstitutions` order by Name;";
        
        OFXINSTITUTION_MODELS = new ArrayList<>();
    }
    
    public OfxInstitutionModel(String nameString, String fId) {
        this(nameString, fId, null, null, null, null, null, null, null, null);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
