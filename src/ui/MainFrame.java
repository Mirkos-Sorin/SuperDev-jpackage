package ui;

import model.AppConfig;
import util.ConfigManager;
import worker.BuildWorker;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.List;
import java.util.zip.*;

/**
 * 超级开发者主窗口 - 现代化界面
 * Java应用打包工具的主界面
 */
public class MainFrame extends JFrame implements BuildWorker.BuildListener {

    // UI组件
    private JList<String> navList;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private JLabel statusBar;
    private JButton buildButton;
    private String configFilePath;

    // jpackage路径
    private String customJpackagePath = null;

    // 核心配置面板组件
    private JComboBox<String> installerTypeCombo;
    private JTextField jarPathField;
    private JTextField mainClassField;
    private JTextField appNameField;
    private JTextField versionField;
    private JTextField iconPathField;
    private JLabel envStatusLabel;
    private JComboBox<String> presetCombo;

    // 高级选项面板组件
    private JTextArea javaOptsArea;
    private JTextArea launchArgsArea;
    private JTextField modulesField;
    private JTextField runtimeImgField;
    private JCheckBox consoleCheckBox;
    private JCheckBox verboseCheckBox;
    private JTextField winMenuGroupField;
    private JTextField jpackagePathField;

    // 日志面板组件
    private JTextArea logTextArea;
    private JProgressBar progressBar;
    private JButton openDistButton;

    // 构建线程
    private BuildWorker currentWorker;
    private String jarPath;
    private String iconPath;
    private String currentOS;

    // 面板引用
    private OverviewPanel overviewPanel;
    private CoreConfigPanel corePanel;
    private AdvancedOptionsPanel advancedPanel;
    private LogsPanel logsPanel;

    public MainFrame() {
        currentOS = System.getProperty("os.name");
        if (currentOS.startsWith("Windows")) currentOS = "Windows";
        else if (currentOS.startsWith("Mac")) currentOS = "Darwin";
        else if (currentOS.startsWith("Linux")) currentOS = "Linux";
        else currentOS = "Unknown";

        initComponents();
        setupLayout();
        setupMenuBar();
        setupEventListeners();
        checkEnvironment();

        setTitle(AppTheme.APP_NAME + " v" + AppTheme.APP_VERSION);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setMinimumSize(new Dimension(1100, 750));
        setLocationRelativeTo(null);
        setBackground(AppTheme.BACKGROUND_DARK);

        // 添加窗口关闭监听器
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmExit();
            }
        });
    }

    private void initComponents() {
        // 导航列表 - 使用中文
        String[] navItems = {"概览", "核心配置", "高级选项", "构建日志"};
        navList = new JList<>(navItems);
        navList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        navList.setSelectedIndex(0);

        // 创建各页面面板
        overviewPanel = new OverviewPanel(this);
        corePanel = new CoreConfigPanel(this);
        advancedPanel = new AdvancedOptionsPanel(this);
        logsPanel = new LogsPanel(this);

        // 卡片布局
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(AppTheme.BACKGROUND_DARK);
        contentPanel.add(overviewPanel, "overview");
        contentPanel.add(corePanel, "core");
        contentPanel.add(advancedPanel, "advanced");
        contentPanel.add(logsPanel, "logs");

        // 状态栏
        statusBar = new JLabel("就绪");
        statusBar.setBackground(AppTheme.BACKGROUND_MEDIUM);
        statusBar.setForeground(AppTheme.TEXT_SECONDARY);
        statusBar.setFont(AppTheme.getChineseFont(Font.PLAIN, 12));
        statusBar.setBorder(new EmptyBorder(8, 15, 8, 15));
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // 左侧边栏
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(AppTheme.BACKGROUND_MEDIUM);
        sidebar.setPreferredSize(new Dimension(220, 0));

        // Logo区域
        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(AppTheme.BACKGROUND_MEDIUM);
        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.Y_AXIS));
        logoPanel.setBorder(new EmptyBorder(25, 0, 25, 0));

        JLabel logoLabel = new JLabel("SuperDev");
        logoLabel.setFont(AppTheme.getChineseFont(Font.BOLD, 24));
        logoLabel.setForeground(AppTheme.ACCENT_CYAN);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoPanel.add(logoLabel);

        JLabel versionLabel = new JLabel("v" + AppTheme.APP_VERSION);
        versionLabel.setFont(AppTheme.getChineseFont(Font.PLAIN, 11));
        versionLabel.setForeground(AppTheme.TEXT_MUTED);
        versionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoPanel.add(versionLabel);

        sidebar.add(logoPanel);

        // 导航列表
        navList.setFont(AppTheme.getChineseFont(Font.PLAIN, 14));
        navList.setFixedCellHeight(50);
        navList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus);
                label.setBorder(new EmptyBorder(12, 25, 12, 20));
                label.setFont(AppTheme.getChineseFont(Font.PLAIN, 14));
                if (isSelected) {
                    label.setBackground(AppTheme.BUTTON_PRIMARY);
                    label.setForeground(AppTheme.TEXT_PRIMARY);
                    label.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 4, 0, 0, AppTheme.ACCENT_CYAN),
                        new EmptyBorder(12, 21, 12, 20)
                    ));
                }
                return label;
            }
        });
        sidebar.add(navList);

        // 底部保存按钮
        JButton saveBtn = AppTheme.createSecondaryButton("保存项目");
        saveBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        saveBtn.setMaximumSize(new Dimension(170, 40));
        saveBtn.addActionListener(e -> saveProject());

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(AppTheme.BACKGROUND_MEDIUM);
        bottomPanel.setBorder(new EmptyBorder(15, 0, 15, 0));
        bottomPanel.add(saveBtn);
        sidebar.add(bottomPanel);

        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
        add(statusBar, BorderLayout.SOUTH);
    }

    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(AppTheme.BACKGROUND_MEDIUM);
        menuBar.setForeground(AppTheme.TEXT_SECONDARY);

        // 项目菜单
        JMenu fileMenu = new JMenu("项目");
        fileMenu.setForeground(AppTheme.TEXT_SECONDARY);

        JMenuItem saveItem = new JMenuItem("保存配置");
        saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        saveItem.addActionListener(e -> saveProject());
        fileMenu.add(saveItem);

        JMenuItem loadItem = new JMenuItem("加载配置");
        loadItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        loadItem.addActionListener(e -> loadProject());
        fileMenu.add(loadItem);

        fileMenu.addSeparator();

        JMenuItem exitItem = new JMenuItem("退出");
        exitItem.addActionListener(e -> confirmExit());
        fileMenu.add(exitItem);

        // 帮助菜单
        JMenu helpMenu = new JMenu("帮助");
        helpMenu.setForeground(AppTheme.TEXT_SECONDARY);

        JMenuItem aboutItem = new JMenuItem("关于");
        aboutItem.addActionListener(e -> showAbout());
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);
    }

    private void setupEventListeners() {
        // 导航切换
        navList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int index = navList.getSelectedIndex();
                String[] keys = {"overview", "core", "advanced", "logs"};
                String[] titles = {"概览", "核心配置", "高级选项", "构建日志"};
                cardLayout.show(contentPanel, keys[index]);
                updateStatusBar("当前页面：" + titles[index]);
            }
        });

        // JAR路径变化监听
        corePanel.getJarPathField().addCaretListener(e -> {
            String text = corePanel.getJarPathField().getText();
            File jarFile = new File(text);
            if (text != null && !text.isEmpty() && jarFile.exists()) {
                jarPath = text;
                buildButton.setEnabled(true);
            } else {
                buildButton.setEnabled(false);
            }
        });
    }

    // ==================== 环境检测 ====================

    public void checkEnvironment() {
        overviewPanel.updateEnvStatus("正在检测 jpackage...", false);

        new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() {
                try {
                    String jpackageCmd = customJpackagePath != null ? customJpackagePath : "jpackage";
                    Process process = Runtime.getRuntime().exec(new String[]{jpackageCmd, "--version"});
                    int exitCode = process.waitFor();
                    return exitCode == 0;
                } catch (Exception e) {
                    return false;
                }
            }

            @Override
            protected void done() {
                try {
                    boolean success = get();
                    if (success) {
                        String jpackageCmd = customJpackagePath != null ? customJpackagePath : "jpackage";
                        BufferedReader reader = new BufferedReader(
                            new InputStreamReader(Runtime.getRuntime()
                                .exec(new String[]{jpackageCmd, "--version"}).getInputStream()));
                        String version = reader.readLine();
                        reader.close();

                        String displayPath = customJpackagePath != null ? customJpackagePath : "系统PATH (自动检测)";
                        overviewPanel.updateEnvStatus("jpackage 已就绪 | 版本: " + version + " | 路径: " + displayPath, true);
                        overviewPanel.updateJpackagePath(customJpackagePath != null ? customJpackagePath : System.getProperty("java.home") + File.separator + "bin" + File.separator + "jpackage", true);
                        updateStatusBar("环境检测通过");
                        buildButton.setEnabled(jarPath != null && !jarPath.isEmpty());
                    } else {
                        throw new Exception("Command failed");
                    }
                } catch (Exception e) {
                    overviewPanel.updateEnvStatus("未找到 jpackage，请安装 JDK 17+ 或手动选择路径", false);
                    overviewPanel.updateJpackagePath(System.getProperty("java.home") + File.separator + "bin" + File.separator + "jpackage", false);
                    updateStatusBar("环境检测失败");
                    buildButton.setEnabled(false);
                }
            }
        }.execute();
    }

    // ==================== jpackage路径选择 ====================

    public void selectJpackagePath() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("选择 jpackage 可执行文件");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        // Windows上添加exe过滤器
        if (currentOS.equals("Windows")) {
            chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "jpackage.exe", "exe"));
        }

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            String path = chooser.getSelectedFile().getAbsolutePath();
            customJpackagePath = path;
            advancedPanel.getJpackagePathField().setText(path);
            updateStatusBar("已设置 jpackage 路径: " + path);
            // 重新检测环境
            checkEnvironment();
        }
    }

    // ==================== 核心功能 ====================

    public void selectJar() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("选择 JAR 文件");
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
            "JAR 文件 (*.jar)", "jar"));
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            String path = chooser.getSelectedFile().getAbsolutePath();
            jarPath = path;
            corePanel.getJarPathField().setText(path);
            updateStatusBar("已加载: " + chooser.getSelectedFile().getName());
        }
    }

    public void selectIcon() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("选择应用图标");
        String filter = "*.*";
        if (currentOS.equals("Windows")) {
            filter = "ico";
        } else if (currentOS.equals("Darwin")) {
            filter = "icns";
        } else {
            filter = "png";
        }
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
            "图标文件 (*." + filter + ")", filter));
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            String path = chooser.getSelectedFile().getAbsolutePath();
            iconPath = path;
            corePanel.getIconPathField().setText(path);
        }
    }

    public void selectRuntime() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("选择运行时镜像目录");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            advancedPanel.getRuntimeImgField().setText(
                chooser.getSelectedFile().getAbsolutePath());
        }
    }

    public void inferMainClass() {
        if (jarPath == null || jarPath.isEmpty() || !new File(jarPath).exists()) {
            JOptionPane.showMessageDialog(this, "请先选择 JAR 文件", "提示",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try (ZipFile zipFile = new ZipFile(jarPath)) {
            ZipEntry manifestEntry = zipFile.getEntry("META-INF/MANIFEST.MF");
            if (manifestEntry != null) {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(zipFile.getInputStream(manifestEntry)))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.startsWith("Main-Class:")) {
                            String mainClass = line.substring("Main-Class:".length()).trim();
                            corePanel.getMainClassField().setText(mainClass);
                            appendLog("自动检测到主类: " + mainClass);
                            JOptionPane.showMessageDialog(this, "找到主类:\n" + mainClass,
                                "检测成功", JOptionPane.INFORMATION_MESSAGE);
                            return;
                        }
                    }
                }
            }
            JOptionPane.showMessageDialog(this, "JAR 中未找到 Main-Class 属性，请手动输入。",
                "提示", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "读取 JAR 失败: " + e.getMessage(),
                "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void applyPreset(String mode) {
        switch (mode) {
            case "standard":
                advancedPanel.getConsoleCheckBox().setSelected(false);
                advancedPanel.getJavaOptsArea().setText("-Xmx512m");
                break;
            case "game":
                advancedPanel.getConsoleCheckBox().setSelected(false);
                advancedPanel.getJavaOptsArea().setText("-Xmx1024m\n-XX:+UseG1GC");
                break;
            case "debug":
                advancedPanel.getConsoleCheckBox().setSelected(true);
                advancedPanel.getJavaOptsArea().setText("-Xmx512m\n-verbose:class");
                advancedPanel.getVerboseCheckBox().setSelected(true);
                break;
            case "minimal":
                advancedPanel.getJavaOptsArea().setText("-Xmx256m");
                advancedPanel.getModulesField().setText("java.base");
                break;
        }
        appendLog("已应用预设: " + mode);
    }

    // ==================== 构建功能 ====================

    public void startBuild() {
        // 验证
        if (jarPath == null || jarPath.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请选择 JAR 文件", "错误",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        String mainClass = corePanel.getMainClassField().getText().trim();
        if (mainClass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "必须填写主类名称", "错误",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        String appName = corePanel.getAppNameField().getText().trim();
        if (appName.isEmpty()) appName = "App";

        String installerType = (String) corePanel.getInstallerTypeCombo().getSelectedItem();

        // 确定jpackage命令
        String jpackageCmd = customJpackagePath != null ? customJpackagePath : "jpackage";

        // 构建命令
        List<String> cmd = new ArrayList<>();
        cmd.add(jpackageCmd);
        cmd.add("--type");
        cmd.add(installerType);
        cmd.add("--name");
        cmd.add(appName);
        cmd.add("--input");
        cmd.add(new File(jarPath).getParent());
        cmd.add("--main-jar");
        cmd.add(new File(jarPath).getName());
        cmd.add("--main-class");
        cmd.add(mainClass);

        // 版本号
        String version = corePanel.getVersionField().getText().trim();
        if (!version.isEmpty()) {
            cmd.add("--app-version");
            cmd.add(version);
        }

        // 图标
        String icon = corePanel.getIconPathField().getText().trim();
        if (!icon.isEmpty()) {
            cmd.add("--icon");
            cmd.add(icon);
        }

        // Windows特定
        if (currentOS.equals("Windows")) {
            String menuGroup = advancedPanel.getWinMenuGroupField().getText().trim();
            if (!menuGroup.isEmpty()) {
                cmd.add("--win-menu-group");
                cmd.add(menuGroup);
            }
            cmd.add("--win-console");
            cmd.add(advancedPanel.getConsoleCheckBox().isSelected() ? "true" : "false");
        }

        // 详细模式
        if (advancedPanel.getVerboseCheckBox().isSelected()) {
            cmd.add("--verbose");
        }

        // Java选项
        String javaOpts = advancedPanel.getJavaOptsArea().getText().trim();
        if (!javaOpts.isEmpty()) {
            for (String opt : javaOpts.split("\\n")) {
                if (!opt.trim().isEmpty()) {
                    cmd.add("--java-options");
                    cmd.add(opt.trim());
                }
            }
        }

        // 启动器参数
        String launchArgs = advancedPanel.getLaunchArgsArea().getText().trim();
        if (!launchArgs.isEmpty()) {
            for (String arg : launchArgs.split("\\n")) {
                if (!arg.trim().isEmpty()) {
                    cmd.add("--arguments");
                    cmd.add(arg.trim());
                }
            }
        }

        // 模块
        String modules = advancedPanel.getModulesField().getText().trim();
        if (!modules.isEmpty()) {
            cmd.add("--add-modules");
            cmd.add(modules);
        }

        // 运行时镜像
        String runtimeImg = advancedPanel.getRuntimeImgField().getText().trim();
        if (!runtimeImg.isEmpty()) {
            cmd.add("--runtime-image");
            cmd.add(runtimeImg);
        }

        String workDir = new File(jarPath).getParent();
        String outputDir = new File(workDir, "dist").getAbsolutePath();

        // 更新UI状态
        buildButton.setEnabled(false);
        buildButton.setText("构建中...");
        logsPanel.getProgressBar().setValue(0);
        logsPanel.getProgressBar().setVisible(true);
        navList.setSelectedIndex(3); // 切换到日志页面
        logsPanel.getLogTextArea().setText("");
        logsPanel.getOpenDistButton().setVisible(false);

        appendLog("========================================");
        appendLog("开始构建: " + appName + " (" + installerType + ")");
        appendLog("工作目录: " + workDir);
        appendLog("命令: " + String.join(" ", cmd));
        appendLog("========================================\n");

        // 启动构建线程
        currentWorker = new BuildWorker(cmd, workDir);
        currentWorker.setListener(this);
        currentWorker.execute();

        updateStatusBar("正在构建...");
    }

    @Override
    public void onLog(String message, String type) {
        SwingUtilities.invokeLater(() -> appendLog(message));
    }

    @Override
    public void onProgress(int progress) {
        SwingUtilities.invokeLater(() -> {
            logsPanel.getProgressBar().setValue(progress);
        });
    }

    @Override
    public void onFinished(boolean success, String message) {
        SwingUtilities.invokeLater(() -> {
            appendLog("\n========================================");
            appendLog(message);

            buildButton.setEnabled(true);
            buildButton.setText("开始打包");
            logsPanel.getProgressBar().setVisible(false);

            if (success) {
                String workDir = new File(jarPath).getParent();
                String outputDir = new File(workDir, "dist").getAbsolutePath();
                appendLog("输出位置: " + outputDir);
                logsPanel.getOpenDistButton().setVisible(true);
                JOptionPane.showMessageDialog(this, message + "\n\n安装包位于:\n" + outputDir,
                    "构建成功", JOptionPane.INFORMATION_MESSAGE);
                updateStatusBar("构建完成");
            } else {
                JOptionPane.showMessageDialog(this, message, "构建失败",
                    JOptionPane.ERROR_MESSAGE);
                updateStatusBar("构建失败");
            }
        });
    }

    public void appendLog(String text) {
        logsPanel.getLogTextArea().append(text + "\n");
        logsPanel.getLogTextArea().setCaretPosition(logsPanel.getLogTextArea().getDocument().getLength());
    }

    public void clearLog() {
        logsPanel.getLogTextArea().setText("");
    }

    public void copyLog() {
        StringSelection selection = new StringSelection(logsPanel.getLogTextArea().getText());
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
        updateStatusBar("日志已复制到剪贴板");
    }

    public void openDistFolder() {
        String workDir = new File(jarPath).getParent();
        String distDir = new File(workDir, "dist").getAbsolutePath();
        File distFolder = new File(distDir);

        if (distFolder.exists()) {
            try {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(distFolder);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "无法打开目录: " + e.getMessage(),
                    "错误", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "输出目录不存在", "提示",
                JOptionPane.WARNING_MESSAGE);
        }
    }

    public void cleanBuild() {
        int result = JOptionPane.showConfirmDialog(this,
            "确定要删除 dist 文件夹吗？", "确认清理",
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (result == JOptionPane.YES_OPTION) {
            if (jarPath == null || jarPath.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请先加载一个 JAR 项目。",
                    "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                String workDir = new File(jarPath).getParent();
                Path distDir = Paths.get(workDir, "dist");

                if (Files.exists(distDir)) {
                    Files.walk(distDir)
                        .sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
                    appendLog("已清理: " + distDir.toString());
                    JOptionPane.showMessageDialog(this, "构建产物已清理。",
                        "完成", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    appendLog("未发现需要清理的 dist 文件夹。");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "清理失败: " + e.getMessage(),
                    "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ==================== 项目配置 ====================

    public void saveProject() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("保存项目配置");
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
            "SuperDev 项目 (*" + AppTheme.CONFIG_EXT + ")", "sdp"));

        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            String path = chooser.getSelectedFile().getAbsolutePath();
            if (!path.endsWith(AppTheme.CONFIG_EXT)) {
                path += AppTheme.CONFIG_EXT;
            }

            AppConfig config = createConfigFromUI();
            if (ConfigManager.saveConfigAsJson(path, config)) {
                configFilePath = path;
                updateStatusBar("项目已保存: " + new File(path).getName());
                JOptionPane.showMessageDialog(this, "配置已保存！",
                    "成功", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "保存失败，请检查权限。",
                    "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void loadProject() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("加载项目配置");
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
            "SuperDev 项目 (*" + AppTheme.CONFIG_EXT + ")", "sdp"));

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            String path = chooser.getSelectedFile().getAbsolutePath();
            AppConfig config = ConfigManager.loadConfigFromJson(path);

            if (config != null) {
                applyConfigToUI(config);
                configFilePath = path;
                updateStatusBar("项目已加载: " + new File(path).getName());
                JOptionPane.showMessageDialog(this, "配置已加载！",
                    "成功", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "配置文件格式错误。",
                    "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private AppConfig createConfigFromUI() {
        AppConfig config = new AppConfig();
        config.setJarPath(corePanel.getJarPathField().getText());
        config.setMainClass(corePanel.getMainClassField().getText());
        config.setAppName(corePanel.getAppNameField().getText());
        config.setVersion(corePanel.getVersionField().getText());
        config.setIconPath(corePanel.getIconPathField().getText());
        config.setInstallerType((String) corePanel.getInstallerTypeCombo().getSelectedItem());
        config.setJavaOpts(advancedPanel.getJavaOptsArea().getText());
        config.setLaunchArgs(advancedPanel.getLaunchArgsArea().getText());
        config.setModules(advancedPanel.getModulesField().getText());
        config.setRuntimeImg(advancedPanel.getRuntimeImgField().getText());
        config.setVerbose(advancedPanel.getVerboseCheckBox().isSelected());
        config.setConsole(advancedPanel.getConsoleCheckBox().isSelected());
        config.setWinMenuGroup(advancedPanel.getWinMenuGroupField().getText());
        return config;
    }

    private void applyConfigToUI(AppConfig config) {
        corePanel.getJarPathField().setText(config.getJarPath());
        jarPath = config.getJarPath();
        corePanel.getMainClassField().setText(config.getMainClass());
        corePanel.getAppNameField().setText(config.getAppName());
        corePanel.getVersionField().setText(config.getVersion());
        corePanel.getIconPathField().setText(config.getIconPath());
        iconPath = config.getIconPath();

        // 尝试匹配安装包类型
        String type = config.getInstallerType();
        JComboBox<String> combo = corePanel.getInstallerTypeCombo();
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (combo.getItemAt(i).equals(type)) {
                combo.setSelectedIndex(i);
                break;
            }
        }

        advancedPanel.getJavaOptsArea().setText(config.getJavaOpts());
        advancedPanel.getLaunchArgsArea().setText(config.getLaunchArgs());
        advancedPanel.getModulesField().setText(config.getModules());
        advancedPanel.getRuntimeImgField().setText(config.getRuntimeImg());
        advancedPanel.getVerboseCheckBox().setSelected(config.isVerbose());
        advancedPanel.getConsoleCheckBox().setSelected(config.isConsole());
        advancedPanel.getWinMenuGroupField().setText(config.getWinMenuGroup());

        buildButton.setEnabled(!jarPath.isEmpty());
    }

    // ==================== 其他功能 ====================

    private void showAbout() {
        JOptionPane.showMessageDialog(this,
            AppTheme.APP_NAME + "\n版本: " + AppTheme.APP_VERSION + "\n\n" +
            "基于 JDK jpackage 打造的专业打包工具。\n" +
            "支持 Windows, macOS, Linux。\n\n" +
            "作者: SuperDev Team",
            "关于 SuperDev",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void confirmExit() {
        if (currentWorker != null && currentWorker.isRunning()) {
            int result = JOptionPane.showConfirmDialog(this,
                "构建正在进行中，确定要退出吗？", "确认退出",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (result == JOptionPane.YES_OPTION) {
                currentWorker.cancel(true);
                System.exit(0);
            }
        } else {
            System.exit(0);
        }
    }

    private void updateStatusBar(String message) {
        statusBar.setText(message);
    }

    // ==================== Getter方法 ====================

    public JButton getBuildButton() {
        return buildButton;
    }

    public void setBuildButton(JButton buildButton) {
        this.buildButton = buildButton;
    }

    public String getCurrentOS() {
        return currentOS;
    }

    public JComboBox<String> getInstallerTypeCombo() {
        return installerTypeCombo;
    }

    public void setInstallerTypeCombo(JComboBox<String> installerTypeCombo) {
        this.installerTypeCombo = installerTypeCombo;
    }

    public JTextField getJarPathField() {
        return jarPathField;
    }

    public void setJarPathField(JTextField jarPathField) {
        this.jarPathField = jarPathField;
    }

    public JTextField getMainClassField() {
        return mainClassField;
    }

    public void setMainClassField(JTextField mainClassField) {
        this.mainClassField = mainClassField;
    }

    public JTextField getAppNameField() {
        return appNameField;
    }

    public void setAppNameField(JTextField appNameField) {
        this.appNameField = appNameField;
    }

    public JTextField getVersionField() {
        return versionField;
    }

    public void setVersionField(JTextField versionField) {
        this.versionField = versionField;
    }

    public JTextField getIconPathField() {
        return iconPathField;
    }

    public void setIconPathField(JTextField iconPathField) {
        this.iconPathField = iconPathField;
    }

    public JComboBox<String> getPresetCombo() {
        return presetCombo;
    }

    public void setPresetCombo(JComboBox<String> presetCombo) {
        this.presetCombo = presetCombo;
    }

    public JTextArea getJavaOptsArea() {
        return javaOptsArea;
    }

    public void setJavaOptsArea(JTextArea javaOptsArea) {
        this.javaOptsArea = javaOptsArea;
    }

    public JTextArea getLaunchArgsArea() {
        return launchArgsArea;
    }

    public void setLaunchArgsArea(JTextArea launchArgsArea) {
        this.launchArgsArea = launchArgsArea;
    }

    public JTextField getModulesField() {
        return modulesField;
    }

    public void setModulesField(JTextField modulesField) {
        this.modulesField = modulesField;
    }

    public JTextField getRuntimeImgField() {
        return runtimeImgField;
    }

    public void setRuntimeImgField(JTextField runtimeImgField) {
        this.runtimeImgField = runtimeImgField;
    }

    public JCheckBox getConsoleCheckBox() {
        return consoleCheckBox;
    }

    public void setConsoleCheckBox(JCheckBox consoleCheckBox) {
        this.consoleCheckBox = consoleCheckBox;
    }

    public JCheckBox getVerboseCheckBox() {
        return verboseCheckBox;
    }

    public void setVerboseCheckBox(JCheckBox verboseCheckBox) {
        this.verboseCheckBox = verboseCheckBox;
    }

    public JTextField getWinMenuGroupField() {
        return winMenuGroupField;
    }

    public void setWinMenuGroupField(JTextField winMenuGroupField) {
        this.winMenuGroupField = winMenuGroupField;
    }

    public String getJarPath() {
        return jarPath;
    }

    public void setJarPath(String jarPath) {
        this.jarPath = jarPath;
    }

    public JTextArea getLogTextArea() {
        return logTextArea;
    }

    public void setLogTextArea(JTextArea logTextArea) {
        this.logTextArea = logTextArea;
    }

    public JProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(JProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public JButton getOpenDistButton() {
        return openDistButton;
    }

    public void setOpenDistButton(JButton openDistButton) {
        this.openDistButton = openDistButton;
    }

    public JList<String> getNavList() {
        return navList;
    }

    public void switchPage(int index) {
        navList.setSelectedIndex(index);
    }

    public JTextField getJpackagePathField() {
        return jpackagePathField;
    }

    public void setJpackagePathField(JTextField jpackagePathField) {
        this.jpackagePathField = jpackagePathField;
    }

    /**
     * 设置自定义jpackage路径
     */
    public void setJpackagePath(String path) {
        this.customJpackagePath = path;
        if (advancedPanel != null && advancedPanel.getJpackagePathField() != null) {
            advancedPanel.getJpackagePathField().setText(path);
        }
        updateStatusBar("已设置 jpackage 路径: " + path);
        // 重新检测环境
        checkEnvironment();
    }

    /**
     * 获取当前jpackage路径
     */
    public String getJpackagePath() {
        return customJpackagePath;
    }
}
