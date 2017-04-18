package org.rchies.example.rhsso.dbprovider;

import java.util.Date;

import javax.naming.InitialContext;

import org.jboss.logging.Logger;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.models.KeycloakSessionTask;
import org.keycloak.models.utils.KeycloakModelUtils;
import org.keycloak.storage.UserStorageProviderFactory;
import org.keycloak.storage.UserStorageProviderModel;
import org.keycloak.storage.user.ImportSynchronization;
import org.keycloak.storage.user.SynchronizationResult;


public class DBUserStorageProviderFactory implements UserStorageProviderFactory<DBUserStorageProvider>, ImportSynchronization {

	private static final Logger logger = Logger.getLogger(DBUserStorageProviderFactory.class);

	@Override
	public DBUserStorageProvider create(KeycloakSession session, ComponentModel model) {
		try {
			logger.info(">>>> Creating DB User Storage Provider .....");
			InitialContext ctx = new InitialContext();
			DBUserStorageProvider provider = (DBUserStorageProvider)ctx.lookup("java:global/rhsso-db-user-storage/DBUserStorageProvider");
			provider.setModel(model);
			provider.setSession(session);
			return provider;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


	@Override
	public String getId() {
		return "DB Provider";
	}

	@Override
	public String getHelpText() {
		return "DB Example User Storage Provider";
	}

	@Override
	public void close() {
		logger.info("<<<<<< Closing factory");
	}

	@Override
	public SynchronizationResult sync(KeycloakSessionFactory sessionFactory, String realmId, UserStorageProviderModel providerModel) {
		SynchronizationResult synchronizationResult = new SynchronizationResult();
		KeycloakModelUtils.runJobInTransaction(sessionFactory, new KeycloakSessionTask() {
			@Override
			public void run(KeycloakSession session) {
				try {
					InitialContext ctx = new InitialContext();
					DBRolesSynchronizer synchronizer = (DBRolesSynchronizer)ctx.lookup("java:global/rhsso-db-user-storage/DBRolesSynchronizer");
					synchronizer.syncAll(session.realms().getRealm(realmId), synchronizationResult);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});
		return synchronizationResult;
	}

	@Override
	public SynchronizationResult syncSince(Date arg0, KeycloakSessionFactory arg1, String arg2, UserStorageProviderModel arg3) {
		return null;
	}
}
