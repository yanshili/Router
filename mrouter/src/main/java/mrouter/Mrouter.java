package mrouter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 作者： mooney
 * 日期： 2018/1/31
 * 邮箱： shili_yan@sina.com
 * 描述：
 */

public enum Mrouter {
    INSTANCE;


    private static final String TAG="Mrouter";

    private Context mContext;

    private Map<String, Class> routerCache=new HashMap<>();

    public static Mrouter getInstance(){
        return INSTANCE;
    }

    public void init(Context context){
        mContext=context.getApplicationContext();
        routerCache=new HashMap<>();
    }

    public boolean open(String routerUri){

        return open(routerUri, null);
    }


    public boolean open(String routerUri, Intent dataIntent){

        if (!MrouterHelper.isValidURI(routerUri)){
            Log.e(TAG, "router \"" +routerUri+ "\" is invalid uri path!!!");
            return false;
        }

        if (routerUri.startsWith("http://")||routerUri.startsWith("https://")){

            Uri uri = Uri.parse(routerUri); // url为你要链接的地址
//            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            dataIntent.setAction(Intent.ACTION_VIEW);
            dataIntent.setData(uri);
            dataIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                mContext.startActivity(dataIntent);
                return true;
            } catch (Exception e) {
                Log.e(TAG, "Errors of opening web view with the router \""+routerUri+"\"!!!\n"+e);
                return false;
            }

        }

        Class activityClazz = findActivity(routerUri);
        if (activityClazz==null){
            Log.e(TAG, "there is no activity of the router \""+routerUri+"\"!!!");
            return false;
        }
        Uri uri = Uri.parse(routerUri);
        if (dataIntent==null){
            dataIntent=new Intent();
        }

        dataIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Set<String> queryParameterNames = uri.getQueryParameterNames();
        if(queryParameterNames != null && queryParameterNames.size() > 0){
            for (String key : queryParameterNames){
                dataIntent.putExtra(key, uri.getQueryParameter(key));
            }
        }

        dataIntent.setClass(mContext,activityClazz);
        try {
            mContext.startActivity(dataIntent);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Errors of opening the router \""+routerUri+"\"!!!\n"+e);
            return false;
        }
    }

    public boolean openForResult(Object context, String routerUri, int requestCode, Intent dataIntent) {

        if (MrouterHelper.isValidURI(routerUri)){
            Log.e(TAG, "router \"" +routerUri+ "\" is invalid uri path!!!");
            return false;
        }

        if (routerUri.startsWith("http://")||routerUri.startsWith("https://")){

            Uri uri = Uri.parse(routerUri); // url为你要链接的地址
//            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            dataIntent.setAction(Intent.ACTION_VIEW);
            dataIntent.setData(uri);
            dataIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                mContext.startActivity(dataIntent);
                return true;
            } catch (Exception e) {
                Log.e(TAG, "Errors of opening web view with the router \""+routerUri+"\"!!!\n"+e);
                return false;
            }

        }else {

            Class activityClazz = findActivity(routerUri);
            if (activityClazz==null){
                Log.e(TAG, "there is no activity of the router \""+routerUri+"\"!!!");
                return false;
            }
            Uri uri = Uri.parse(routerUri);
            if (dataIntent==null){
                dataIntent=new Intent();
            }

//        dataIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Set<String> queryParameterNames = uri.getQueryParameterNames();
            if(queryParameterNames != null && queryParameterNames.size() > 0){
                for (String key : queryParameterNames){
                    dataIntent.putExtra(key, uri.getQueryParameter(key));
                }
            }

            dataIntent.setClass(mContext,activityClazz);
        }



        if (context instanceof Activity){
            try {
                ((Activity)context).startActivityForResult(dataIntent,requestCode);
                return true;
            } catch (Exception e) {
                Log.e(TAG, "Errors of opening the router \""+routerUri+"\" for result!!!\n"+e);
                return false;
            }
        }else if (context instanceof Fragment){
            try {
                ((Fragment)context).startActivityForResult(dataIntent,requestCode);
                return true;
            } catch (Exception e) {
                Log.e(TAG, "Errors of opening the router \""+routerUri+"\" for result!!!\n"+e);
                return false;
            }
        }else if (context instanceof android.app.Fragment){
            try {
                ((Fragment)context).startActivityForResult(dataIntent,requestCode);
                return true;
            } catch (Exception e) {
                Log.e(TAG, "Errors of opening the router \""+routerUri+"\" for result!!!\n"+e);
                return false;
            }
        }else {
            Log.e(TAG, "Errors of opening the router \""+routerUri+"\" for result!!!" +
                    "\nError:the context "+context.getClass().getCanonicalName()+" is not proper context!!!");
            return false;
        }

    }


    private Class findActivity(String router){

        Class activityClz=routerCache.get(router);

        if (activityClz == null){
            try {
                activityClz= MrouterHelper.reflectActivity(router);
            } catch (Exception e) {
                Log.e(TAG, "Errors of reflecting the activity of the router \""+router+"\"!!!\n"+e);
                return null;
            }

            if (activityClz!=null){
                routerCache.put(router,activityClz);
            }
        }

        return activityClz;
    }


}
