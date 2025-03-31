import javassist.*;
import java.lang.instrument.*;
import java.security.ProtectionDomain;

public class Agent {
    public static void premain(String agentArgs, Instrumentation inst) {
        inst.addTransformer(new ClassFileTransformer() {
            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                                    ProtectionDomain protectionDomain, byte[] classfileBuffer) {

                if ("com/intellij/ssh/NamedPipeAgentConnector".equals(className)) {
                    try {
                        ClassPool cp = ClassPool.getDefault();
                        cp.insertClassPath(new LoaderClassPath(loader));

                        CtClass ctClass = cp.get("com.intellij.ssh.NamedPipeAgentConnector");
                        CtMethod method = ctClass.getDeclaredMethod("query");
method.insertBefore(
  "com.intellij.openapi.diagnostic.Logger.getInstance(" +
  "com.intellij.ssh.NamedPipeAgentConnector.class" +
  ").warn(\"query(Buffer): calling dispose$lambda$4\");" +

  "com.intellij.ssh.NamedPipeAgentConnector.dispose$lambda$4(this);" +
  "com.intellij.openapi.diagnostic.Logger.getInstance(" +
  "com.intellij.ssh.NamedPipeAgentConnector.class" +
  ").warn(\"query(Buffer): called dispose$lambda$4\");"
);

                        return ctClass.toBytecode();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
        });
    }
}
