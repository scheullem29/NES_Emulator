package SE.project.nes;

import javafx.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.application.Platform;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import SE.project.core.*;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;



public class NES extends Application {

    private FileChooser fc = new FileChooser();
    private File f; //Holds the Rom file upon selection
    private ArrayList<String> defaultKeys = new ArrayList<String>(Arrays.asList("W","A","S","D","V","B","K","L"));
    private CPU nes = new CPU();
    private NESreader NR;
    private int stopCycle = 58110;
    private int time = 1000;
    private boolean reset = false;
    private boolean stepMode = false;
    
    
    @Override
    public void start(Stage stage) throws Exception {
        DataHelper dataHelper = new DataHelper("Counter: ","Cycle: ", "Memory: ", "Carry: ", "Zero: ", "Interrupt: ", "Decimal: ", "Break: ", "Overflow: ", "Sign: ", "Accumulator: ", "IndexRegX: ", "IndexRegY: ");
        Label ctr = new Label("");
        ctr.setFont(new Font(30));
        Label cycle = new Label("");
        cycle.setFont(new Font(30));
        Label memory = new Label("");
        memory.setFont(new Font(30));
        Label accumulator = new Label("");
        accumulator.setFont(new Font(30));
        Label regX = new Label("");
        regX.setFont(new Font(30));
        Label regY = new Label("");
        regY.setFont(new Font(30));
        Label carry = new Label("");
        carry.setFont(new Font(30));
        Label zero = new Label("");
        zero.setFont(new Font(30));
        Label interrupt = new Label("");
        interrupt.setFont(new Font(30));
        Label decimal = new Label("");
        decimal.setFont(new Font(30));
        Label brk = new Label("");
        brk.setFont(new Font(30));
        Label overflow = new Label("");
        overflow.setFont(new Font(30));
        Label sign = new Label("");
        sign.setFont(new Font(30));
        Service<DataHelper> service = new Service<DataHelper>() {
            @Override
            protected Task<DataHelper> createTask() {
                return new Task<DataHelper>() {
                    @Override
                    protected DataHelper call() throws Exception {   
                        Thread.sleep(time);
                        nes.printMemory();
                        return new DataHelper("Counter: " + nes.getpgrmCtr(), "Cycle: " + nes.getcycleCtr(), "Memory: " + String.format("%02X", (nes.getCPUmemory()[nes.getpgrmCtr()]&0xff)) + " - " + Integer.toBinaryString((nes.getCPUmemory()[nes.getpgrmCtr()]&0xff)), 
                                "Carry: " + nes.carryFlag(), "Zero: " + nes.zeroFlag(), "Interrupt: " + nes.getInterruptState(),"Decimal: " + nes.decimalModeFlag(), "Break: " + nes.breakStatus(), "Overflow: " + nes.overflowFlag(), 
                                "Sign: " + nes.signFlag(), "Accumulator: " + String.format("%02X",nes.getAccumulator()), "IndexRegX: " + String.format("%02X", nes.getIndexRegX()), "IndexRegY: " + String.format("%02X", nes.getIndexRegY()));
                    }   
                };
            }
        };
        ctr.textProperty().bind(dataHelper.ctrProperty());
        cycle.textProperty().bind(dataHelper.cycleProperty());
        memory.textProperty().bind(dataHelper.memoryProperty());
        carry.textProperty().bind(dataHelper.carryProperty());
        zero.textProperty().bind(dataHelper.zeroProperty());
        interrupt.textProperty().bind(dataHelper.interruptProperty());
        decimal.textProperty().bind(dataHelper.decimalProperty());
        brk.textProperty().bind(dataHelper.brkProperty());
        overflow.textProperty().bind(dataHelper.overflowProperty());
        sign.textProperty().bind(dataHelper.signProperty());
        accumulator.textProperty().bind(dataHelper.accumulatorProperty());
        regX.textProperty().bind(dataHelper.regXProperty());
        regY.textProperty().bind(dataHelper.regYProperty());
        service.setOnSucceeded(e -> {
            dataHelper.setCtr(service.getValue().getCtr());
            dataHelper.setCycle(service.getValue().getcycle());
            dataHelper.setmemory(service.getValue().getmemory());
            dataHelper.setcarry(service.getValue().getcarry());
            dataHelper.setzero(service.getValue().getzero());
            dataHelper.setinterrupt(service.getValue().getinterrupt());
            dataHelper.setdecimal(service.getValue().getdecimal());
            dataHelper.setbrk(service.getValue().getbrk());
            dataHelper.setoverflow(service.getValue().getoverflow());
            dataHelper.setsign(service.getValue().getsign());
            dataHelper.setaccumulator(service.getValue().getaccumulator());
            dataHelper.setregX(service.getValue().getregX());
            dataHelper.setregY(service.getValue().getregY());
            if(nes.getcycleCtr()< 100000){
                if(nes.getcycleCtr() == stopCycle){
                    stepMode = !stepMode;
                }
                if(reset){
                    nes = new CPU();
                    stepMode = false;
                    time = 1000;
                    fc.setTitle("Select Rom File");
                    //f = fc.showOpenDialog(stage);
                    //NR = new NESreader(f.getAbsolutePath());
                    NR = new NESreader("src\\main\\resources\\ROMS\\Super Mario Bros (E).nes");
                    NR.readFile(nes);
                    service.restart();
                    reset = false;
                } else {
                    if(!stepMode){
                        service.restart();
                    }
                }
            }
            
        });
        
        MenuButton load = new MenuButton("File");
            //Loading function
            MenuItem load1 = new MenuItem("Load Rom");
            load1.setOnAction((ActionEvent event) -> {
                fc.setTitle("Select Rom File");
                //f = fc.showOpenDialog(stage);
                //NR = new NESreader(f.getAbsolutePath());
                NR = new NESreader("src\\main\\resources\\ROMS\\Super Mario Bros (E).nes");
                NR.readFile(nes);
                service.start();
                
            });
            //Exit function
            MenuItem load2 = new MenuItem("Exit");
            load2.setOnAction(e -> Platform.exit());
            load.getItems().addAll(load1,load2);
            
        MenuButton settings = new MenuButton("Settings");
            MenuItem settings1 = new MenuItem("Change Keybindings");
            settings1.setOnAction((ActionEvent event) -> {
                BorderPane root = new BorderPane();
                HBox KBBackground = new HBox();
                KBBackground.setId("keyBindingPane");
                GridPane KBKeys = new GridPane();
                KBKeys.setId("Keys");
                KBKeys.setAlignment(Pos.CENTER);
                KBKeys.setHgap(50);
                KBKeys.setPadding(new Insets(0,0,15,0));
                root.setBottom(KBKeys);
                root.setCenter(KBBackground);
                
                ImageView up = new ImageView(new Image(getClass().getClassLoader().getResource("styles/Images/up.jpg").toExternalForm()));
                TextField upField = new TextField(defaultKeys.get(0));
                upField.setMaxSize(65, 24);
                textEvent(upField);
                ImageView left = new ImageView(new Image(getClass().getClassLoader().getResource("styles/Images/left.jpg").toExternalForm()));
                TextField leftField = new TextField(defaultKeys.get(1));
                leftField.setMaxSize(65,24);
                textEvent(leftField);
                ImageView down = new ImageView(new Image(getClass().getClassLoader().getResource("styles/Images/down.jpg").toExternalForm()));
                TextField downField = new TextField(defaultKeys.get(2));
                downField.setMaxSize(65,24);
                textEvent(downField);
                ImageView right = new ImageView(new Image(getClass().getClassLoader().getResource("styles/Images/right.jpg").toExternalForm()));
                TextField rightField = new TextField(defaultKeys.get(3));
                rightField.setMaxSize(65,24);
                textEvent(rightField);
                Text select = new Text("Select");
                select.setFont(Font.font(null, FontWeight.BOLD, 24));
                TextField selectField = new TextField(defaultKeys.get(4));
                selectField.setMaxSize(65,24);
                textEvent(selectField);
                Label start = new Label("Start");
                start.setFont(Font.font(null, FontWeight.BOLD, 24));
                TextField startField = new TextField(defaultKeys.get(5));
                startField.setMaxSize(65,24);
                textEvent(startField);
                Label A = new Label("A");
                A.setFont(Font.font(null, FontWeight.BOLD, 24));
                TextField AField = new TextField(defaultKeys.get(6));
                AField.setMaxSize(65,24);
                textEvent(AField);
                Label B = new Label("B");
                B.setFont(Font.font(null, FontWeight.BOLD, 24));
                TextField BField = new TextField(defaultKeys.get(7));
                BField.setMaxSize(65,24);
                textEvent(BField);
                
                KBKeys.add(up, 0, 0);
                KBKeys.add(upField, 1, 0);
                KBKeys.add(left, 0, 1);
                KBKeys.add(leftField, 1, 1);
                KBKeys.add(down, 0, 2);
                KBKeys.add(downField, 1, 2);
                KBKeys.add(right, 0, 3);
                KBKeys.add(rightField, 1, 3);
                
                KBKeys.add(select, 2, 0);
                KBKeys.add(selectField, 3, 0);
                KBKeys.add(start, 2, 1);
                KBKeys.add(startField, 3, 1);
                KBKeys.add(A, 2, 2);
                KBKeys.add(AField, 3, 2);
                KBKeys.add(B, 2, 3);
                KBKeys.add(BField, 3, 3);
                
                Scene KBScene = new Scene(root, 600, 350);
                KBScene.getStylesheets().addAll(getClass().getClassLoader().getResource("styles/Styles.css").toExternalForm());
                Stage KBStage = new Stage();
                KBStage.setTitle("KeyBinding Settings");
                KBStage.setScene(KBScene);
                KBStage.setResizable(false);
                KBStage.show();
        });
            settings.getItems().addAll(settings1);
        
        Button resetButton = new Button("reset");
        resetButton.setOnAction((ActionEvent event) -> {
            reset = true;
            if(stepMode){
                service.restart();
            }
        });
        Button Timer = new Button("Timer");
        Timer.setOnAction((ActionEvent event) -> {
            if(time == 0){
                time = 1000;
            } else {
                time = 0;
            }
        });
        HBox hbox = new HBox();
        hbox.getChildren().add(resetButton);
        hbox.getChildren().add(Timer);
        Button stepModeButton = new Button("Step Mode");
        stepModeButton.setOnAction((ActionEvent event) -> {
            if(stepMode){
                stepMode = !stepMode;
                service.restart();
            } else {
                stepMode = !stepMode;
            }  
        });
        Button steppingButton = new Button ("Step");
        steppingButton.setOnAction((ActionEvent event) -> {
            service.restart();
        });
        HBox hbox1 = new HBox();
        hbox1.getChildren().add(stepModeButton);
        hbox1.getChildren().add(steppingButton);
        
        ToolBar tb = new ToolBar(load, settings);
        BorderPane pane = new BorderPane();
        pane.setTop(tb);
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(8);
        vbox.getChildren().add(ctr);
        vbox.getChildren().add(cycle);
        vbox.getChildren().add(memory);
        vbox.getChildren().add(accumulator);
        vbox.getChildren().add(regX);
        vbox.getChildren().add(regY);
        vbox.getChildren().add(hbox);
        vbox.getChildren().add(hbox1);
        VBox vbox2 = new VBox();
        vbox2.setPadding(new Insets(10));
        vbox2.setSpacing(8);
        vbox2.getChildren().add(carry);
        vbox2.getChildren().add(zero);
        vbox2.getChildren().add(interrupt);
        vbox2.getChildren().add(decimal);
        vbox2.getChildren().add(brk);
        vbox2.getChildren().add(overflow);
        vbox2.getChildren().add(sign);
        pane.setLeft(vbox);
        pane.setRight(vbox2);
        Scene scene = new Scene(pane,600,450);
        stage.setTitle("NES Emulator");
        stage.setScene(scene);
        stage.show();
    }
    
    public void textEvent(TextField tf){
        tf.setOnKeyPressed((KeyEvent ke) -> {
            //System.out.println(ke.getCode());
            if(ke.getCode().toString().equals("SHIFT") || ke.getCode().toString().equals("CAPS") || ke.getCode().toString().equals("TAB") || ke.getCode().toString().equals("BACK_SPACE")){
                //ignore
            } else {
                String oldKey = tf.getText();
                if(oldKey.charAt(0) >= 97 && oldKey.charAt(0) <= 122){
                    oldKey = "" + Character.toUpperCase(oldKey.charAt(0));
                }
                int keyIndex = defaultKeys.indexOf(oldKey);
                tf.clear();
                String key = ke.getText();
                boolean goodKey = checkKey(key);
                if(goodKey){
                    if(key.charAt(0) >= 97 && key.charAt(0) <= 122){
                        key = "" + Character.toUpperCase(key.charAt(0));
                    }
                    else if(key.charAt(0) >= 48 && key.charAt(0) <= 57){
                        key = "" + key.charAt(0);
                    }
                    defaultKeys.set(keyIndex, key);
                    System.out.println(defaultKeys.toString());
                } else {
                    tf.setText(oldKey);
                }
            }
            
            /*String oldKey = tf.getText();
            if(oldKey.charAt(0) >= 97 && oldKey.charAt(0) <= 122){
                oldKey = "" + Character.toUpperCase(oldKey.charAt(0));
            }
            
            int keyIndex = defaultKeys.indexOf(oldKey);
            tf.clear();
            String key = ke.getText();
            
            if(key.charAt(0) >= 97 && key.charAt(0) <= 122){
                key = "" + Character.toUpperCase(key.charAt(0));
            }
            
            defaultKeys.set(keyIndex, key);
            System.out.print(defaultKeys.toString());
            */
        });
    }
    
    public boolean checkKey(String key){
        boolean goodKey = false;
        boolean dupCheck = false;
        if(key.charAt(0) == 32 || key.charAt(0) == 39 || (key.charAt(0) >= 44 && key.charAt(0) <= 57) 
        || key.charAt(0) ==59 || key.charAt(0) == 61 || (key.charAt(0) >= 65 && key.charAt(0) <= 93)
        || key.charAt(0) == 96 || (key.charAt(0) >= 97 && key.charAt(0) <= 122)){
            goodKey = true;
            if(key.charAt(0) >= 97 && key.charAt(0) <= 122){
                key = "" + Character.toUpperCase(key.charAt(0));
            }
            for(String defaultKey : defaultKeys){
                if(defaultKey.charAt(0) == key.charAt(0)){
                    dupCheck = true;
                }
            }
        }
        if(dupCheck){
            goodKey = false;
        }
        return goodKey;
    }

    public static void main(String[] args) {
        launch(args);
    }

}
