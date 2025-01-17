package ui;

import javax.swing.*;

import java.awt.event.*;
import java.awt.*;

import model.EventLog;
import model.exception.GraphException;

// GUI app for personal project.
public class GraphSimulator extends JFrame {
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 960;

    private GraphPanel graphPanel;

    // EFFECTS: create a new GraphSimulator with an empty Graph and load/save
    // buttons.
    public GraphSimulator() {
        super("Graph Simulator");
        initializeGraphics(); // from SimpleDrawingPlayer
        initializeAdapters();
    }

    // Originaly from SimpleDrawingPlayer
    // MODIFIES: this
    // EFFECTS: draws the JFrame window where this GraphSimulator will operate, and
    // populates the tools to be used
    // to manipulate this drawing
    private void initializeGraphics() {
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        addGraphPanel();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // EFFECTS: initialize a MouseListener and a WindowListener
    private void initializeAdapters() {
        MouseListener ml = new MouseListener();
        addMouseListener(ml);
        addMouseMotionListener(ml);

        QuitLogger ql = new QuitLogger();
        addWindowListener(ql);
    }

    // Originaly from SimpleDrawingPlayer
    // MODIFIES: this
    // EFFECTS: declares and instantiates a GraphPanel, and adds it to drawings
    private void addGraphPanel() {
        GraphPanel newGraphPanel = new GraphPanel();
        graphPanel = newGraphPanel;
        add(graphPanel, BorderLayout.CENTER);
        validate();
    }

    // MODIFIES: this
    // EFFECTS: handle an event where the mouse was clicked
    public void handleMouseClicked(MouseEvent e) {
        try {
            graphPanel.handleMouseClicked(e);
            repaint();
        } catch (GraphException ge) {
            System.out.println("Unexpected error");
            ge.printStackTrace();
        }
    }

    // MODIFIES: this
    // EFFECTS: handle an event where the mouse was dragged
    public void handleMouseDragged(MouseEvent e) {
        graphPanel.handleMouseDragged(e);
        repaint();
    }

    // Originaly from SimpleDrawingPlayer
    private class MouseListener extends MouseAdapter {
        // EFFECTS: Forward mouse clicked event to the active tool
        public void mouseClicked(MouseEvent e) {
            handleMouseClicked(translateEvent(e));
        }

        // EFFECTS: Forward mouse dragged event to the active tool
        public void mouseDragged(MouseEvent e) {
            handleMouseDragged(translateEvent(e));
        }

        // EFFECTS: translates the mouse event to current drawing's coordinate system
        private MouseEvent translateEvent(MouseEvent e) {
            return SwingUtilities.convertMouseEvent(e.getComponent(), e, graphPanel);
        }
    }

    private class QuitLogger extends WindowAdapter {
        // EFFECT: print all logs to terminal the moment the app quits
        @Override
        public void windowClosing(WindowEvent we) {
            for (model.Event e : EventLog.getInstance()) {
                System.out.println(e.toString());
            }
        }
    }
}
