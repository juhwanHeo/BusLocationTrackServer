package main;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import BusProgress.BusProgress;
import Scheduler.Scheduler;
import database.GetDBData;
import database.PushDB;
import distance.Distance;
import location.Location;
import station.BusStation;

public class Main {
	static double[] curDis;
	static double[] maxDis;
	static int[] progress;
	static BusStation[] busStation;
	static Scheduler sd;
	
	
	public static void main(String[] args) {
		int cnt=1;
		while(true) {
			
			/*
			 * 10�ʸ��� ����ȭ.
			 */
			int sleepTime = 1000 * 10;
			long now = System.currentTimeMillis();
            Date date = new Date(now);
            SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            System.out.println((cnt++)+"]����ȭ �ð�: "+ sdfNow.format(date));
            
			/*
			 * �ð�ǥ�� �ð��� ���ݽð� ���ؼ� �ڽ� ����
			 */
			sd = new Scheduler();
			BusStation.checkTime(sd);

			/*
			 * �ڽ� ����(�׽�Ʈ�� ����)
			 */
//          sd = new Scheduler();
//			sd.setCurrentCourse('G');
			
			/*
			 * busStation ��ü�� ���ٸ�
			 */
			if(sd.getCurrentCourse() == 'E') {
				System.err.println("Course Error");
			}
            if(busStation == null) {
            	// ��ü ����
            	busStation = BusStation.checkCourse(sd.getCurrentCourse());
            	
            	/*
            	 * busStation�� ������ ������ �����޴ٸ�
            	 */
            } else if(busStation[busStation.length-1].getIsBusArrived()){
            	// ��ü �ٽ� ����
            	busStation = BusStation.checkCourse(sd.getCurrentCourse());
            } 
			Calendar cal = Calendar.getInstance();
			int curHour = cal.get(Calendar.HOUR_OF_DAY);
			int curMinute = cal.get(Calendar.MINUTE);
			

			try {
				if(sd.getCurrentCourse() == 'E') throw new Exception("�ڽ� ����");
				processing();
			} catch (NullPointerException e) {
				System.out.println("�� ����.");
			} catch (Exception e) {
				System.err.println("�����߻�: " + e.getMessage());
			}
			
			if (BusStation.getHour() <= curHour && BusStation.getMinute() <= curMinute) {
				try {
					push();
				} catch (Exception e) {
					System.out.println("push Error");
				}
				
			}
			
//			try {
//				push();
//			} catch (Exception e) {
//				System.out.println("Ŭ���� �̸� ��ã��");
//			}
			
			try {
				Thread.sleep(sleepTime);
			}catch (Exception e) {
				e.printStackTrace();
			}

		}
			
	}
	
	static void push() {
		PushDB pushDB = new PushDB();
			try {
				pushDB.pushData(busStation);
			} catch (Exception e) {
				System.out.println("���� ����");
			}
		
	}
	
	static void processing() throws NullPointerException{
		
		/*
		 * �ڽ� ���
		 */
		System.out.println("�ڽ�: " + sd.getCurrentCourse());
		
		curDis = new double[busStation.length - 1];
		maxDis = new double[busStation.length - 1];
		progress = new int[busStation.length - 1];

		GetDBData getDBData = new GetDBData();
		try {
			getDBData.getJSONData();
		} catch (Exception e) {
			e.printStackTrace();
		}
		getDBData.getDBData();


		Location currentLocation = new Location(getDBData.getLatitude(), getDBData.getLongitude());
		for (int i = 0; i < curDis.length; i++) {
			curDis[i] = Distance.distance(currentLocation, busStation[i+1]);
		}
		

		for (int i = 0; i < busStation.length - 1; i++) {
			maxDis[i] = Distance.distance(busStation[i], busStation[i + 1]);
		}

		
		
		
		// Progress
		for (int i = 0; i < progress.length; i++) {
			progress[i] = BusProgress.getProgress(maxDis[i], curDis[i]);
		}
		
		
		
		
//		System.out.println(maxDis[6]);
//		System.out.println(curDis[6]);
//		System.out.println(BusProgress.getProgress(maxDis[6], curDis[6]));
		/*
		 * �����
		 */
//		Scanner input = new Scanner(System.in);
//		for (int i = 0; i < 5; i++) {
//			System.out.print("�Է�>>> ");
//			progress[i] = input.nextInt();
//		}
//		progress[0] = 99;
//		progress[1] = 100;
//		progress[2] = 100;
//		progress[3] = 100;
//		progress[4] = 100;
//		progress[5] = 100;
//		progress[6] = 100;
		
		
		
		
		busStation[0].setIsBusArrived(true);
		for (int i = 0; i < progress.length; i++) {
			// �� �����忡 ���� �����޴��� ���� Ȯ��
			if (i != 0 && !busStation[i].getIsBusArrived()) {
				continue;
			}
			else busCheck(busStation[i + 1], progress[i]);
		}
		
		
		
//		input.close();
	}
	
	static void print() throws NullPointerException{
		System.out.println();
		System.out.println("-------------------------------------");
		System.out.println();

		/*
		 * ���
		 */
		System.out.println("curDis:" + curDis.length);
		for (int i = 0; i < curDis.length; i++) {
			System.out.println(i+"] DB��ġ -> " + busStation[i+1].getStationName() + ": " + curDis[i]);

		}
		System.out.println();

		System.out.println("maxDis:" + maxDis.length);
		for (int i = 0; i < maxDis.length; i++) {
			System.out.println(
					i+"] "+busStation[i].getStationName() + "->" 
			+ busStation[i + 1].getStationName() + ": " + maxDis[i]);

		}
		System.out.println();

		System.out.println("Progress: " + progress.length);
		for (int i = 0; i < progress.length; i++) {
			System.out.println(i+"] DB��ġ -> "
			+ busStation[i + 1].getStationName() + ": " + progress[i]);
		}
		System.out.println();

		System.out.println("isBusArrived: ");
		System.out.println("����: " + busStation[0].getStationName());
		for (int i = 1; i <= progress.length; i++) {
			System.out.println(busStation[i].getStationName() + ": \n\t" + busStation[i].getIsBusArrived());
		}
		System.out.println();
		
	}

	static void busCheck(BusStation busStation, int progress) {
//		if(busStation.getIsBusArrived()) {
//			busStation.setIsBusArrived(true);
//		}
		if (progress >= 96) {
			busStation.setIsBusArrived(true);
		}
	}

}
