package mainFiles.src;
//THIS FILE CAN BE CHANGED

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Shortest Job First.
 */
public class SJF implements Scheduler {
	private List<SimProcess> queue;

	public SJF() {
		queue = new ArrayList<>();
	}
	@Override
	public void onProcessArrival(SimProcess p, int time) {
		queue.add(p);
		System.out.println("Process {" + "Id=" + p.getId() + ", Arrival Time=" + p.getTimeOfArrival() + ", Burst Time=" + p.getBurstTime() + "} arrived at time " + time);
		queue.sort(Comparator.comparingInt(SimProcess::getBurstTime));
		if (queue.size() == 1) {
			System.out.println("Start running Process {" + "Id=" + queue.get(0).getId() + ", Arrival Time=" + queue.get(0).getTimeOfArrival() + ", Burst Time=" + queue.get(0).getBurstTime() + ", Current Time=" + time + "}");
		}
	}

	@Override
	public void onProcessExit(SimProcess p, int time) {
		queue.remove(p);
		System.out.println("Process {" + "Id=" + p.getId() + "} finished at time " + time);
		if (!queue.isEmpty()) {
			SimProcess nextProcess = queue.get(0);
			System.out.println("Start running Process {" + "Id=" + nextProcess.getId() + ", Arrival Time=" + nextProcess.getTimeOfArrival() + ", Burst Time=" + nextProcess.getBurstTime() + ", Current Time=" + time + "}");
		}
	}

	@Override
	public void onClockInterrupt(int timeElapsed, int time) {
		//none
	}

	@Override
	public String getAlgorithmName() {
		return "SJF";
	}

	@Override
	public SimProcess currentProcess() {
		if (queue.isEmpty()) {
			return null;
		}
		return queue.get(0);
	}

	@Override
	public boolean isEmpty() {
		return queue.isEmpty();
	}
}
