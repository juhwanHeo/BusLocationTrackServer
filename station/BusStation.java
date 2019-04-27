package station;

import java.util.Calendar;
import Scheduler.Scheduler;
import location.Location;

public class BusStation implements Station {
	private double latitude;
	private double longitude;
	private String stationName;

	private static int hour;
	private static int minute;
	
	private boolean isBusArrived;
	private double maxDis[];
	
	public static int length;

	
	public BusStation() {
		this("no",null);
	}


	public BusStation(String stationName, Location location) {
		this(stationName, location.getLatitude(),location.getLongitude());
	}
	public BusStation(String stationName, double latitude, double longitude) {
		this.stationName = stationName;
		this.latitude = latitude;
		this.longitude = longitude;
		this.isBusArrived = false;
	}

	public static BusStation[] checkCourse(char course) {
		/*
		 * ����Ʈ -> 1���ⱸ -> �ٸ��� -> 2���ⱸ -> ����Ʈ
		 */
		if (course == 'A') {
			BusStation[] station = { 
					new BusStation("����Ʈ", apartmentLat, apartmentLon),
					new BusStation("������ 1���ⱸ", mStationExit1Lat, mStationExit1Lon),
					new BusStation("�ٸ�", underBridgeLat, underBridgeLon),
					new BusStation("������ 2���ⱸ", mStationExit2Lat, mStationExit2Lon),
					new BusStation("����Ʈ", apartmentLat, apartmentLon) };
			return station;

		}

		/*
		 * ����Ʈ -> 1���ⱸ -> �ٸ��� -> �ɼ��� -> �۶��� -> ������ �޹� -> 2���ⱸ -> ����Ʈ
		 */
		else if (course == 'B') {

			BusStation station[] = { 
					new BusStation("����Ʈ", apartmentLat, apartmentLon),
					new BusStation("������ 1���ⱸ", mStationExit1Lat, mStationExit1Lon),
					new BusStation("�ٸ�", underBridgeLat, underBridgeLon),
					new BusStation("�ɼ���", simSchoolLat, simSchoolLon),
					new BusStation("�۶���", songSchoolLat, songSchoolLon),
					new BusStation("������ �޹�", maSchoolBackLat, maSchoolBackLon),
					new BusStation("������ 2���ⱸ", mStationExit2Lat, mStationExit2Lon),
					new BusStation("����Ʈ", apartmentLat, apartmentLon) };
			return station;

		}

		/*
		 * ����Ʈ -> 1���ⱸ -> �ٸ��� -> �ɼ��� -> ���繫�� -> ������(�޹�) -> 2���ⱸ -> ����Ʈ
		 */
		else if (course == 'C') {
			BusStation station[] = { 
					new BusStation("����Ʈ", apartmentLat, apartmentLon),
					new BusStation("������ 1���ⱸ", mStationExit1Lat, mStationExit1Lon),
					new BusStation("�ٸ�", underBridgeLat, underBridgeLon),
					new BusStation("�ɼ���", simSchoolLat, simSchoolLon), new BusStation("���繫��", maOfficeLat, maOfficeLon),
					new BusStation("������ �޹�", maSchoolBackLat, maSchoolBackLon),
					new BusStation("������ 2���ⱸ", mStationExit2Lat, mStationExit2Lon),
					new BusStation("����Ʈ", apartmentLat, apartmentLon) };
			return station;
		}

		/*
		 * ����Ʈ -> 1���ⱸ -> �ٸ��� -> �ɼ��� -> �۶��� -> ������(�� ��) -> ������ -> 2���ⱸ -> ����Ʈ
		 */
		else if (course == 'D') {
			BusStation station[] = { 
					new BusStation("����Ʈ", apartmentLat, apartmentLon),
					new BusStation("������ 1���ⱸ", mStationExit1Lat, mStationExit1Lon),
					new BusStation("�ٸ�", underBridgeLat, underBridgeLon),
					new BusStation("�ɼ���", simSchoolLat, simSchoolLon),
					new BusStation("�۶���", songSchoolLat, songSchoolLon),
					new BusStation("������ �չ�", maSchoolFrontLat, maSchoolFrontLon),
					new BusStation("������", maHiSchoolLat, maHiSchoolLon),
					new BusStation("������ 2���ⱸ", mStationExit2Lat, mStationExit2Lon),
					new BusStation("����Ʈ", apartmentLat, apartmentLon) };
			

			return station;
		}
		else if(course == 'F') {
			BusStation station[] = { 
					new BusStation("������",maseokStation),
					new BusStation("û��",cheongpyeongStation),
					new BusStation("����",GapyeongStation),
					new BusStation("���̿�", KangchonStation),
					new BusStation("��õ��", ChuncheonStation)
			};
			return station;
		}
		else if(course == 'G') {
			BusStation station[] = { 
					new BusStation("��õ��", ChuncheonStation),
					new BusStation("���̿�", KangchonStation),
					new BusStation("����",GapyeongStation),
					new BusStation("û��",cheongpyeongStation),
					new BusStation("������",maseokStation)

			};
			return station;
		}
		else {
			return null;
		}

	}

	public static void checkTime(Scheduler scheduler) {
		Calendar cal = Calendar.getInstance();

		int curhour = cal.get(Calendar.HOUR_OF_DAY);
		int curminute = cal.get(Calendar.MINUTE);

		for (int i = 0; i < scheduler.getTimeTable().length; i++) {
			/*
			 * �ð� Ȯ��
			 */
			if (scheduler.getTimeTable()[i].getHour() == curhour) {
				/*
				 * �� Ȯ��
				 */
				if (scheduler.getTimeTable()[i].getMinute() <= curminute) {
					hour = scheduler.getTimeTable()[i].getHour();
					
					if (scheduler.getTimeTable()[i + 1].getHour() == curhour
							&& scheduler.getTimeTable()[i + 1].getMinute() <= curminute) {
						minute = scheduler.getTimeTable()[i + 1].getMinute();
						scheduler.setCurrentCourse(scheduler.getTimeTable()[i].getCourse()); 
						
						return;
					}
					if (scheduler.getTimeTable()[i].getMinute() <= curminute) {
						minute = scheduler.getTimeTable()[i].getMinute();
						scheduler.setCurrentCourse(scheduler.getTimeTable()[i].getCourse());
					}
				}
			}
		}
	}

	public boolean getIsBusArrived() {
		return isBusArrived;
	}

	public void setIsBusArrived(boolean is) {
		this.isBusArrived = is;
	}

	public String getStationName() {
		return stationName;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public double getMaxDis(int index) {
		return maxDis[index];
	}

	public void setMaxDis(double maxDis, int index) {
		this.maxDis[index] = maxDis;
	}

	public static int getHour() {
		return hour;
	}

	public static int getMinute() {
		return minute;
	}

	
}
