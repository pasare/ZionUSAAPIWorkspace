package org.zionusa.management.util;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifDirectoryBase;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.jpeg.JpegDirectory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class PictureUtil {

    public static byte[] cropImageSquare(byte[] image) throws IOException, ImageProcessingException, MetadataException {
        // Get a BufferedImage object from a byte array
        InputStream in = new ByteArrayInputStream(image);
        InputStream original = new ByteArrayInputStream(image);
        BufferedImage originalImage = fixOrientationIfNeeded(in, original);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        //convert image to jpg for processing
        BufferedImage convertedImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        convertedImage.createGraphics().drawImage(originalImage, 0, 0, Color.WHITE, null);

        // Get image dimensions
        int height = convertedImage.getHeight();
        int width = convertedImage.getWidth();

        // The image is already a square
        if (height == width) {
            ImageIO.write(convertedImage, "jpg", baos);
            return baos.toByteArray();
        }

        // Compute the size of the square
        int squareSize = (height > width ? width : height);

        // Coordinates of the image's middle
        int xc = width / 2;
        int yc = height / 2;

        // Crop
        BufferedImage croppedImage = convertedImage.getSubimage(
            xc - (squareSize / 2), // x coordinate of the upper-left corner
            yc - (squareSize / 2), // y coordinate of the upper-left corner
            squareSize,            // width
            squareSize             // height
        );

        //convert to byte array for db

        ImageIO.write(croppedImage, "jpg", baos);
        return baos.toByteArray();
    }

    public static byte[] resizeImage(byte[] image, int width, int height) throws IOException, ImageProcessingException, MetadataException {
        // Get a BufferedImage object from a byte array
        InputStream in = new ByteArrayInputStream(image);
        InputStream original = new ByteArrayInputStream(image);
        BufferedImage originalImage = fixOrientationIfNeeded(in, original);

        //convert image to jpg for processing
        Image betweenImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(betweenImage, 0, 0, null);
        g.dispose();

        //convert to byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(resizedImage, "jpg", baos);
        baos.flush();
        byte[] resizedImageBytes = baos.toByteArray();
        baos.close();

        return resizedImageBytes;
    }

    public static byte[] convertToJpg(byte[] image) throws IOException {
        InputStream in = new ByteArrayInputStream(image);
        BufferedImage originalImage = ImageIO.read(in);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(originalImage, "jpg", baos);

        byte[] convertedImageBytes = baos.toByteArray();
        baos.close();

        return convertedImageBytes;

    }

    private static BufferedImage fixOrientationIfNeeded(InputStream imageStream, InputStream originalImageStream) throws ImageProcessingException, IOException, MetadataException {
        Metadata metadata = ImageMetadataReader.readMetadata(imageStream);

        BufferedImage originalImage = ImageIO.read(originalImageStream);
        ExifIFD0Directory exifIFD0Directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
        JpegDirectory jpegDirectory = metadata.getFirstDirectoryOfType(JpegDirectory.class);

        if (jpegDirectory != null) {
            int orientation = 1;
            try {
                if (exifIFD0Directory != null && exifIFD0Directory.containsTag(ExifDirectoryBase.TAG_ORIENTATION)) {
                    orientation = exifIFD0Directory.getInt(ExifDirectoryBase.TAG_ORIENTATION);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            int width = jpegDirectory.getImageWidth();
            int height = jpegDirectory.getImageHeight();

            AffineTransform affineTransform = new AffineTransform();

            switch (orientation) {
                case 1:
                    break;
                case 2: // Flip X
                    affineTransform.scale(-1.0, 1.0);
                    affineTransform.translate(-width, 0);
                    break;
                case 3: // PI rotation
                    affineTransform.translate(width, height);
                    affineTransform.rotate(Math.PI);
                    break;
                case 4: // Flip Y
                    affineTransform.scale(1.0, -1.0);
                    affineTransform.translate(0, -height);
                    break;
                case 5: // - PI/2 and Flip X
                    affineTransform.rotate(-Math.PI / 2);
                    affineTransform.scale(-1.0, 1.0);
                    break;
                case 6: // -PI/2 and -width
                    affineTransform.translate(height, 0);
                    affineTransform.rotate(Math.PI / 2);
                    break;
                case 7: // PI/2 and Flip
                    affineTransform.scale(-1.0, 1.0);
                    affineTransform.translate(-height, 0);
                    affineTransform.translate(0, width);
                    affineTransform.rotate(3 * Math.PI / 2);
                    break;
                case 8: // PI / 2
                    affineTransform.translate(0, width);
                    affineTransform.rotate(3 * Math.PI / 2);
                    break;
                default:
                    break;
            }

            AffineTransformOp affineTransformOp = new AffineTransformOp(affineTransform, AffineTransformOp.TYPE_BILINEAR);
            BufferedImage destinationImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), originalImage.getType());
            return affineTransformOp.filter(originalImage, destinationImage);
        }
        return originalImage;
    }
}
