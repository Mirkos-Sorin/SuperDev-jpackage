package worker;

import javax.swing.*;
import java.io.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 后台构建线程
 * 在独立线程中执行jpackage命令，避免阻塞UI
 */
public class BuildWorker extends SwingWorker<Boolean, String> {

    private final List<String> command;
    private final String workDir;
    private final AtomicBoolean isRunning = new AtomicBoolean(true);

    // 信号定义
    private BuildListener listener;

    public BuildWorker(List<String> command, String workDir) {
        this.command = command;
        this.workDir = workDir;
    }

    public void setListener(BuildListener listener) {
        this.listener = listener;
    }

    @Override
    protected Boolean doInBackground() {
        try {
            publish(">> Starting build process...");
            if (listener != null) listener.onProgress(5);

            ProcessBuilder pb = new ProcessBuilder(command);
            pb.directory(new File(workDir.isEmpty() ? "." : workDir));
            pb.redirectErrorStream(true);

            Process process = pb.start();

            BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream(), "UTF-8")
            );

            String line;
            int lineCount = 0;
            while (isRunning.get()) {
                line = reader.readLine();
                if (line == null) {
                    try {
                        process.exitValue();
                        break;
                    } catch (IllegalThreadStateException e) {
                        Thread.sleep(100);
                        continue;
                    }
                }

                lineCount++;
                String type = "info";
                if (line.contains("Error:") || line.contains("error:")) {
                    type = "error";
                } else if (line.contains("Success") || line.contains("created")) {
                    type = "success";
                }

                publish(line);
                if (listener != null) {
                    listener.onLog(line, type);
                    listener.onProgress(Math.min(90, 5 + (lineCount % 85)));
                }
            }

            process.waitFor();
            if (listener != null) listener.onProgress(100);

            return process.exitValue() == 0;

        } catch (Exception e) {
            if (listener != null) {
                listener.onLog("[X] System exception: " + e.getMessage(), "error");
            }
            return false;
        }
    }

    @Override
    protected void process(List<String> chunks) {
        // 在EDT上更新UI
    }

    @Override
    protected void done() {
        try {
            boolean success = get();
            if (listener != null) {
                if (success) {
                    listener.onFinished(true, "[OK] Build successful! Package generated.");
                } else {
                    listener.onFinished(false, "[X] Build failed (exit code: non-zero)");
                }
            }
        } catch (Exception e) {
            if (listener != null) {
                listener.onFinished(false, "[X] System exception: " + e.getMessage());
            }
        }
    }

    public void stop() {
        isRunning.set(false);
    }

    public boolean isRunning() {
        return isRunning.get() && !isDone();
    }

    /**
     * 构建监听器接口
     */
    public interface BuildListener {
        void onLog(String message, String type);
        void onProgress(int progress);
        void onFinished(boolean success, String message);
    }
}
