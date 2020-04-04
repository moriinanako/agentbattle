package tools;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class LogExporter {
	//ログ出力管理クラス

	private String logDirectory;
	Map<String, LogWriter> writers = new HashMap<String, LogWriter>();

	public LogExporter(String path) {
		File dir = new File("./" + path);
		if (!dir.exists())
			dir.mkdirs();

		System.out.println(path);

		logDirectory = "./" + path + "/";
		writers.clear();
	}

	public String makeDir(String path) {
		File dir = new File(logDirectory + path);
		if (!dir.exists())
			dir.mkdirs();

		return path;
	}

	public LogWriter createWriter(String key) {
		if (writers.containsKey(key)) {
			if (writers.get(key).isDisposed() == false)
				return writers.get(key);
			writers.remove(key);
		}
		LogWriter writer = new LogWriter(logDirectory + key + ".csv");
		writers.put(key, writer);
		return writer;
	}
}
