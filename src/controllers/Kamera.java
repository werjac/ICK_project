package controllers;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import java.nio.FloatBuffer;

import javax.media.opengl.*;
import com.sun.opengl.util.*;

import entity.Figura;
import entity.ListaFigur;
import entity.Wierzcholek;

/**
 * Kamera3.java <BR>
 * author: Brian Paul (converted to Java by Ron Cemer and Sven Goethel)
 * <P>
 *
 * This version is equal to Brian Paul's version 1.2 1999/10/21
 */
public class Kamera implements GLEventListener, MouseListener, MouseMotionListener {

    private double view_rotx = 0.0, view_roty = 0.0, view_rotz = 0.0;
    private int prevMouseX, prevMouseY;
    private boolean mouseRButtonDown = false;

    public static void main(String[] args) {
        Frame frame = new Frame("Kamera3");
        GLCanvas canvas = new GLCanvas();

        canvas.addGLEventListener(new Kamera());
        frame.add(canvas);
        frame.setSize(800, 800);
        final Animator animator = new Animator(canvas);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                // Run this on another thread than the AWT event queue to
                // make sure the call to Animator.stop() completes before
                // exiting
                new Thread(new Runnable() {
                    public void run() {
                        animator.stop();
                        System.exit(0);
                    }
                }).start();
            }
        });
        frame.show();
        animator.start();
    }

    public void init(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();

        System.err.println("INIT GL IS: " + gl.getClass().getName());

        System.err.println("Chosen GLCapabilities: " + drawable.getChosenGLCapabilities());

        gl.setSwapInterval(1);

        float ambientLight[] = {0.3f, 0.3f, 0.3f, 1.0f};
        float diffuseLight[] = {0.7f, 0.7f, 0.7f, 1.0f};
        float specular[] = {1.0f, 1.0f, 1.0f, 1.0f};
        float lightPos[] = {0.0f, 150.0f, 150.0f, 1.0f};
        float specref[] = {1.0f, 1.0f, 1.0f, 1.0f};

        gl.glEnable(GL.GL_CULL_FACE);
        gl.glEnable(GL.GL_DEPTH_TEST);

        gl.glEnable(GL.GL_LIGHTING);
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_AMBIENT, FloatBuffer.wrap(ambientLight));
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE, FloatBuffer.wrap(diffuseLight));
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_SPECULAR, FloatBuffer.wrap(specular));
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, FloatBuffer.wrap(lightPos));
        gl.glEnable(GL.GL_LIGHT0);

        gl.glEnable(GL.GL_COLOR_MATERIAL);
        gl.glColorMaterial(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE);

        gl.glEnable(GL.GL_NORMALIZE);

        drawable.addMouseListener(this);
        drawable.addMouseMotionListener(this);
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL gl = drawable.getGL();

        float h = (float) height / (float) width;

        gl.glMatrixMode(GL.GL_PROJECTION);

        System.err.println("GL_VENDOR: " + gl.glGetString(GL.GL_VENDOR));
        System.err.println("GL_RENDERER: " + gl.glGetString(GL.GL_RENDERER));
        System.err.println("GL_VERSION: " + gl.glGetString(GL.GL_VERSION));
        gl.glLoadIdentity();
        gl.glFrustum(-1.0f, 1.0f, -h, h, 5.0f, 60.0f);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glTranslatef(0.0f, 0.0f, -40.0f);
    }

    public void display(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();

        // Special handling for the case where the GLJPanel is translucent
        // and wants to be composited with other Java 2D content
        if ((drawable instanceof GLJPanel)
                && !((GLJPanel) drawable).isOpaque()
                && ((GLJPanel) drawable).shouldPreserveColorBufferIfTranslucent()) {
            gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
        } else {
            gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        }

        // Rotate the entire assembly of gears based on how the user
        // dragged the mouse around
        gl.glPushMatrix();
        gl.glRotated(view_rotx, 1.0, 0.0, 0.0);
        gl.glRotated(view_roty, 0.0, 1.0, 0.0);
        gl.glRotated(view_rotz, 0.0, 0.0, 1.0);

        //rysowaniePodstawy(gl);
        ListaFigur listaFigur = ListaFigurBean.pobierzDane("dane.txt");
        Wierzcholek w = obliczSrodek(listaFigur);
        gl.glTranslated(-w.getX(), -w.getY(), 0);
        rysowaniePodstawy(gl);
        for (int i = 0; i < listaFigur.getLiczbaFigur(); i++) {
            rysowanieFigury(gl, listaFigur.getListaFigur().get(i));
        }

        // Remember that every push needs a pop; this one is paired with
        // rotating the entire gear assembly
        gl.glPopMatrix();
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        prevMouseX = e.getX();
        prevMouseY = e.getY();
        if ((e.getModifiers() & e.BUTTON3_MASK) != 0) {
            mouseRButtonDown = true;
        }
    }

    public void mouseReleased(MouseEvent e) {
        if ((e.getModifiers() & e.BUTTON3_MASK) != 0) {
            mouseRButtonDown = false;
        }
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        Dimension size = e.getComponent().getSize();

        double thetaY = 360.0 * ((double) (x - prevMouseX) / (double) size.width);
        double thetaX = 360.0 * ((double) (prevMouseY - y) / (double) size.height);

        prevMouseX = x;
        prevMouseY = y;

        view_rotx += thetaX;
        view_roty += thetaY;
    }

    public void mouseMoved(MouseEvent e) {
    }

    private void rysowanieFigury(GL gl, Figura figura) {
        Wierzcholek wierzcholek;
        List<Double> norm;
        //podstawa
        gl.glBegin(GL.GL_POLYGON);
        gl.glColor3d(1, 0, 0);
        norm = Normalne.calcNormal(figura.getListaWierzcholkow().get(0), 3.0, figura.getListaWierzcholkow().get(1), 3.0, figura.getListaWierzcholkow().get(2), 3.0);
        gl.glNormal3d(norm.get(0), norm.get(1), norm.get(2));
        for (int i = 0; i < figura.getLiczbaWierzcholkow(); i++) {
            wierzcholek = figura.getListaWierzcholkow().get(i);
            gl.glVertex3d(wierzcholek.getX(), wierzcholek.getY(), 3.0);
        }
        gl.glEnd();

        gl.glBegin(GL.GL_POLYGON);
        gl.glColor3d(1, 0, 0);
        norm = Normalne.calcNormal(figura.getListaWierzcholkow().get(0), figura.getListaWierzcholkow().get(1), figura.getListaWierzcholkow().get(2));
        gl.glNormal3d(norm.get(0), norm.get(1), norm.get(2));
        for (int i = figura.getLiczbaWierzcholkow(); i > 0; i--) {
            wierzcholek = figura.getListaWierzcholkow().get(i - 1);
            gl.glVertex3d(wierzcholek.getX(), wierzcholek.getY(), 3.0);
        }
        gl.glEnd();

        //boki
        gl.glBegin(GL.GL_QUAD_STRIP);
        gl.glColor3d(0, 0, 1);
        for (int i = 0; i < figura.getLiczbaWierzcholkow(); i++) {
            wierzcholek = figura.getListaWierzcholkow().get(i);
            if (i > 1) {
                norm = Normalne.calcNormal(figura.getListaWierzcholkow().get(i - 1), 0.0, figura.getListaWierzcholkow().get(i - 1), 3.0, wierzcholek, 0.0);
                gl.glNormal3d(norm.get(0), norm.get(1), norm.get(2));
            }
            gl.glVertex3d(wierzcholek.getX(), wierzcholek.getY(), 0);
            gl.glVertex3d(wierzcholek.getX(), wierzcholek.getY(), 3.0);
        }
        wierzcholek = figura.getListaWierzcholkow().get(0);
        norm = Normalne.calcNormal(figura.getListaWierzcholkow().get(figura.getLiczbaWierzcholkow() - 1), 0.0, figura.getListaWierzcholkow().get(figura.getLiczbaWierzcholkow() - 1), 3.0, wierzcholek, 0.0);
        gl.glNormal3d(norm.get(0), norm.get(1), norm.get(2));
        gl.glVertex3d(wierzcholek.getX(), wierzcholek.getY(), 0);
        gl.glVertex3d(wierzcholek.getX(), wierzcholek.getY(), 3.0);
        gl.glEnd();

        gl.glBegin(GL.GL_QUAD_STRIP);
        gl.glColor3d(0, 0, 1);
        for (int i = figura.getLiczbaWierzcholkow() - 1; i >= 0; i--) {
            wierzcholek = figura.getListaWierzcholkow().get(i);
            if (i < figura.getLiczbaWierzcholkow() - 1) {
                norm = Normalne.calcNormal(figura.getListaWierzcholkow().get(i + 1), 0.0, figura.getListaWierzcholkow().get(i + 1), 3.0, wierzcholek, 0.0);
                gl.glNormal3d(norm.get(0), norm.get(1), norm.get(2));
            }
            gl.glVertex3d(wierzcholek.getX(), wierzcholek.getY(), 0);
            gl.glVertex3d(wierzcholek.getX(), wierzcholek.getY(), 3.0);
        }
        wierzcholek = figura.getListaWierzcholkow().get(figura.getLiczbaWierzcholkow() - 1);
        norm = Normalne.calcNormal(figura.getListaWierzcholkow().get(0), 0.0, figura.getListaWierzcholkow().get(0), 3.0, wierzcholek, 0.0);
        gl.glNormal3d(norm.get(0), norm.get(1), norm.get(2));
        gl.glVertex3d(wierzcholek.getX(), wierzcholek.getY(), 0);
        gl.glVertex3d(wierzcholek.getX(), wierzcholek.getY(), 3.0);
        gl.glEnd();
    }
    
    private void rysowaniePodstawy(GL gl) {
        double wymiar = 10;
        
        gl.glBegin(GL.GL_QUADS);
        gl.glColor3d(0, 1, 0);
        gl.glNormal3d(0, 0, 1);
        gl.glVertex3d(-wymiar, wymiar, 0.0);
        gl.glVertex3d(-wymiar, -wymiar, 0.0);
        gl.glVertex3d(wymiar, -wymiar, 0.0);
        gl.glVertex3d(wymiar, wymiar, 0.0);
        gl.glEnd();
        
        gl.glBegin(GL.GL_QUADS);
        gl.glColor3d(0, 1, 0);
        gl.glNormal3d(0, 0, -1);
        gl.glVertex3d(wymiar, wymiar, 0.0);
        gl.glVertex3d(wymiar, -wymiar, 0.0);
        gl.glVertex3d(-wymiar, -wymiar, 0.0);
        gl.glVertex3d(-wymiar, wymiar, 0.0);
        gl.glEnd();
    }
    private Wierzcholek obliczSrodek(ListaFigur lista){
        double maxX=0, minX=0, maxY=0, minY=0;
        for(int i=0; i<lista.getLiczbaFigur();i++){
            for(int j=0; j<lista.getListaFigur().get(i).getLiczbaWierzcholkow();j++){
               double tmpX= lista.getListaFigur().get(i).getListaWierzcholkow().get(j).getX();
               double tmpY=lista.getListaFigur().get(i).getListaWierzcholkow().get(j).getY();
               if(tmpX>maxX) maxX=tmpX;
               if(tmpX<minX) minX=tmpX;
               if(tmpY>maxY) maxY=tmpY;
               if(tmpY<minY) minY=tmpY;                                                   
            }
        }
        Wierzcholek w = new Wierzcholek((maxX+minX)/2,(maxY+minY)/2);       
        return w;
    }
}
