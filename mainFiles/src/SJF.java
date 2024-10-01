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
	private SimProcess currentProcess;
	private List<Integer> waitingTimes;
	private int totalWaitingTime = 0;



	public SJF() {
		queue = new ArrayList<>();
		waitingTimes = new ArrayList<Integer>();
		currentProcess = null;
	}
	@Override
	public void onProcessArrival(SimProcess p, int time) {
		//System.out.println("Arrival Process {" + "Id=" + p.getId() + ", Arrival Time=" + p.getTimeOfArrival() + ", Burst Time=" + p.getBurstTime() + ", Current Time=" + time + "}");
		queue.add(p);

		queue.sort(Comparator.comparingInt(SimProcess::getTimeOfArrival));
		if (currentProcess == null || (p.getTimeOfArrival() == currentProcess.getTimeOfArrival() && p.getBurstTime() <currentProcess.getBurstTime())) {
			currentProcess = p;


			//System.out.printf("\nis current process finished: %s \n",currentProcess().isFinished());
		    //System.out.printf("\nis current process: %s \n",currentProcess().getId());
			System.out.println("Start running Process {Id=" + currentProcess.getId() + ", Arrival Time=" + currentProcess.getTimeOfArrival() + ", Burst Time=" + currentProcess.getBurstTime() + ", Current Time=" + time + "}");

		}
		}

	@Override
	public void onProcessExit(SimProcess p, int time) {
		queue.remove(p);
		//System.out.println("Process {" + "Id=" + p.getId() + "} finished at time " + time);
		int waitingTime = time - p.getTimeOfArrival() - p.getBurstTime();
		waitingTimes.add(waitingTime);
		totalWaitingTime += waitingTime;

		System.out.println(p.getId() + " finished at time " + time + ". Its waiting time is " + waitingTime);
		System.out.println("Current average waiting time: " + calculateAvgWaiting());

		if (!queue.isEmpty()) {
			queue.sort(Comparator.comparingInt(SimProcess::getBurstTime));
			System.out.println("Start running Process {" + "Id=" + queue.getFirst().getId() + ", Arrival Time=" + queue.getFirst().getTimeOfArrival() + ", Burst Time=" + queue.getFirst().getBurstTime() + ", Current Time=" + time + "}");
		}
	}

	@Override
	public void onClockInterrupt(int timeElapsed, int time) {
		//none
	}

	private double calculateAvgWaiting() {
		double totalWaiting = 0;
		for (int waitingTime: waitingTimes) {
			totalWaiting += waitingTime;
		}
		return totalWaiting / waitingTimes.size();
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
		return queue.getFirst();
	}

	@Override
	public boolean isEmpty() {
		return queue.isEmpty();
	}
}
