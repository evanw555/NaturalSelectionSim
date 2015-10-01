import java.awt.Color;
import java.awt.Graphics;


public class Map{
	private int width, height, leftRoom, rightRoom;
	public static final short EMPTY = 0, BLOCK = 1;
	private short[][] map;
	
	public Map(int width, int height){
		this.width = width;
		this.height = height;
		this.leftRoom = 0;
		this.rightRoom = 0;
		//create map
		map = new short[height][width];
		//set all cells to empty
		this.clear();
	}
	
	public void clear(){
		for(int y = 0; y < height; y++)
			for(int x = 0; x < width; x++)
				map[y][x] = EMPTY;
	}
	
	public void generateRandomly(int leftRoom, int rightRoom, double intensity){
		this.leftRoom = leftRoom;
		this.rightRoom = rightRoom;
		//clear all cells
		this.clear();
		//set border to blocks
		this.createBlockBorder();
		//randomly place blocks in specified bounds at specified intensity
		for(int y = 0; y < height; y++)
			for(int x = leftRoom; x < width-rightRoom; x++){
				double chance = Math.random();
				if(chance < intensity)
					map[y][x] = BLOCK;
			}
	}
	
	public void createBlockBorder(){
		for(int y = 0; y < height; y++){
			map[y][0] = BLOCK;
			map[y][width-1] = BLOCK;
		}
		for(int x = 0; x < width; x++){
			map[0][x] = BLOCK;
			map[height-1][x] = BLOCK;
		}
	}
	
	public void update(){
		//TODO randomly alter environment
		//TODO TEMP
		if(Util.randomDouble(0, 1) < Refs.sim.getMapAlterationValue())
			alterMap();
		//
	}
	
	public void paint(Graphics g){
		g.setColor(Color.BLACK);
		for(int y = 0; y < height; y++)
			for(int x = 0; x < width; x++)
				if(map[y][x] == BLOCK)
					g.fillRect(x*Util.BLOCK_DIMS, y*Util.BLOCK_DIMS,
							Util.BLOCK_DIMS, Util.BLOCK_DIMS);
		if(Refs.sim.showLines()){
			//average x-coordinate line
			if(Refs.ents.hasPopulationAbove(0)){
				g.setColor(Color.RED);
				g.drawLine(Refs.sim.getAverageX(), 0,
					Refs.sim.getAverageX(), Refs.canvas.getHeight());
			}
			//average 3/4 x-coordinate line
			if(Refs.ents.hasPopulationAbove(0)){
				g.setColor(Color.MAGENTA);
				g.drawLine(Refs.sim.getTopQuarterX(), 0,
					Refs.sim.getTopQuarterX(), Refs.canvas.getHeight());
			}
		}
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	public short getType(int x, int y){
		return map[y][x];
	}
	
	public void alterMap(){
		short alteration = (short)Util.randomIntInclusive(EMPTY, BLOCK);
		int x = 0;
		int y = 0;
		//finds location that isn't of the specified type
		do{
			x = Util.randomIntInclusive(leftRoom, width-rightRoom);
			y = Util.randomIntInclusive(1, height-2);
		}while(map[y][x] == alteration);
		//sets that location to the specified type
		map[y][x] = alteration;
	}
}