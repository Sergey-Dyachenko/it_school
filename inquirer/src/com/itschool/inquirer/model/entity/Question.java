package com.itschool.inquirer.model.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.itschool.inquirer.Constants.QuestionType;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true, value = { "parent",  "answers", "items" })
public class Question {

	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false)
	private String content;

	@Column(nullable = false)
	private QuestionType type;

	@Column(nullable = false)
	private int sort = 0;

	@ManyToOne
	private Question parent;

	@OneToMany(targetEntity = Question.class, mappedBy = "parent", fetch = FetchType.LAZY)
	private Set<Question> children;

	@OneToMany(targetEntity = Item.class, mappedBy = "question", fetch = FetchType.LAZY)
	private Set<Item> items;
	
	@OneToMany(targetEntity = Answer.class, mappedBy = "question", fetch = FetchType.LAZY)
	private Set<Answer> answers;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public QuestionType getType() {
		return type;
	}

	public void setType(QuestionType type) {
		this.type = type;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public Question getParent() {
		return parent;
	}

	public void setParent(Question parent) {
		this.parent = parent;
	}

	public Set<Question> getChildren() {
		return children;
	}

	public void setChildren(Set<Question> children) {
		this.children = children;
	}

	public Set<Item> getItems() {
		return items;
	}

	public void setItems(Set<Item> items) {
		this.items = items;
	}

}
