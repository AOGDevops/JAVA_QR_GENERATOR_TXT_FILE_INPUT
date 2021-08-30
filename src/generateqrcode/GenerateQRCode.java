/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generateqrcode;


import java.awt.image.BufferedImage;
import java.io.FileOutputStream;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

// QR Code Generator in Java using opensource library ZXing
public class GenerateQRCode {

    // Image properties
    private static final int qr_image_width = 150;
    private static final int qr_image_height = 150;
    private static final String IMAGE_FORMAT = "png";

    // Let's do it
    public static void main(String[] args) throws Exception {
        String archivoConDatos = args[0];
        String folderDestino = args[1];
        FileReader leerArchivo;

        ArrayList<String[]> AUX = new ArrayList<>();
        int lineas = 0;//Para numero de filas de la matriz
        int cols = 0;//Para numero de columnas de la matriz
        String cadena = "";
        String info[][];
        leerArchivo = new FileReader(archivoConDatos,StandardCharsets.UTF_8);
        BufferedReader textoArchivo = new BufferedReader(leerArchivo);
        //Quiza no sea la manera mas optima de hacer la lectura del archivo... 
        while (cadena != null) {
            cadena = textoArchivo.readLine();
            if (cadena != null) {
                //System.out.println(cadena);
                AUX.add(cadena.split(","));//Almacena cada array (lineas del archivo) en la lista
                lineas++;
            }
        }

        if (AUX.size() > 0) {
            cols = AUX.get(0).length;
            info = new String[lineas][cols];
            System.out.println("Total codigosQR: "+cols);
            
            for (int i = 0; i < lineas; i++) {
                for (int j = 0; j < cols; j++) {
                    //Captura el elemento (array) de la lista y [j] trae el dato en esa posición del arreglo.
                    info[i][j] = AUX.get(i)[j];
                   // System.out.print(info[i][j] + "\t");
                    String nombre = "QR" +  "-" + j;
                    String IMG_PATH = folderDestino+"/" + nombre + ".png";
                    BitMatrix matrix;
                    Writer writer = new QRCodeWriter();
                    try {

                        matrix = writer.encode("" + info[i][j] + "", BarcodeFormat.QR_CODE, qr_image_width, qr_image_height);

                    } catch (Error e){
                        return;
                    }

                    // Create buffered image to draw to
                    BufferedImage image = new BufferedImage(qr_image_width,
                            qr_image_height, BufferedImage.TYPE_INT_RGB);

                    // Iterate through the matrix and draw the pixels to the image
                    for (int y = 0; y < qr_image_height; y++) {
                        for (int x = 0; x < qr_image_width; x++) {
                            int grayValue = (matrix.get(x, y) ? 0 : 1) & 0xff;
                            image.setRGB(x, y, (grayValue == 0 ? 0 : 0xFFFFFF));
                        }
                    }

                    // Write the image to a file
                    FileOutputStream qrCode = new FileOutputStream(IMG_PATH);
                    ImageIO.write(image, IMAGE_FORMAT, qrCode);
                    qrCode.close();
                }
                System.out.println();
            }
        } else {
            System.out.printf("No hay datos%n");
        }

        textoArchivo.close();
        leerArchivo.close();

        // Encode URL in QR format
    }

}
