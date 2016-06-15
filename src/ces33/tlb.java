package ces33;

public class tlb {
	private int numPag;
	private int numQuad;
	private int numSegment;
	
	public tlb (int Pag, int Quad, int Segment){
		this.numPag = Pag;
		this.numQuad = Quad;
		this.numSegment = Segment;
	}
	
	public void setnumPag(int Pag){
		this.numPag = Pag;
	}
	
	public void setnumQuad(int Quad){
		this.numQuad = Quad;
	}
	
	public void setnumSegment(int Segment){
		this.numSegment = Segment;
	}
	
	public int getnumPag(){
		return this.numPag;
	}
	
	public int getnumQuad(){
		return this.numQuad;
	}
	
	public int getnumSegment(){
		return this.numSegment;
	}
	
}
