import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.imageio.ImageIO;

public class FileToImage{

public static void main(String[] args) throws IOException{
	File f=new File(args[0]);
	File imageFile=new File(args[1]);
	int yw=1024;
	int yh=768;
	if(imageFile.isDirectory()){
		for(File lf:imageFile.listFiles()){
			if(lf.getName().endsWith("png")){
				readOneImage(lf,yw,yh);
			}
		}
	}else{
		readOneImage(imageFile,yw,yh);
	}

}

private static void readOneImage(File imageFile,int yw,int yh){
	try{
		BufferedImage image=ImageIO.read(imageFile);
		Point fp=getFirstPoint(image,yw,yh);
		BufferedImage sb=image.getSubimage(fp.x+1,fp.y+1,yw,yh);
		int length =readByte(sb,0,0)<<0|readByte(sb,1,0)<<8|readByte(sb,2,0)<<16;
		int block=readByte(sb,3,0);
		DecimalFormat df=new DecimalFormat("000");
		File f=new File(imageFile.getParent(),df.format(block)+imageFile.getName()+".data");
		BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(f));
		readData(bos,length,sb,yw,yh);
		bos.close();
	}catch(IOException e){

	}
}

private static void readData(BufferedOutputStream  bos,int length,BufferedImage subimage,int yw,int yh) throws IOException {
	int i=4;
	for(int y=0;y<yh;y++){
		for(int x=0;x<yw;x++){
			if(i>=length){
				return;
			}
			int rgb=readByte(subimage,x,y);
			bos.write(rgb);
			i++;
		}
	}

}

private static int readByte(BufferedImage image,int x,int y){
	int rgb=image.getRGB(x,y)&0xFFFFFF;
	int r=(rgb>>>16)&0xFF;
	int g=(rgb>>>8)&0xFF;
	int b=(rgb>>>0)&0xFF;
	return (r>>5)|(g>>5<<3)|(b>>5<<6);
}


private static Point getFirstPoint(BufferedImage image,int yw,int yh){
	int width=image.getWidth();
	int height=image.getHeight();
	for(int x=0;x<width;x++){
		for(int y=0;y<height;y++){
			int rgb=image.getRGB(x,y)&0xFF;
			if(rgb==0){
				boolean isBorder=true;
				int w=x+yw;
				for(int t=x;t<w;t++){
					if((image.getRGB(t,y)&0xFF)!=0){
						isBorder=false;
						break;
					}
				}
				if(isBorder){
					w=x+yh;
					for(int t=y;t<w;t++){
						if((image.getRGB(x,t)&0xFF)!=0){
							isBorder=false;
							break;
						}
					}
				}	
				if(isBorder){
					return new Point(x,y);
				}
			}
		}
	}
	return null;
}
}

