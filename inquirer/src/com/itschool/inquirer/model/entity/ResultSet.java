package com.itschool.inquirer.model.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true, value = { "results", "owner", "inqurer" })
public class ResultSet {

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	private Profile owner;

	@Column(nullable = false)
	private boolean completed = false;

	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;

	@Temporal(TemporalType.TIMESTAMP)
	private Date finishDate;

	private Double totalScore;

	@OneToMany(targetEntity = UserAnswer.class, mappedBy = "result", fetch = FetchType.LAZY)
	private Set<UserAnswer> results;

	@ManyToOne
	private Inquirer inqurer;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Profile getOwner() {
		return owner;
	}

	public void setOwner(Profile owner) {
		this.owner = owner;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(Date finishDate) {
		this.finishDate = finishDate;
	}

	public Double getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(Double totalScore) {
		this.totalScore = totalScore;
	}

	public Inquirer getInqurer() {
		return inqurer;
	}

	public void setInqurer(Inquirer inqurer) {
		this.inqurer = inqurer;
	}

	public Set<UserAnswer> getResults() {
		return results;
	}

	public void setResults(Set<UserAnswer> results) {
		this.results = results;
	}

}
