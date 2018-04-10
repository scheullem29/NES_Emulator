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



public class NES extends Application {

    private FileChooser fc = new FileChooser();
    private File f; //Holds the Rom file upon selection
    private ArrayList<String> defaultKeys = new ArrayList<String>(Arrays.asList("W","A","S","D","V","B","K","L"));
    private CPU nes = new CPU();
    private NESreader NR;
    
    
    @Override
    public void start(Stage stage) throws Exception {
        
        MenuButton load = new MenuButton("File");
            //Loading function
            MenuItem load1 = new MenuItem("Load Rom");
            load1.setOnAction((ActionEvent event) -> {
                fc.setTitle("Select Rom File");
                f = fc.showOpenDialog(stage);
                //System.out.println(f.getAbsoluteFile());
                NR = new NESreader(f.getAbsolutePath());
                NR.readFile(nes);
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
                ImageView down = new ImageView(new Image(getClass().getClassLoader().getResource("styles/Images/down.jpg").toExternalForm()));
                TextField downField = new TextField(defaultKeys.get(2));
                downField.setMaxSize(65,24);
                ImageView right = new ImageView(new Image(getClass().getClassLoader().getResource("styles/Images/right.jpg").toExternalForm()));
                TextField rightField = new TextField(defaultKeys.get(3));
                rightField.setMaxSize(65,24);
                Text select = new Text("Select");
                select.setFont(Font.font(null, FontWeight.BOLD, 24));
                TextField selectField = new TextField(defaultKeys.get(4));
                selectField.setMaxSize(65,24);
                Label start = new Label("Start");
                start.setFont(Font.font(null, FontWeight.BOLD, 24));
                TextField startField = new TextField(defaultKeys.get(5));
                startField.setMaxSize(65,24);
                Label A = new Label("A");
                A.setFont(Font.font(null, FontWeight.BOLD, 24));
                TextField AField = new TextField(defaultKeys.get(6));
                AField.setMaxSize(65,24);
                Label B = new Label("B");
                B.setFont(Font.font(null, FontWeight.BOLD, 24));
                TextField BField = new TextField(defaultKeys.get(7));
                BField.setMaxSize(65,24);
                
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
            
        ToolBar tb = new ToolBar(load, settings);
        BorderPane pane = new BorderPane();
        pane.setTop(tb);
        Scene scene = new Scene(pane,1000,800);
        stage.setTitle("NES Emulator");
        stage.setScene(scene);
        stage.show();
    }
    
    public void textEvent(TextField tf){
        tf.setOnKeyPressed((KeyEvent ke) -> {
            String oldKey = tf.getText();
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
            
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

}
