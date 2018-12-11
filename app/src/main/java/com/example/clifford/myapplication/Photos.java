package App;

import Model.Account;
import View.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;

import java.io.*;
import java.util.*;

public class Photos extends Application {

    HashSet<String> users;
    ArrayList<Model.Account> accounts;

    @Override
    public void start(Stage primaryStage) throws IOException {

        try {
            // must populate the hashset
            accounts = new ArrayList<Account>();
            File tmpDir = new File("./Data/METADATA.DAT");
            if (tmpDir.exists()) {

                FileInputStream filei = new FileInputStream("./Data/METADATA.DAT");
                ObjectInputStream obji = new ObjectInputStream(filei);
                users = (HashSet<String>) obji.readObject();

                Iterator iterator = users.iterator();

                File dir = new File("./Data/Users");
                if (!dir.exists())
                {
                    dir.mkdir();
                }
                while (iterator.hasNext()) {

                    String curr = (String) iterator.next();
                    if (!new File("./Data/Users/" + curr + ".DAT").exists())
                    {
                        users.remove(curr);
                        continue;
                    }
                    //System.out.println(curr);
                    filei = new FileInputStream("./Data/Users/" + curr + ".DAT");
                    obji = new ObjectInputStream(filei);
                    Account a = (Account) obji.readObject();
                    //System.out.println(a.getUsername());
                    accounts.add(a);
                }
            }
            else{
            users = new HashSet<String>();
            users.add("stock");
            Account stock = new Account("stock");
            stock.createAlbum("stock");
            Model.Album stockalb = stock.getAlbums().get(0);
            stock.addPhotoToAlbum(stockalb, "./Data/Pic/avl.png");
            stock.addPhotoToAlbum(stockalb, "./Data/Pic/bnha.png");
            stock.addPhotoToAlbum(stockalb, "./Data/Pic/pepe.jpeg");
            stock.addPhotoToAlbum(stockalb, "./Data/Pic/sponge.png");
            stock.addPhotoToAlbum(stockalb, "./Data/Pic/akame.png");
            accounts.add(stock);

            }



            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Login.fxml"));

            Parent root = (Parent)loader.load();
            LoginController control = loader.getController();

            control.start(primaryStage, users, accounts);

            Scene scene = new Scene(root, 600, 400);
            primaryStage.setScene(scene);
            primaryStage.show();

            primaryStage.setOnCloseRequest(event -> {
                try {
                    serialize();
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }

            });
        }


        catch (Exception e){

            e.printStackTrace();
        }

    }

    void serialize()
    {
        try {
            File file = new File("./Data/METADATA.DAT");
            //System.out.println(file.getAbsoluteFile());
            file.delete();
            file.createNewFile();

            FileOutputStream fileo = new FileOutputStream(file);
            ObjectOutputStream objo = new ObjectOutputStream(fileo);
            objo.writeObject(users);
            fileo.close();
            objo.close();
            File dir = new File("./Data/Users");
            if (!dir.exists())
            {
                dir.mkdir();
            }
            for(Model.Account a: accounts)
            {
                file = new File("./Data/Users/"+a.getUsername()+".DAT");
                file.delete();
                file.createNewFile();
                fileo = new FileOutputStream(file);
                objo = new ObjectOutputStream(fileo);
                objo.writeObject(a);
                objo.close();
                fileo.close();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
