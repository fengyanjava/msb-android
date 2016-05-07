package com.mianshibang.main.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.mianshibang.main.ui.About;
import com.mianshibang.main.ui.Detail;
import com.mianshibang.main.ui.EditInfo;
import com.mianshibang.main.ui.FavoriteDetail;
import com.mianshibang.main.ui.Feedback;
import com.mianshibang.main.ui.LoginRegister;
import com.mianshibang.main.ui.Main;
import com.mianshibang.main.ui.Post;
import com.mianshibang.main.ui.PostDetail;
import com.mianshibang.main.ui.QuestionsClassify;
import com.mianshibang.main.ui.QuestionsHistory;
import com.mianshibang.main.ui.QuestionsLiked;
import com.mianshibang.main.ui.QuestionsPosted;
import com.mianshibang.main.ui.Search;
import com.mianshibang.main.ui.Settings;
import com.mianshibang.main.ui.UserInfo;
import com.mianshibang.main.ui.Web;

public class UiNavigation {

	public static void startMain(Context context) {
		Intent intent = buildIntent(context, Main.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	public static void startLoginRegister(Context context) {
		Intent intent = buildIntent(context, LoginRegister.class);
		context.startActivity(intent);
	}

	public static void startDetail(Context context, String id) {
		Intent intent = buildIntent(context, Detail.class);
		intent.putExtra("id", id);
		context.startActivity(intent);
	}

	public static void startPostDetail(Context context, String id) {
		Intent intent = buildIntent(context, PostDetail.class);
		intent.putExtra("id", id);
		context.startActivity(intent);
	}

	public static void startQuestionsClassify(Context context, String title, String classifyId) {
		Intent intent = buildIntent(context, QuestionsClassify.class);
		intent.putExtra(QuestionsClassify.TitleString, title);
		intent.putExtra(QuestionsClassify.ClassifyId, classifyId);
		context.startActivity(intent);
	}

	public static void startQuestionsLiked(Context context) {
		Intent intent = buildIntent(context, QuestionsLiked.class);
		context.startActivity(intent);
	}

	public static void startQuestionsPosted(Context context) {
		Intent intent = buildIntent(context, QuestionsPosted.class);
		context.startActivity(intent);
	}

	public static void startFavoriteDetail(Context context, String folderId) {
		Intent intent = buildIntent(context, FavoriteDetail.class);
		intent.putExtra(FavoriteDetail.FolderId, folderId);
		context.startActivity(intent);
	}

	public static void startUserInfo(Context context) {
		Intent intent = buildIntent(context, UserInfo.class);
		context.startActivity(intent);
	}

	public static void startEditInfo(int requestCode, Activity context, String name, String value, String hint, int maxLength, boolean allowEmpty) {
		Intent intent = buildIntent(context, EditInfo.class);
		intent.putExtra(EditInfo.EXTRA_NAME, name);
		intent.putExtra(EditInfo.EXTRA_VALUE, value);
		intent.putExtra(EditInfo.EXTRA_HINT, hint);
		intent.putExtra(EditInfo.EXTRA_ALLOW_EMPTY, allowEmpty);
		intent.putExtra(EditInfo.EXTRA_MAX_LENGTH, maxLength);
		context.startActivityForResult(intent, requestCode);
	}

	public static void startSettings(Context context) {
		Intent intent = buildIntent(context, Settings.class);
		context.startActivity(intent);
	}

	public static void startFeedback(Context context) {
		Intent intent = buildIntent(context, Feedback.class);
		context.startActivity(intent);
	}

	public static void startBrowseHistory(Context context) {
		Intent intent = buildIntent(context, QuestionsHistory.class);
		context.startActivity(intent);
	}

	public static void startPost(Context context) {
		Intent intent = buildIntent(context, Post.class);
		context.startActivity(intent);
	}

	public static void startAbout(Context context) {
		Intent intent = buildIntent(context, About.class);
		context.startActivity(intent);
	}

	public static void startSearch(Context context) {
		startSearch(context, null);
	}

	public static void startSearch(Context context, String keyword) {
		Intent intent = buildIntent(context, Search.class);
		intent.putExtra(Search.Keyword, keyword);
		context.startActivity(intent);
	}

	public static void startWithUri(Context context, String uri) {
		if (TextUtils.isEmpty(uri)) {
			return;
		}

		if (uri.startsWith("http://") || uri.startsWith("https://")) {
			UiNavigation.startWeb(context, uri);
			return;
		}

		try {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setData(Uri.parse(uri));
			context.startActivity(intent);
		} catch (Exception e) {
		}
	}

	public static void startWeb(Context context, String url) {
		try {
			Intent intent = buildIntent(context, Web.class);
			intent.putExtra("url", url);
			context.startActivity(intent);
		} catch (Exception e) {
		}
	}

	private static Intent buildIntent(Context context, Class<?> cls) {
		return new Intent(context, cls);
	}

	public static void installAPK(Context context, String path) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.parse("file://" + path), "application/vnd.android.package-archive");
		context.startActivity(intent);
	}

}
