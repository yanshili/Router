package mrouter.compiler;

import com.google.auto.service.AutoService;

import java.io.Writer;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import mrouter.annotations.MrouterConfig;
import mrouter.annotations.MrouterUri;


/**
 * 作者： mooney
 * 日期： 2018/1/23
 * 邮箱： shili_yan@sina.com
 * 描述： 模块插槽代码生成器
 */
@AutoService(Processor.class)
public class MrouterProcessor extends AbstractProcessor {

    public static final String ROUTER_PACKAGE = "mrouter.compiler.generator";
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        messager = processingEnv.getMessager();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(MrouterUri.class.getCanonicalName());
        types.add(MrouterConfig.class.getCanonicalName());
        return types;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }


    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnv) {

        if (set.isEmpty()) {
            return false;
        }

        Set<? extends Element> routerConfigE = roundEnv.getElementsAnnotatedWith(MrouterConfig.class);

        MrouterConfig config = routerConfigE.iterator().next().getAnnotation(MrouterConfig.class);
        if (config==null){
            log("there is no MrouterConfig annotation!!!");
        }

        String module = config.module();

        String scheme = config.protocol()+"://"+config.domain()+"/"+module+"/";

        String clzName = module;

        Set<? extends Element> routerElements = roundEnv.getElementsAnnotatedWith(MrouterUri.class);

        StringBuilder sb = new StringBuilder();
        sb.append("package " + ROUTER_PACKAGE + ";\n\n");

        sb.append("import java.util.HashMap;\n");
        sb.append("import java.util.Map;\n");

        sb.append("public class " + clzName + "{\n\n");

        sb.append("   public static Class findActivity(String router){\n");

        sb.append("       Map<String, Class> map = new HashMap<>();\n");

        for (Element element : routerElements) {
            String routerUri = scheme+element.getAnnotation(MrouterUri.class).value();
            TypeElement typeElement = (TypeElement) element;

            String activity = typeElement.getQualifiedName().toString();

            sb.append("       map.put(\"" + routerUri+"\", " + activity + ".class);\n");

            log("activity==" + activity + "\nrouterUri==" + routerUri);
        }
        sb.append("       return map.get(router);\n");
        sb.append("   }\n\n");

        sb.append("}\n");

        String javaStr = sb.toString();

        String fileFullName = ROUTER_PACKAGE + "." + clzName;
        try {
            JavaFileObject jfo = processingEnv.getFiler().createSourceFile(
                    fileFullName);

            generateFile(jfo, javaStr);
        } catch (Exception e) {
            logE(e.toString());
        }


        return false;
    }


    public static void generateFile(JavaFileObject jfo, String javaCodeStr) throws Exception {
        Writer writer = jfo.openWriter();
        writer.write(javaCodeStr);
        writer.flush();
        writer.close();
    }

    /************************************
     * 编译时日志函数
     *****************************************/
    protected void log(String str) {
        l(Diagnostic.Kind.NOTE, str);
    }

    protected void logE(String str) {
        l(Diagnostic.Kind.ERROR, str);
    }

    protected void logW(String str) {
        l(Diagnostic.Kind.WARNING, str);
    }

    private void l(Diagnostic.Kind kind, String str) {

        str = getClass().getName() + "-->\n" + str;
        //输出日志
        messager.printMessage(kind, str);
    }


}