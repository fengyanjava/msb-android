package com.mianshibang.main.model;

import java.util.Date;
import java.util.List;

import com.mianshibang.main.api.CurrentUserApis;
import com.mianshibang.main.utils.TimeUtils;

public class MQuestion extends MData {
	public String description;
	public String tags;
	public String from;
	public String webUrl;
	public int disposed;
	public int userId;
	public int visible;
	public long dateline;

	public boolean isFavoritedByMe;
	public boolean isVotedByMe;

	public long voteCount;
	public long viewCount;
	public long favoriteCount;
	public Date favDateline;

	public MClassify classify;
	public List<MAnswer> answers;
	public MQuestionAudit audit;

	public String getDateline() {
		return TimeUtils.convert(dateline);
	}

	public MAnswer findBestAnswer() {
		if (answers == null) {
			return null;
		}

		for (MAnswer answer : answers) {
			if (answer.isBest()) {
				return answer;
			}
		}

		return null;
	}

	public boolean isFavoritedByMe() {
		if (CurrentUserApis.isUnLogin()) {
			return false;
		}
		return isFavoritedByMe;
	}

	public boolean isVotedByMe() {
		if (CurrentUserApis.isUnLogin()) {
			return false;
		}
		return isVotedByMe;
	}

	public boolean isPostByUser() {
		return audit != null;
	}
}
