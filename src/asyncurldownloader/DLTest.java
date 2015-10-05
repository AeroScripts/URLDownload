/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asyncurldownloader;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 *
 * @author Aero
 */
public class DLTest {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JFrame test = new JFrame("Download Test");
        test.add(new Component() {
            
            Downloader downloader = new Downloader();
            Downloader.Progress progress;
            
            {
                Dimension size = new Dimension(798, 54);
                setSize(size);
                setPreferredSize(size);
                try {
                    progress = downloader.download(new URL("http://speedtest.reliableservers.com/100MBtest.bin"), new File("./test.bin"), new DownloadResult() {
                        
                        @Override
                        public void finished(long time) {
                            System.out.println("Donwload finished after " + (int)(time/1000) + " seconds");
                        }
                        
                        @Override
                        public void failed(long time, IOException status, int response) {
                            System.out.println("Donwload failed after " + (int)(time/1000) + " seconds, with response code " + response);
                        }
                    });
                } catch (MalformedURLException ex) {
                    Logger.getLogger(DLTest.class.getName()).log(Level.SEVERE, null, ex);
                }
                new Thread(new Runnable() { // BAD

                    @Override
                    public void run() {
                        long rate = 1000/30;
                        while(true){
                            try {
                                Thread.sleep(rate);
                                repaint();
                            } catch (InterruptedException ex) {
                                Logger.getLogger(DLTest.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                }, "repainter").start();
                
            }
            
            @Override
            public void paint(Graphics g) {
                g.setColor(Color.DARK_GRAY);
                g.fillRect(0,0,800,100);
                g.setColor(Color.YELLOW);
                g.drawString("Download rate: " + progress.getDownloadRate()/1024 +
                        "Kb/s Progress: " + (int)(100*progress.getProgress()) + "%", 8, 18);
                g.drawRect(4,24,788, 24);
                int width = (int) (785 * progress.getProgress());
                g.fillRect(6,26,width, 21);
            }
            
        });
        test.pack();
        test.setLocationRelativeTo(null);
        test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        test.setVisible(true);
    }
    
}
