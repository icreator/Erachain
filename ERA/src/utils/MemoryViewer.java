package utils;

import org.apache.log4j.Logger;

import controller.Controller;

public class MemoryViewer extends Thread {
	private static final Logger LOGGER = Logger.getLogger(Controller.class);

	public MemoryViewer() {

		setName("Memory manager");

	}

	public void run() {
		// if memory !Ok
		while (true) {
			try {
				sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (Runtime.getRuntime().maxMemory() == Runtime.getRuntime().totalMemory()) {
				// System.out.println("########################### Free Memory:"
				// + Runtime.getRuntime().freeMemory());
				if (Runtime.getRuntime().freeMemory() < controller.Controller.MIN_MEMORY_TAIL) {
					System.gc();
					if (Runtime.getRuntime().freeMemory() < controller.Controller.MIN_MEMORY_TAIL>>1)
						Controller.getInstance().stopAll(99);
				}
			}
		}
	}

}
