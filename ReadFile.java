import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JWindow;
import javax.swing.RepaintManager;


public class ReadFile extends JWindow {

	
	Rectangle imageBounds=null;
	
	int x=0;
	int y=0;
	BufferedImage image=null;
	int page=0;
	private long length;
	private BufferedInputStream bis;
	
	private long pageCount=0;
	
	/**
	 * @param args
	 * @throws AWTException 
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws AWTException, FileNotFoundException {
		
		Rectangle imageBounds=new Rectangle(40,40,240,480);//实际数据图形范围，宽度必须是8的倍数
		
		ReadFile aa=new ReadFile(imageBounds);
		aa.setSize(new Dimension(imageBounds.width+20,imageBounds.height+30));
		aa.setLocation(imageBounds.x-10, imageBounds.x-20);
		aa.setVisible(true);
	}
	
	
	public ReadFile(Rectangle imageBounds) throws FileNotFoundException{
		this.imageBounds=imageBounds;
		long imageBytes= imageBounds.width*imageBounds.height/8;
		
		JFileChooser jfc=new JFileChooser();
		jfc.showOpenDialog(this);
//		jfc.showSaveDialog(null);
		File f = jfc.getSelectedFile();
		bis = new BufferedInputStream(new FileInputStream(f));
		length = f.length();//374569 /5031
		pageCount=length/imageBytes+1;
		paint();
		this.addMouseListener(new MouseAdapter(){

		    public void mouseReleased(MouseEvent e) {
		    	if(e.getButton()==MouseEvent.BUTTON2){
		    		paint();
		    	}
		    }
		});
	}
	 public void paint(Graphics g) {
		 Dimension size = this.getSize();
		 g.setColor(Color.WHITE);
		 g.fillRect(0, 0, size.width, size.height);
		 g.setColor(Color.BLACK);
 		g.drawString(page+"/"+pageCount+"( "+(length/74913.8)+"s)", 10, 10);
 		if(image!=null){
 			 g.drawImage(image, 10, 20, null);
 		}
	 }
	public void paint(){
		long time=System.currentTimeMillis();
		x=0;
		y=0;
		image=new BufferedImage(imageBounds.width,imageBounds.height,BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		if(page==0){
			writeLength(g,length);
		}
			try {
				Point bytePoint = getBytePoint();
				while(bytePoint!=null){
					byte[] b=new byte[1];
					if(bis.read(b)<0){
						break;
					};
					writeByte(g, bytePoint, b[0]);
					bytePoint = getBytePoint();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		

		Rectangle bounds = this.getBounds();
		RepaintManager.currentManager(this).addDirtyRegion(this, 0,0,bounds.width,bounds.height);
		page++;
		System.out.println(System.currentTimeMillis()-time);
	}
	private Point getBytePoint() {
		if (x >= imageBounds.width) {
			x = 0;
			y++;
		}
		if (y >= imageBounds.height) {
			return null;
		} else {
			Point p = new Point(x, y);
			x+=8;
			return p;
		}
	}
	
	private void writeLength(Graphics g,long length){
		int bi=0;
		for(;bi<8;bi++){
			Point bytePoint = getBytePoint();
			writeByte(g,bytePoint,((int)(length>>>(bi*8))&0xFF));
		}
	}
	
	private void writeByte(Graphics g,Point p,int b){
		for(int i=0;i<8;i++){
			writeBit(g,(b>>>i)&1,p.x+i,p.y);
		}
	}
	
	private void writeBit(Graphics g,int b,int x,int y){
		if(b==1){
			g.setColor(Color.BLACK);
			g.drawLine(x, y, x, y);
		}
		
	}

}