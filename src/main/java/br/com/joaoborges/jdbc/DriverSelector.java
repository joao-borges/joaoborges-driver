package br.com.joaoborges.jdbc;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

import br.com.joaoborges.database.DatabaseInfoCache;
import br.com.joaoborges.database.DatabaseInfoCache.ConnectionInfo;

/**
 * @author joaoborges
 */
public class DriverSelector implements Driver {

	@Override
	public Connection connect(String url, Properties info) throws SQLException {
		ConnectionInfo connectionInfo = DatabaseInfoCache.findConnectionInfo(url);
		Driver driver = connectionInfo.driver;
		String delegateUrl = connectionInfo.url;
		info.put("user", connectionInfo.username);
		info.put("username", connectionInfo.username);
		info.put("pass", connectionInfo.password);
		info.put("password", connectionInfo.password);
		info.put("v$session.program", System.getProperty("touch.nodeName"));

		System.out.println("[DRIVER] Connecting " + delegateUrl);

		return driver.connect(delegateUrl, info);
	}

	@Override
	public boolean acceptsURL(String url) throws SQLException {
		if (!DatabaseInfoCache.hasDatabase(url)) {
			return false;
		}

		ConnectionInfo connectionInfo = DatabaseInfoCache.findConnectionInfo(url);
		Driver driver = connectionInfo.driver;
		String delegateUrl = connectionInfo.url;

		return driver.acceptsURL(delegateUrl);
	}

	@Override
	public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
		ConnectionInfo connectionInfo = DatabaseInfoCache.findConnectionInfo(url);
		Driver driver = connectionInfo.driver;
		String delegateUrl = connectionInfo.url;
		info.put("username", connectionInfo.username);
		info.put("password", connectionInfo.password);

		return driver.getPropertyInfo(delegateUrl, info);
	}

	@Override
	public int getMajorVersion() {
		return 1;
	}

	@Override
	public int getMinorVersion() {
		return 0;
	}

	@Override
	public boolean jdbcCompliant() {
		return true;
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		throw new SQLFeatureNotSupportedException();
	}

}
