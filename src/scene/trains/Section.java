package scene.trains;

public class Section {
	
	private int sectAB=0, sectBA=0, sectBC=0, sectCB=0, sectCD=0, sectDC=0, sectCE=0, sectEC=0; //broj vozova na pruznoj sekciji
	private int sectABR=0, sectBAR=0, sectBCR=0, sectCBR=0, sectCDR=0, sectDCR=0, sectCER=0, sectECR=0; //broj vozova prije pruznog prelaza
	private double section1Speed=0,  section2Speed=0,  section3Speed=0,  section4Speed=0;
	
	private boolean s1Start=false, s2Start=false, s3Start=false, s4Start=false;
	
	public synchronized boolean getS1() { return this.s1Start; }
	public synchronized boolean getS2() { return this.s2Start; }
	public synchronized boolean getS3() { return this.s3Start; }
	public synchronized boolean getS4() { return this.s4Start; }
	
	public synchronized void setS1() { this.s1Start = true; }
	public synchronized void setS2() { this.s2Start = true; }
	public synchronized void setS3() { this.s3Start = true; }
	public synchronized void setS4() { this.s4Start = true; }
	
	public synchronized void resetS1() { this.s1Start = false; }
	public synchronized void resetS2() { this.s2Start = false; }
	public synchronized void resetS3() { this.s3Start = false; }
	public synchronized void resetS4() { this.s4Start = false; }
	
	public synchronized void setSpeed(int ID, double speed) {
		if (ID == 1) {
			this.section1Speed = speed;
		} else if(ID == 2) {
			this.section2Speed = speed;
		} else if(ID == 3) {
			this.section3Speed = speed;
		} else if(ID == 4) {
			this.section4Speed = speed;
		}
	}
	
	public synchronized double getSpeed(int ID) {
		if (ID == 1) {
			return this.section1Speed;
		} else if(ID == 2) {
			return this.section2Speed;
		} else if(ID == 3) {
			return this.section3Speed;
		} else if(ID == 4) {
			return this.section4Speed;
		}
		return 0;
	}
	
	public synchronized int getAB() { return this.sectAB; }
	public synchronized int getBA() { return this.sectBA; }
	public synchronized int getBC() { return this.sectBC; }
	public synchronized int getCB() { return this.sectCB; }
	public synchronized int getCD() { return this.sectCD; }
	public synchronized int getDC() { return this.sectDC; }
	public synchronized int getCE() { return this.sectCE; }
	public synchronized int getEC() { return this.sectEC; }
	
	public synchronized void incAB() { this.sectAB++; }
	public synchronized void incBA() { this.sectBA++; }
	public synchronized void incBC() { this.sectBC++; }
	public synchronized void incCB() { this.sectCB++; }
	public synchronized void incCD() { this.sectCD++; }
	public synchronized void incDC() { this.sectDC++; }
	public synchronized void incCE() { this.sectCE++; }
	public synchronized void incEC() { this.sectEC++; }
	
	public synchronized void decAB() { this.sectAB--; }
	public synchronized void decBA() { this.sectBA--; }
	public synchronized void decBC() { this.sectBC--; }
	public synchronized void decCB() { this.sectCB--; }
	public synchronized void decCD() { this.sectCD--; }
	public synchronized void decDC() { this.sectDC--; }
	public synchronized void decCE() { this.sectCE--; }
	public synchronized void decEC() { this.sectEC--; }
	
	//////////////////////////////////////////////////////////
	
	public synchronized int getABR() { return this.sectABR; }
	public synchronized int getBAR() { return this.sectBAR; }
	public synchronized int getBCR() { return this.sectBCR; }
	public synchronized int getCBR() { return this.sectCBR; }
	public synchronized int getCDR() { return this.sectCDR; }
	public synchronized int getDCR() { return this.sectDCR; }
	public synchronized int getCER() { return this.sectCER; }
	public synchronized int getECR() { return this.sectECR; }
	
	public synchronized void incABR() { this.sectABR++; }
	public synchronized void incBAR() { this.sectBAR++; }
	public synchronized void incBCR() { this.sectBCR++; }
	public synchronized void incCBR() { this.sectCBR++; }
	public synchronized void incCDR() { this.sectCDR++; }
	public synchronized void incDCR() { this.sectDCR++; }
	public synchronized void incCER() { this.sectCER++; }
	public synchronized void incECR() { this.sectECR++; }
	
	public synchronized void decABR() { this.sectABR--; }
	public synchronized void decBAR() { this.sectBAR--; }
	public synchronized void decBCR() { this.sectBCR--; }
	public synchronized void decCBR() { this.sectCBR--; }
	public synchronized void decCDR() { this.sectCDR--; }
	public synchronized void decDCR() { this.sectDCR--; }
	public synchronized void decCER() { this.sectCER--; }
	public synchronized void decECR() { this.sectECR--; }
	
}
