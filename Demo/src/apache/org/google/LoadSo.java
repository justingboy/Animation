package apache.org.google;

import android.util.Log;

public class LoadSo {

	static {
		System.loadLibrary("my-ndk");
	}

	public native void getName();

	public native static String stringFromJNI();

	public native static String hello_form_c();

	// 两个整数相加
	public native static int add(int x, int y);

	// 将java中的字符串传给C,并在C中添加Hello后返回
	public native static String add_hello_InC(String str);

	// 在C中每个数组元素对应的值都加10
	public native static int[] add_intArray_InC(int arr[]);

	// 目的是让c代码调用Java方法
	public native void calMethod1();

	// 处理参数的方法
	public native void calMethod2();

	// 返回值是对象的方法
	public native void calMethod3();

	// 返回值是对象的方法
	public static native void calMethod4();

	public void sayHelloToHeXing() {
		Log.i("WLJ", "星星你好，在成都过的怎样了？");
	}

	public int addXY(int x, int y) {
		Log.i("WLJ", (x + y) + "");
		return x + y;
	}

	public void getName(String name) {
		Log.i("WLJ", name + "");
	}

	public static void getHello(String hello) {
		Log.i("WLJ", hello + "");
	}
}
