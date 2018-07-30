package ir.codetower.smsbegir;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;

/**
 * Created by Mr-R00t on 1/19/2018.
 */

public class App extends Application {
    public static Context context;
    public static SharedPrefManager prefManager;
    public static Handler handler=new Handler();
    public static WebService webService;
    public static Contact contact=new Contact();
    public static DbHelper dbHelper;
    public static String IMEI="0";
    public static float inventory=0;
    public static String api="http://www.atomix.studio/smsService/api/v1/";
    public static Group defaultGroup;

    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
        webService=new WebService();
        dbHelper=new DbHelper(context);
        App.dbHelper.getDefaultGroup();
        prefManager=new SharedPrefManager("sms_begir");

    }
    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
