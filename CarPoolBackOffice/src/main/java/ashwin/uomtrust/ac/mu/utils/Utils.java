package ashwin.uomtrust.ac.mu.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import javax.imageio.ImageIO;

import org.apache.tomcat.util.codec.binary.Base64;

import ashwin.uomtrust.ac.mu.dto.AccountDTO;
import ashwin.uomtrust.ac.mu.dto.CarDTO;
import ashwin.uomtrust.ac.mu.dto.MessageDTO;



public class Utils {	
	
	//Home
	private static final String carDirectory = "D:/UOM/Dissertation/rod1LiftImages/CarGalley/";
	private static final String profilePicDirectory = "D:/UOM/Dissertation/rod1LiftImages/ProfilePicGalley/";
	
	//Office
	//private static final String carDirectory = "I:/UOM/D/rod1LiftImages/CarGalley/";
	//private static final String profilePicDirectory = "I:/UOM/D/rod1LiftImages/ProfilePicGalley/";

	
	public static void saveProfilePictureToServer(AccountDTO accountDTO){
		try {
			
			File filePath = getOutputFile(profilePicDirectory);
			
			if(accountDTO.getsProfilePicture() != null){
				byte [] profilePic = Base64.decodeBase64(accountDTO.getsProfilePicture());
				InputStream in = new ByteArrayInputStream(profilePic);					
				BufferedImage bImageFromConvert = ImageIO.read(in);
				
				String imageName = "/"+accountDTO.getAccountId()+".jpg";
				ImageIO.write(bImageFromConvert, "jpg", new File(filePath +imageName));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void saveImageToServer(CarDTO carDTO){
		try {
			
			getOutputFile(carDirectory);
			
			String imageDirectory = getCarImageDirectory(carDTO.getAccountId());
			
			if(carDTO.getsPicture1() != null){
				byte [] pic1 = Base64.decodeBase64(carDTO.getsPicture1());
				InputStream in1 = new ByteArrayInputStream(pic1);					
				BufferedImage bImageFromConvert1 = ImageIO.read(in1);
				
				String imageName = "1.jpg";
				ImageIO.write(bImageFromConvert1, "jpg", new File(imageDirectory + imageName));
			}
			
			if(carDTO.getsPicture2() != null){
				byte [] pic2 = Base64.decodeBase64(carDTO.getsPicture2());
				InputStream in2 = new ByteArrayInputStream(pic2);					
				BufferedImage bImageFromConvert2 = ImageIO.read(in2);
				String imageName = "2.jpg";
				ImageIO.write(bImageFromConvert2, "jpg", new File(imageDirectory + imageName));
			}
			
			if(carDTO.getsPicture3() != null){
				byte [] pic3 = Base64.decodeBase64(carDTO.getsPicture3());
				InputStream in3 = new ByteArrayInputStream(pic3);					
				BufferedImage bImageFromConvert3 = ImageIO.read(in3);
				String imageName = "3.jpg";
				ImageIO.write(bImageFromConvert3, "jpg", new File(imageDirectory + imageName));
			}
			
			if(carDTO.getsPicture4() != null){
				byte [] pic4 = Base64.decodeBase64(carDTO.getsPicture4());
				InputStream in4 = new ByteArrayInputStream(pic4);					
				BufferedImage bImageFromConvert4 = ImageIO.read(in4);
				String imageName = "4.jpg";
				ImageIO.write(bImageFromConvert4, "jpg", new File(imageDirectory + imageName));
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static CarDTO getImageCar(CarDTO carDTO){
		
		String filePath = getCarImageDirectory(carDTO.getAccountId());
		String pic1 = "1.jpg";
		String pic2 = "2.jpg";
		String pic3 = "3.jpg";
		String pic4 = "4.jpg";

		
		try {
			File file1 = new File(filePath+pic1);
			if(file1.exists()){
				byte[] bytesArray = new byte[(int) file1.length()];
	
				FileInputStream fis = new FileInputStream(file1);
				fis.read(bytesArray);
				fis.close();
				carDTO.setsPicture1(Base64.encodeBase64String(bytesArray));
			}
			
			File file2 = new File(filePath+pic2);
			if(file2.exists()){
				byte[] bytesArray = new byte[(int) file2.length()];
	
				FileInputStream fis = new FileInputStream(file2);
				fis.read(bytesArray);
				fis.close();
				carDTO.setsPicture2(Base64.encodeBase64String(bytesArray));
			}
			
			File file3 = new File(filePath+pic3);
			if(file3.exists()){
				byte[] bytesArray = new byte[(int) file3.length()];
	
				FileInputStream fis = new FileInputStream(file3);
				fis.read(bytesArray);
				fis.close();
				carDTO.setsPicture3(Base64.encodeBase64String(bytesArray));
			}
			
			File file4 = new File(filePath+pic4);
			if(file4.exists()){
				byte[] bytesArray = new byte[(int) file4.length()];
	
				FileInputStream fis = new FileInputStream(file4);
				fis.read(bytesArray);
				fis.close();
				carDTO.setsPicture4(Base64.encodeBase64String(bytesArray));
			}
			
			
		} catch (Exception e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}

		return carDTO;
	}
	
	public static AccountDTO getImageProfile(AccountDTO accountDTO){
		
		String filePath = getProfilePicImageDirectory(accountDTO.getAccountId());
		
		try {
			File file = new File(filePath);
			if(file.exists()){
				byte[] bytesArray = new byte[(int) file.length()];
	
				FileInputStream fis = new FileInputStream(file);
				fis.read(bytesArray);
				fis.close();
				accountDTO.setsProfilePicture(Base64.encodeBase64String(bytesArray));
			}
		} catch (Exception e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}

		return accountDTO;
	}
	
	public static MessageDTO getImageProfile(MessageDTO messageDTO){
		
		String filePath = getProfilePicImageDirectory(messageDTO.getAccountId());
		
		try {
			File file = new File(filePath);
			if(file.exists()){
				byte[] bytesArray = new byte[(int) file.length()];
	
				FileInputStream fis = new FileInputStream(file);
				fis.read(bytesArray);
				fis.close();
				messageDTO.setsProfilePicture(Base64.encodeBase64String(bytesArray));
			}
		} catch (Exception e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}

		return messageDTO;
	}
	
	private static File getOutputFile(String path){
		
		try{
	
			File imageDirectory = new File(path);
	
			
			if (!imageDirectory.exists()) {
				imageDirectory.mkdir();
			}
			
			return imageDirectory;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	private static String getCarImageDirectory(Long userId){
		
		try{
			
			String path = carDirectory+String.valueOf(userId)+"/";
	
			File imageDirectory = new File(path);
	
			
			if (imageDirectory.exists()) {
				imageDirectory.delete();
			}

			imageDirectory.mkdir();
			
			return path;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	private static String getProfilePicImageDirectory(Long userId){
		return profilePicDirectory+String.valueOf(userId)+".jpg";
	}

	public static Double roundTo1DP(Double val){
		DecimalFormat df2 = new DecimalFormat("#.#");
        String valDf2 = df2.format(val);
        valDf2 = valDf2.replace(",", ".");
        return Double.valueOf(valDf2);
	}
}
