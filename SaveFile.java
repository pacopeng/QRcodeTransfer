import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

public class SaveFile {

	int x = 0;
	int y = 0;
	int w = 240;
	int h = 480;
	
	int sx=1340;
	int sy=250;
	
	BufferedImage image = null;
	
	String[] files=null;
	File rootFile=null;
	int fileIndex=0;

	public static void main(String[] args) throws IOException {

		SaveFile rd = new SaveFile();
		try {
			Thread.sleep(10 * 1000);
			long time = System.currentTimeMillis();
			rd.readHead();
			
			System.out.println(System.currentTimeMillis() - time);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	private void readHead() throws Exception {
		rootFile=new File("d://temp/t");
		nextPage(sx, sy);
		long length = readLength();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		writeStream(bos,length);
		bos.close();
	
		

		
		byte[] byteArray = bos.toByteArray();
		String s=new String(byteArray);
		files=s.split(";");
		for(String f:files){
			try {
				readFile();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	private void readFile() throws Exception {
		
		Robot r = new Robot();
		r.mouseMove(sx, sy);
		r.mousePress(InputEvent.BUTTON2_MASK);
		r.mouseRelease(InputEvent.BUTTON2_MASK);
		Thread.sleep(5000);
		
		if(fileIndex>=files.length){
			return;
		}
		File file = new File(rootFile,files[fileIndex]);
		nextPage(sx, sy);
		
		file.getParentFile().mkdirs();
		
		long length = readLength();
		BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(file));
		writeStream(bos,length);
		bos.close();
		

		
		fileIndex++;
		
	}
	
	private void writeStream(OutputStream bos,long length) throws Exception{

		long len = 0;
		while (true) {
			long time = System.currentTimeMillis();
			int readByte = readByte();
			byte ba[] = new byte[1];
			while (readByte >= 0) {
				byte b = (byte) readByte;
				ba[0] = b;
				bos.write(ba);
				len++;
				if (len > length) {
					break;
				}
				readByte = readByte();
			}
			if (len > length) {
				break;
			}
			System.out.println(System.currentTimeMillis() - time);
			nextPage(sx, sy);
		}
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
	private void nextPage(int sx, int sy) throws Exception {
		Robot r = new Robot();
		BufferedImage screenCapture = r.createScreenCapture(new Rectangle(0, 0,
				1600, 900));
		image = screenCapture.getSubimage(sx, sy, w, h);
		x = 0;
		y = 0;

		// ImageIO.write(image,"png",new File("d:/temp/t/1.png"));
		r.mouseMove(sx, sy);
		r.mousePress(InputEvent.BUTTON3_MASK);
		r.mouseRelease(InputEvent.BUTTON3_MASK);
		Thread.sleep(100);

	}
	
	private int readByte() {
		int b = 0;
		int bi = 0;
		for (; bi < 8; bi++) {
			int i = readBit();
			if (i < 0) {
				break;
			} else {
				b = b | (i << bi);
			}
		}
		if (bi == 8) {
			return b;
		} else {
			return -1;
		}
	}

	private int readBit() {
		if (x >= w) {
			x = 0;
			y++;
		}
		if (y >= h) {
			return -1;
		}
		int rgb = image.getRGB(x, y) & 0xFF;
		x++;
		return rgb < 100 ? 1 : 0;

	}

} 