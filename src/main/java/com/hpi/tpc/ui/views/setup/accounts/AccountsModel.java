package com.hpi.tpc.ui.views.setup.accounts;

import com.hpi.tpc.data.datamart.entities.DmBroker2Mapper;
import com.hpi.tpc.data.datamart.entities.DmBroker2Model;
import com.hpi.tpc.data.datamart.entities.DmAccount2Model;
import com.hpi.tpc.data.datamart.entities.DmAccount2Mapper;
import com.hpi.tpc.data.ofx.entities.DbBroker2Mapper;
import com.hpi.tpc.data.ofx.entities.DbBroker2Model;
import com.hpi.tpc.data.ofx.entities.DbAccount2Mapper;
import com.hpi.tpc.data.ofx.entities.DbAccount2Model;
import com.hpi.tpc.app.security.*;
import com.hpi.tpc.data.entities.*;
import com.hpi.tpc.ui.views.baseClass.*;
import com.vaadin.flow.data.provider.*;
import com.vaadin.flow.spring.annotation.*;
import java.util.*;
import lombok.*;
import org.springframework.stereotype.*;

/**
 * handles state of application
 * contains objects representing the data
 * provides ways to query and change the data
 * responds to requests from View and instructions from Controller
 */
@UIScope
@VaadinSessionScope
@Component
@NoArgsConstructor
public class AccountsModel
    extends MVCModelBase
{
    /**
     * all institutions in the OFX database
     */
    @Getter private ListDataProvider<OfxInstitutionModel> allInstitutionsDataProvider;

    /**
     * selected institution in the GUI
     */
    @Getter @Setter private OfxInstitutionModel selectedInstitutionModel = null;

    /**
     * institutions in use by client
     */
    @Getter private ListDataProvider<OfxInstitutionModel> clientFIDataProvider;

    /**
     * track add/drop of client institutions
     */
    @Getter @Setter private Boolean bClientInstitutionsDataProviderIsDirty = false;

    /**
     * selected client institution in GUI
     */
    @Getter @Setter private OfxInstitutionModel selectedClientInstitutionModel = null;

    /**
     * all client accounts in all client financial institutions
     */
    @Getter private ListDataProvider<EditAccountModel> allClientAccountsDataProvider;
    
    /**
     * track add/drop of accounts
     */
    @Getter @Setter private Boolean bAccountsDataProviderIsDirty = false;
    
    /**
     * selected account model in GUI
     */
    @Getter @Setter private EditAccountModel selectedAccountModel = null;

    /**
     * Called from add/edit dialog. For institutions, add to dbOfx.
     *
     * @param oiModel add/edit dialog selected institution
     */
    protected void addInstitution(OfxInstitutionModel oiModel)
    {
        Collection<OfxInstitutionModel> collectionInstitutionModels;
        OfxInstitutionModel tmpInstitutionModel;

        collectionInstitutionModels = this.clientFIDataProvider.getItems();

        if (!collectionInstitutionModels.isEmpty())
        {
            //there are client institutions to check
            for (OfxInstitutionModel oim : collectionInstitutionModels)
            {
                if (oiModel.getFId().equalsIgnoreCase(oim.getFId()))
                {
                    //client already has it
                    return;
                }
            }
        }

        //must create institution in dbOfx
        if ((this.jdbcTemplate.update(String.format(
            DbBroker2Model.SQL_DBOFX_BROKERS_INSERT,
            oiModel.getName(), oiModel.getUrl(), oiModel.getFId()))) == 1)
        {
            this.bClientInstitutionsDataProviderIsDirty = true;
            //create the institution
            tmpInstitutionModel = new OfxInstitutionModel(
                oiModel.getName(), oiModel.getFId(),
                oiModel.getOrg(), oiModel.getBrokerId(),
                oiModel.getUrl(), oiModel.getOfxFail(),
                oiModel.getSslFail(), oiModel.getLastOfxValidation(),
                oiModel.getLastSslValidation(), oiModel.getProfile());

            //add institution to client list
            collectionInstitutionModels.add(tmpInstitutionModel);

            this.clientFIDataProvider = DataProvider.ofCollection(
                collectionInstitutionModels);

            this.clientFIDataProvider.refreshAll();
        }
    }

    /**
     * Called from add/edit dialog. Change arrays, not the database.
     * This allows a Cancel to work.
     *
     * @param accountName
     * @param oiModel
     * @param acctNumber
     * @param userId
     * @param pwd
     */
    protected void AddAccount(
        String accountName,
        OfxInstitutionModel oiModel,
        String acctNumber,
        String userId,
        String pwd)
    {
        Collection<EditAccountModel> collectionAccountModels;
        EditAccountModel tmpAccountModel;
        List<DbBroker2Model> dbBroker2Models;

        collectionAccountModels = this.allClientAccountsDataProvider.getItems();

        if (!collectionAccountModels.isEmpty())
        {
            //there are client accounts to check
            for (EditAccountModel eaModel : collectionAccountModels)
            {
                if (eaModel.getDmOfxFId().equalsIgnoreCase(oiModel.getFId()))
                {
                    //already have the account
                    return;
                }
            }
        }

        dbBroker2Models = jdbcTemplate.query(String.format(
            DbBroker2Model.SQL_DBOFX_BROKERS_GET_OFXINSTFID, oiModel.getFId()),
            new DbBroker2Mapper());

        this.bAccountsDataProviderIsDirty = true;

        //todo: must be a more elegant way to do this
        tmpAccountModel = new EditAccountModel(
            dbBroker2Models.get(0).getDbBrokerId(), //integer dmOfx.BrokerId,
            null, //dmOfx.AcctId, not assigned yet
            null, //dbOfx.AcctId, not assigned yet
            oiModel.getFId(),
            acctNumber, userId, pwd, accountName,
            "Yes", true);
        collectionAccountModels = this.allClientAccountsDataProvider.getItems();
        collectionAccountModels.add(tmpAccountModel);

        this.allClientAccountsDataProvider = DataProvider.ofCollection(
            collectionAccountModels);

        this.allClientAccountsDataProvider.refreshAll();
    }

    protected Boolean upsertAccounts()
    {
        return this.checkDbOfxBroker() && this.checkDmOfxBroker()
            && this.checkDbOfxAccounts() && this.checkDmOfxAccounts();
    }

    /**
     * Checks existence of dbOfx.Brokers.OfxInstFId
     * <p>
     * returns true if it exists
     * else
     * creates and returns true on success
     * else
     * returns false
     *
     * @return true/false
     */
    private Boolean checkDbOfxBroker()
    {
        Collection<OfxInstitutionModel> clientInstModels;
        List<DbBroker2Model> dbBrokerList;
        Integer rows;

        clientInstModels = this.clientFIDataProvider.getItems();

        for (OfxInstitutionModel ofxInstModel : clientInstModels)
        {
            dbBrokerList = this.jdbcTemplate.query(
                String.format(DbBroker2Model.SQL_DBOFX_BROKERS_EXISTS,
                    ofxInstModel.getFId()), new DbBroker2Mapper());

            if (dbBrokerList.size() > 1)
            {
                //problem
                //todo: error message
                return false;
            }

            if (dbBrokerList.size() == 1)
            {
                //nothing to update here so just return
                continue;
            }

            //create it; should never happen
            rows = this.jdbcTemplate.update(
                String.format(DbBroker2Model.SQL_DBOFX_BROKERS_INSERT,
                    ofxInstModel.getName(), ofxInstModel.getUrl(), ofxInstModel
                    .getFId()));

            //check it
            if (rows > 1)
            {
                //problem
                //todo: error message
                return false;
            }

            if (rows == 1)
            {
                //nothing to update here so just return
                continue;
            }

            this.setBClientInstitutionsDataProviderIsDirty(true);
        }

        return true;
    }

    /**
     * Checks existence of dmOfx.Brokers.OfxInstFId
     * <p>
     * returns true if it exists
     * else
     * creates and returns true on success
     * else
     * returns false
     *
     * @return true/false
     */
    private Boolean checkDmOfxBroker()
    {
        List<DmBroker2Model> dmBrokerList;
        List<DbBroker2Model> dbBrokerList;
        Integer rows;

        dbBrokerList = this.jdbcTemplate.query(
            DbBroker2Model.SQL_DBOFX_BROKERS_GET_ALL, new DbBroker2Mapper());

        //check dmOfx.Brokers against dbOfx.Brokers; they are the same
        for (DbBroker2Model dbBrokerModel : dbBrokerList)
        {
            dmBrokerList = this.jdbcTemplate.query(
                String.format(DmBroker2Model.SQL_DMOFX_BROKERS_EXISTS,
                    dbBrokerModel.getOfxInstFId()), new DmBroker2Mapper());

            if (dmBrokerList.size() > 1)
            {
                //problem
                //todo: error message
                continue;
            }

            if (dmBrokerList.size() == 1)
            {
                //nothing to update here so just return
                continue;
            }

            //create it
            rows = this.jdbcTemplate.update(
                String.format(DmBroker2Model.SQL_DMOFX_BROKERS_INSERT,
                    dbBrokerModel.getDbBrokerId(), dbBrokerModel.getName(),
                    dbBrokerModel.getUrl(), dbBrokerModel.getOfxInstFId()));

            this.setBClientInstitutionsDataProviderIsDirty(true);

            //check it
            if (rows > 1)
            {
                //problem
                //todo: error message
                return false;
            }

            if (rows == 1)
            {
                //nothing to update here so just return
                continue;
            }
        }

        return true;
    }

    /**
     * Checks existence of dbOfx.Accounts
     * <p>
     * returns true if it exists
     * else
     * creates and returns true on success
     * else
     * returns false
     *
     * @return true/false
     */
    private Boolean checkDbOfxAccounts()
    {
        Collection<EditAccountModel> editAccountModels;
        List<DbAccount2Model> dbAccount2List;
        Integer rows;

        editAccountModels = this.allClientAccountsDataProvider.getItems();

        //iterate accountModels
        for (EditAccountModel editAccountModel : editAccountModels)
        {
            //check exist?
            dbAccount2List = this.jdbcTemplate.query(String.format(
                DbAccount2Model.SQL_DBOFX_ACCOUNTS_EXISTS2,
                editAccountModel.getBrokerId(),
                editAccountModel.getInvAcctIdFi(),
                SecurityUtils.getUserId()),
                new DbAccount2Mapper());

            if (dbAccount2List.size() > 1)
            {
                //bummer
                return false;
            }

            if (dbAccount2List.size() == 1)
            {
                //do update
                if (this.jdbcTemplate.update(String.format(
                    DbAccount2Model.SQL_DBOFX_ACCOUNTS_UPDATE,
                    editAccountModel.getInvAcctIdFi(), editAccountModel
                    .getDbAcctId())) == 1)
                {
                    continue;
                } else
                {
                    //todo: update failed
                    continue;
                }
            }

            //create it
            rows = this.jdbcTemplate.update(String.format(
                DbAccount2Model.SQL_DBOFX_ACCOUNTS_INSERT,
                editAccountModel.getBrokerId(), SecurityUtils.getUserId(),
                editAccountModel.getInvAcctIdFi()));

            this.setBAccountsDataProviderIsDirty(true);

            //check exist?
            if (rows > 1)
            {
                //bummer
                return false;
            }

            if (rows == 1)
            {
                continue;
            }
        }
        return true;
    }

    /**
     * Checks existence of dmOfx.Accounts
     * <p>
     * returns true if it exists
     * else
     * creates and returns true on success
     * else
     * returns false
     *
     * @return true/false
     */
    private Boolean checkDmOfxAccounts()
    {
        Collection<EditAccountModel> editAccountModels;
        List<EditAccountModel> editAccountList;
        List<DbAccount2Model> dbAccount2Models;
        List<DmAccount2Model> dmAccount2List;
        Integer rows;

        editAccountModels = this.allClientAccountsDataProvider.getItems();

        //iterate accountModels
        for (EditAccountModel editAccountModel : editAccountModels)
        {
            dmAccount2List = this.jdbcTemplate.query(String.format(
                DmAccount2Model.SQL_DMOFX_ACCOUNTS_EXISTS,
                editAccountModel.getDmAcctId(), editAccountModel.getBrokerId(),
                SecurityUtils.getUserId()), new DmAccount2Mapper());

            if (dmAccount2List.size() > 1)
            {
                //bummer
                return false;
            }

            if (dmAccount2List.size() == 1)
            {
                //exists: do update
                //todo: error
                if (this.jdbcTemplate.update(String.format(
                    DmAccount2Model.SQL_DMOFX_ACCOUNTS_UPDATE,
                    editAccountModel.getInvAcctIdFi(),
                    editAccountModel.getUserId(),
                    editAccountModel.getPwd(),
                    editAccountModel.getClientAcctName(),
                    editAccountModel.getActive(),
                    editAccountModel.getDmAcctId())) == 1)
                {
                    continue;
                }
            }

            //create it
            //get dbOfx.Accounts.AcctId
            //editAccountsModel.getDbAcctId() = null?
            dbAccount2Models = this.jdbcTemplate.query(String.format(
                DbAccount2Model.SQL_DBOFX_ACCOUNTS_EXISTS2,
                editAccountModel.getBrokerId(),
                editAccountModel.getInvAcctIdFi(),
                SecurityUtils.getUserId()),
                new DbAccount2Mapper());

            this.setBAccountsDataProviderIsDirty(true);

            if (dbAccount2Models.isEmpty() || dbAccount2Models.size() > 1)
            {
                //bummer
                //todo: error
                return false;
            }

            rows = this.jdbcTemplate.update(String.format(
                DmAccount2Model.SQL_DMOFX_ACCOUNTS_INSERT,
                SecurityUtils.getUserId(),
                editAccountModel.getBrokerId(),
                dbAccount2Models.get(0).getDbAcctId(),
                editAccountModel.getInvAcctIdFi(),
                editAccountModel.getUserId(),
                editAccountModel.getPwd(),
                editAccountModel.getDmOfxFId(),
                editAccountModel.getClientAcctName(),
                editAccountModel.getBActive() ? "Yes" : "No"));

            //check exist?
            if (rows > 1)
            {
                //bummer
                return false;
            }

            if (rows == 1)
            {
                continue;
            }
        }
        return true;
    }

    protected DataProvider retrieveAllFIDataProvider()
    {
        //all institutions
        this.allInstitutionsDataProvider = DataProvider.ofCollection(this.serviceTPC.getOfxInstitutionModels());
        return this.allInstitutionsDataProvider;
    }

    protected DataProvider retrieveClientFIDataProvider()
    {
        //client institutions
        this.clientFIDataProvider = DataProvider.ofCollection(this.serviceTPC.getClientOfxInstitutionModels());
        return this.clientFIDataProvider;
    }

    protected void retrieveAllClientAccountsDataProvider()
    {
        //client accounts for all client financial institutions
        //will filter when a financial institution is selected to reduce database calls
        this.allClientAccountsDataProvider = DataProvider.ofCollection(this.serviceTPC.getAccountModels());
    }

    @Override
    public void getPrefs(String prefix)
    {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void writePrefs(String prefix)
    {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
