package com.example.form;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

public class CommentForm {

	@Valid
	private String articleId;
	@NotBlank(message="入力してください")
	private String name;
	@NotBlank(message="入力してください")
	private String content;
	
	public int getIntArticleId() {
		return Integer.parseInt(articleId);
	}
	
	public String getArticleId() {
		return articleId;
	}
	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "CommentForm [articleId=" + articleId + ", name=" + name + ", content=" + content + "]";
	}

	
	
	
}
