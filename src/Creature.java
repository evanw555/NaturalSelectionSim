import java.awt.Color;
import java.awt.Graphics;

public class Creature{
	private double yi, x, y, vx, vy;
	private double[] ay;
	private Color color;
	
	public Creature(){
		yi = Refs.canvas.getHeight()*.5;
		x = Util.BLOCK_DIMS*1.5;
		y = yi;
		vx = 1;
		vy = 0;
		ay = new double[Refs.map.getWidth()];
		for(int i = 0; i < ay.length; i++)
			ay[i] = 0;
		color = new Color(Util.randomIntInclusive(0, 255),
				Util.randomIntInclusive(0, 255),
				Util.randomIntInclusive(0, 255));
	}
	
	public Creature(double yi, double vx, double[] ay, Color color){
		this.yi = yi;
		x = Util.BLOCK_DIMS*1.5;
		y = yi;
		this.vx = vx;
		vy = 0;
		this.ay = ay;
		this.color = color;
	}
	
	public void update(){
		vy += ay[getX()/Util.BLOCK_DIMS];
		x += vx;
		if(Refs.sim.isSmoothMotion()){
			y += vy;
		}else{
			y += 70*ay[getX()/Util.BLOCK_DIMS];
		}
		if(Math.random()<.01+.04*(x/Refs.canvas.getWidth())){
			if(Refs.sim.getReproductionQualificationValue() == 0){
				Refs.ents.add(createOffspring());
			}else if(Refs.sim.getReproductionQualificationValue() == 1 &&
					getX() >= Refs.ents.getAverageX()){
				Refs.ents.add(createOffspring());
			}else if(Refs.sim.getReproductionQualificationValue() == 2 &&
					getX() >= Refs.ents.getTopQuarterX()){
				Refs.ents.add(createOffspring());
			}
		}
		//
	}
	
	public void paint(Graphics g){
		g.setColor(color);
		g.fillOval(getX()-3, getY()-3, 6, 6);
		g.setColor(Color.BLACK);
		g.drawOval(getX()-3, getY()-3, 6, 6);
	}
	
	public int getX(){
		return (int)x;
	}
	
	public int getY(){
		return (int)y;
	}
	
	public Color getColor(){
		return color;
	}
	
	public boolean isDead(){
		return Refs.map.getType((int)(x/Util.BLOCK_DIMS), (int)(y/Util.BLOCK_DIMS)) == Map.BLOCK;
	}
	
	public Creature createOffspring(){
		return new Creature(yi+Util.randomIntInclusive(-1, 1),
				vx/*+Util.randomDouble(-.05, .05)*/,
				Util.getAlteredAccels(ay),
				Util.getAlteredColor(color));
	}
}
