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
 	private boolean processChanged = false;


	public SJF() {
		queue = new ArrayList<>();
		waitingTimes = new ArrayList<Integer>();
		currentProcess = null;
	}
	@Override
	public void onProcessArrival(SimProcess p, int time) {
		if (queue.isEmpty()){
			queue.add(p);
			processChanged = true;
			//test();
			return;
		}

		if (queue.getFirst().getTimeOfArrival() == p.getTimeOfArrival()){
			if (queue.getFirst().getBurstTime() > p.getBurstTime()){
				queue.addFirst(p);
				processChanged = true;
				return;
			}
			if (queue.getFirst().getBurstTime() <= p.getBurstTime()){
				queue.add(p);
				return;
			}
			if (queue.getFirst().getBurstTime() == p.getBurstTime()){
				queue.add(p);
				return;
			}

		} else {
			queue.add(p);
		}

		if (processChanged) {
			System.out.println("Start running Process {Id=" + queue.getFirst().getId() + ", Arrival Time=" + queue.getFirst().getTimeOfArrival() + ", Burst Time=" + queue.getFirst().getBurstTime() + ", Current Time=" + time + "}");
			processChanged = false;
		}



		//System.out.println("Start running Process {Id=" + queue.getFirst().getId() + ", Arrival Time=" + queue.getFirst().getTimeOfArrival() + ", Burst Time=" + queue.getFirst().getBurstTime() + ", Current Time=" + time + "}");


		//test();


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

	private void test(){
		System.out.println("----------------------");
		for (int i = 0; i < queue.size(); i++) {
			System.out.println(queue.get(i).getId());
		}
		System.out.println("----------------------");
	}
}
