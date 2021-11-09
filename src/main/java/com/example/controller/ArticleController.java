package com.example.controller;


import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.Article;
import com.example.domain.Comment;
import com.example.form.ArticleForm;
import com.example.form.CommentForm;
import com.example.repository.ArticleRepository;
import com.example.repository.CommentRepository;

@Controller
@RequestMapping("article")
public class ArticleController {

	@Autowired
	private ArticleRepository repository;
	
	@Autowired
	private CommentRepository commentRepository;
	
	@ModelAttribute
	public ArticleForm setUpArticleForm() {
		return new ArticleForm();
	}
	
	@ModelAttribute
	public CommentForm setUpCommentForm() {
		return new CommentForm();
	}
	
	@RequestMapping("index")
	public String index(Model model) {
		List<Article> articleList = repository.findAll();
		
		List<Comment> commentList;
		for(int i = 0; i < articleList.size(); i++) {
			Article article = articleList.get(i);
			commentList = commentRepository.findByArticle(article.getId());
			article.setCommentList(commentList);
			articleList.set(i, article);
		}
		
		model.addAttribute("articleList", articleList);
		
		return "input-article";
	}
	
	@RequestMapping("/insert")
	public String insertArticle(ArticleForm form) {
		
		Article article = new Article();
		
		BeanUtils.copyProperties(form, article);
		repository.insert(article);
		
		return "redirect:/article/index";
	}
	
	@RequestMapping("insert-comment")
	public String insert(CommentForm form) {
		Comment comment = new Comment();
		System.out.println(form);
		BeanUtils.copyProperties(form, comment);
		comment.setArticleId(form.getIntArticleId());
		System.out.println(comment);
		commentRepository.insert(comment);
		
		return "redirect:/article/index";
		
		
	}
	
	@RequestMapping("delete")
	public String deleteArticle(Integer id) {
		commentRepository.deleteByArticleId(id);
		repository.deleteById(id);
		return "redirect:/article/index";
		
	}
}
