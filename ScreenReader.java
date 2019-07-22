import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class ScreenReader{

private static Map map=new HashMap();
public static void main(String[] args) throws Exception{
	
	Robot r=new Robot();
init();
	BufferedImage screenCapture=r.createScreenCapture(new Rectangle(0,0,1600,900));
	Point firstPoint=getFirstPoint(screenCapture,25);
	BufferedImage image=screenCapture.getSubimage(firstPoint.x,firstPoint.y,75*10,25*20);

	for(int row=1;row<2;row++){
		for(int col=0;col<75 ;col++){
			System.out.print(getChar(image,col,row));
		}
System.out.println();
	}	
}

private static void init(){
map.put(275834778L,"A");
map.put(410609566L,"B");
map.put(374940830L,"C");
map.put(209266578L,"D");
map.put(410642334L,"E");
map.put(104458130L,"F");
map.put(443131038L,"G");
map.put(309946270L,"H");
map.put(4842642L,"I");
map.put(41493656L,"J");
map.put(309905310L,"K");
map.put(440477586L,"L");
map.put(511297438L,"M");
map.put(41477534L,"N");
map.put(209266590L,"O");
map.put(104441746L,"P");
map.put(754658462L,"Q");
map.put(276342686L,"R");
map.put(142173598L,"S");
map.put(106554502L,"T");
map.put(41494158L,"U");
map.put(37332870L,"V");
map.put(242796446L,"W");
map.put(309979034L,"X");
map.put(39437698L,"Y");
map.put(7989142L,"Z");
map.put(275891996L,"a");
map.put(208750494L,"b");
map.put(340870940L,"c");
map.put(276383004L,"d");
map.put(410109724L,"e");
map.put(39446420L,"f");
map.put(83053852L,"g");
map.put(275859358L,"h");
map.put(272758036L,"i");
map.put(15989028L,"j");
map.put(343000978L,"k");
map.put(339862674L,"l");
map.put(477218076L,"m");
map.put(275859228L,"n");
map.put(208749852L,"o");
map.put(208883516L,"p");
map.put(619922716L,"q");
map.put(72435476L,"r");
map.put(7456532L,"s");
map.put(273762180L,"t");
map.put(275859228L,"u");
map.put(70371076L,"v");
map.put(208766732L,"w");
map.put(343000852L,"x");
map.put(70487844L,"y");
map.put(5359380L,"z");
map.put(7939982L,"0");
map.put(4318354L,"1");
map.put(5891730L,"2");
map.put(7955602L,"3");
map.put(7988104L,"4");
map.put(7955856L,"5");
map.put(175728520L,"6");
map.put(1695874L,"7");
map.put(7956366L,"8");
map.put(209299332L,"9");
map.put(68280580L,"+");
map.put(845328L,"/");
map.put(204522252L,"=");
map.put(116480L,";");
map.put(307872520L,"<");
map.put(3728258L,"*");
map.put(9167360L,"{");
map.put(2353280L,"}");
map.put(13230080L,"(");
map.put(118656L,")");
map.put(253952L,"|");
map.put(66560L,".");
map.put(1597824L,"\"");
}
private static boolean hasValue(BufferedImage image,int x,int y){
	int rgb=image.getRGB(x,y)&0xFF;
	
	return rgb<200;
}
private static boolean hasCellValue(BufferedImage image,int sx,int sy,int ccol,int crow){
	int ix=sx+ccol*2;
	int iy=sy+crow*3;
	for(int x=0;x<2;x++){
		for(int y=0;y<3;y++){
			if(hasValue(image,x+ix,y+iy)){
				return true;
			}
		}
	}
	
	return false;
}

private static String getChar(BufferedImage image,int col,int row){
	long v=0;
int n=0;
for(int ccol=0;ccol<5;ccol++){
	for(int crow=0;crow<6;crow++){
		if(hasCellValue(image,col*10,row*20+1,ccol,crow)){
			v=v|(1<<n);
		}
		n++;
	}
}
Object o=map.get(v);
System.out.println(v);
System.out.println(o);
if(o==null){

return  " ";
}

return o.toString();
}


private static Point getFirstPoint(BufferedImage image,int rows){
	int width=image.getWidth();
	int height=image.getHeight();
	int th=(rows-1)*20;
	int tw=75*10;
	for(int y=height-th;y>=0;y--){
		int sx=width-tw;
		if(hasValue(image,sx,y)){
			boolean isBorder=true;
			for(int x=sx;x<sx+100;x++){
				if(!hasValue(image,x,y)){
					isBorder=false;
					break;
				}
			}
if(isBorder){

			for(int x=sx;x>=0;x--){
				if(!hasValue(image,x,y)){
					return new Point(x+1,y-19);
				}
			}
		}

		
		}	

	}
	return null;
}
}

