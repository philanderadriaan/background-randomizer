import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class Main
{

  private static final Properties PROPERTIES = new Properties();

  public static void main(String[] args) throws Exception
  {
    try
    {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      PROPERTIES.load(new FileInputStream("background-randomizer.properties"));

      File destinationDirectory = getDirectory("destination.directory");
      for (File destinationFile : destinationDirectory.listFiles())
      {
        log("Delete", destinationFile.getAbsolutePath());
        destinationFile.delete();
      }

      File[] sourceSubDirectories = getDirectory("source.directory").listFiles();
      List<File> sourceFileList = Arrays.asList(rename(sourceSubDirectories[new Random().nextInt(sourceSubDirectories.length)]).listFiles());
      Collections.shuffle(sourceFileList);

      for (File sourceFile : sourceFileList)
      {
        if (sourceFile.isDirectory())
        {
          File[] sourceFiles = sourceFile.listFiles();
          sourceFile = sourceFiles[new Random().nextInt(sourceFiles.length)];
        }
        renameCopy(sourceFile, destinationDirectory);
        Thread.sleep(1);
      }
    }
    catch (Exception exception)
    {
      JOptionPane.showMessageDialog(null, exception.getMessage(), exception.getClass().toString(), JOptionPane.ERROR_MESSAGE);
      throw exception;
    }
  }

  private static File rename(File file)
  {
    String oldFileName = file.getName();
    File newFile = new File(file.getParent() + '\\' + System.currentTimeMillis() + (file.isFile() ? oldFileName.substring(oldFileName.lastIndexOf('.')) : ""));
    log("Rename", file.getAbsolutePath(), newFile.getAbsolutePath());
    file.renameTo(newFile);
    return newFile;
  }

  private static void renameCopy(File sourceFile, File destinationDirectory) throws IOException
  {
    sourceFile = rename(sourceFile);
    String destinationFileName = destinationDirectory.getAbsolutePath() + "\\" + sourceFile.getName();
    log("Copy", sourceFile.getAbsolutePath(), destinationFileName);
    Files.copy(sourceFile.toPath(), Paths.get(destinationFileName));
  }

  private static File getDirectory(String key)
  {
    String[] directoryNames = PROPERTIES.getProperty(key).split(",");
    return new File(directoryNames[new Random().nextInt(directoryNames.length)]);
  }

  private static void log(String... logs)
  {
    for (String log : logs)
    {
      System.out.println(new Date() + " " + log);
    }
  }

}
