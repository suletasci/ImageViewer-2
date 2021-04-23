
package MidtermProject;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
public class ımageViewer extends JFrame {
    private ArrayList<Integer> list=new ArrayList<Integer>();
    private ArrayList<Integer> list2=new ArrayList<Integer>();
    JFrame f;
    JPanel p1;
    private ImagePanel ip;
    private drawRectangle dr;
    private DrawHorizantal dh;
    private DrawVertical dv;
    private BufferedImage image;
    private GrayScalePanel gray;
    private static int[] R;
    private static int[] G;
    private static int[] B;
    private static int[] pixels;
    private static int[] pix;
    private static int[] temp;
    private static int[][] array;
    private static int esik=1;
    
    private int j=0,k=0,z=0,x=0,b=0;
    private int width=514,height=98;
   // gelen resimde width ve height değeri vardı o yüzden resimden almak yerine kendimiz verdik
    public ımageViewer() {
        ip=new ImagePanel();
        dh=new DrawHorizantal();
        dv=new DrawVertical();
        dr=new drawRectangle();
        gray=new GrayScalePanel();
        p1=new JPanel();
        try {          
            image = ImageIO.read(new File("image.jpeg"));
            JLabel picLabel=new JLabel(new ImageIcon(image));
            p1.add(picLabel);
            // extractBytes ile colorimage resim i byte byte data byte arrayine attık
            // daha sonra extractBytes 1 tane byte data sı döndü
            // bu döndüğü byte arrayı R,G,B arraylerine atıp
            // daha sonra bu R,G,B arrayini grayscale byte larını tutacak pixels arrayine ekledik
            // ve pixels arrayi bizim grayScale imiz oldu
            // en sonunda eşik degeri ile karşılaştırarak resmi siyah yada beyaz olacak şekilde boyadık
            // eger eşik değeri >byte  ise beyaza(255)
            // eger eşik degeri <byte ise siyaha(0) boyadık.
            checkTrashHold(convertColorToGrayScale(convertImageToByteArray("image.jpeg")));
            //JTABBEDPANE
            JTabbedPane tb=new JTabbedPane();
            tb.setBounds(50, 50, 250, 250);
            tb.add("Original Image",p1);
            tb.add("gray Scale",gray);
            tb.add("Binary Image", ip);
            tb.add("Vertical(Histogram)", dv);
            tb.add("Horizantal(Histogram)",dh);
            tb.add("Draw Rectangle",dr);
         
          // JFRAME
          f=new JFrame();
          f.add(tb);
          f.setTitle("IMAGE VİEWER");
          f.setSize(800, 600);
          f.setDefaultCloseOperation(EXIT_ON_CLOSE);
          f.setVisible(true);  
       } catch (IOException ex) {} 
    }
    
   
    public void checkTrashHold(int[] p){
        pix=new int[514*98];
        array=new int[height][width];
        for(int i=0;i<p.length;i=i+1){
             if(p[i]>esik){
                 pix[b]=0;
             }
             else{
                 pix[b]=255;
             }
             b++;
        }
        // arraydaki sayıları width ve height o göre okuyabilmek için çift boyutlu array e çevirdik.
        int i=0;
        for(int r=0;r<height;r++){
            for(int c=0;c<width;c++){
                if(pix[i]==0){
                    array[r][c]=1;
                }
                else if(pix[i]==255){
                array[r][c]=0;
                }
                i++;
               // System.out.print(array[r][c]);
            }
            //System.out.println("");
        }
        readhorizantal();  
        readVertical();
    }
    public void readhorizantal(){
        int temp=0;
        for(int l=0;l<height;l++){
            for(int n=0;n<width;n++){
                temp+=array[l][n];   
            }
            list.add(temp);
            temp=0;
        } 
        /*for(Integer q:list){
            System.out.println(q);
        }*/
    }
    public void readVertical(){
    int total=0;
      for(int col=0;col<width;col++){
       for(int row=0;row<height;row++){
         total+=array[row][col];
         }
       list2.add(total);
       total=0;
   }
      /*for(Integer q:list2){
            System.out.println(q);
        } */
    }
    public int[] convertColorToGrayScale(DataBufferByte data){
        R=new int[514*98];
        G=new int[514*98];
        B=new int[514*98];
        pixels=new int[514*98];
        temp=new int[514*98];
        for(int i=0;i<data.getData().length;i=i+3){
             R[z]=data.getData()[i];
             G[j]=data.getData()[i+1];
             B[k]=data.getData()[i+2];
             pixels[x]=(int) ((R[z]*0.3)+(G[j]*0.6)+(B[k]*0.1));
             temp[x]=(int) (Math.abs((R[z]*0.3))+Math.abs((G[j]*0.3))+Math.abs((B[k]*0.3)));
             k++;
             j++;
             z++;
             x++; 
        }
        //System.out.println(pixels.length);
        return pixels;
    }
    public DataBufferByte convertImageToByteArray (String ImageName) throws IOException {
        // open image
        File imgPath = new File(ImageName);
        BufferedImage bufferedImage = ImageIO.read(imgPath);
        // get DataBufferBytes from Raster
        WritableRaster raster = bufferedImage .getRaster();
        DataBufferByte data   = (DataBufferByte) raster.getDataBuffer();
        return data; // it will return an images byte data
    }
    
    class GrayScalePanel extends JPanel{
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            for(int x=0;x<width;x++) {
                for(int y=0;y<height;y++) {
                    g.setColor(new Color(temp[y*width+x],temp[y*width+x],temp[y*width+x]));
                    g.fillRect(x, y, 1, 1);
                }
            }
        }
    }
    class ColorPanel extends JPanel{
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            for(int x=0;x<width;x++) {
                for(int y=0;y<height;y++) {
                    g.setColor(new Color(R[y*width+x],G[y*width+x],B[y*width+x]));
                    g.fillRect(x, y, 1, 1);
                }
            }
        }
    }
    class ImagePanel extends JPanel{
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            for(int x=0;x<width;x++) {
                for(int y=0;y<height;y++) {
                    g.setColor(new Color(pix[y*width+x],pix[y*width+x],pix[y*width+x]));
                    g.fillRect(x, y, 1, 1);
                }
            }
        }
    }
    class DrawHorizantal extends JPanel{
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            for(int x=0;x<list.size();x++) {
                g.drawLine(x, 400,x+10,400-list.get(x));
            }
        }
    }
    class DrawVertical extends JPanel{
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            for(int x=0;x<list2.size();x++) {
                g.drawLine(x, 400,x+10,400-list2.get(x));
            }
        }
    }
    class drawRectangle extends JPanel{
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            for(int x=0;x<width;x++) {
                for(int y=0;y<height;y++) {
                    g.setColor(new Color(pix[y*width+x],pix[y*width+x],pix[y*width+x]));
                    g.fillRect(x, y, 1, 1);
                }
            }
            for(int x=0;x<list.size();x++){
                if(list.get(x)<50){
                     g.setColor(Color.red);
                     g.drawRect(x+25, 25 ,50,100); 
                }
            }
        }
    }
    public static void main(String[] args) {
        new ımageViewer();
        
    }
}

