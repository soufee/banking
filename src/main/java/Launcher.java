import lombok.extern.log4j.Log4j;
import org.apache.catalina.Context;
import org.apache.catalina.Host;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import java.io.File;

@Log4j
public class Launcher {

    public static void main(String[] args) {
        Launcher launchr = new Launcher();
        try {
            launchr.startServer();
        } catch (Exception e) {
            log.error("Could not start server");
        }
    }

    private void startServer() throws LifecycleException {
        File war = new File(System.getProperty("java.class.path"));
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8888);
        tomcat.setBaseDir(System.getProperty("user.dir"));
        Host host = tomcat.getHost();
        host.setAppBase(System.getProperty("user.dir"));
        host.setAutoDeploy(true);
        host.setDeployOnStartup(true);
        Context appContext = tomcat.addWebapp(host, "/", war.getAbsolutePath());
        log.debug("Deployed "+ appContext.getBaseName()+" as"+appContext.getDocBase());
        tomcat.start();
        tomcat.getServer().await();
    }
}
