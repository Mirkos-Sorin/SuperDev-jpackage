package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * 日志页面面板
 * 显示构建日志，提供日志操作功能
 */
public class LogsPanel extends JPanel {

    private MainFrame mainFrame;

    // 日志组件
    private JTextArea logTextArea;
    private JProgressBar progressBar;
    private JButton openDistButton;
    private JLabel statusLabel;

    public LogsPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(AppTheme.BACKGROUND_LIGHT);
        setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        initComponents();
    }

    private void initComponents() {
        // 顶部标题
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // 中间内容区域
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(AppTheme.BACKGROUND_LIGHT);

        // 日志显示卡片
        JPanel logCard = createLogCard();
        contentPanel.add(logCard);
        contentPanel.add(Box.createVerticalStrut(15));

        // 进度条卡片
        JPanel progressCard = createProgressCard();
        contentPanel.add(progressCard);
        contentPanel.add(Box.createVerticalStrut(15));

        // 操作按钮卡片
        JPanel actionCard = createActionCard();
        contentPanel.add(actionCard);
        contentPanel.add(Box.createVerticalGlue());

        // 包装到滚动面板
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBackground(AppTheme.BACKGROUND_LIGHT);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(AppTheme.BACKGROUND_LIGHT);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // 自定义滚动条
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setPreferredSize(new Dimension(8, 0));
        verticalScrollBar.setBackground(AppTheme.INPUT_BG);
        verticalScrollBar.setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * 创建顶部标题区域
     */
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(AppTheme.BACKGROUND_LIGHT);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel titleLabel = new JLabel("构建日志");
        titleLabel.setFont(AppTheme.getChineseFont(Font.BOLD, 24));
        titleLabel.setForeground(AppTheme.TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(titleLabel);

        JLabel subtitleLabel = new JLabel("查看打包过程中的详细信息");
        subtitleLabel.setFont(AppTheme.getChineseFont(Font.PLAIN, 13));
        subtitleLabel.setForeground(AppTheme.TEXT_SECONDARY);
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(subtitleLabel);

        return panel;
    }

    /**
     * 创建日志显示卡片
     */
    private JPanel createLogCard() {
        JPanel card = new JPanel() {
            @Override
            public Dimension getMaximumSize() {
                return new Dimension(Integer.MAX_VALUE, 400);
            }

            @Override
            public Dimension getMinimumSize() {
                return new Dimension(Integer.MAX_VALUE, 250);
            }
        };
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(AppTheme.BACKGROUND_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR, 1, true),
            new EmptyBorder(20, 25, 20, 25)
        ));

        // 卡片标题行
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
        titlePanel.setBackground(AppTheme.BACKGROUND_CARD);

        JLabel cardTitle = new JLabel("实时日志");
        cardTitle.setFont(AppTheme.getChineseFont(Font.BOLD, 16));
        cardTitle.setForeground(AppTheme.TEXT_PRIMARY);
        titlePanel.add(cardTitle);

        titlePanel.add(Box.createHorizontalGlue());

        // 状态指示器
        statusLabel = new JLabel("等待构建");
        statusLabel.setFont(AppTheme.getChineseFont(Font.PLAIN, 12));
        statusLabel.setForeground(AppTheme.TEXT_MUTED);
        statusLabel.setBorder(new EmptyBorder(5, 10, 5, 10));
        statusLabel.setOpaque(true);
        statusLabel.setBackground(new Color(AppTheme.TEXT_MUTED.getRed(), AppTheme.TEXT_MUTED.getGreen(), AppTheme.TEXT_MUTED.getBlue(), 30));
        titlePanel.add(statusLabel);

        card.add(titlePanel);

        card.add(Box.createVerticalStrut(15));

        // 日志文本区域
        logTextArea = new JTextArea();
        logTextArea.setBackground(AppTheme.INPUT_BG);
        logTextArea.setForeground(AppTheme.TEXT_PRIMARY);
        logTextArea.setCaretColor(AppTheme.TEXT_PRIMARY);
        logTextArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        logTextArea.setEditable(false);
        logTextArea.setText(">>> 等待任务开始...\n");
        logTextArea.setLineWrap(true);
        logTextArea.setWrapStyleWord(true);
        logTextArea.setBorder(null);

        JScrollPane logScroll = new JScrollPane(logTextArea);
        logScroll.setBackground(AppTheme.INPUT_BG);
        logScroll.setBorder(BorderFactory.createLineBorder(AppTheme.INPUT_BORDER, 1, true));
        logScroll.getViewport().setBackground(AppTheme.INPUT_BG);
        logScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));

        // 自定义滚动条
        JScrollBar vScrollBar = logScroll.getVerticalScrollBar();
        vScrollBar.setPreferredSize(new Dimension(8, 0));
        vScrollBar.setBackground(AppTheme.INPUT_BG);

        card.add(logScroll);

        mainFrame.setLogTextArea(logTextArea);

        return card;
    }

    /**
     * 创建进度条卡片
     */
    private JPanel createProgressCard() {
        JPanel card = new JPanel() {
            @Override
            public Dimension getMaximumSize() {
                return new Dimension(Integer.MAX_VALUE, 80);
            }
        };
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(AppTheme.BACKGROUND_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR, 1, true),
            new EmptyBorder(20, 25, 20, 25)
        ));

        // 卡片标题
        JLabel cardTitle = new JLabel("构建进度");
        cardTitle.setFont(AppTheme.getChineseFont(Font.BOLD, 14));
        cardTitle.setForeground(AppTheme.TEXT_PRIMARY);
        cardTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(cardTitle);

        card.add(Box.createVerticalStrut(12));

        // 进度条
        progressBar = new JProgressBar(0, 100);
        progressBar.setBackground(AppTheme.INPUT_BG);
        progressBar.setForeground(AppTheme.ACCENT_TEAL);
        progressBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        progressBar.setVisible(false);
        progressBar.setStringPainted(true);
        progressBar.setFont(AppTheme.getChineseFont(Font.PLAIN, 12));
        card.add(progressBar);

        mainFrame.setProgressBar(progressBar);

        return card;
    }

    /**
     * 创建操作按钮卡片
     */
    private JPanel createActionCard() {
        JPanel card = new JPanel() {
            @Override
            public Dimension getMaximumSize() {
                return new Dimension(Integer.MAX_VALUE, 80);
            }
        };
        card.setLayout(new BorderLayout(15, 0));
        card.setBackground(AppTheme.BACKGROUND_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR, 1, true),
            new EmptyBorder(15, 25, 15, 25)
        ));

        // 左侧按钮组
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.X_AXIS));
        leftPanel.setBackground(AppTheme.BACKGROUND_CARD);

        JButton clearBtn = createModernButton("清空日志", AppTheme.BUTTON_SECONDARY);
        clearBtn.addActionListener(e -> mainFrame.clearLog());
        leftPanel.add(clearBtn);

        leftPanel.add(Box.createHorizontalStrut(10));

        JButton copyBtn = createModernButton("复制日志", AppTheme.BUTTON_SECONDARY);
        copyBtn.addActionListener(e -> mainFrame.copyLog());
        leftPanel.add(copyBtn);

        // 右侧按钮
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.X_AXIS));
        rightPanel.setBackground(AppTheme.BACKGROUND_CARD);

        openDistButton = createModernButton("打开输出目录", AppTheme.BUTTON_SUCCESS);
        openDistButton.setEnabled(false);
        openDistButton.setVisible(false);
        openDistButton.addActionListener(e -> mainFrame.openDistFolder());
        rightPanel.add(openDistButton);

        mainFrame.setOpenDistButton(openDistButton);

        card.add(leftPanel, BorderLayout.WEST);
        card.add(rightPanel, BorderLayout.EAST);

        return card;
    }

    /**
     * 创建现代化按钮
     */
    private JButton createModernButton(String text, Color baseColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color normalColor = baseColor;
                Color hoverColor = baseColor.brighter();

                if (getModel().isPressed()) {
                    g2.setColor(baseColor.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(hoverColor);
                } else {
                    g2.setColor(normalColor);
                }

                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();

                super.paintComponent(g);
            }
        };

        button.setFont(AppTheme.getChineseFont(Font.BOLD, 13));
        button.setForeground(AppTheme.TEXT_PRIMARY);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(10, 20, 10, 20));

        return button;
    }

    /**
     * 更新状态标签
     */
    public void updateStatus(String status, Color color) {
        if (statusLabel != null) {
            statusLabel.setText(status);
            statusLabel.setForeground(color);
            statusLabel.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 30));
        }
    }

    // Getter方法
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

    public JLabel getStatusLabel() {
        return statusLabel;
    }
}
