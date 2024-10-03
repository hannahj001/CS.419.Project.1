package mainFiles.src;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;


// if exist on a time it expects to interupt dont print, if

/**
 * Round-Robin
 */
public class RR implements Scheduler {

	private List<SimProcess> queue;
	private List<Integer> waitingTimes;
	private int timeQuantum = 0;
	private int totalWaitingTime = 0;
	private int interuptExitTime = 0;
	private int lastExit = 0;
	private SimProcess firstElement = null;


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

	}

	@Override
	public void onProcessExit(SimProcess p, int time) {
		queue.remove(p);
		int waitingTime = time - p.getTimeOfArrival() - p.getBurstTime();
		waitingTimes.add(waitingTime);
		totalWaitingTime += waitingTime;

		System.out.println(p.getId() + " finished at time " + time + ". Its waiting time is " + waitingTime);
		System.out.println("Current average waiting time: " + calculateAvgWaiting());


		if (!(time % timeQuantum == 0)){
			System.out.println("Start running Process {" + "Id=" + queue.getFirst().getId() + ", Arrival Time=" + queue.getFirst().getTimeOfArrival() + ", Burst Time=" + queue.getFirst().getBurstTime() + ", Current Time=" + time + "}");
		}

		lastExit = time;

	}

	@Override
	public void onClockInterrupt(int timeElapsed, int time) {

		if (!(queue.isEmpty())){
			firstElement = queue.getFirst();
			queue.removeFirst();
			queue.add(firstElement);
			if (!(lastExit == time)) {
				System.out.println("Stop running Process {" + "Id=" + firstElement.getId() + ", Current Time=" + time + "}");
			}
			System.out.println("Start running Process {" + "Id=" + queue.getFirst().getId() + ", Arrival Time=" + queue.getFirst().getTimeOfArrival() + ", Burst Time=" + queue.getFirst().getBurstTime() + ", Current Time=" + time + "}");
		}
		interuptExitTime = time;

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


	private void test(){
		System.out.println("----------------------");
		for (int i = 0; i < queue.size(); i++) {
			System.out.println(queue.get(i).getId());
		}
		System.out.println("----------------------");
	}
}
