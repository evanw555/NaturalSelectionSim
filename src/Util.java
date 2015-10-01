import java.awt.Color;


public class Util{
	public static final int FRAME_RATE = 50, THRESH_POP = 10;
	public static final long FRAME_DELAY = 1000/FRAME_RATE;
	public static int BLOCK_DIMS;
	
	public static void setBlockDims(int blockDims){
		BLOCK_DIMS = blockDims;
	}
	
	public static void sleep(long millis){
		try{
			Thread.sleep(millis);
		}catch(Exception e){}
	}
	
	public static int randomIntInclusive(int a, int b){
		return (int)(Math.random()*(b-a+1))+a;
	}
	
	public static double randomDouble(double a, double b){
		return (Math.random()*(b-a))+a;
	}
	
	//TODO make sliding scale of mutation potential based on area of mutation relative to area of parent's death
	public static double[] getAlteredAccels(double[] ayi){
		double[] ay = new double[ayi.length];
		for(int i = 0; i < ay.length; i++)
			ay[i] = ayi[i]+randomDouble(Refs.sim.getMutation()*(-1), Refs.sim.getMutation());
		return ay;
	}
	
	public static double[] getAlteredAccelsSpecific(double[] ayi){
		double[] ay = new double[ayi.length];
		//copy old values to new array
		for(int i = 0; i < ay.length; i++)
			ay[i] = ayi[i];
		//number of times to change an index
		int num = Util.randomIntInclusive(0, 3);
		//change random indexes
		for(int i = 0; i < num; i++){
			int index = randomIntInclusive(0, ay.length-1);
			ay[index] = ayi[index]+randomDouble(-.008, .008);
		}
		return ay;
	}
	
	public static Color getAlteredColor(Color color){
		int r = Util.trimInt(color.getRed()+Util.randomIntInclusive(-7, 7), 0, 255);
		int g = Util.trimInt(color.getGreen()+Util.randomIntInclusive(-7, 7), 0, 255);
		int b = Util.trimInt(color.getBlue()+Util.randomIntInclusive(-7, 7), 0, 255);
		return new Color(r, g, b);
	}
	
	public static int trimInt(int x, int a, int b){
		if(x < a)
			return a;
		if(x > b)
			return b;
		return x;
	}
}