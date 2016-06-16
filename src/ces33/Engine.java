package ces33;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class Engine {
	
	private static final String INPUT_FILE_NAME = "BACKING_STORE.bin";
	public static final boolean LRU = false; //Using LRU or FIFO
	public static boolean isTLBSearchDone = false;
	
	public static void main(String[] args){
		
		Engine test = new Engine();
	    //read in the bytes
	    byte[] fileContents = test.read(INPUT_FILE_NAME);
		Logic Logica = new Logic();
		ArrayList<tlb> TLB = new ArrayList<tlb>();
		ArrayList<Integer> PageFIFO = new ArrayList<Integer>();
		int[] tabDesc = new int[4];
		int[][] tabPag = new int[4][64];
		int[][][] memFis = new int [4][128][256];
		int numQuadro, endFisico, byteSinalizado, numSegmento;
		int translatedAddresses = 0;
		int PageFaults = 0;
		int SegmentationFaults = 0;
		int TLBHits = 0;
		Arrays.fill(tabDesc, -1);
		for(int i=0; i<4; i++){
			Arrays.fill(tabPag[i], -1);
		}
		for(int i=0; i<4; i++){
			for(int j=0; j<128; j++){
				Arrays.fill(memFis[i][j], -1);
			}
		}
		Charset charset = Charset.forName("US-ASCII");
		Path addresses_path = Paths.get("addresses.txt");
		Path output_path = Paths.get("saida.txt");
		try (BufferedReader reader = Files.newBufferedReader(addresses_path, charset)) {
			try (BufferedWriter writer = Files.newBufferedWriter(output_path, charset)){
			    String line = null;
			    while ((line = reader.readLine()) != null) {
			    	numSegmento = translatedAddresses%4;
			    	int memVirt = Integer.parseInt(line);
			    	//numQuadro = Logica.searchTlb(Logica.numPag(memVirt), TLB);
			    	//Página não se encontra na TLB:
			    	/*if (numQuadro==-1){*/
				    	numQuadro = Logica.numQuadro(tabPag, Logica.numPag(memVirt));
				    	//PageFault:
				    	if(numQuadro==-1){
				    		PageFaults++;
				    		int j, k;
				    		for(j=0; j<128 && memFis[j][0]!=-1; j++);
				    		if(j>=128){ //Memória real cheia
				    			k = PageFIFO.get(0);
				    			PageFIFO.remove(0);
				    			numQuadro = tabPag[k];
				    			tabPag[k] = -1;
				    			j = numQuadro;
				    		}
				    		else{
				    			numQuadro = j;
				    		}
				    		tabPag[Logica.numPag(memVirt)] = numQuadro;
				    		PageFIFO.add(Logica.numPag(memVirt));
				    		for(int i=0; i<256; i++){
				    			memFis[j][i] = fileContents[Logica.numPag(memVirt)*256 + i];
				    		}
				    	}
				    	//tlb tlbEntry = new tlb(Logica.numPag(memVirt), numQuadro);
			    		
				    	//Remove último elemento da lista tlb. Se LRU for true, o último elemento será o LRU. Caso contrário, será o first in (FIFO)
			    		Logica.FIFO(tlbEntry, TLB);
			    	/*}
			    	else{
			    		TLBHits++;	
			    	}*/
			    	endFisico = numQuadro*256+Logica.numDes(memVirt);
			    	byteSinalizado = Logica.byteSinalizado(memFis, tabPag, memVirt);
			    	String s = "Virtual address: " + memVirt + " Physical address: " + endFisico + " Value: " + byteSinalizado + "\n";
			    	writer.write(s, 0, s.length());
			    	translatedAddresses++;
			    }
			    writer.write("Number of Translated Addresses = " + translatedAddresses + "\n");
			    writer.write("Page Faults = " + PageFaults + "\n");
			    writer.write("Page Fault Rate = " + (float)PageFaults/(float)translatedAddresses + "\n");
			    writer.write("TLB Hits = " + TLBHits + "\n");
			    writer.write("TLB Hit Rate = " + (float)TLBHits/(float)translatedAddresses);
		    } catch (IOException x) {
	    		System.err.format("IOException: %s%n", x);
		    }
		} catch (IOException x) {
		    System.err.format("IOException: %s%n", x);
		}
	}
	
	/** Read the given binary file, and return its contents as a byte array.*/ 
	public byte[] read(String aInputFileName){
		System.out.println("Reading in binary file named : " + aInputFileName);
		File file = new File(aInputFileName);
		System.out.println("File size: " + file.length());
		byte[] result = new byte[(int)file.length()];
		try {
		  InputStream input = null;
		  try {
		    int totalBytesRead = 0;
		    input = new BufferedInputStream(new FileInputStream(file));
		    while(totalBytesRead < result.length){
		      int bytesRemaining = result.length - totalBytesRead;
		      //input.read() returns -1, 0, or more :
		      int bytesRead = input.read(result, totalBytesRead, bytesRemaining); 
		      if (bytesRead > 0){
		        totalBytesRead = totalBytesRead + bytesRead;
		      }
		    }
		    System.out.println("Num bytes read: " + totalBytesRead);
		  }
		  finally {
		    System.out.println("Closing input stream.");
		    input.close();
		  }
		}
		catch (FileNotFoundException ex) {
		  System.out.println("File not found.");
		}
		catch (IOException ex) {
		  System.out.println(ex);
		}
		return result;
	}
}
