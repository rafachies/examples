/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.rchies.example.rhsso.dbprovider;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.jboss.logging.Logger;
import org.keycloak.component.ComponentModel;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialInputValidator;
import org.keycloak.credential.CredentialModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.RoleModel;
import org.keycloak.models.UserCredentialModel;
import org.keycloak.models.UserModel;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.user.UserLookupProvider;


@Stateful
@Local(DBUserStorageProvider.class)
public class DBUserStorageProvider implements UserStorageProvider, UserLookupProvider, CredentialInputValidator {
	
	public static final String PASSWORD_CACHE_KEY = UserAdapter.class.getName() + ".password";

	private static final Logger logger = Logger.getLogger(DBUserStorageProvider.class);

	@PersistenceContext
	protected EntityManager em;

	protected ComponentModel model;
	protected KeycloakSession session;
	

	@Override
	public void close() {

	}

	@Override
	public UserModel getUserByEmail(String email, RealmModel realmModel) {
		return null;
	}

	@Override
	public UserModel getUserById(String id, RealmModel realmModel) {
		logger.info("getUserById: " + id);
		String persistenceId = StorageId.externalId(id);
		UserEntity entity = em.find(UserEntity.class, persistenceId);
		if (entity == null) {
			logger.info("could not find user by id: " + id);
			return null;
		}
		return new UserAdapter(session, realmModel, model, entity);
	}

	@Override
	public UserModel getUserByUsername(String username, RealmModel realmModel) {
		logger.info("getUserByUsername: " + username);
		TypedQuery<UserEntity> query = em.createNamedQuery("getUserByUsername", UserEntity.class);
		query.setParameter("username", username);
		List<UserEntity> result = query.getResultList();
		if (result.isEmpty()) {
			logger.info("could not find username: " + username);
			return null;
		}
		UserEntity userEntity = result.get(0);
		return new UserAdapter(session, realmModel, model, userEntity);
	}
	
	@Override
	public boolean isConfiguredFor(RealmModel realMode, UserModel userModel, String credentialType) {
		 logger.info(">>> isConfiguredFor");
		return supportsCredentialType(credentialType) && getPassword(userModel, realMode) != null;
	}

	@Override
	public boolean isValid(RealmModel realmModel, UserModel userModel, CredentialInput credentialInput) {
		logger.info(">>> Validating credential: " + ((UserCredentialModel)credentialInput).getValue());
		String password = getPassword(userModel, realmModel);
		UserCredentialModel credential = (UserCredentialModel)credentialInput;
		return password != null && password.equals(credential.getValue());
	}

	@Override
	public boolean supportsCredentialType(String credentialType) {
		 return CredentialModel.PASSWORD.equals(credentialType);
	}

	public void setModel(ComponentModel model) {
		this.model = model;
	}

	public void setSession(KeycloakSession session) {
		this.session = session;
	}
	
	public String getPassword(UserModel user, RealmModel realmModel) {
		TypedQuery<UserEntity> query = em.createNamedQuery("getUserByUsername", UserEntity.class);
		query.setParameter("username", user.getUsername());
		UserEntity userEntity = query.getSingleResult();
        logger.info(">>> Get Password: " + userEntity.getPassword());
        for (RoleEntity roleEntity : userEntity.getRoles()) {
        	 RoleModel roleModel = realmModel.getRole(roleEntity.getUsername());
             if (roleModel != null) {
                 user.grantRole(roleModel);
                 logger.infof("Granted user %s, role %s", userEntity.getUsername(), roleEntity.getRole());
             }
		}
        return userEntity.getPassword();
    }


}
