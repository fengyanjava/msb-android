package com.mianshibang.main.utils;

import java.lang.reflect.Method;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.text.ClipboardManager;
import android.text.TextUtils;

@SuppressWarnings("deprecation")
public class SystemUtils {

	private static String DeviceId;

	public static String getVersionName() {
		try {
			Context context = UIUtils.getContext();
			PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionName;
		} catch (Exception e) {
			return null;
		}
	}

	public static int getVersionCode() {
		try {
			Context context = UIUtils.getContext();
			PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (Exception e) {
			return 0;
		}
	}

	public static String getDeviceId() {
		if (DeviceId == null) {
			StringBuilder sb = new StringBuilder();
			sb.append(getIMEI());
			sb.append(getMacAddress());
			sb.append(getSerialNo());
			DeviceId = Md5.md5(sb.toString());
		}
		return DeviceId;
	}

	public static String getSerialNo() {
		try {
			Class<?> c = Class.forName("android.os.SystemProperties");
			Method get = c.getMethod("get", String.class, String.class);
			return (String) (get.invoke(c, "ro.serialno", "unknown"));
		} catch (Exception e) {
			return null;
		}
	}

	public static int getActivityCount() {
		try {
			ActivityManager am = (ActivityManager) UIUtils.getContext().getSystemService(Context.ACTIVITY_SERVICE);
			List<RunningTaskInfo> tasks = am.getRunningTasks(1);
			if (tasks == null || tasks.isEmpty()) {
				return 0;
			}
			return tasks.get(0).numActivities;
		} catch (Exception e) {
			return Integer.MAX_VALUE;
		}
	}

	public static void copyToClipboard(String text) {
		if (TextUtils.isEmpty(text)) {
			return;
		}
		ClipboardManager clipboard = (ClipboardManager) UIUtils.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
		clipboard.setText(text);
	}

	/** 获取android系统版本号 */
	public static String getOSVersion() {
		String release = android.os.Build.VERSION.RELEASE; // android系统版本号
		release = "android" + release;
		return release;
	}

	/** 获得android系统sdk版本号 */
	public static int getOSVersionSDKINT() {
		return android.os.Build.VERSION.SDK_INT;
	}

	/** 获取手机型号 */
	public static String getDeviceModel() {
		return android.os.Build.MODEL;
	}

	/** 获取设备的IMEI */
	public static String getIMEI() {
		try {
			Context context = UIUtils.getContext();
			TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			return tm.getDeviceId();
		} catch (Exception e) {
			return null;
		}
	}

	/** 取得当前sim手机卡的imsi */
	public static String getIMSI() {
		Context context = UIUtils.getContext();
		if (null == context) {
			return null;
		}
		String imsi = null;
		try {
			TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			imsi = tm.getSubscriberId();
		} catch (Exception e) {
			MLog.e(e.getMessage());
		}
		return imsi;
	}

	/** 返回手机服务商名字 */
	public static String getProvidersName() {
		String ProvidersName = null;
		// 返回唯一的用户ID;就是这张卡的编号神马的
		String IMSI = getIMSI();
		// IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
		if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
			ProvidersName = "中国移动";
		} else if (IMSI.startsWith("46001")) {
			ProvidersName = "中国联通";
		} else if (IMSI.startsWith("46003")) {
			ProvidersName = "中国电信";
		} else {
			ProvidersName = "其他服务商:" + IMSI;
		}
		return ProvidersName;
	}

	/** 获取当前设备的MAC地址 */
	public static String getMacAddress() {
		try {
			Context context = UIUtils.getContext();
			WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			WifiInfo info = wm.getConnectionInfo();
			return info.getMacAddress();
		} catch (Exception e) {
			return null;
		}
	}

	/** 获取CPU核心数 */
	public static int getCpuCount() {
		return Runtime.getRuntime().availableProcessors();
	}

	/** 获取Rom版本 */
	public static String getRomversion() {
		String rom = "";
		try {
			String modversion = getSystemProperty("ro.modversion");
			String displayId = getSystemProperty("ro.build.display.id");
			if (modversion != null && !modversion.equals("")) {
				rom = modversion;
			}
			if (displayId != null && !displayId.equals("")) {
				rom = displayId;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rom;
	}

	/** 获取系统配置参数 */
	public static String getSystemProperty(String key) {
		String pValue = null;
		try {
			Class<?> c = Class.forName("android.os.SystemProperties");
			Method m = c.getMethod("get", String.class);
			pValue = m.invoke(null, key).toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pValue;
	}

	/** 获取手机内部空间大小，单位为byte */
	public static long getTotalInternalSpace() {
		long totalSpace = -1L;
		try {
			String path = Environment.getDataDirectory().getPath();
			StatFs stat = new StatFs(path);
			long blockSize = stat.getBlockSize();
			long totalBlocks = stat.getBlockCount();// 获取该区域可用的文件系统数
			totalSpace = totalBlocks * blockSize;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return totalSpace;
	}

	/** 获取手机内部可用空间大小，单位为byte */
	public static long getAvailableInternalMemorySize() {
		long availableSpace = -1l;
		try {
			String path = Environment.getDataDirectory().getPath();// 获取 Android
																	// 数据目录
			StatFs stat = new StatFs(path);// 一个模拟linux的df命令的一个类,获得SD卡和手机内存的使用情况
			long blockSize = stat.getBlockSize();// 返回 Int ，大小，以字节为单位，一个文件系统
			long availableBlocks = stat.getAvailableBlocks();// 返回 Int
																// ，获取当前可用的存储空间
			availableSpace = availableBlocks * blockSize;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return availableSpace;
	}

	/** 获取单个应用最大分配内存，单位为byte */
	public static long getOneAppMaxMemory() {
		Context context = UIUtils.getContext();
		if (context == null) {
			return -1;
		}
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		return activityManager.getMemoryClass() * 1024 * 1024;
	}

	/** 获取手机剩余内存，单位为byte */
	public static long getAvailableMemory() {
		Context context = UIUtils.getContext();
		if (context == null) {
			return -1;
		}
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
		activityManager.getMemoryInfo(info);
		return info.availMem;
	}

	/** 手机低内存运行阀值，单位为byte */
	public static long getThresholdMemory() {
		Context context = UIUtils.getContext();
		if (context == null) {
			return -1;
		}
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
		activityManager.getMemoryInfo(info);
		return info.threshold;
	}

	/** 手机是否处于低内存运行 */
	public static boolean isLowMemory() {
		Context context = UIUtils.getContext();
		if (context == null) {
			return false;
		}
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
		activityManager.getMemoryInfo(info);
		return info.lowMemory;
	}
}
