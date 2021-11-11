package com.example.controller;


import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
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
		
		
		for(int i = 0; i < articleList.size(); i++) {
			Article article = articleList.get(i);
			List<Comment> commentList = commentRepository.findByArticle(article.getId());
			article.setCommentList(commentList);
			//articleList.set(i, article);
		}
		
		model.addAttribute("articleList", articleList);
		
		return "input-article";
	}
	
	@RequestMapping("/all")
	public String findByAllTable(Model model) {
		List<Article> articleList = repository.findByAllTable();
		
		model.addAttribute("articleList", articleList);
		
		return "input-article";
	}
	
	@RequestMapping("/insert")
	public String insertArticle(@Validated ArticleForm form,
			BindingResult result, Model model) {
		if(result.hasErrors()) {
			return findByAllTable(model);
		}
		
		Article article = new Article();
		
		BeanUtils.copyProperties(form, article);
		repository.insert(article);
		
		return "redirect:/article/index";
	}
	
	@RequestMapping("insert-comment")
	public String insert(@Validated CommentForm commentForm,
			BindingResult result, Model model) {
		
		if(result.hasErrors()) {
			return findByAllTable(model);
		}
		
		Comment comment = new Comment();
		BeanUtils.copyProperties(commentForm, comment);
		comment.setArticleId(commentForm.getIntArticleId());
		commentRepository.insert(comment);
		
		return "redirect:/article/all";
		
		
	}
	
	@RequestMapping("delete")
	public String deleteArticle(Integer id) {
		commentRepository.deleteByArticleId(id);
		repository.deleteById(id);
		return "redirect:/article/all";
		
	}
}
