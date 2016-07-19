package com.dotmarketing.startup.runonce;

import java.util.List;

import com.dotmarketing.startup.AbstractJDBCStartupTask;

/**
 * Creates the new {@code system_event} table. The main goal of this table is to
 * store events generated by the system (i.e., notifications, errors,
 * information messages, etc.) that can be handled and interpreted by different
 * pieces of the application so they can react to them.
 * 
 * @author Jose Castro
 * @version 3.7
 * @since Jul 11, 2016
 */
public class Task03700AddingSystemEventTable extends AbstractJDBCStartupTask {

	@Override
	public boolean forceRun() {
		return true;
	}

	/**
	 * The SQL DDL for creating the table and index for PostgreSQL.
	 *
	 * @return
	 */
	@Override
	public String getPostgresScript () {
		return "CREATE TABLE system_event (\n" + 
				"identifier VARCHAR(36) NOT NULL,\n" +
				"event_type VARCHAR(50) NOT NULL,\n" +
				"payload TEXT NOT NULL,\n" +
				"created BIGINT NOT NULL,\n" +
				"PRIMARY KEY (identifier)\n" +
			");\n" +
			"CREATE INDEX idx_system_event ON system_event (created);";
	}

	/**
	 * The SQL DDL for creating the table and index for MySQL.
	 *
	 * @return
	 */
	@Override
	public String getMySQLScript () {
		return "CREATE TABLE system_event (\n" + 
					"identifier VARCHAR(36) NOT NULL,\n" +
					"event_type VARCHAR(50) NOT NULL,\n" +
					"payload LONGTEXT NOT NULL,\n" +
					"created BIGINT NOT NULL,\n" +
					"PRIMARY KEY (identifier)\n" +
				");\n" +
				"CREATE INDEX idx_system_event ON system_event (created);";
	}

	/**
	 * The SQL DDL for creating the table and index for Oracle.
	 *
	 * @return
	 */
	@Override
	public String getOracleScript () {
		return "CREATE TABLE system_event (\n" +
					"identifier VARCHAR(36) NOT NULL,\n" +
					"event_type VARCHAR(50) NOT NULL,\n" +
					"payload NCLOB NOT NULL,\n" +
					"created NUMBER(19, 0) NOT NULL,\n" +
					"PRIMARY KEY (identifier)\n" +
				");\n" +
				"CREATE INDEX idx_system_event ON system_event (created);";
	}

	/**
	 * The SQL DDL for creating the table and index for MSSQL.
	 *
	 * @return
	 */
	@Override
	public String getMSSQLScript () {
		return "CREATE TABLE system_event (\n" +
					"identifier VARCHAR(36) NOT NULL,\n" +
					"event_type VARCHAR(50) NOT NULL,\n" +
					"payload TEXT NOT NULL,\n" +
					"created BIGINT NOT NULL,\n" +
					"PRIMARY KEY (identifier)\n" +
				");\n" +
				"CREATE INDEX idx_system_event ON system_event (created);";
	}

	/**
	 * The SQL DDL for creating the table and index for H2.
	 *
	 * @return
	 */
	@Override
	public String getH2Script () {
		return "CREATE TABLE system_event (\n" +
					"identifier VARCHAR(36) NOT NULL,\n" +
					"event_type VARCHAR(50) NOT NULL,\n" +
					"payload TEXT NOT NULL,\n" +
					"created BIGINT NOT NULL,\n" +
					"PRIMARY KEY (identifier)\n" +
				")\n" +
				"CREATE INDEX idx_system_event ON system_event (created);";
	}

	@Override
	protected List<String> getTablesToDropConstraints() {
		// Not required for this task
		return null;
	}

}
