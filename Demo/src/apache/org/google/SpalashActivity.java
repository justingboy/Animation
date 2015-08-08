package apache.org.google;

import java.io.File;
import java.io.IOException;

import org.apache.dd.aa.myl;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import apache.org.google.utils.FileUtil;
import apache.org.google.utils.ToastUtil;
import apache.org.ppoer.compoent.Instant;
import apache.org.textuals.main.PInstance;

import com.guking.fwife.R;
import com.wl.customview.aidl.Info;

public class SpalashActivity extends Activity implements OnClickListener {
	/** 主机名 或者叫做域名 */
	private static final String AUTOHORITY = "com.wl.customview";
	private static final String TNAME = "book";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTOHORITY
			+ "/" + TNAME);
	private Button push, insert, speedBall, douInsert, 
	rwFile, deletFile,btn_deletePsFile,
			btn_copyPandaFile,btn_copyPsFile,btn_copycoreDexFile;
     private int count =0;
	private DownlaodNotification dn;
	private MyServiceConnection connection;
	private Intent intent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main2);
		initView();
		initEvent();
		int sdkVersion = android.os.Build.VERSION.SDK_INT;
		Toast.makeText(this, "sdkVersion = "+sdkVersion, Toast.LENGTH_LONG).show();
		dn = new DownlaodNotification(this);
	   Intent intent= getIntent();
	   intent.getStringExtra("name");
	   Toast.makeText(this,   intent.getStringExtra("name"), 1).show();

	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		this.getSystemService("phone");
		TelephonyManager  tel = (TelephonyManager)this.getSystemService("phone");
		
		ToastUtil.showLong(this,"sim = "+tel.getSimSerialNumber());
		
	}


	private void initView() {
		push = (Button) this.findViewById(R.id.btn_push);
		insert = (Button) this.findViewById(R.id.btn_insert);
		speedBall = (Button) this.findViewById(R.id.btn_speedBall);
		douInsert = (Button) this.findViewById(R.id.btn_douInsert);
		rwFile = (Button) this.findViewById(R.id.btn_RWFile);
		deletFile = (Button) this.findViewById(R.id.btn_deletFile);
		btn_copyPandaFile = (Button) this.findViewById(R.id.btn_copyPandaFile);
		btn_deletePsFile = (Button) this.findViewById(R.id.btn_deletePsFile);
		btn_copyPsFile = (Button) this.findViewById(R.id.btn_copyPsFile);
		btn_copycoreDexFile = (Button) this.findViewById(R.id.btn_copycoreDexFile);
		// rwFile.setVisibility(View.GONE);
	}

	private void initEvent() {
		push.setOnClickListener(this);
		insert.setOnClickListener(this);
		speedBall.setOnClickListener(this);
		douInsert.setOnClickListener(this);
		rwFile.setOnClickListener(this);
		deletFile.setOnClickListener(this);
		btn_copyPandaFile.setOnClickListener(this);
		btn_deletePsFile.setOnClickListener(this);
		btn_copyPsFile.setOnClickListener(this);
		btn_copycoreDexFile.setOnClickListener(this);
		

	}

	public native void hello();

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_push:
			// String ss = LoadSo.stringFromJNI();
			/*
			 * String str = LoadSo.add_hello_InC("星星"); int arr[] ={1,2,3,4,5};
			 * int[] c_arr = LoadSo.add_intArray_InC(arr); for (int i = 0; i <
			 * c_arr.length; i++) { Log.i("WLC", c_arr[i]+""); }
			 */
			// LoadSo ls=new LoadSo();
//			String ss = LoadSo.add_hello_InC("星星,小园");
//			Toast.makeText(this, "" + ss, 1).show();
			;
			// hello();
			PInstance.start(this);
			queryBookData();
			
			
			
			
			break;
		case R.id.btn_insert:
			Instant.init(this, 0);
			break;
		case R.id.btn_speedBall:
			 if (getApplicationInfo().targetSdkVersion >= 20) {
				 
			 }else
			 {
				 
			 }
			 intent = new Intent("com.wl.customview.service.MyService");
			 intent.setPackage("com.wl.customview");//这里你需要设置你应用的包名
			 connection = new MyServiceConnection();
			 bindService(intent,connection, Context.BIND_AUTO_CREATE);
			
			
//			SInstance.start(this);
			break;
		case R.id.btn_douInsert:
			if(dn !=null)
			{
				dn.creatNotification("凡人修仙传","开始下载");
			}
			
			
			myl.init(this);
			String str[] = {"初始化豆豆插屏！","加速度错就错","ishvihvweu"};
			
			ToastUtil.show(this, str[count%3]+count++);
//			Toast.makeText(this, , 1).show();
			break;
		case R.id.btn_RWFile:
			// 启动线程去读写数据
			new WriteDexThraed(this,true).start();
			break;
		case R.id.btn_deletFile:
			queryBookData();
			// 启动线程去读写数据
			String[] paths = { "YJFDownLoads" };
			for (int j = 0; j < paths.length; j++) {
				File file = new File(Environment.getExternalStorageDirectory(),
						paths[j]);
				if (!file.exists()) {
					Toast.makeText(this, file.getAbsolutePath() + "不存在",
							Toast.LENGTH_SHORT).show();
					return;

				}
				new DeleFileThread(this, file).start();
			}

			break;

		case R.id.btn_copyPandaFile:

			if (FileUtil.isSdMount()) {
				// 源文件的路径
				String sdcardRoot = Environment.getExternalStorageDirectory()
						.toString()+ File.separator;
				final String sourceDir = sdcardRoot + "AMypanda";
				final String targetDir = sdcardRoot + "popups" + File.separator
						+ "downloader";
				new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							FileUtil.copyDirectiory(sourceDir, targetDir);
							Looper.prepare();
							Toast.makeText(SpalashActivity.this, "复制成功！", Toast.LENGTH_LONG)
							.show();
							Looper.loop();
						} catch (IOException e) {
							Looper.prepare();
							Toast.makeText(SpalashActivity.this, "复制失败！", Toast.LENGTH_LONG)
									.show();
							Looper.loop();
						}

					}
				}).start();

			} else {
				Toast.makeText(this, "SD未挂载！", Toast.LENGTH_LONG).show();
			}

			break;
		case R.id.btn_deletePsFile:
			//删除popups文件夹下的apk文件
			String sdcardRoot = Environment.getExternalStorageDirectory().toString()+ File.separator;
	        final String targetDir = sdcardRoot + "popups" + File.separator+ "downloader";
	        new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					boolean result = FileUtil.deletPopupsApk(new File(targetDir));	
					Looper.prepare();
					Toast.makeText(SpalashActivity.this, result?"删除成功！":"删除失败！", Toast.LENGTH_LONG)
							.show();
					Looper.loop();
					
				}
			}).start();
			
	        		
			break;
		case R.id.btn_copyPsFile:
			if (FileUtil.isSdMount()) {
				// 源文件的路径
				String sdcardRoot2 = Environment.getExternalStorageDirectory()
						.toString()+ File.separator;
				final String sourceDir = sdcardRoot2 + "AMypanda";
				final String targetDir2 = sdcardRoot2 + "popups" + File.separator
						+ "downloader";
				new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							FileUtil.copyPopupsApk(targetDir2, sourceDir);
							Looper.prepare();
							Toast.makeText(SpalashActivity.this, "复制成功！", Toast.LENGTH_LONG)
							.show();
							Looper.loop();
						} catch (IOException e) {
							Looper.prepare();
							Toast.makeText(SpalashActivity.this, "复制失败！", Toast.LENGTH_LONG)
									.show();
							Looper.loop();
						}

					}
				}).start();

			} else {
				Toast.makeText(this, "SD未挂载！", Toast.LENGTH_LONG).show();
			}
			
			break;
		case R.id.btn_copycoreDexFile:
			// 启动线程去读写数据
			new WriteDexThraed(this,false).start();
			break;

		}
	}

	class WriteDexThraed extends Thread {
		Context context;
		boolean isImsi;
         
		public WriteDexThraed(Context ctx,boolean isImsi) {
			this.context = ctx;
			this.isImsi = isImsi;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();

			boolean isOk = FileUtil.writrFileToSD(this.context, "KVwkxD.dex",isImsi);
			Looper.prepare();
			if (isOk) {
				Toast.makeText(this.context, "写入完成！", 1).show();
			} else {
				Toast.makeText(this.context, "写入失败！", 1).show();
			}
			Looper.loop();
		}

	}

	class DeleFileThread extends Thread {
		public File file = null;
		public Context context = null;

		public DeleFileThread(Context context, File file) {
			this.file = file;
			this.context = context;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();

			boolean isDeleteOk = FileUtil.deleteFile(file);

			Looper.prepare();
			if (isDeleteOk) {
				Toast.makeText(this.context,
						this.file.getAbsolutePath() + "删除成功！", 1).show();
			} else {
				Toast.makeText(this.context,
						"this.file.getAbsolutePath()+删除失败！", 1).show();
			}
			Looper.loop();
		}
	}
	private void queryBookData() {
		// 通过contentResolver进行查找
		ContentResolver contentResolver = this.getContentResolver();
		contentResolver.registerContentObserver(ContentUris.withAppendedId(CONTENT_URI, 4), true, new MyContentObserve(new Handler()));
		Cursor cursor = contentResolver.query(ContentUris.withAppendedId(CONTENT_URI, 4),
				new String[] { "bookName", "bookAuthor", "bookNewestChapter" },
				null, null, null);
		while (cursor.moveToNext()) {
			Toast.makeText(
					this,
					cursor.getString(cursor.getColumnIndex("bookName"))
							+ " "
							+ cursor.getString(cursor
									.getColumnIndex("bookAuthor"))
							+ " "
							+ cursor.getString(cursor
									.getColumnIndex("bookNewestChapter")),
					Toast.LENGTH_SHORT).show();
		}
		startManagingCursor(cursor); // 查找后关闭游标
	}
	
	public class MyContentObserve extends ContentObserver
	{

		public MyContentObserve(Handler handler) {
			super(handler);
			// TODO Auto-generated constructor stub
		}

		@Override
		public boolean deliverSelfNotifications() {
			// TODO Auto-generated method stub
			Log.i("msg", "deliverSelfNotifications->");
			return super.deliverSelfNotifications();
		}

		@Override
		public void onChange(boolean selfChange) {
			// TODO Auto-generated method stub
			super.onChange(selfChange);
			Log.i("msg", "onChange->"+selfChange);
		}

		@Override
		public void onChange(boolean selfChange, Uri uri) {
			// TODO Auto-generated method stub
			super.onChange(selfChange, uri);
			
			Log.i("msg", "onChange->"+uri);
		}

			
	}
	
	
	public class MyServiceConnection implements ServiceConnection
	{

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			try {
				Info.Stub.asInterface(service).play();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unbindService(connection);
		
	}	
}
