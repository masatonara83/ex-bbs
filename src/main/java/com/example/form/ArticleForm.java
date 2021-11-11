package com.example.form;

import javax.validation.constraints.Size;

public class ArticleForm {

	// 名前
	@Size(min=1,max=15,message="名前は１文字以上１５文字以内で入力してください")
	private String name;
	//記事の内容
	@Size(min=1,max=200,message="コメントは１文字以上２００文字以内で入力してください")
	private String content;
	
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
		return "ArticleForm [name=" + name + ", content=" + content + "]";
	}
	
	
}
