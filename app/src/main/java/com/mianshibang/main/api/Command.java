package com.mianshibang.main.api;

public interface Command {

	String feeds = "feeds";
	String recommendations = "recommendations";
	String questions = "questions";
	String question_with_ids = "question_with_ids";
	String detail = "question_detail";
	
	String post_question = "post_question";
	String post_questions = "post_questions";
	String post_detail = "post_detail";
	
	String classify = "classify";
	
	String add_favorite_folder = "add_favorite_folder";
	String update_favorite_folder = "update_favorite_folder";
	String delete_favorite_folder = "delete_favorite_folder";
	
	String favorite_folders = "favorite_folders";
	String favorite_detail = "favorite_detail";
	String favorite_folders_for_select = "folders_for_select";
	String favorite = "favorite";
	
	String login = "login";
	String register = "register";
	String update_user = "update_user_info";
	
	String like_question = "add_vote_question";
	String dislike_question = "remove_vote_question";
	String liked_questions = "voted_questions";
	
	String feedback = "feedback";
	String checkUpdate = "check_update";
	String banner = "banner";
}
