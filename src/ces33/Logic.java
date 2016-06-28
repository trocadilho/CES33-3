package ces33;

import java.util.ArrayList;

public class Logic {
	
	public String dectoBin(int dec){
		String bin = new String();
		bin = Integer.toBinaryString(dec);
		return bin;
	}
	
	public int numPag(int memVirt){
		String numPag = new String();
		String bin = dectoBin(memVirt);
		for(int i=bin.length(); i<32; i++){
			bin = "0" + bin;
		}
		numPag = bin.substring(16, 24);
		return(Integer.parseInt(numPag, 2));
	}
	
	public int numDes(int memVirt){
		String numDes = new String();
		String bin = dectoBin(memVirt);
		for(int i=bin.length(); i<32; i++){
			bin = "0" + bin;
		}
		numDes = bin.substring(24);
		return(Integer.parseInt(numDes, 2));
	}
	
	/*public int numSeg(int memVirt){
		String numSeg = new String();
		String bin = dectoBin(memVirt);
		for(int i=bin.length(); i<32; i++){
			bin = "0" + bin;
		}
		numSeg = bin.substring(14,16);
		return(Integer.parseInt(numSeg, 2));
	}*/
	
	public int numQuadro(int[][] tabPag, int numSeg, int numPag){
		return tabPag[numSeg][numPag];
	}
	
	public int numDesc(int[] tabDesc, int numSeg){
		return tabDesc[numSeg];
	}
	
	public int byteSinalizado(int[][][] memFis, int[][] tabPag, int memVirt, int numSeg){
		int numPag = numPag(memVirt);
		int numDes = numDes(memVirt);
		int numQuadro = numQuadro(tabPag, numSeg, numPag);
		if(numQuadro==-1){
			System.out.println(""+numSeg+ " "+memVirt);
		}
		return (memFis[numSeg][numQuadro][numDes]);
	}
	
	
	public int searchTlb(int numSeg, int numPag, ArrayList<tlb> TLB){
		for(int i=0; i<TLB.size(); i++){
			if(TLB.get(i).getnumPag()==numPag &&TLB.get(i).getnumSegment()==numSeg){
				if(Engine.LRU){
					tlb renew = TLB.get(i);
					TLB.remove(i);
					TLB.add(renew);
				}
				return TLB.get(i).getnumQuad();
			}
		}
		return -1;
	}

	public void FIFO (tlb tlbEntry, ArrayList<tlb> TLB){
		if (TLB.size() == 16){
			TLB.remove(0);
			TLB.add(tlbEntry);
		}
		else
			TLB.add(tlbEntry);
	}
}
