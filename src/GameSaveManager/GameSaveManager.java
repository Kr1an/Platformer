package GameSaveManager;


import java.io.*;

public class GameSaveManager {

    private String folderName = "GAME_TRAMP_SETTINGS";
    private String standartExtension;
    private String folderPath;
    private String oparationSystem;


    public GameSaveManager(){

            oparationSystem = System.getProperty("os.name").toLowerCase();
            standartExtension = ".data";
            folderName = "Tramp";
            folderPath = getFolderPathDependsOnOS();
    }

    private String getFolderPathDependsOnOS() {
        if(isLinuxOS())
            return getFolderPathOnLinuxOS();
        else if(isWindowsOS())
            return getFolderPathOnWindowsOS();
        else
            return null;

    }

    private String getFolderPathOnLinuxOS(){
        String storyDirectory = System.getProperty("user.home");
        return storyDirectory + File.separator + folderName + File.separator;
    }
    private String getFolderPathOnWindowsOS(){
        String storyDirectory = System.getenv("APPDATA");
        return storyDirectory + File.separator + folderName + File.separator;
    }

    public boolean Save(String fileName, Object obj){
        String FilePath = combinePathString(fileName);
        return Serialize(FilePath, obj);
    }

    public Object Load(String fileName){
        String filePath = combinePathString(fileName);
        if (new File(filePath).exists())
            return Deserialize(filePath);
        return null;
    }

    public void setFolderPath(String val){folderPath = val;}
    public void setStandartExtension(String val){standartExtension = val;}
    public String getFolderPath(){return folderPath;}
    public String getStandartExtension(){return standartExtension;}


    private boolean Serialize(String Path, Object obj){
        try{
            new File(Path).getParentFile().mkdirs();
            FileOutputStream fos = new FileOutputStream(Path);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(obj);
            oos.flush();
            oos.close();
            return true;
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
    private Object Deserialize(String Path){
        try{
            FileInputStream fis = new FileInputStream(Path);
            ObjectInputStream oin = new ObjectInputStream(fis);
            Object obj = (Object)oin.readObject();
            return obj;
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private String combinePathString(String filename){return folderPath + filename + standartExtension;}

    private boolean isWindowsOS(){
        return oparationSystem.indexOf("win") >= 0;
    }
    private boolean isLinuxOS(){
        return oparationSystem.indexOf("nux") >= 0;
    }



}
