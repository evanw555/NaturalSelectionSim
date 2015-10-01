import java.awt.Graphics;
import java.util.ArrayList;

public class EntityHandler{
	private ArrayList<Creature> ents, purgatory, transfer;
	private int count;
	
	public EntityHandler(){
		ents = new ArrayList<Creature>();
		purgatory = new ArrayList<Creature>();
		transfer = new ArrayList<Creature>();
		count = 0;
	}
	
	public void update(){
		count++;
		//after 1 sec, transfer threshold population into transfer
		if(count%Util.FRAME_RATE == 0){
			transfer();
			count = 0;
		}
		//feed transfer slowly into ents
		if(transfer.size() > 0 && Util.randomIntInclusive(0, 2) == 0)
			ents.add(transfer.remove(0));
		//update ents
		for(Creature c : ents)
			c.update();
		for(int i = 0; i < ents.size(); i++)
			if(ents.get(i).isDead()){
				ents.remove(i);
				i--;
			}
	}
	
	public void paint(Graphics g){
		synchronized(ents){
			try{
				for(Creature c : ents)
					c.paint(g);
			}catch(Exception e){};
		}
	}
	
	public void transfer(){
		//if under threshold population, put all it
		if(purgatory.size() < Util.THRESH_POP)
			for(Creature c : purgatory)
				transfer.add(c);
		//else, put only 10 random ones
		else
			for(int i = 0; i < Util.THRESH_POP; i++)
				transfer.add(purgatory.remove(Util.randomIntInclusive(0, purgatory.size()-1)));
		purgatory.clear();
	}
	
	public void add(Creature c){
		purgatory.add(c);
	}
	
	public int getAverageX(){
		//get sum of x-coordinates of all creatures
		int sum = 0;
		for(Creature c : ents)
			sum += c.getX();
		//get number of creatures
		int count = ents.size();
		//return average x-coordinate
		return sum/count;
	}
	
	public int getTopQuarterX(){
		//get average x
		int avgX = Refs.sim.getAverageX();
		//get sum of x-coordinates of all creatures past avg line
		int sum = 0;
		int count = 0;
		for(Creature c : ents)
			if(c.getX() >= avgX){
				sum += c.getX();
				count++;
			}
		//return average x-coordinate
		return sum/count;
	}
	
	public boolean hasPopulationAbove(int n){
		return ents.size() > n;
	}
}
