package mainFiles.src;

import java.util.ArrayList;
import java.util.List;

//TODO: if the interrupt is at the same time as a process finishes it causes wierd things to happen
/**
 * Round-Robin
 */
public class RR implements Scheduler {

	private List<SimProcess> queue;
	private List<Integer> waitingTimes;
	private int timeQuantum = 0;
	private int totalWaitingTime = 0;
	private boolean processFinishedDuringInterrupt = false;


	public RR(int i) {
		System.out.println(i);
		timeQuantum = i;
		queue = new ArrayList<SimProcess>();
		waitingTimes = new ArrayList<Integer>();

		// Configure a clock interrupt
		Clock.ENABLE_INTERRUPT = true;
		Clock.INTERRUPT_INTERVAL = i;


	}

	@Override
	public void onProcessArrival(SimProcess p, int time) {
		queue.add(p);
		if (queue.size() == 1){
			System.out.println("Start running Process {" + "Id=" + queue.getFirst().getId() + ", Arrival Time=" + queue.getFirst().getTimeOfArrival() + ", Burst Time=" + queue.getFirst().getBurstTime() + ", Current Time=" + time + "}");
		}

		//System.out.println("Queue size: " + queue.size());
		//System.out.println("New Process ID: " + p.getId());
		//System.out.println("Arrival Time: " + p.getTimeOfArrival());
	}

	@Override
	public void onProcessExit(SimProcess p, int time) {
		queue.remove(p);
		int waitingTime = time - p.getTimeOfArrival() - p.getBurstTime();
		waitingTimes.add(waitingTime);
		totalWaitingTime += waitingTime;

		System.out.println(p.getId() + " finished at time " + time + ". Its waiting time is " + waitingTime);
		System.out.println("Current average waiting time: " + calculateAvgWaiting());

		processFinishedDuringInterrupt = true;

	}

	@Override
	public void onClockInterrupt(int timeElapsed, int time) {

		//for (int i = 0; i < queue.size(); i++) {
		//	System.out.println(queue.get(i).getId());
		//}

		if (processFinishedDuringInterrupt) {
			processFinishedDuringInterrupt = false;
			return;
		}

		SimProcess firstElement = queue.getFirst();
		queue.removeFirst();
		queue.add(firstElement);
		System.out.println("Stop running Process {" + "Id=" + firstElement.getId() + ", Current Time=" + time + "}");
		System.out.println("Start running Process {" + "Id=" + queue.getFirst().getId() + ", Arrival Time=" + queue.getFirst().getTimeOfArrival() + ", Burst Time=" + queue.getFirst().getBurstTime() + ", Current Time=" + time + "}");


	}

	@Override
	public String getAlgorithmName() {
		return "RR";
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

	private double calculateAvgWaiting() {
		double totalWaiting = 0;
		for (int waitingTime: waitingTimes) {
			totalWaiting += waitingTime;
		}
		return totalWaiting / waitingTimes.size();
	}

}
