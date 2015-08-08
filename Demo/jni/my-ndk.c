#include <stdio.h>
#include <jni.h>
#include <android/log.h>//表示调用Log头文件
#include <string.h>
#include <malloc.h>
#include "apache_org_google_LoadSo.h"

#define N 10//相当于java中的静态常量，分为简单的宏
#define LOG_TAG  "WLC"

#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)

//放在后面的函数，要先声明才能使用
char* Jstring2CStr(JNIEnv* env, jstring jstr);
void calljavaMethodforme(JNIEnv* env);
char* JstringToCStr(JNIEnv* env, jstring jstr);

JNIEXPORT jstring JNICALL Java_apache_org_google_LoadSo_stringFromJNI(
		JNIEnv *env, jclass obj) {

	LOGD("while make c");
	LOGI("可以使用中文吗？");

	//申请内存空间
	char* p = (char*) malloc(sizeof(char) * 1);
	*p = 'A';
	*(p + 1) = 'B';
	*(p + 2) = 'C';
	*(p + 3) = '\0';

	char prr[3] = { 'A', 'B', '\0' };
	LOGI("拷贝1之前prr = %s", prr);
	char arr[3] = { 'D', 'E', '\0' };
	strcat(prr, arr);
	LOGI("拷贝1之后prr = %s", prr);
	free(p);
	p = NULL; //释放完成一定设置为NULL,否则容易出错
	jint ja = Java_apache_org_google_LoadSo_add(env, obj, 100, 200);
	LOGI("ja = %d", ja);

	char* rtn = (char*) malloc(5 + 1); //"\0"

	//rtn[alen]='\0';
	int ij = 0;
	for (; ij < 5; ij++) {
		*(rtn + ij) = 'B';
	}
	*(rtn + 5) = 0;
	// free(rtn);//释放内存会报错
	jstring jstr = (*env)->NewStringUTF(env, rtn);


//	 jstring NewStringUTF(const char* bytes)
//	 { return functions->NewStringUTF(this, bytes); }这是c和C++的区别，C++进行了进一步的封装，
	free(rtn);
	rtn = NULL;
	return jstr;

}

//两个整数相加
JNIEXPORT jint JNICALL Java_apache_org_google_LoadSo_add(JNIEnv * evn,
		jclass obj, jint x, jint y) {
	return x + y;
}

//将java中的字符串传给C,并在C中添加Hello后返回
JNIEXPORT jstring JNICALL Java_apache_org_google_LoadSo_add_1hello_1InC(
		JNIEnv * env, jclass obj, jstring str) {
	//转换成
	//char* arr = Jstring2CStr(env, str);
	char* arr = JstringToCStr(env, str);
	//如果想要表示字符串的话，所定义的数组长度必须大于实际字符的个数即可，系统会在后面自动添加\0作为结束标记
	char he[12] = { ' ', 'H', 'e', 'l', 'l', 'o' };
	//系统会在后面自动添加\0作为结束标记
	char* hello = " Hello ni hao";
	strcat(arr, he);
	jstring sss = (*env)->NewStringUTF(env, arr);
	LOGI("执行完成了");

	return (*env)->NewStringUTF(env, arr);
}

JNIEXPORT jintArray JNICALL Java_apache_org_google_LoadSo_add_1intArray_1InC(
		JNIEnv * env, jclass obj, jintArray array) {

	//获取数组的长度
	jsize len = (*env)->GetArrayLength(env, array);
	int* parr = (*env)->GetIntArrayElements(env, array, 0);
	int i = 0;
	for (; i < len; i++) {
		*(parr + i) += 100;

	}

	return array;

}

/**
 * 返回值 char* 这个代表char数组的首地址
 *  Jstring2CStr 把java中的jstring的类型转化成一个c语言中的char 字符串
 */
char* Jstring2CStr(JNIEnv* env, jstring jstr) {

	char* rtn = NULL;
	jclass clsstring = (*env)->FindClass(env, "java/lang/String"); //String
	jstring strencode = (*env)->NewStringUTF(env, "utf-8"); // 得到一个java字符串  "utf-8"
	jmethodID mid = (*env)->GetMethodID(env, clsstring, "getBytes",
			"(Ljava/lang/String;)[B"); //[ String.getBytes("utf-8");
	jbyteArray barr = (jbyteArray) (*env)->CallObjectMethod(env, jstr, mid,
			strencode); // String .getByte("utf-8");
	jsize alen = (*env)->GetArrayLength(env, barr); // byte数组的长度
	jbyte* ba = (*env)->GetByteArrayElements(env, barr, JNI_FALSE);
	if (alen > 0) {
		rtn = (char*) malloc(alen + 1); //"\0"
		LOGI("分配ds1");
		memcpy(rtn, ba, alen);
		LOGI("分配2");
		rtn[alen] = '\0';
	}
	(*env)->ReleaseByteArrayElements(env, barr, ba, 0); //释放空间
	LOGI("分配2--rtn=%s", rtn);
	return rtn;
}


char* JstringToCStr(JNIEnv* env, jstring jstr) {

	char* cstr = NULL;
	//定义一个字符数组，这一点与上面的方法相比，缺点就出了，无法确定字符串的长度
	//不好定义数组的大小
	char*  charStr = (char*)malloc(128*sizeof(char));
	//初始化为0，代表着'\0',C语言中字符串的结束标记
	//JNI_TRUE JNI_FALSE表示返回JVM内部源字符串的一份拷贝，
	//并为新产生的字符串分配内存空间。如果值为JNI_FALSE，
	//表示返回JVM内部源字符串的指针，意味着可以通过指针修改源字符串的内容，
	//不推荐这么做，因为这样做就打破了Java字符串不能修改的规定。
	//但我们在开发当中，并不关心这个值是多少，通常情况下这个参数填NULL即可。
	//Java 虚拟机中得编码采用的是Unicode编码,C中采用的utf-8编码，故使用GetStringUTFChars
	jboolean* jb = NULL;
	cstr = (*env)->GetStringUTFChars(env,jstr,jb);
	if(cstr == NULL)
		return NULL;
	LOGI("cstr = %s",cstr);
	//将拼接好的字符串保存到字符数组中
	sprintf(charStr,"%s",cstr);
    //释放内存 GetStringUTFChars 与ReleaseStringUTFChars是一对，必须同时出现
	//就像malloc()与free()一样成对出现，一夫一妻制
	(*env)->ReleaseStringUTFChars(env, jstr, cstr);

	return charStr;
}








JNIEXPORT void JNICALL Java_apache_org_google_LoadSo_calMethod1(JNIEnv * env,
		jobject obj) {

	LOGD("------>calMethod1");
	//主要是同反射来调用Java的方法
	//1.找到java中的class
	jclass dpclass = (*env)->FindClass(env, "apache/org/google/LoadSo");
	LOGD("------>calMethod2");
	if (dpclass == 0) {
		LOGD("没有找到该类！");
		return;
	}
	//2. 找到类中的方法
	jmethodID methodId = (*env)->GetMethodID(env, dpclass, "sayHelloToHeXing",
			"()V");
	if (methodId == 0) {
		LOGD("没有找到该方法！");
		return;
	}
	LOGD("找到该方法！");
	//3 .执行方法
	(*env)->CallVoidMethod(env, obj, methodId);
	return;
}

JNIEXPORT void JNICALL Java_apache_org_google_LoadSo_calMethod2(JNIEnv * env,
		jobject obj) {

	LOGD("------>calMethod1");
	//主要是同反射来调用Java的方法
	//1.找到java中的class
	jclass dpclass = (*env)->FindClass(env, "apache/org/google/LoadSo");
	LOGD("------>calMethod2");
	if (dpclass == 0) {
		LOGD("没有找到该类！");
		return;
	}
	//2. 找到类中的方法
	jmethodID methodId = (*env)->GetMethodID(env, dpclass, "addXY", "(II)I");
	if (methodId == 0) {
		LOGD("没有找到该方法！");
		return;
	}
	LOGD("找到该方法！");
	//3 .执行方法
	int a = (*env)->CallIntMethod(env, obj, methodId, 3, 7);
	LOGD("a = %d", a);
	return;
}

JNIEXPORT void JNICALL Java_apache_org_google_LoadSo_calMethod3(JNIEnv * env,
		jobject obj) {

	LOGD("------>calMethod1");
	//主要是同反射来调用Java的方法
	//1.找到java中的class
	jclass dpclass = (*env)->FindClass(env, "apache/org/google/LoadSo");
	LOGD("------>calMethod2");
	if (dpclass == 0) {
		LOGD("没有找到该类！");
		return;
	}
	//2. 找到类中的方法
	jmethodID methodId = (*env)->GetMethodID(env, dpclass, "getName",
			"(Ljava/lang/String;)V");
	if (methodId == 0) {
		LOGD("没有找到该方法！");
		return;
	}
	LOGD("找到该方法！");
	//3 .执行方法
	(*env)->CallVoidMethod(env, obj, methodId,
			(*env)->NewStringUTF(env, "何星星"));

	return;
}

JNIEXPORT void JNICALL Java_apache_org_google_LoadSo_calMethod4(JNIEnv * env,
		jclass classz) {

	LOGD("------>calMethod1");
	//主要是同反射来调用Java的方法
	//1.找到java中的class
	//jclass dpclass = (*env)->FindClass(env,"apache/org/google/LoadSo");
	LOGD("------>calMethod2");
	if (classz == 0) {
		LOGD("没有找到该类！");
		return;
	}
	//2. 找到类中的方法
	jmethodID methodId = (*env)->GetStaticMethodID(env, classz, "getHello",
			"(Ljava/lang/String;)V");
	if (methodId == 0) {
		LOGD("没有找到该方法！");
		return;
	}
	LOGD("找到该方法！");
	//3 .执行方法
	(*env)->CallStaticVoidMethod(env, classz, methodId,
			(*env)->NewStringUTF(env, "static void method in c "));
	calljavaMethodforme(env);
	return;
}

JNIEXPORT void JNICALL Java_apache_org_google_SpalashActivity_hello(
		JNIEnv * env, jobject obj) {
	calljavaMethodforme(env);
	return;

}

void calljavaMethodforme(JNIEnv* env) {

	LOGD("------>calMethod1");
	//主要是同反射来调用Java的方法
	//1.找到java中的class
	jclass dpclass = (*env)->FindClass(env, "apache/org/google/LoadSo");
	LOGD("------>calMethod2");
	if (dpclass == 0) {
		LOGD("没有找到该类！");
		return;
	}
	//2. 找到类中的方法
	jmethodID methodId = (*env)->GetMethodID(env, dpclass, "getName",
			"(Ljava/lang/String;)V");
	if (methodId == 0) {
		LOGD("没有找到该方法！");
		return;
	}
	LOGD("找到该方法！");
	//3 .执行方法

	(*env)->CallVoidMethod(env, (*env)->AllocObject(env, dpclass), methodId,
			(*env)->NewStringUTF(env, "何星星-->AA"));
     return;
}

