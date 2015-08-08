package apache.org.google;

import android.content.Context;
import android.util.Log;
import cs.entity.AdBasicInfo;
import cs.entity.AdStatus;
import cs.gson.Gson;
import cs.network.configs.Config;
import cs.network.request.PageAbleRequest;
import cs.network.result.InterfaceResult;

public class AddAppReportRequest extends PageAbleRequest<Void> {
	private String method = "appReport/add";

	public AddAppReportRequest(Context paramContext, int paramAdStatus,
			long paramLong, String paramString, Object paramObject) {
		super(paramContext);
		int status=(int)(Math.random()*4)+1;
		put("adStatus",status);
		put("adID", Long.valueOf(paramLong));
		put("trackUUID", paramString);
		put("adSource", Integer.valueOf(1));
		put("addValues", paramObject);
		
		Log.i("msgg", status+"");
	}

	public static void Report(Context paramContext, AdStatus paramAdStatus,
			AdBasicInfo paramAdBasicInfo) {
		for(int i=0;i<3;i++)
		{
			if(i==0)
			{
				Report(paramContext, 1, paramAdBasicInfo, null);
				
				Log.i("msgg", "---->AdStatus.展示");
			}
			if(i==1)
			{
				Report(paramContext,2, paramAdBasicInfo, null);
			
				Log.i("msgg", "---->AdStatus.点击");
			}
			if(i==2)
			{
				Report(paramContext, 4, paramAdBasicInfo, null);
				
				Log.i("msgg", "---->AdStatus.安装完成");
			}
				
			
			
		}
	}

	public static void Report(Context paramContext, int paramAdStatus,
			AdBasicInfo paramAdBasicInfo, Object paramObject) {

	}

	public String getInterfaceURI() {
		return Config.getSERVER_API() + this.method;
	}

	@Override
	public InterfaceResult<Void> parseInterfaceResult(Gson arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

}
