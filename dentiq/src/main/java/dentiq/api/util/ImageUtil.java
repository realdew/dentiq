package dentiq.api.util;


import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
 
import javax.imageio.ImageIO;


public class ImageUtil {
	
	public static final int BASED_WIDTH		= 0;
	public static final int BASED_HEIGHT	= 1;
	
	public static final int SCALE_DEFAULT			= Image.SCALE_DEFAULT;			// 기본 이미지 스케일링
	public static final int SCALE_FAST				= Image.SCALE_FAST;				// 속도 우선
	public static final int SCALE_REPLICATE			= Image.SCALE_REPLICATE;		// ReplicateScaleFilter 사용
	public static final int SCALE_SMOOTH			= Image.SCALE_SMOOTH;			// 부드러움 우선
	public static final int SCALE_AREA_AVERAGING	= Image.SCALE_AREA_AVERAGING;	// 평균 알고리즘
	
	
	public static final String FORMAT_JPG			= "JPG";
	public static final String FORMAT_PNG			= "PNG";
	public static final String FORMAT_BMP			= "BMP";
	public static final String FORMAT_WBMP			= "WBMP";
	public static final String FORMAT_GIF			= "GIF";
	
	
	
	/**
	 * 이미지를 리사이징한다.
	 * 
	 * @param path				원본 파일이 저장되었고, 리사이징 파일이 저장될 (전체) 경로 (파일명 및 마지막 세퍼레이터 제외) 
	 * @param originalFileName	원본 파일명 (경로 제외)
	 * @param resizeBase		리사이즈 기준 BASED_WIDTH or BASED_HEIGHT
	 * @param newSize			리사이즈 기준에 따른 새로운 크기(예: BASED_WIDTH & 700 ==> 가로 기준 700으로 변경하고, 세로는 비율에 맞게
	 * @param scaleHint			스케일 힌트
	 * @param imgFormat			저장될 파일의 이미지 포맷 (FORMAT_JPG, FORMAT_PNG, FORMAT_BMP, FORMAT_WBMP, FORMAT_GIF)
	 * @param resizedFileName	저장될 파일의 이름 (경로 제외)
	 * 
	 * @throws Exception
	 */
	public static void resize(String path, String originalFileName, int resizeBase, int newSize, int scaleHint, String imgFormat, String resizedFileName) throws Exception {
		Image image;
		
		try {
			image = ImageIO.read( new File(path + "/" + originalFileName) );
		} catch(Exception ex) {
			ex.printStackTrace();
			throw new Exception("Cannot read image file [" + path + "/" + originalFileName + "]");
		}
		
		try {			
			int width = image.getWidth(null);
			int height = image.getHeight(null);
			
			double ratio;
			if ( resizeBase == BASED_WIDTH ) {
				ratio = (double) newSize / (double) width;
			} else if ( resizeBase == BASED_HEIGHT ) {
				ratio = (double) newSize / (double) height;				
			} else {
				throw new Exception("리사이즈 기준 오류 [" + resizeBase + "]");
			}
			
			int w = (int) (width * ratio);
			int h = (int) (height * ratio);
			System.out.println("*** 원본 : " + width + ", " + height + " ==>  리사이즈 크기(" + ratio + ") : " + w + ", " + h);
			Image resizedImage = image.getScaledInstance(w, h, scaleHint);
			
			
			BufferedImage newImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
			Graphics g = newImage.getGraphics();
			g.drawImage(resizedImage,  0,  0,  null);
			g.dispose();
			ImageIO.write( newImage,  imgFormat, new File(path + "/" + resizedFileName) );	// imgFormat ==> 가급적 png
			
			
		} catch(Exception ex) {
			ex.printStackTrace();
			throw new Exception(ex);
		}
		
	}
	
	
}
