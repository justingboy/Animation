package apache.org.google.utils;

import android.content.Context;
import android.widget.Toast;

//Toast统一管理类
public class ToastUtil {
	/** 判断是否显示 */
	public static boolean isShow = true;

	private static Toast toast = null;

	private ToastUtil() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	/**
	 * 短时间显示Toast
	 * 
	 * @param context
	 * @param message
	 */
	public static void showShort(Context context, CharSequence message) {
		if (isShow)
			Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 短时间显示Toast
	 * 
	 * @param context
	 * @param message
	 */
	public static void showShort(Context context, int message) {
		if (isShow)
			Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 长时间显示Toast
	 * 
	 * @param context
	 * @param message
	 */
	public static void showLong(Context context, CharSequence message) {
		if (isShow)
			Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}

	/**
	 * 长时间显示Toast
	 * 
	 * @param context
	 * @param message
	 */
	public static void showLong(Context context, int message) {
		if (isShow)
			Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}

	/**
	 * 自定义显示Toast时间
	 * 
	 * @param context
	 * @param message
	 * @param duration
	 */
	public static void show(Context context, CharSequence message, int duration) {
		if (isShow)
			Toast.makeText(context, message, duration).show();
	}

	/**
	 * 自定义显示Toast时间
	 * 
	 * @param context
	 * @param message
	 * @param duration
	 */
	public static void show(Context context, int message, int duration) {
		if (isShow)
			Toast.makeText(context, message, duration).show();
	}

	/**
	 * 短时间显示Toast ,并且是立即显示Toast 
	 * 主要用于连续显示时间长的问题
	 * 
	 * @param context
	 * @param message
	 */
	public static void show(Context context, CharSequence message) {
		if (null == toast)
			toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
		else
			toast.setText(message);
		toast.show();

	}

}