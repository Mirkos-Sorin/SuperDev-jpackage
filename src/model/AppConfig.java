package model;

import java.io.Serializable;

/**
 * 应用程序配置数据模型
 * 用于保存和加载项目配置
 */
public class AppConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    private String jarPath;
    private String mainClass;
    private String appName;
    private String version;
    private String iconPath;
    private String installerType;
    private String javaOpts;
    private String launchArgs;
    private String modules;
    private String runtimeImg;
    private boolean verbose;
    private boolean console;
    private String winMenuGroup;

    public AppConfig() {
        this.jarPath = "";
        this.mainClass = "";
        this.appName = "MyAwesomeApp";
        this.version = "1.0.0";
        this.iconPath = "";
        this.installerType = "";
        this.javaOpts = "";
        this.launchArgs = "";
        this.modules = "";
        this.runtimeImg = "";
        this.verbose = false;
        this.console = false;
        this.winMenuGroup = "My Company";
    }

    // Getter和Setter方法
    public String getJarPath() {
        return jarPath;
    }

    public void setJarPath(String jarPath) {
        this.jarPath = jarPath;
    }

    public String getMainClass() {
        return mainClass;
    }

    public void setMainClass(String mainClass) {
        this.mainClass = mainClass;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public String getInstallerType() {
        return installerType;
    }

    public void setInstallerType(String installerType) {
        this.installerType = installerType;
    }

    public String getJavaOpts() {
        return javaOpts;
    }

    public void setJavaOpts(String javaOpts) {
        this.javaOpts = javaOpts;
    }

    public String getLaunchArgs() {
        return launchArgs;
    }

    public void setLaunchArgs(String launchArgs) {
        this.launchArgs = launchArgs;
    }

    public String getModules() {
        return modules;
    }

    public void setModules(String modules) {
        this.modules = modules;
    }

    public String getRuntimeImg() {
        return runtimeImg;
    }

    public void setRuntimeImg(String runtimeImg) {
        this.runtimeImg = runtimeImg;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public boolean isConsole() {
        return console;
    }

    public void setConsole(boolean console) {
        this.console = console;
    }

    public String getWinMenuGroup() {
        return winMenuGroup;
    }

    public void setWinMenuGroup(String winMenuGroup) {
        this.winMenuGroup = winMenuGroup;
    }
}
