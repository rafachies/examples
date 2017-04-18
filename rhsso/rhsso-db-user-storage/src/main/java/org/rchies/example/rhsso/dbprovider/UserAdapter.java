package org.rchies.example.rhsso.dbprovider;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.logging.Logger;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.RoleModel;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.adapter.AbstractUserAdapterFederatedStorage;


public class UserAdapter extends AbstractUserAdapterFederatedStorage {
    
	private static final Logger logger = Logger.getLogger(UserAdapter.class);

	protected UserEntity entity;
    protected String keycloakId;
    
    @PersistenceContext
	protected EntityManager em;

    public UserAdapter(KeycloakSession session, RealmModel realm, ComponentModel model, UserEntity entity) {
        super(session, realm, model);
        this.entity = entity;
        if (entity.getRoles() != null) {
            for (RoleEntity role : entity.getRoles()) {
                RoleModel roleModel = realm.getRole(role.getUsername());
                if (roleModel != null) {
                    this.grantRole(roleModel);
                    logger.infof("Granted user %s, role %s", entity.getUsername(), role);
                }
            }
        }
        keycloakId = StorageId.keycloakId(model, entity.getId());
    }
    
    public String getPassword() {
        return entity.getPassword();
    }

    public void setPassword(String password) {
        entity.setPassword(password);
    }

    @Override
    public String getUsername() {
        return entity.getUsername();
    }

    @Override
    public void setUsername(String username) {
        entity.setUsername(username);

    }

    @Override
    public void setEmail(String email) {
        entity.setEmail(email);
    }

    @Override
    public String getEmail() {
        return entity.getEmail();
    }

    @Override
    public String getId() {
        return keycloakId;
    }
}
