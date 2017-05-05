package org.rchies.example.rhsso.dbprovider;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.logging.Logger;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.UserProvider;
import org.keycloak.storage.user.SynchronizationResult;

@Stateless
public class DBRolesSynchronizer {

	@PersistenceContext
	protected EntityManager em;

	private static final Logger logger = Logger.getLogger(DBRolesSynchronizer.class);

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public SynchronizationResult syncAll(KeycloakSessionFactory sessionFactory, RealmModel realm, SynchronizationResult synchronizationResult) {
		int itensAdded = 0;
		Query query = em.createNamedQuery("getAllRoles");
		List<RoleEntity> result = query.getResultList();
		for (RoleEntity roleEntity : result) {
			try {
				if (isNewRole(realm, roleEntity)) {
					logger.info(">>> Syncing roles: " + roleEntity.getRole());
					realm.addRole(roleEntity.getRole());
					itensAdded++;
				}
			} catch (Exception e) {
				logger.warn(e);
			}
		}
		
		query = em.createNamedQuery("getAllUsers");
		KeycloakSession session = sessionFactory.create();
		UserProvider userProvider = session.userLocalStorage();
		List<UserEntity> federatedUsers = query.getResultList();
		for (UserEntity federatedUser : federatedUsers) {
			UserModel userModel = userProvider.getUserByUsername(federatedUser.getUsername(), realm);
			if (userModel == null) {
				UserModel userAdded = userProvider.addUser(realm, federatedUser.getUsername());
				userAdded.setEnabled(true);
				itensAdded++;
			}
		}
		synchronizationResult.setAdded(itensAdded);
		return synchronizationResult;
	}

	private boolean isNewRole(RealmModel realm, RoleEntity roleEntity) {
		return realm.getRole(roleEntity.getRole()) == null;
	}

}
