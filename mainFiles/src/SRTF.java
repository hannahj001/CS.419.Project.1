package mainFiles.src;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Shortest Remaining Time First.
 */
public class SRTF implements Scheduler {
	private List<SimProcess> queue;
	private List<Integer> waitingTimes;
	private int totalWaitingTime = 0;

	public SRTF() {
		queue = new ArrayList<>();
		waitingTimes = new ArrayList<Integer>();
	}

	@Override
	public void onProcessArrival(SimProcess p, int time) {
		//System.out.printf("\nTIME--------------------------%s\n",time);
		//System.out.println("Arrival Process {" + "Id=" + p.getId() + ", Arrival Time=" + p.getTimeOfArrival() + ", Burst Time=" + p.getBurstTime() + ", Current Time=" + time + "}");
		if(queue.isEmpty()){
			queue.add(p);

		} else if (p.getBurstTime() < queue.getFirst().getBurstTime()) {
			//System.out.printf("SWITCH\n");

			SimProcess firstElement = queue.getFirst();
			//System.out.printf("\nFIRST ELEMENT BEFORE: %s\n",queue.getFirst().getId());
			queue.removeFirst();
			queue.add(p);
			firstElement.setBurstTime(firstElement.getBurstTime() - time);
			queue.add(firstElement);
			queue.sort(Comparator.comparingInt(SimProcess::getBurstTime));
			//System.out.printf("\nFIRST ELEMENT AFTER: %s\n",queue.getFirst().getId());
			System.out.println("Stop running Process {" + "Id=" + firstElement.getId() + ", Remaining Burst Time=" + firstElement.getBurstTime() + ", Current Time=" + time + "}");

		}else {
			queue.add(p);
		}
		queue.sort(Comparator.comparingInt(SimProcess::getBurstTime));
		System.out.println("Start running Process {" + "Id=" + queue.getFirst().getId() + ", Arrival Time=" + queue.getFirst().getTimeOfArrival() + ", Burst Time=" + queue.getFirst().getBurstTime() + ", Current Time=" + time + "}");


	}

	@Override
	public void onProcessExit(SimProcess p, int time) {
		if (!queue.isEmpty()) {
			queue.remove(p);
			queue.sort(Comparator.comparingInt(SimProcess::getBurstTime));
			int waitingTime = time - p.getTimeOfArrival() - p.getBurstTime();
			waitingTimes.add(waitingTime);
			totalWaitingTime += waitingTime;

			System.out.println(p.getId() + " finished at time " + time + ". Its waiting time is " + waitingTime);
			System.out.println("Current average waiting time: " + calculateAvgWaiting());
		}else {
			System.out.println("ALL DONE!");
		}
	}

	@Override
	public void onClockInterrupt(int timeElapsed, int time) {
		System.out.println("Clock interrupt! Current time: " + time + " Time Elapsed: " +timeElapsed );
	}

	@Override
	public String getAlgorithmName() {
		return "SRTF";
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

	private double calculateAvgWaiting() {
		double totalWaiting = 0;
		for (int waitingTime: waitingTimes) {
			totalWaiting += waitingTime;
		}
		return totalWaiting / waitingTimes.size();
	}

}
