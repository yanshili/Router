package router.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 作者： mooney
 * 日期： 2018/1/31
 * 邮箱： shili_yan@sina.com
 * 描述： protocol://domain/module
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface RouterConfig {
    //模块名称（routerAnnotations）
    String module();
    String domain() default "appName";
    String protocol() default "router";
}
