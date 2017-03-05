package java.main;

import java.dataStructures.interfaces.Heap;
import java.graph.Graph;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

/**
 * Created by hristo on 01/11/2016.
 */
public class UserInterface {
    // Dijkstra Shortest path attributes
    private static String inputFileName;
    private static Graph graph;
    private static int startVertex;
    private static int endVertex;
    private static long elapsedTime;

    //UI Components
    private static JFrame frame;
    private static JPanel panel;

    private static JButton openFileBtn;
    private static JButton createGraphBtn;
    private static JButton findShortestPathBtn;
    private static JButton resetBtn;

    private static JFileChooser fileChooser;
    private static JComboBox<String> heapTypes;

    private static JLabel startVertexLabel;
    private static JLabel endVertexLabel;
    private static JLabel outputLabel;

    private static JTextField startVertexTextField;
    private static JTextField endVertexTextField;

    private static JTextArea outputTextArea;
    private static JScrollPane outputTextAreaScrollPane;


    public static void init() {
        initFrame();
        initPanel();

        initFileChooser();
        initOpenFileBtn();
        initCreateGraphBtn();
        initResetBtn();
        initFindShortestPathBtn();

        initHeapTypesCombo();

        initStartVertexField();
        initEndVertexField();
        initOutputField();

        resetAttributes();
        addComponentsToFrame();
    }

    private static void initFrame() {
        frame = new JFrame();
        frame.setTitle("Dijkstra Shortest Path");
        frame.setSize(530, 670);
    }

    private static void initPanel() {
        panel = new JPanel();
        panel.setLayout(null);
    }

    private static void initFileChooser() {
        fileChooser = new JFileChooser();
    }

    private static void initOpenFileBtn() {
        openFileBtn = new JButton("Select File");
        openFileBtn.setBounds(10,10 ,100, 40);
        openFileBtn.addActionListener(e -> {
            int returnVal = fileChooser.showOpenDialog(frame);
            if (returnVal == JFileChooser.APPROVE_OPTION){
                inputFileName = fileChooser.getSelectedFile().getName();
                createGraphBtn.setEnabled(true);
                fileChooser.setCurrentDirectory(fileChooser.getCurrentDirectory());
            }
        });
    }

    private static void initCreateGraphBtn() {
        createGraphBtn = new JButton("Create graph");
        createGraphBtn.setBounds(120,10,150,40);
        createGraphBtn.addActionListener(e -> new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                outputTextArea.setText("Graph from input file " + inputFileName + " is being generated." );
                return null;
            }

            @Override
            protected void done() {
                try {
                    graph = Utils.createGraphFrom(inputFileName);
                    outputTextArea.setText("Graph with " + graph.size() + " vertices was created successfully." );
                    enableFindShortestPathButton();
                } catch (IOException e1) {
                    e1.printStackTrace();
                    System.exit(-1);
                }
            }
        }.execute());
    }

    private static void initResetBtn() {
        resetBtn = new JButton("Reset");
        resetBtn.setBounds(200, 140, 150 ,40);
        resetBtn.addActionListener(e -> {
            resetAttributes();
        });
    }

    private static void resetAttributes() {
        inputFileName = "";
        graph = null;
        startVertex = 0;
        endVertex = 0;
        elapsedTime = 0;

        startVertexTextField.setText("");
        endVertexTextField.setText("");
        outputTextArea.setText("");

        heapTypes.setSelectedIndex(0);

        createGraphBtn.setEnabled(false);
        findShortestPathBtn.setEnabled(false);
    }

    private static void initFindShortestPathBtn() {
        findShortestPathBtn = new JButton("Find Shortest Path");
        findShortestPathBtn.setBounds(10, 140, 180, 40);
        findShortestPathBtn.addActionListener(e -> new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                outputTextArea.setText("Search for shortest path from vertex " + startVertex +
                        " to vertex " + endVertex + " is running." );
                return null;
            }
            @Override
            protected void done() {
                String heapType = (String) heapTypes.getSelectedItem();
                Heap heap = Utils.createHeapFrom(heapType, graph.size());
                long start = System.currentTimeMillis();
                graph.findShortestPath(startVertex, heap);
                long end = System.currentTimeMillis();
                elapsedTime = end - start;

                outputTextArea.setText(Utils.produceOutput(graph, startVertex, endVertex, elapsedTime));
            }
        }.execute());
    }

    private static void initHeapTypesCombo() {
        heapTypes = new JComboBox<>(Constants.HEAP_TYPES);
        heapTypes.setSelectedIndex(0);
        heapTypes.setBounds(280, 10, 100, 40);
    }

    private static void initStartVertexField() {
        startVertexLabel = new JLabel("Start vertex");
        startVertexLabel.setBounds(10, 60, 100, 40);
        startVertexTextField = new JTextField();
        startVertexTextField.setBounds(120, 60, 70, 30);

        startVertexTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String text = startVertexTextField.getText();
                if(text.equals("")) {
                    startVertex = 0;
                }
                else {
                    startVertex = Integer.parseInt(startVertexTextField.getText());
                }
                enableFindShortestPathButton();
            }
        });
    }

    private static void initEndVertexField() {
        endVertexLabel = new JLabel("End vertex");
        endVertexLabel.setBounds(10, 100, 100, 40);
        endVertexTextField = new JTextField();
        endVertexTextField.setBounds(120, 100, 70, 30);

        endVertexTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String text = endVertexTextField.getText();
                if(text.equals("")) {
                    endVertex = 0;
                }
                else {
                    endVertex = Integer.parseInt(endVertexTextField.getText());
                }
                enableFindShortestPathButton();
            }
        });
    }

    private static void initOutputField() {
        outputLabel = new JLabel("Output:");
        outputLabel.setBounds(10, 180, 100, 40);

        outputTextArea = new JTextArea();
        outputTextArea.setLineWrap(true);
        outputTextArea.setWrapStyleWord(true);

        outputTextAreaScrollPane = new JScrollPane(outputTextArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        outputTextAreaScrollPane.setBounds(10,220, 500, 400);
    }

    private static void addComponentsToFrame() {
        panel.add(openFileBtn);
        panel.add(createGraphBtn);
        panel.add(resetBtn);
        panel.add(heapTypes);
        panel.add(startVertexLabel);
        panel.add(startVertexTextField);
        panel.add(endVertexLabel);
        panel.add(endVertexTextField);
        panel.add(findShortestPathBtn);
        panel.add(outputLabel);
        panel.add(outputTextAreaScrollPane);

        frame.add(panel);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private static void enableFindShortestPathButton() {
        boolean enabled = graph != null && startVertex != 0 && endVertex != 0;
        findShortestPathBtn.setEnabled(enabled);
    }
}
