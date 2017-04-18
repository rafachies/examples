package org.rchies.example.rhsso.dbprovider;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


@NamedQueries({
        @NamedQuery(name="getRolesByUser", query="select u from RoleEntity u where u.username = :username"),
        @NamedQuery(name="getAllRoles", query="select u from RoleEntity u"),
})

@Entity
@Table(name="userrole")
public class RoleEntity {
    
	@Id
    private Integer id;
    private String username;
    private String role;
    
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
}
