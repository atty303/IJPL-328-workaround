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

                        CtMethod query = ctClass.getDeclaredMethod("query");
                        query.insertBefore(
                            "com.intellij.openapi.diagnostic.Logger.getInstance(com.intellij.ssh.NamedPipeAgentConnector.class).info(\"query: calling dispose\");" +
                            "com.intellij.ssh.NamedPipeAgentConnector.dispose$lambda$4(this);");

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
