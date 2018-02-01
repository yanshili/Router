package mrouter;

import android.net.Uri;

import java.lang.reflect.Method;

import mrouter.compiler.MrouterProcessor;

/**
 * 作者： mooney
 * 日期： 2018/2/1
 * 邮箱： shili_yan@sina.com
 * 描述：
 */

class MrouterHelper {

    public static Class reflectActivity(String router) throws Exception{
        String path=Uri.parse(router).getPath();
        String clzName = path.substring(1, path.indexOf("/", 1));
        String cacheClzName = MrouterProcessor.ROUTER_PACKAGE+"."+clzName;

        Class cacheClz=Class.forName(cacheClzName);
        Method method=cacheClz.getDeclaredMethod("findActivity",String.class);
        return  (Class) method.invoke(null, router);
    }

    public static boolean isValidURI(String uri) {
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
