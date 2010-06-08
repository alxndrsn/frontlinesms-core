/**
 * 
 */
package net.frontlinesms.ui;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * @author Morgan Belkadi <morgan@frontlinesms.com>
 * @author Alex Anderson <alex@frontlinesms.com>
 */
public class FrontlineUiUtils {

	public static Image getLimitedSizeImageFromImage(Image img, int maxWidth,
			int maxHeight) {
		return getLimitedSizeImage(getBufferedImage(img), maxWidth, maxHeight);
	}
	public static Image getLimitedSizeImage(BufferedImage image, double maxWidth, double maxHeight) {
		int width = image.getWidth();
		int height = image.getHeight();
		
		if (height > maxHeight) {
			if (width > maxWidth) {
				if (width / maxWidth > height / maxHeight) {
					height *= (maxWidth / width);
					width = (int)(maxWidth);
				}
				else {
					width *= (maxHeight / height);
					height = (int)maxHeight;
				}
			}
			else {
				width *= (maxHeight / height);
				height = (int) maxHeight;
			}
		}
		else if (width > maxWidth) {
			height *= (maxWidth / width);
			width = (int)(maxWidth);
		}
		
		return image.getScaledInstance(width, height, BufferedImage.SCALE_SMOOTH);
	}

	public static BufferedImage getBufferedImage(Image image) {
		int width = image.getWidth(null);
		int height = image.getHeight(null);
		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics g = bufferedImage.getGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();
		return bufferedImage;
	}
	
	public static byte[] getImageAsBytes(BufferedImage imgimage, String encoding) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ImageIO.write(imgimage, "jpeg", out);
		return out.toByteArray();
	}
	public static Image getImage(byte[] data) {
		return Toolkit.getDefaultToolkit().createImage(data);
	}
}
