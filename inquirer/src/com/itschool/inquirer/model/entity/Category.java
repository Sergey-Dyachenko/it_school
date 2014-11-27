package com.itschool.inquirer.model.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties({ "inquirers" })
public class Category {
	
	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false)
	private String title;
	
	@ManyToMany(targetEntity = Inquirer.class, mappedBy = "categories", fetch = FetchType.LAZY)
	private Set<Inquirer> inquirers;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Set<Inquirer> getInquirers() {
		return inquirers;
	}

	public void setInquirers(Set<Inquirer> inquirers) {
		this.inquirers = inquirers;
	}
	
}
