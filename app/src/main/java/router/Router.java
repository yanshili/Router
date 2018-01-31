package router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import router.compiler.RouterProcessor;

/**
 * 作者： mooney
 * 日期： 2018/1/31
 * 邮箱： shili_yan@sina.com
 * 描述：
 */

public class Router {

    private static Context mContext;

    private static Map<String, Class> routerCache=new HashMap<>();


    public static boolean openUriForResult(Activity activity, String routerUri, int requestCode, Intent dataIntent) {

        Class activityClazz = findActivity(routerUri);
        if (activityClazz==null){
            return false;
        }
        routerUri="test://"+routerUri;
        Uri uri = Uri.parse(routerUri);
        if (dataIntent==null){
            dataIntent=new Intent();
        }

        dataIntent.setClass(activity,activityClazz);
        dataIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Set<String> queryParameterNames = uri.getQueryParameterNames();
        if(queryParameterNames != null && queryParameterNames.size() > 0){
            for (String key : queryParameterNames){
                dataIntent.putExtra(key, uri.getQueryParameter(key));
            }
        }

        try {
            activity.startActivityForResult(dataIntent,requestCode);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Class findActivity(String router){

        Class activityClz=routerCache.get("test://"+router);

        if (activityClz == null){
            try {
                String cacheClzName= RouterProcessor.ROUTER_PACKAGE+"."+router.substring(0, router.indexOf("/"));

                Class cacheClz=Class.forName(cacheClzName);
                Method method=cacheClz.getDeclaredMethod("findActivity",String.class);
                activityClz= (Class) method.invoke(null, "test://"+router);

                if (activityClz==null){
                    throw new IllegalArgumentException(
                            "there is no activity of the router '"+router+"'");
                }
            } catch (Exception e) {
//                e.printStackTrace();
            }

            routerCache.put(router,activityClz);
        }
        return activityClz;
    }

    private static boolean isValidURI(String uri) {
        if (uri == null || uri.indexOf(' ') >= 0 || uri.indexOf('\n') >= 0) {
            return false;
        }
        String scheme = Uri.parse(uri).getScheme();
        if (scheme == null) {
            return false;
        }

        // Look for period in a domain but followed by at least a two-char TLD
        // Forget strings that don't have a valid-looking protocol
        int period = uri.indexOf('.');
        if (period >= uri.length() - 2) {
            return false;
        }
        int colon = uri.indexOf(':');
        if (period < 0 && colon < 0) {
            return false;
        }
        if (colon >= 0) {
            if (period < 0 || period > colon) {
                // colon ends the protocol
                for (int i = 0; i < colon; i++) {
                    char c = uri.charAt(i);
                    if ((c < 'a' || c > 'z') && (c < 'A' || c > 'Z')) {
                        return false;
                    }
                }
            } else {
                // colon starts the port; crudely look for at least two numbers
                if (colon >= uri.length() - 2) {
                    return false;
                }
                for (int i = colon + 1; i < colon + 3; i++) {
                    char c = uri.charAt(i);
                    if (c < '0' || c > '9') {
                        return false;
                    }
                }
            }
        }
        return true;
    }

}
