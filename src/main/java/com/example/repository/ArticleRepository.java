package com.example.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.domain.Article;
import com.example.domain.Comment;

@Repository
public class ArticleRepository {

	private static final RowMapper<Article> ARTICLE_ROW_MAPPER = new BeanPropertyRowMapper<>(Article.class);
	
	//ResultSetオブジェクトに格納された複数行分のデータをList<Article>変数にセットしてreturnする
	private static final ResultSetExtractor<List<Article>> ARTICLE_COMMENT_RESULTSET = (rs) -> {
		
		//初めにデータを格納する為の変数を宣言
		List<Article> articleList = new ArrayList<>();
		
		//コメントを格納する為のList<Comment>変数を宣言
		List<Comment> commentList = null;
		
		//articlesテーブルは結合した際に複数行にわたり同じデータが出力される可能性があるため、前のarticlesテーブルのIDを保持するための変数を宣言
		int beforeIdNum = 0;
		//ResultSetオブジェクトに格納された複数のデータをList<Article>変数に格納していく
		while(rs.next()) {
			//現在検索しているArticleテーブルのIDを格納するための変数を宣言
			int nowIdNum = rs.getInt("a_id");
			//現在検索しているArticleテーブルのIDと前のArticleテーブルのIDが違う場合は新たにClabオブジェクトを作成する
			if(nowIdNum != beforeIdNum) {
				Article article = new Article();
				article.setId(nowIdNum);
				article.setName(rs.getString("a_name"));
				article.setContent(rs.getString("a_content"));
				//コメントがあった際にArticleオブジェクトのcommentListに格納するため空のArrayListをセットしておく
				commentList = new ArrayList<Comment>();
				article.setCommentList(commentList);
				
				articleList.add(article);
			}
			
			//ArticleにCommentがいない場合はCommentオブジェクトを作成しないようにする
			if(rs.getInt("c_id") != 0) {
				Comment comment = new Comment();
				comment.setId(rs.getInt("c_id"));
				comment.setName(rs.getString("c_name"));
				comment.setContent(rs.getString("c_content"));
				comment.setArticleId(rs.getInt("c_article_id"));
				
				//commentをArticleオブジェクト内にセットされているcommentListに直接追加する
				commentList.add(comment);
			}
			
			beforeIdNum = nowIdNum;
		}
		return articleList;
	};
	
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	public List<Article> findByAllTable(){
		
		String sql = "SELECT a.id AS a_id, a.name AS a_name, a.content AS a_content, c.id AS c_id, c.name AS c_name, c.content AS C_content, c.article_id AS c_article_id FROM "
				+ "articles a JOIN comments c ON a.id = c.article_id ORDER BY a.id DESC, c.id DESC;";
		
		List<Article> articleList = template.query(sql, ARTICLE_COMMENT_RESULTSET);
		
		return articleList;
		
	}
	
	public List<Article> findAll(){
		String sql = "SELECT * FROM articles ORDER BY id DESC";
		
		List<Article> articleList = template.query(sql, ARTICLE_ROW_MAPPER);
		
		return articleList;
	}
	
	public void insert(Article article) {
		String insertSql = "INSERT INTO articles (name, content) VALUES (:name, :content);";
		SqlParameterSource param = new BeanPropertySqlParameterSource(article);
		template.update(insertSql, param);
	}
	
	public void deleteById(Integer id) {
		String deleteSql = "DELETE FROM articles WHERE id = :id;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		template.update(deleteSql,param);
	}
}
