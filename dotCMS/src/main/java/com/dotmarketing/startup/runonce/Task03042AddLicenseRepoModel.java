package com.dotmarketing.startup.runonce;

import com.dotcms.business.CloseDBIfOpened;
import java.util.List;

import com.dotmarketing.common.db.DotConnect;
import com.dotmarketing.db.DbConnectionFactory;
import com.dotmarketing.startup.AbstractJDBCStartupTask;
import com.dotmarketing.util.Logger;

public class Task03042AddLicenseRepoModel extends AbstractJDBCStartupTask {

    @Override
    @CloseDBIfOpened
    public boolean forceRun() {
        try {
            DotConnect dc=new DotConnect();
            dc.setSQL("select * from sitelic");
            dc.loadResult();
            return false;
        }
        catch(Exception ex) {
            return true;
        }
    }

    @Override
    public String getPostgresScript() {
        return "create table sitelic(id varchar(36) primary key, serverid varchar(100), license text not null, lastping timestamp not null)";
    }

    @Override
    public String getMySQLScript() {
        return "create table sitelic(id varchar(36) primary key, serverid varchar(100), license longtext not null, lastping datetime not null)";
    }

    @Override
    public String getOracleScript() {
        return "create table sitelic(id varchar(36) primary key, serverid varchar(100), license nclob not null, lastping date not null)";
    }

    @Override
    public String getMSSQLScript() {
        return "create table sitelic(id varchar(36) primary key, serverid varchar(100), license text not null, lastping datetime not null)";
    }


    @Override
    protected List<String> getTablesToDropConstraints() {
        return null;
    }

}
