import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

public class DirectoryListener {

	public static void main(final String[] args) {
		try {
			Runnable r = new Runnable() {
				@Override
				public void run() {
					while (true) {
						handleDirectoryChangeEvent("C:/Programowanie");
					}
				}
			};

			Thread t = new Thread(r);
			t.start();
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static void handleDirectoryChangeEvent(String path) {
		try {
			Path dir = Paths.get(path);
			WatchService watchService = FileSystems.getDefault()
					.newWatchService();
			dir.register(watchService, ENTRY_CREATE, ENTRY_MODIFY);
			WatchKey watchKey = watchService.take();
			for (WatchEvent<?> event : watchKey.pollEvents()) {
				WatchEvent<Path> ev = (WatchEvent<Path>) event;
				Path name = ev.context();
				Path child = dir.resolve(name);
				System.out.format("%s: %s\n", event.kind().name(), child);
			}
		} catch (Exception x) {
			return;
		}
	}
}
