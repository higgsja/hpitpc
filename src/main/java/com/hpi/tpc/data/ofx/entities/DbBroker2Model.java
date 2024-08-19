package com.hpi.tpc.data.ofx.entities;

/**
 * last: Aug 2020
 */
import lombok.*;

@AllArgsConstructor
@Getter @Setter
@Builder
/**
 * All dbOfx.Broker elements
 */
public class DbBroker2Model {
    public static final String SQL_DBOFX_BROKERS_GET_ALL;
    public static final String SQL_DBOFX_BROKERS_GET_OFXINSTFID;
    public static final String SQL_DBOFX_BROKERS_EXISTS;
    public static final String SQL_DBOFX_BROKERS_INSERT;
    public static final String SQL_DBOFX_BROKERS_UPDATE;

    //dbOfx.Brokers.BrokerId auto
    private Integer dbBrokerId;
    //dbOfx.Brokers.Name
    private String name;
    //dbOfx.Brokers.Url
    private String url;
    //dbOfx.Brokers.OfxInstFId unique from dmOfx.OfxInstitutions
    private String ofxInstFId;

    static {
        SQL_DBOFX_BROKERS_GET_ALL = "select BrokerId, Name, Url, OfxInstFId from hlhtxc5_dbOfx.Brokers2;";
        SQL_DBOFX_BROKERS_GET_OFXINSTFID = "select BrokerId, Name, Url, OfxInstFId from hlhtxc5_dbOfx.Brokers2 where OfxInstFId = '%s';";
        SQL_DBOFX_BROKERS_EXISTS = "select Brokers2.Name, Brokers2.BrokerId, Brokers2.Url, OfxInstFId from hlhtxc5_dbOfx.Brokers2, hlhtxc5_dmOfx.OfxInstitutions where Brokers2.OfxInstFId = OfxInstitutions.FId and Brokers2.OfxInstFid = '%s';";
        SQL_DBOFX_BROKERS_INSERT = "insert into hlhtxc5_dbOfx.Brokers2 (Name, Url, OfxInstFId) values('%s', '%s', '%s');";
        SQL_DBOFX_BROKERS_UPDATE = "";
    }

    @Override
    public String toString() {
        return this.ofxInstFId;
    }
}
