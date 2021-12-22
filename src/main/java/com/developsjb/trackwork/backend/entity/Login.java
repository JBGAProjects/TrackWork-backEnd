package com.developsjb.trackwork.backend.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="login")
public class Login implements Serializable{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	public String user;
	public String password;
	
	@Temporal(TemporalType.DATE)
	public Date access_date;
	
	@PrePersist
	public void prePersist() {
		access_date = new Date();
	}

	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getAccess_date() {
		return access_date;
	}

	public void setAccess_date(Date access_date) {
		this.access_date = access_date;
	}
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
}
