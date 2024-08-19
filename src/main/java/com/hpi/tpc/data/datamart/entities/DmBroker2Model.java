package com.hpi.tpc.data.datamart.entities;

/**
 * last: Aug 2020
 */
import lombok.*;

@AllArgsConstructor
@Getter @Setter
@Builder
/**
 * All dmOfx.Broker elements
 */
public class DmBroker2Model {
    public static final String SQL_DMOFX_BROKERS_GET_ALL;
    public static final String SQL_DMOFX_BROKERS_EXISTS;
    public static final String SQL_DMOFX_BROKERS_INSERT;

    //dmOfx.Brokers.BrokerId
    private Integer dmBrokerId;
    //name, same as dbOfxBrokers
    private String name;
    //dmOfx.Url
    private String url;
    //dmOfx.Brokers.OfxInstFId samd as dbOfx unique
    private String ofxInstFId;

    static {
        SQL_DMOFX_BROKERS_GET_ALL =
            "select BrokerId, Name, Url, OfxInstFId from hlhtxc5_dmOfx.Brokers2;";
        SQL_DMOFX_BROKERS_EXISTS =
            "select BrokerId, Name, Url, OfxInstFId from hlhtxc5_dmOfx.Brokers2 as dmBrokers where ofxInstFId = '%s';";
        SQL_DMOFX_BROKERS_INSERT =
            "insert into hlhtxc5_dmOfx.Brokers2 (BrokerId, Name, Url, OfxInstFId)  values ('%s', '%s', '%s', '%s');";
    }

    @Override
    public String toString() {
        return this.name;
    }
}
