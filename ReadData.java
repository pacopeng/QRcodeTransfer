import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import javax.swing.JFileChooser;

public class ReadData{

	int x=0;
	int y=0;
	int w=240;
	int h=480;
	BufferedImage image=null;
public static void main(String[] args) throws IOException{
	
	ReadData rd=new ReadData();
	try{
	JFileChooser jfc=new JFileChooser();
	jfc.showSaveDialog(null);
	File f=jfc.getSelectedFile();
		Thread.sleep(10*1000);
		long time=System.currentTimeMillis();
		rd.readFile(40,40,f);
		System.out.println(System.currentTimeMillis()-time);
	}catch(Exception e){
		e.printStackTrace();
	}

}

private void readFile(int sx,int sy,File f)  throws Exception {

	BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(f));
	nextPage(sx,sy);
	long length=readLength();
	long len=0;
	while(true){
		long time=System.currentTimeMillis();
		int readByte=readByte();
		byte ba[] =new byte[1];
		while(readByte>=0){
			byte b=(byte) readByte;
			ba[0]=b;
			bos.write(ba);
			len++;
			if(len>length){
				break;
			}
			readByte=readByte();
		}
		if(len>length){
			break;
		}
		System.out.println(System.currentTimeMillis()-time);
		nextPage(sx,sy);
	}
	bos.close();
}	
	

private void nextPage(int sx,int sy) throws Exception {
	Robot r=new Robot();
	
	image=r.createScreenCapture(new Rectangle(sx,sy,w,h));
	x=0;
	y=0;
	r.mouseMove(sx,sy);
	r.mousePress(InputEvent.BUTTON2_MASK);
	r.mouseRelease(InputEvent.BUTTON2_MASK);
	Thread.sleep(100);

}

private long readLength(){
	long b=0;
	int bi=0;
	for(;bi<8;bi++){
		int i=readByte();
		b=b|(i<<(bi*8));
	}
	return b;
}
private int readByte(){
	int b=0;
	int bi=0;
	for(;bi<8;bi++){
		int i=readBit();
		if(i<0){
			break;
		}else{
			b=b|(i<<bi);
		}
	}
	if(bi==8){
		return b;
	}else{
		return -1;
	}
}


private int readBit(){
	if(x>=w){
		x=0;
		y++;
	}
	if(y>=h){
		return -1;
	}
	int rgb=image.getRGB(x,y)&0xFF;
	x++;
	return rgb<100?1:0;

}


}