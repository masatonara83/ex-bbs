package com.example.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.Article;
import com.example.form.ArticleForm;
import com.example.repository.ArticleRipository;

@Controller
@RequestMapping("article")
public class ArticleController {

	@Autowired
	private ArticleRipository ripository;
	
	
	
	@ModelAttribute
	public ArticleForm setUpArticleForm() {
		return new ArticleForm();
	}
	
	@RequestMapping("index")
	public String index(Model model) {
		List<Article> articleList = ripository.findAll();
		model.addAttribute("articleList", articleList);
		
		return "input-article";
	}
	
	@RequestMapping("/insert")
	public String insertArticle(ArticleForm form, Model model) {
		
		Article article = new Article();
		
		BeanUtils.copyProperties(form, article);
		ripository.insert(article);
		
		return "redirect:/article/index";
	}
	
//	@RequestMapping("insert-comment")
//	public String insert
	
	@RequestMapping("delete")
	public String deleteArticle(Integer id) {
		ripository.deleteById(id);
		return "redirect:/article/index";
		
	}
}
