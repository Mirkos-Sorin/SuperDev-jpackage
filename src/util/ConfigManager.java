package util;

import model.AppConfig;
import java.io.*;

/**
 * 配置管理器
 * 负责保存和加载应用配置
 */
public class ConfigManager {

    /**
     * 保存配置到文件
     * @param filePath 文件路径
     * @param config 配置对象
     * @return 是否保存成功
     */
    public static boolean saveConfig(String filePath, AppConfig config) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(config);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 从文件加载配置
     * @param filePath 文件路径
     * @return 配置对象，失败返回null
     */
    public static AppConfig loadConfig(String filePath) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (AppConfig) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 保存配置为JSON格式
     * @param filePath 文件路径
     * @param config 配置对象
     * @return 是否保存成功
     */
    public static boolean saveConfigAsJson(String filePath, AppConfig config) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println("{");
            writer.println("  \"jarPath\": \"" + escapeJson(config.getJarPath()) + "\",");
            writer.println("  \"mainClass\": \"" + escapeJson(config.getMainClass()) + "\",");
            writer.println("  \"appName\": \"" + escapeJson(config.getAppName()) + "\",");
            writer.println("  \"version\": \"" + escapeJson(config.getVersion()) + "\",");
            writer.println("  \"iconPath\": \"" + escapeJson(config.getIconPath()) + "\",");
            writer.println("  \"installerType\": \"" + escapeJson(config.getInstallerType()) + "\",");
            writer.println("  \"javaOpts\": \"" + escapeJson(config.getJavaOpts()) + "\",");
            writer.println("  \"launchArgs\": \"" + escapeJson(config.getLaunchArgs()) + "\",");
            writer.println("  \"modules\": \"" + escapeJson(config.getModules()) + "\",");
            writer.println("  \"runtimeImg\": \"" + escapeJson(config.getRuntimeImg()) + "\",");
            writer.println("  \"verbose\": " + config.isVerbose() + ",");
            writer.println("  \"console\": " + config.isConsole() + ",");
            writer.println("  \"winMenuGroup\": \"" + escapeJson(config.getWinMenuGroup()) + "\"");
            writer.println("}");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 从JSON文件加载配置
     * @param filePath 文件路径
     * @return 配置对象，失败返回null
     */
    public static AppConfig loadConfigFromJson(String filePath) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return parseJsonConfig(content.toString());
    }

    private static AppConfig parseJsonConfig(String json) {
        AppConfig config = new AppConfig();
        try {
            config.setJarPath(extractJsonValue(json, "jarPath"));
            config.setMainClass(extractJsonValue(json, "mainClass"));
            config.setAppName(extractJsonValue(json, "appName"));
            config.setVersion(extractJsonValue(json, "version"));
            config.setIconPath(extractJsonValue(json, "iconPath"));
            config.setInstallerType(extractJsonValue(json, "installerType"));
            config.setJavaOpts(extractJsonValue(json, "javaOpts"));
            config.setLaunchArgs(extractJsonValue(json, "launchArgs"));
            config.setModules(extractJsonValue(json, "modules"));
            config.setRuntimeImg(extractJsonValue(json, "runtimeImg"));
            config.setVerbose(extractJsonBoolean(json, "verbose"));
            config.setConsole(extractJsonBoolean(json, "console"));
            config.setWinMenuGroup(extractJsonValue(json, "winMenuGroup"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return config;
    }

    private static String extractJsonValue(String json, String key) {
        String pattern = "\"" + key + "\"";
        int index = json.indexOf(pattern);
        if (index == -1) return "";
        int colonIndex = json.indexOf(":", index);
        int startQuote = json.indexOf("\"", colonIndex);
        int endQuote = json.indexOf("\"", startQuote + 1);
        if (startQuote == -1 || endQuote == -1) return "";
        return json.substring(startQuote + 1, endQuote);
    }

    private static boolean extractJsonBoolean(String json, String key) {
        String pattern = "\"" + key + "\"";
        int index = json.indexOf(pattern);
        if (index == -1) return false;
        int colonIndex = json.indexOf(":", index);
        int commaIndex = json.indexOf(",", colonIndex);
        int closeIndex = json.indexOf("}", colonIndex);
        int endIndex = Math.min(commaIndex > 0 ? commaIndex : Integer.MAX_VALUE,
                               closeIndex > 0 ? closeIndex : Integer.MAX_VALUE);
        String boolStr = json.substring(colonIndex + 1, endIndex).trim();
        return "true".equalsIgnoreCase(boolStr);
    }

    private static String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }
}
