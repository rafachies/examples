package org.rchies.example.rhsso.dbprovider;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.logging.Logger;
import org.keycloak.models.RealmModel;
import org.keycloak.storage.user.SynchronizationResult;

@Stateless
public class DBRolesSynchronizer {

	@PersistenceContext
	protected EntityManager em;

	private static final Logger logger = Logger.getLogger(DBRolesSynchronizer.class);

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public SynchronizationResult syncAll(RealmModel realm, SynchronizationResult synchronizationResult) {
		int rolesAdded = 0;
		Query query = em.createNamedQuery("getAllRoles");
		List<RoleEntity> result = query.getResultList();
		for (RoleEntity roleEntity : result) {
			try {
				if (isNewRole(realm, roleEntity)) {
					logger.info(">>> Syncing roles: " + roleEntity.getRole());
					realm.addRole(roleEntity.getRole());
					rolesAdded++;
				}
			} catch (Exception e) {
				logger.warn(e);
			}
		}
		synchronizationResult.setAdded(rolesAdded);
		return synchronizationResult;
	}

	private boolean isNewRole(RealmModel realm, RoleEntity roleEntity) {
		return realm.getRole(roleEntity.getRole()) == null;
	}

}
